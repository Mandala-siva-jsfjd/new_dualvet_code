import React, { useState, useContext, useRef ,useEffect,useCallback} from 'react';
import * as XLSX from 'xlsx';
import { toast } from 'react-toastify';
import { AuthContext } from '../security/AuthContext';
import ItiExcelDetails from "../components/excel_templates/ItiDetails.xlsx";
import {HistoryComponent} from '../components/subComponent/HistoryComponent';
import {itiHistoryData} from '../components/subComponent/HistoryDownloadData'
import {postItiService, getItiHistoryData ,getItiMetadata} from "../components/axios/services"
import Loading from '../Loding/loding'; // Import the Loading component
import { itirequiredField,itiheaders } from '../components/axios/Validations';
import { Box, Typography,Button,FormControl,Input  } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
const ItiDetails = () => {
  const [excelFile, setExcelFile] = useState(null);
  const [fileName, setFileName] = useState('');
  const [showHistory, setShowHistory] = useState(false);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isUploading, setIsUploading] = useState(false); // New state for upload button
  const fileInputRef = useRef(null); // Ref to clear file input
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


  const handleDownloaded = () => {
    const url = ItiExcelDetails;
    window.open(url);
  };
  // const fetchMetadata = async () => {
  //   setLoading(true);
  //   const data = await getItiMetadata(auth);
  //   if (data) {
  //     data.sort((a, b) => new Date(b.uploadDate) - new Date(a.uploadDate))
  //     setUsers(data);
      
  //   }
  //   setLoading(false);
  // };

  // useEffect(() => {
  //   fetchMetadata(); // This is safe now with auth as dependency
  // }, [auth]);
  


