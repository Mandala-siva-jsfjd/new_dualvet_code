import React, { useState, useContext, useRef,useEffect,useCallback } from 'react';
import * as XLSX from 'xlsx';
import { toast } from 'react-toastify';
import "../assets/css/ipt.css"
import { AuthContext } from '../security/AuthContext';
import InPlantTrainingDetails from "../components/excel_templates/Ipt-details.xlsx";
import { HistoryComponent } from '../components/subComponent/HistoryComponent';
import {ipthistorydownloadData} from '../components/subComponent/HistoryDownloadData'
//import {FileUpload} from '../components/subComponent/FileUpload'
import { getIptHistoryData,getIptMetadata,getIptIndustryName,getIptDistricts,postIptService } from "../components/axios/services"
import Loading from '../Loding/loding'; // Import the Loading component
import { parse, format } from 'date-fns';
import DownloadIcon from '@mui/icons-material/Download';
import { inplantrequiredFields,inplantheaders, } from '../components/axios/Validations';
import { STATES_LIST,inplant_acemadic_year} from '../constant/Constant';
import {FormControl,InputLabel,Select,MenuItem,Radio,RadioGroup,FormControlLabel,Button,Typography,Box,Input} from '@mui/material';
const InplantTraining = () => {
  const [excelFile, setExcelFile] = useState(null);
  const [fileName, setFileName] = useState('');
  const [showHistory, setShowHistory] = useState(false);
  const [users, setUsers] = useState([]);
  const fileInputRef = useRef(null);
  const [selectedYear, setSelectedYear] = useState('');
  const [academicYear, setAcademicYear] = useState('');
  const [isUploading, setIsUploading] = useState(false);
  const { auth } = useContext(AuthContext);
  const [loading, setLoading] = useState(false);
  const [selectedState, setSelectedState] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [districts, setDistricts] = useState([]);
  const [industries, setIndustries] = useState([]);
  const [loadingDistricts, setLoadingDistricts] = useState(false);
  const [loadingIndustries, setLoadingIndustries] = useState(false);
  const [selectedIndustry, setSelectedIndustry] = useState('');
  const [industryId, setIndustryId] = useState('');
  const [industryName, setIndustryName] = useState('');
  const formatDate = (dateStr) => {
    if (!dateStr) {
      console.warn('Empty date string');
      return ''; // Handle empty dates
    }
  
    // Check if dateStr is a number (Excel date format)
    if (!isNaN(dateStr)) {
      const excelDate = parseInt(dateStr);
      const utcDays = excelDate - 25569; // Convert Excel date to UTC days
      const milliseconds = utcDays * 86400 * 1000; // Convert UTC days to milliseconds
      const parsedDate = new Date(milliseconds);
      // Use UTC methods to avoid timezone issues
      const formattedDate = `${parsedDate.getUTCFullYear()}-${String(parsedDate.getUTCMonth() + 1).padStart(2, '0')}-${String(parsedDate.getUTCDate()).padStart(2, '0')}`;
     // console.log('Excel date parsed and formatted:', parsedDate, formattedDate);
      return formattedDate;
    }
  
    // Ensure dateStr is a string
    if (typeof dateStr !== 'string') {
     // console.warn('Invalid date string format:', dateStr);
      return ''; // Handle invalid dates
    }
  
    // Parse string date in 'MM/dd/yyyy' format
    const parsedDate = parse(dateStr, 'MM/dd/yyyy', new Date());
    if (isNaN(parsedDate)) {
     // console.warn('Invalid date string:', dateStr);
      return ''; // Handle invalid dates
    }
  
    const formattedDate = format(parsedDate, 'yyyy-MM-dd');
    //console.log('String date parsed and formatted:', parsedDate, formattedDate);
  
    return formattedDate;
  };
  const handleFile = (e) => {
    let fileTypes = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet','application/vnd.ms-excel.sheet.macroEnabled.12', 'text/csv'];
    let selectedFile = e.target.files[0];
    if (selectedFile && fileTypes.includes(selectedFile.type)) {
      let reader = new FileReader();
      reader.readAsArrayBuffer(selectedFile);
      reader.onload = (e) => {
        setExcelFile(e.target.result);
        setFileName(selectedFile.name);  // Set the file name
      }
    } else {
      toast.error('Please select only excel file types');
      setExcelFile(null);
      setFileName('');  // Reset the file name on error
      if (fileInputRef.current) {
        fileInputRef.current.value = ''; // Clear file input
      }
    }
  };
  const handleStateChange = (event) => {
    setSelectedState(event.target.value);
    setSelectedDistrict('');
    setDistricts([]);
    setIndustries([]);
  };
  const handleDistrictChange = (event) => {
    setSelectedDistrict(event.target.value);
    setIndustries([]);
    setSelectedIndustry('');
  };
  const handleIndustryChange = (event) => {
    const selectedValue = event.target.value;
    setSelectedIndustry(selectedValue);
    if (selectedValue) {
      const [sno, name] = selectedValue.split('$');
      setIndustryId(sno);
      setIndustryName(name);
      
      console.log(name);
      console.log(sno);
    } else {
      setIndustryId('error in id');
      setIndustryName('error in industry name');
    }
  };
  const handleSelection = (event) => {
    setSelectedYear(event.target.value);
  };
  const handleacemadicyearSelection = (event) => {
    setAcademicYear(event.target.value);
  };
  const handleFileSubmit = async (e) => {
    e.preventDefault();
    if (fileInputRef.current) {
      fileInputRef.current.value = ''; // Clear file input
    }
    // Check if all required fields are selected
    if (!selectedState) {
      toast.error('Please select a state.');
      return;
    }
    if (!selectedDistrict) {
      toast.error('Please select a district.');
      return;
    }
    if (!selectedIndustry) {
      toast.error('Please select an industry.');
      return;
    }
    if (!selectedYear) {
      toast.error('Please select an internship year.');
      return;
    }
    if (!academicYear) {
      toast.error('Please select an academic Year.');
      return;
    }
    if (excelFile === null) {
      toast.error('Please select an Excel file.');
      return;
    }
    setIsUploading(true);
    try {
      const workbook = XLSX.read(excelFile, { type: 'buffer' });
      const worksheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[worksheetName];
      const data = XLSX.utils.sheet_to_json(worksheet);
      if (data.length === 0) {
        toast.error('Excel file is empty.');
        setIsUploading(false);
        return;
      }
      const formDataArray = [];
      for (let i = 0; i < data.length; i++) {
        const rowData = data[i];
        for (let field of inplantrequiredFields) {
            if (!rowData[field] && field !== 'Stipend') {
                      toast.error(`Missing required field: ${field} in row ${i +2}`);
                      setIsUploading(false);
                      // if (fileInputRef.current) {
                      //   fileInputRef.current.value = ''; // Clear file input
                      // }
                      return;
                    }
          if (!rowData[field] && field !== 'Stipend') {
            toast.error(`Please use the correct template for upload. The column in the upload file and sample template does not match. Row ${i + 2}: ${field}`);
            setIsUploading(false);
            if (fileInputRef.current) {
              fileInputRef.current.value = '';
            }
            return;
          }
         
          if (field === 'Phone_Number') {
            if (!/^\d{10}$/.test(rowData[field])) {
              toast.error(`Invalid contact number in column ${field} at row ${i + 2}`);
              setIsUploading(false);
              return;
            }
          }
  
          if (field === 'Academic_Year') {
            const academicYearRegex = /^\d{4}-\d{4}$/;
            if (!academicYearRegex.test(rowData[field])) {
              toast.error(`Invalid academic year format in column ${field} at row ${i + 2}. Expected format: YYYY-YYYY`);
              setIsUploading(false);
              return;
            }
          }
        }
        function cleanString(str) {
          if (typeof str !== 'string') {
              return ''; // Return an empty string for non-string inputs
          }
          return str.trim(); // Removes only leading and trailing spaces
      }
      
      function cleanString1(str) {
        // Allow alphanumeric characters, spaces, and commas
        return str.replace(/[^a-zA-Z0-9\s,]/g, '').trim();
    }

    function email(str) {
      // Allow alphanumeric characters, spaces, commas, '@', and '.'
      return str.replace(/[^a-zA-Z0-9\s,@.]/g, '').trim();
  }
        const inPlantTrainingData = {
          traineesName: cleanString1(rowData['Trainees_Name']),
          traineesCollegeRegId: cleanString(rowData['Trainees_College_Reg_Id']),
          traineesItiTrade: cleanString1(rowData['Trainees_Iti_Trade']),
          phoneNumber: rowData['Phone_Number'],
          emailId: email(rowData['Email_Id'].toLowerCase()),
          stipend: parseInt(rowData['Stipend'], 10),
          itiName: (rowData['Iti_Name']),
          instructorName: cleanString1(rowData['Instructor_Name']),
          //traineesJoiningDate: formatDate(rowData['Trainees_Joining_Date']),
          //traineesEndDate: formatDate(rowData['Trainees_End_Date']),
          iptYear: selectedYear,
          industrialId: industryId,
          industryName: industryName,
          insuranceStatus: cleanString(rowData['Insurance_Status']),
          ppeKitStatus: cleanString(rowData['ppe_kit_Status']),
          iptStart: formatDate(rowData['Ipt_Start']),
          iptEnd: formatDate(rowData['Ipt_End']),
          
          iptDiaryProvided: cleanString(rowData['Ipt_Diary_Provided']),
          academicYear: academicYear,
          employmentType: cleanString1(rowData['Employment_Type']),

        };
        //console.log(inPlantTrainingData);
        let existingFormData = formDataArray.find(item => item.fileName === fileName);
        if (existingFormData) {
          existingFormData.ipt.push(inPlantTrainingData);
        } else {
          formDataArray.push({
            fileName: fileName,
            ipt: [inPlantTrainingData]
          });
        }
      }
      const uploadPromises = formDataArray.map(formData => postIptService(formData));
      await Promise.all(uploadPromises);
      toast.success('Document uploaded successfully!');
      // Reset state and district
      setSelectedState('');
      setSelectedDistrict('');
      setSelectedIndustry('');
      setSelectedYear('');
      setIndustryId('');
      setIndustryName('');
      setExcelFile(null);
      setFileName('');
      setDistricts([]);
      setIndustries([]);
      setAcademicYear([]);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    } catch (error) {
      //console.error('Failed to upload data:', error);
      toast.error('Failed to upload document. Please try again.');
    } finally {
      setIsUploading(false);
    }
  };
  const handleDownloaded = () => {
    const url = InPlantTrainingDetails;
    window.open(url);
  };

  const historyDownload = async (metaDataRegId) => {
    //console.log('metaDataRegId:', metaDataRegId);
    try {
      const regId = parseInt(metaDataRegId);
      if (isNaN(regId)) {
        throw new Error('Invalid metaDataRegId');
      }
      const data = await getIptHistoryData(metaDataRegId, auth.accessToken);
      const historyData = ipthistorydownloadData(data);
      const csvRows = [inplantheaders, ...historyData];
      const csvContent = "data:text/csv;charset=utf-8," + csvRows.map(row => row.join(",")).join("\n");
      const encodedUri = encodeURI(csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "Ipt-history-data.csv");
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
     // console.error('Error while downloading history:', error);
      toast.error('Failed to download history. Please try again.');
    }
  };

  // const fetchMetadata = async () => {
  //   setLoading(true);
  //   const data = await getIptMetadata(auth);
  //   if (data) {
  //     data.sort((a, b) => new Date(b.uploadDate) - new Date(a.uploadDate))
  //     setUsers(data);
  //   }
  //   setLoading(false);
  // };
  // useEffect(() => {
  //   fetchMetadata()
  // }, [auth]);

   const fetchMetadata = useCallback(async () => {
      setLoading(true);
      try {
        const data = await getIptMetadata(auth);
        data.sort((a, b) => new Date(b.uploadDate) - new Date(a.uploadDate));
        setUsers(data);
      } catch (error) {
        toast.error('Failed to fetch data. Please try again.');
      } finally {
        setLoading(false);
      }
    }, [auth]);
    
    useEffect(() => {
      fetchMetadata();
    }, [fetchMetadata]); // Add fetchMetadata as a dependency
    // Handle template download
  useEffect(() => {
    const fetchDistricts = async () => {
      if (selectedState) {
        setLoadingDistricts(true);
        try {
          const accessToken = localStorage.getItem('access_token');
          if (!accessToken) {
            throw new Error('Access token not found in local storage.');
          }
          const data = await getIptDistricts(selectedState);
          const districtNames = data.map(district => district.district);
          setDistricts(districtNames);
        } catch (error) {
          //console.error('Error fetching districts:', error);
          setDistricts([]);
        } finally {
          setLoadingDistricts(false);
        }
      }
    };

    fetchDistricts();
  }, [selectedState]);

  useEffect(() => {
    const fetchIndustries = async () => {
      if (selectedState && selectedDistrict) {
        setLoadingIndustries(true);
        try {
          const data = await getIptIndustryName(selectedState, selectedDistrict);
          const industryNames = data.map(industry => ({ sno: industry.sno, industryName: industry.industryName }));
          setIndustries(industryNames);
        } catch (error) {
          //console.error('Error fetching industries:', error);
          setIndustries([]);
        } finally {
          setLoadingIndustries(false);
        }
      }
    };

    fetchIndustries();
  }, [selectedState, selectedDistrict]);

  return (
    <div>
           <Box sx={{ backgroundColor: '#DDEBF6', padding: 2, borderRadius: 2 }}>
            <Typography variant="h5" component="h3" gutterBottom>
            Inplant Training Details
            </Typography>
              <Typography variant="subtitle1" component="h4">
              This Screen is for Capturing Inplant Training Details through Bulk Upload.
              </Typography>
        </Box><br />
        <Box sx={{ padding: 2, backgroundColor: "#DDEBF6",borderRadius:2 }}>
    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 2 }}>
      <Typography variant="h6">Bulk Upload</Typography>
      <Button
        variant="contained"
        color="primary"
        startIcon={<DownloadIcon />}
        onClick={handleDownloaded}
      >
        Download Template
      </Button>
    </Box>
    <Box className='state-dropdown' sx={{ marginBottom: 2 }}>
      <Typography sx={{paddingTop:"10px"}} >Select State:</Typography>
      <FormControl sx={{ minWidth: 150 }} size="small">
        <InputLabel id="state-select-label">Select State</InputLabel>
        <Select
          labelId="state-select-label"
          id="state-select"
          value={selectedState}
          label="Select State"
          onChange={handleStateChange}
        >
          {STATES_LIST.map((state, i) => (
            <MenuItem key={i} value={state}>
              {state}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>

    {districts.length > 0 && (
      <Box className='district-dropdown' sx={{ marginBottom: 2 }}>
        <Typography sx={{paddingTop:"10px"}}>Select District:</Typography>
        <FormControl sx={{ minWidth: 150 }} size="small">
          <InputLabel id="district-select-label">Select District</InputLabel>
          <Select
            labelId="district-select-label"
            id="district-select"
            value={selectedDistrict}
            label="Select District"
            onChange={handleDistrictChange}
          >
            {districts.map((district, index) => (
              <MenuItem key={index} value={district}>
                {district}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>
    )}

    {!loadingDistricts && districts.length === 0 && selectedState && selectedState !== 'Select State' && (
      <Typography>No Districts available for the selected State in industry master table.</Typography>
    )}

    {loadingIndustries && industries.length === 0 && (
      <Typography>Loading industries...</Typography>
    )}
    {industries.length > 0 && (
      <Box className='industry-dropdown' sx={{ marginBottom: 2 }}>
        <Typography sx={{paddingTop:"10px"}}>Select Industry:</Typography>
        <FormControl sx={{ minWidth: 150 }} size="small">
          <InputLabel id="industry-select-label">Select Industry</InputLabel>
          <Select
            labelId="industry-select-label"
            id="industry-select"
            value={selectedIndustry}
            label="Select Industry"
            onChange={handleIndustryChange}
          >
            {industries.map((industry, index) => (
              <MenuItem key={index} value={`${industry.sno}$${industry.industryName}`}>
                {industry.industryName}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>
    )}

    {!loadingIndustries && industries.length === 0 && selectedDistrict && selectedState !== 'Select State' && (
      <Typography>No Industries available in industry master table for the selected District.</Typography>
    )}
<Box display="flex" 
  flexDirection={{ xs: 'column', md: 'row' }} 
  gap={2}>
  <FormControl
    sx={{
      display: 'flex',
      flexDirection: { xs: 'column', md: 'row'  }, // Vertical on small screens, horizontal on medium+
     
      gap: 1, // spacing between the label and options
    }}
    component="fieldset"
  >
    <Typography sx={{ paddingTop:'8px',}}>Inplant Training Year:</Typography>
    <FormControl sx={{ minWidth: 150 }} size="small">
      <RadioGroup
        aria-label="internshipYear"
        name="internshipYear"
        value={selectedYear}
        onChange={handleSelection}
        sx={{
          display: 'flex',
          flexDirection: 'row',
          gap: 0.5, // spacing between radio buttons
        }}
      >
        <FormControlLabel value="1" control={<Radio />} label="1st Year" />
        <FormControlLabel value="2" control={<Radio />} label="2nd Year" />
      </RadioGroup>
    </FormControl>
  </FormControl>

  <Box sx={{display: 'flex',}}>
    <Typography sx={{paddingTop:"10px"}}>Academic Year:</Typography>
    <FormControl
      sx={{
        minWidth: 150,
        borderRadius: 1,
        
      }}
      size="small"
    >
      <InputLabel id="Academic-select-label">Academic Year</InputLabel>
      <Select
        labelId="Academic Year"
        id="Academic-select"
        value={academicYear}
        label="academicYear"
        onChange={handleacemadicyearSelection}
        sx={{
          color: '#333',
        }}
      >
        {inplant_acemadic_year.map((academic_year, i) => (
          <MenuItem key={i} value={academic_year}>
            {academic_year}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  </Box>
</Box>

    <Box>
      <form onSubmit={handleFileSubmit}>
      {/* <FileUpload onFileChange={(file, name) => {
        setExcelFile(file);
        setFileName(name);
      }} /> */}
       <FormControl  margin="normal">
                    <Input
                      id="file-upload"
                      type="file"
                      inputRef={fileInputRef}
                      onChange={handleFile}
                      required
                    />
                  </FormControl>
        <Box sx={{ marginTop: 2 }}>
          <Typography color="error">Note-1: Always Download Excel file from Download Template Button and use only Download Template for Upload Excel.<br />
                        Note-2:In the Excel sheet, the field names "Trainee College Reg ID", "Email ID", and "IPT Year" are highlighted in light green to indicate that their values must form unique combinations in the Inplant Training records. Additionally, the "ITI Name" must match an entry in the ITI master data. Yellow highlighting will be used exclusively for validation alerts within Excel.
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', marginTop: 2 }}>
          <Button
            type="submit"
            variant="contained"
            color="success"
            disabled={isUploading}
          >
            Upload Document
          </Button>
          <Button
           variant="outlined"
           color="secondary"
           onClick={() => setShowHistory(!showHistory)}
           sx={{ textDecoration: 'none' }} // Ensure no underline
         >
           {showHistory ? 'Hide History' : 'Show History'}
         </Button>
                     
        </Box>
      </form>
    </Box>
  </Box>
  <br />
      {showHistory && <HistoryComponent loading={loading} users={users} historyDownload={historyDownload} />}
      <Loading open={isUploading} /> {/* Add Loading component */}
    </div>
  );
}

export default InplantTraining;