import React, { useState, useContext, useRef ,useEffect,useCallback} from 'react';
import * as XLSX from 'xlsx';
import { toast } from 'react-toastify';
import { AuthContext } from '../security/AuthContext';
import IndustryDetails from "../components/excel_templates/Industrial_details.xlsx";
import {HistoryComponent} from '../components/subComponent/HistoryComponent';
import {industryhistoryData} from '../components/subComponent/HistoryDownloadData'
//import {FileUpload} from '../components/subComponent/FileUpload'
import { postIndustryService, getIndustrialHistoryData, getIndustryMetadata } from '../components/axios/services';
import Loading from '../Loding/loding'; // Import the Loading component
import { Button,Box, Typography,FormControl,Input } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download'
import { industryrequiredFields,industryheaders } from '../components/axios/Validations';
const IndustryPartner = () => {
  const [excelFile, setExcelFile] = useState(null);
  const [fileName, setFileName] = useState('');
  const [showHistory, setShowHistory] = useState(false);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null);
  const [isUploading, setIsUploading] = useState(false);
  const { auth } = useContext(AuthContext);

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

  // Handle file submission
  const handleFileSubmit = async (e) => {
    e.preventDefault();
    if (fileInputRef.current) {
      fileInputRef.current.value = ''; // Clear file input
    }
    if (excelFile !== null) {
      setIsUploading(true);
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
        // Validations
        for (let field of industryrequiredFields) {
          if (!rowData[field] && field !== 'No_of_Ojt_Trainees' && field !== 'No_of_Apprenticeship' && field !== 'No_of_Placements') {
            toast.error(`Please use the correct template for upload. Column in the upload file and sample template do not match. Row: ${i + 2}, Field: ${field}`);
            setIsUploading(false);
            if (fileInputRef.current) {
              fileInputRef.current.value = '';
            }
            return;
          }
          
          if (field === 'Contact_Number') {
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
  
          if (field === 'No_of_Trainees') {
            if (rowData[field] !== undefined && isNaN(parseInt(rowData[field]))) {
              toast.error(`Invalid value for number of trainees in row ${i + 2}`);
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
        if (typeof str !== 'string') {
            return ''; // Return an empty string for non-string inputs
        }
        // Allow alphanumeric characters, spaces, and commas
        return str.replace(/[^a-zA-Z0-9\s,]/g, '').trim();
    }
    
    function email(str) {
        if (typeof str !== 'string') {
            return ''; // Return an empty string for non-string inputs
        }
        // Allow alphanumeric characters, spaces, commas, '@', and '.'
        return str.replace(/[^a-zA-Z0-9\s,@.]/g, '').trim();
    }
        const industryData = {
          industryName: cleanString1(rowData['Industry_Name']),
          contactName: cleanString1(rowData['Contact_Name']),
          contactNumber: rowData['Contact_Number'],
          emailId:email(rowData['Email_Id'].toLowerCase()),
          nearestCity: cleanString(rowData['Nearest_City']),
          district: cleanString1(rowData['District']),
          state: cleanString1(rowData['State']),
          pincode: parseInt(rowData['Pincode']),
          lat: parseFloat(rowData['Lat']),
          lan: parseFloat(rowData['Lan']),
          designationOfSpoc: cleanString1(rowData['Designation_of_Spoc']),
          sizeOfIndustry: cleanString1(rowData['Size_of_Industry']),
          sector: cleanString1(rowData['Sector']),
         // domain: cleanString1(rowData['Domain']),
          trade: cleanString1(rowData['Trade']),
          academicYear: rowData['Academic_Year'],
          ojtSupport: cleanString(rowData['Ojt_Support']),
          noOfOjtTrainees: isNaN(parseFloat(rowData['No_of_Ojt_Trainees'])) ? 0 : parseFloat(rowData['No_of_Ojt_Trainees']),
          apprenticeshipSupport: cleanString(rowData['Apprenticeship_Support']),
          noOfApprenticeship: isNaN(parseFloat(rowData['No_of_Apprenticeship'])) ? 0 : parseFloat(rowData['No_of_Apprenticeship']),
          placementsSupport: cleanString(rowData['Placements_Support']),
          noOfPlacements: isNaN(parseFloat(rowData['No_of_Placements'])) ? 0 : parseFloat(rowData['No_of_Placements']),
          isStipendProvided: cleanString(rowData['Is_Stipend_Provided']),
          isFoodProvided: cleanString(rowData['Is_Food_Provided']),
          isTransportProvided: cleanString(rowData['Is_Transport_Provided']),
          isAccommodationProvided: cleanString(rowData['Is_Accommodation_Provided']), 
          isMouSign: cleanString(rowData['Is_Mou_Sign'])
        };

        let existingFormData = formDataArray.find(item => item.fileName === fileName);
        if (existingFormData) {
          existingFormData.ind.push(industryData);
        } else {
          formDataArray.push({
            fileName: fileName,
            ind: [industryData]
          });
        }
      }

      try {
        const uploadPromises = formDataArray.map(formData => postIndustryService(formData));
        await Promise.all(uploadPromises);
        toast.success('Document uploaded successfully!');
        fetchMetadata();
        setExcelFile(null);
        setFileName('');
        if (fileInputRef.current) {
          fileInputRef.current.value = '';
        }
      } catch (error) {
       // console.error('Failed to upload data:', error);
        toast.error('Failed to upload document. Please try again.');
      } finally {
        setIsUploading(false);
      }
    } else {
      toast.error('Please select an Excel file.');
      setIsUploading(false);
    }
  };

  // Handle history download
  const historyDownload = async (metaDataRegId) => {
    try {
      const regId = parseInt(metaDataRegId);
      if (isNaN(regId)) {
        throw new Error('Invalid metaDataRegId');
      }
      const data = await getIndustrialHistoryData(metaDataRegId, auth.accessToken);
      const historyData = industryhistoryData(data);
      const csvRows = [industryheaders, ...historyData];
      const csvContent = "data:text/csv;charset=utf-8," + csvRows.map(row => row.join(",")).join("\n");
      const encodedUri = encodeURI(csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "industry_history_data.csv");
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
     // console.error('Error while downloading history:', error);
      toast.error('Failed to download history. Please try again.');
    }
  };
  const fetchMetadata = useCallback(async () => {
    setLoading(true);
    try {
      const data = await getIndustryMetadata(auth);
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
  const handleDownloaded = () => {
    window.open(IndustryDetails);
  };

  return (
   <div>
  <Box
    sx={{
      backgroundColor: "#DDEBF6",
      padding: { xs: 1, sm: 2 }, // Adjust padding for smaller screens
      borderRadius: 2,
    }}
  >
    <Typography
      variant="h5"
      component="h3"
      gutterBottom
      sx={{ fontSize: { xs: "1.2rem", sm: "1.5rem" } }} // Responsive font size
    >
      Industry Partner Details
    </Typography>
    <Typography
      variant="subtitle1"
      component="h4"
      sx={{ fontSize: { xs: "0.9rem", sm: "1rem" } }}
    >
      This Screen is for Capturing Industry Partner Details through Bulk Upload.
    </Typography>
  </Box>
  <br />
  <Box
    sx={{
      padding: { xs: 1, sm: 2 },
      backgroundColor: "#DDEBF6",
      borderRadius: 2,
    }}
  >
    <Box
      sx={{
        display: "flex",
        flexDirection: { xs: "column", sm: "row" },
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: 2,
      }}
    >
      <Typography variant="h6" sx={{ fontSize: { xs: "1rem", sm: "1.2rem" } }}>
        Bulk Upload
      </Typography>
      <Button
        variant="contained"
        color="primary"
        startIcon={<DownloadIcon />}
        onClick={handleDownloaded}
        sx={{ width: { xs: "100%", sm: "auto" }, marginTop: { xs: 1, sm: 0 } }}
      >
        Download Template
      </Button>
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
        <Typography
          variant="body2"
          color="error"
          sx={{ marginY: 2, fontSize: { xs: "0.8rem", sm: "1rem" } }}
        >
          Note-1: Always Download Excel file from Download Template Button and use only Download Template for Upload Excel.
          <br />
          Note-2:In the Excel sheet, the field names "Industry Name", "Email ID", and "Contact Number" are highlighted in light green to indicate that their values must be unique in the Industry Partner records. Yellow highlighting will be used exclusively for validation alerts within Excel.
        </Typography>
        <Box
          sx={{
            display: "flex",
            flexDirection: { xs: "column", sm: "row" },
            justifyContent: "space-between",
            gap: 2, // Adds spacing between buttons in smaller screens
            marginTop: 2,
          }}
        >
          <Button
            type="submit"
            variant="contained"
            color="success"
            disabled={isUploading}
            sx={{
              textDecoration: "none", // Ensure no underline
              width: { xs: "100%", sm: "auto" }, // Full width on mobile
            }}
          >
            Upload Document
          </Button>

          <Button
            variant="outlined"
            color="secondary"
            onClick={() => setShowHistory(!showHistory)}
            sx={{
              textDecoration: "none", // Ensure no underline
              width: { xs: "100%", sm: "auto" }, // Full width on mobile
            }}
          >
            {showHistory ? "Hide History" : "Show History"}
          </Button>
        </Box>
      </form>
    </Box>
  </Box>
  <br />
  {showHistory && (
    <HistoryComponent
      loading={loading}
      users={users}
      historyDownload={historyDownload}
    />
  )}
  <Loading open={isUploading} />
</div>

  );
}

export default IndustryPartner;