const fetchMetadata = useCallback(async () => {
    setLoading(true);
    try {
      const data = await getItiMetadata(auth);
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






  
  const handleFileSubmit = async (e) => {
    e.preventDefault();
    if (fileInputRef.current) {
      fileInputRef.current.value = ''; // Clear file input
    }
    if (excelFile !== null) {
      setIsUploading(true); // Disable upload button
      const workbook = XLSX.read(excelFile, { type: 'buffer' });
      const worksheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[worksheetName];
      const data = XLSX.utils.sheet_to_json(worksheet);
     // console.log('Uploaded Data:', data); // Log the uploaded data
      if (data.length === 0) {
        toast.error('Excel file is empty.');
        setIsUploading(false);
        return;
      }
      const formDataArray = [];
      for (let i = 0; i < data.length; i++) {
        const rowData = data[i];
  
        // Column-specific validations
        for (let field of itirequiredField) {
          if (!rowData[field] && field !== 'No_of_Trainees'&& field !== 'No_Of_Seats_Available') {
            toast.error(`Missing required field: ${field} in row ${i +2}`);
            setIsUploading(false);
            // if (fileInputRef.current) {
            //   fileInputRef.current.value = ''; // Clear file input
            // }
            return;
          }
          if (!rowData[field] && field !== 'No_of_Trainees'&& field !== 'No_Of_Seats_Available') {
             toast.error(`Please use the correct template for upload. Column in the upload file and sample template do not match. Row: ${i + 2}, Field: ${field}`)
            setIsUploading(false);
            // if (fileInputRef.current) {
            //   fileInputRef.current.value = ''; // Clear file input
            // }
            return;
          }
          if (field === 'Principal_Contact_Number' || field === 'Instructor_Contact_Number') {
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
      //   function cleanString(str) {
      //     if (typeof str !== 'string') {
      //         return ''; // Return an empty string for non-string inputs
      //     }
      //     return str.trim(); // Removes only leading and trailing spaces
      // }
      
      function cleanString1(str) {
        return str.replace(/_/g, ' ')  // Replace underscores with spaces
        .replace(/[^a-zA-Z0-9\s,]/g, '')  // Remove special characters except spaces and commas
        .trim(); // Trim leading/trailing spaces
    }

    function email(str) {
      // Allow alphanumeric characters, spaces, commas, '@', and '.'
      return str.replace(/[^a-zA-Z0-9\s,@.]/g, '').trim();
  }
 
        // Constructing the data object
        const itiData = {
           itiName:(rowData['Iti_Name']),
           //address: cleanString(rowData['Address']),
          // principalAndVicePrincipal:cleanString1(rowData['Principal_and_Vice_Principal']),
          // itiEmailId: email(rowData['Iti_Email_Id'].toLowerCase()),
          instructorName: cleanString1(rowData['Instructor_Name']),
         // principalContactNumber: rowData['Principal_Contact_Number'],
          instructorEmailId: email(rowData['Instructor_Email_Id'].toLowerCase()),
          instructorContactNumber: rowData['Instructor_Contact_Number'], 
          itiTrade: cleanString1(rowData['Iti_Trade']),
           state: rowData['State'] ? rowData['State'].replace(/_/g, ' ') : '',
           district: cleanString1(rowData['Cluster']),
          noOfTrainees: rowData['No_of_Trainees'],
          courseDurationYear: rowData['Course_Duration_Year'],
          noOfSeatsAvailable: isNaN(parseFloat(rowData['No_Of_Seats_Available'])) ? 0 : parseFloat(rowData['No_Of_Seats_Available']),
          employmentType: cleanString1(rowData['Employment_Type']),
        };
        // Grouping data by file name
        let existingFormData = formDataArray.find(item => item.fileName === fileName);
        if (existingFormData) {
          existingFormData.itiList.push(itiData);
        } else {
          formDataArray.push({
            fileName: fileName,
            itiList: [itiData],
          });
        }
      }
  
      try {
        const uploadPromises = formDataArray.map(formData => postItiService(formData));
        await Promise.all(uploadPromises); // No need to assign it to responses
        toast.success('Document uploaded successfully!');
        fetchMetadata();
        setExcelFile(null); // Clear file
        setFileName(''); // Clear file name
        if (fileInputRef.current) {
          fileInputRef.current.value = ''; // Clear file input
        }
      } catch (error) {
        //console.error('Failed to upload data:', error);
        toast.error('Failed to upload document. Please try again.');
      } finally {
        setIsUploading(false); // Re-enable upload button
      }
      
    }}
  const historyDownload = async (metaDataRegId) => {
    //console.log('metaDataRegId:', metaDataRegId);
    try {
      // Ensure metaDataRegId is a valid integer
      const regId = parseInt(metaDataRegId);

      if (isNaN(regId)) {
        throw new Error('Invalid metaDataRegId');
      }
      const data = await getItiHistoryData(metaDataRegId, auth.accessToken);
      const historyData = itiHistoryData(data);
      // Combine headers and data
      const csvRows = [itiheaders, ...historyData];
      // Create CSV content
      const csvContent = "data:text/csv;charset=utf-8," + csvRows.map(row => row.join(",")).join("\n");
      // Create a temporary anchor element and set its attributes for download
      const encodedUri = encodeURI(csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "Iti-history-data.csv");
      // Append the anchor element to the body and trigger the download
      document.body.appendChild(link);
      link.click();
      // Remove the anchor element
      document.body.removeChild(link);
    } catch (error) {
      console.error('Failed to download history:', error);
      toast.error('Failed to download history data. Please try again.');
    }
  };

  return (
    <div>
      <Box  sx={{
      backgroundColor: "#DDEBF6",
      padding: { xs: 1, sm: 2 }, // Adjust padding for smaller screens
      borderRadius: 2,
    }}>
            <Typography variant="h5" component="h3" gutterBottom sx={{ fontSize: { xs: "1.2rem", sm: "1.5rem" } }} >
            ITI Details
            </Typography>
              <Typography variant="subtitle1" component="h4">
              These are vocational training institutes in India that provide technical education in various trades.
              </Typography>
        </Box><br />
        <Box sx={{
      padding: { xs: 1, sm: 2 },
      backgroundColor: "#DDEBF6",
      borderRadius: 2,
    }}>
            <Box  sx={{
        display: "flex",
        flexDirection: { xs: "column", sm: "row" },
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: 2,
      }}>
              <Typography variant="h6" sx={{ fontSize: { xs: "1rem", sm: "1.2rem" } }}>Bulk Upload</Typography>
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
                      <Typography  variant="body2"color="error"sx={{ marginY: 2, fontSize: { xs: "0.8rem", sm: "1rem" } }}>
                      Note-1: Always Download Excel file from Download Template Button and use only Download Template for Upload Excel. <br />
                        Note-2: In the Excel sheet, the field names "ITI Name", "Instructor Email Id", "ITI Trade"",and "Course Duration Year" are highlighted in light green to indicate that their values must be unique combinations in the Iti records. Yellow highlighting will be used exclusively for validation alerts within Excel.
                      </Typography>
                  <Box  sx={{
            display: "flex",
            flexDirection: { xs: "column", sm: "row" },
            justifyContent: "space-between",
            gap: 2, // Adds spacing between buttons in smaller screens
            marginTop: 2,
          }}>
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
                      {showHistory ? 'Hide History' : 'Show History'}
                    </Button>
                  </Box>
                </form>
              </Box>
           </Box><br />
      {showHistory && <HistoryComponent loading={loading} users={users} historyDownload={historyDownload} />}
      <Loading open={isUploading} /> {/* Add Loading component */}

    </div>
  );
};

export default ItiDetails;