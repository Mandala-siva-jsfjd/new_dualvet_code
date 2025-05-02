import axios from 'axios';
import { toast } from 'react-toastify';
import { saveAs } from 'file-saver'; // Assuming file-saver is installed

// Set up axios instance
const axiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_ENDPOINT,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token to headers
axiosInstance.interceptors.request.use(config => {
  const token = localStorage.getItem('access_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// Response interceptor to handle errors
axiosInstance.interceptors.response.use(response => response, error => {
  if (error.response && error.response.status === 403) {
    toast.error('Access forbidden: Invalid or expired token.');
  } 
  // else {
  //   toast.error(error.response?.data?.message || 'State was not present in industry data');
  // }
  return Promise.reject(error);
});


// ITI services
export const postItiService = async (formData) => {
  try {
    const response = await axiosInstance.post('/iti/save', formData);
    return response.data;
  } catch (error) {
    throw new Error('Error uploading ITI data');
  }
};

// Industry services
export const postIndustryService = async (formData) => {
  try {
    const response = await axiosInstance.post('/industry/save', formData);
    return response.data;
  } catch (error) {
    throw new Error('Error uploading industry data');
  }
};

// IPT services
export const postIptService = async (formData) => {
  try {
    const response = await axiosInstance.post('/ipt/save', formData);
    return response.data;
  } catch (error) {
    throw new Error('Error uploading IPT data');
  }
};

// Metadata services
export const getIndustryMetadata = async () => {
  try {
    const response = await axiosInstance.get('/industry/meta');
    return response.data;
  } catch (error) {
    throw new Error('Error fetching industry metadata');
  }
};

export const getItiMetadata = async () => {
  try {
    const response = await axiosInstance.get('/iti/meta');
    return response.data;
  } catch (error) {
    throw new Error('Error fetching ITI metadata');
  }
};

export const getIptMetadata = async () => {
  try {
    const response = await axiosInstance.get('/ipt/meta');
    return response.data;
  } catch (error) {
    throw new Error('Error fetching IPT metadata');
  }
};

// History data services
export const getIptHistoryData = async (metaDataRegId) => {
  try {
    const response = await axiosInstance.get(`/ipt/${metaDataRegId}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch IPT history data');
  }
};

export const getIndustrialHistoryData = async (metaDataRegId) => {
  try {
    const response = await axiosInstance.get(`/industry/${metaDataRegId}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch industrial history data');
  }
};

export const getItiHistoryData = async (metaDataRegId) => {
  try {
    const response = await axiosInstance.get(`/iti/${metaDataRegId}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch ITI history data');
  }
};

// Fetching select for state and district
export const getIptDistricts = async (selectedState) => {
  try {
    const response = await axiosInstance.get(`/getDistricts?state=${selectedState}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch districts');
  }
};

export const getIptIndustryName = async (selectedState, selectedDistrict) => {
  try {
    const response = await axiosInstance.get(`/getStateAndDistrict?state=${selectedState}&district=${selectedDistrict}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch industry names');
  }
};

// Fetching profile details
export const getProfileDetails = async () => {
  try {
    const response = await axiosInstance.get('/user/me');
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch profile details');
  }
};

//Dashboard details
export const getItiRegisteredCount = async () => {
  try {
    const response = await axiosInstance.get('/itiMaster/Iticount');
    return response.data;
  } catch (error) {
    throw new Error('Error fetching ITI registered count');
  }
};

export const getIndustryPartnersCount = async () => {
  try {
    const response = await axiosInstance.get('/last-sno');
    return response.data;
  } catch (error) {
    throw new Error('Error fetching industry partners count');
  }
};

export const getStudentsOfferedTrainingCount = async () => {
  try {
    const response = await axiosInstance.get('/iptMaster/iptCount');
    return response.data;
  } catch (error) {
    throw new Error('Error fetching students offered training count');
  }
};

export const IndustrygenerateReport = async (format, states, districts) => {
  console.log('States:', states); // Debugging log
  console.log('Districts:', districts); // Debugging log

  const stateParam = states.includes('allStates') ? 'allStates' : states.join(',');
  const districtParam = districts.includes('allDistricts') ? 'allDistricts' : districts.join(',');
  const url = `/generateReport?format=${format}&states=${stateParam}&districts=${districtParam}`;

  console.log('Request URL:', url); // Debugging log

  try {
    const response = await axiosInstance.get(url, { responseType: 'blob' });

    console.log('Response received:', response); // Debugging log

    let fileName = `industryReport.${format}`;
    let mimeType = 'application/octet-stream';

    if (format === 'pdf') {
      mimeType = 'application/pdf';
    } else if (format === 'html') {
      mimeType = 'text/html';
    } else if (format === 'xml') {
      mimeType = 'application/xml';
    } else if (format === 'xlsx') {
      mimeType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    }

    const blob = new Blob([response.data], { type: mimeType });
    saveAs(blob, fileName);
    
    return 'Report downloaded successfully';
  } catch (error) {
    console.error('Error in generating report:', error); // Debugging log
    throw new Error('Error generating report: ' + error.message);
  }
};
export const getIndustryDistricts = async (selectedState) => {
  try {
    const response = await axiosInstance.get(`/getDistricts?state=${selectedState}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch districts');
  }
};
// Fetching select for state and district
export const getItiDistricts = async (selectedState) => {
  try {
    const response = await axiosInstance.get(`/itiMaster/districts?state=${selectedState}`);
    console.log('Districts fetched:', response.data); // Debugging log
    return response.data;
  } catch (error) {
    console.error('Failed to fetch districts:', error);
    return [];
  }
};

export const getItiName = async (selectedState, selectedDistrict) => {
  try {
    const response = await axiosInstance.get(`/itiMaster/itiNames?state=${selectedState}&district=${selectedDistrict}`);
    console.log('ITI Names fetched:', response.data); // Debugging log
    return response.data;
  } catch (error) {
    console.error('Failed to fetch ITI names:', error);
    return [];
  }
};

export const getItiStates = async () => {
  try {
    const response = await axiosInstance.get(`/itiMaster/allStates`);
    console.log('ITI States fetched:', response.data); // Debugging log
    return response.data;
  } catch (error) {
    console.error('Failed to fetch ITI States:', error);
    return [];
  }
};

export const itigenerateReport = async (format, state, district, itiName) => {
  try {
    // Step 1: Fetch valid districts for the selected states
    const validDistricts = await getItiDistricts(state);
    console.log('Valid districts:', validDistricts);

    // Step 2: Filter out districts that are not valid for the selected state
    const filteredDistricts = district.filter(dist => validDistricts.includes(dist));
    console.log('Filtered districts:', filteredDistricts);

    if (filteredDistricts.length === 0) {
      throw new Error('Selected districts are not valid for the selected state(s).');
    }

    
    const districtParam = filteredDistricts.join(',');
    const url = `/itiReport/generateItiReport?format=${format}&state=${encodeURIComponent(state)}&district=${encodeURIComponent(districtParam)}&itiName=${encodeURIComponent(itiName)}`;
    const response = await axiosInstance.get(url, { responseType: 'blob' });
    const mimeType = format === 'pdf' 
      ? 'application/pdf' 
      : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    
    const blob = new Blob([response.data], { type: mimeType });
    const downloadUrl = window.URL.createObjectURL(blob);

    return { data: downloadUrl };
  } catch (error) {
    console.error('Error generating report:', error);
    throw error;
  }
};


                  //    IPT REPORT GET DITRICT BY STATES

// Fetching select for state and district

export const getIptreportDistricts = async (selectedState) => {
  try {
    const response = await axiosInstance.get(`/iptReportGenerateTest/getDistricts?states=${selectedState}`);
    return response.data;  // Ensure this matches the structure you are mapping in the fetch function
  } catch (error) {
    throw new Error('Failed to fetch districts');
  }
};

export const getIptReportIndustryName = async (selectedDistrict, selectedYear) => {
  try {

    const response = await axiosInstance.get(`/iptReportGenerateTest/getIndustryNames?districts=${selectedDistrict}&iptYears=${selectedYear}`);
    
    console.log('ITI Names fetched:', response.data); // Debugging log
    return response.data;
  } catch (error) {
    console.error('Failed to fetch ITI names:', error);
    return [];
  }
};

export const generateIptReport = async (format, selectedIndustries) => {
  // Ensure selectedIndustries is defined and has a value
  if (!selectedIndustries || selectedIndustries.length === 0) {
    throw new Error('No industries selected.');
  }

  const url = `/iptReportGen/generateIptReport?format=${format}&industryName=${encodeURIComponent(selectedIndustries)}`;
  console.log('Request URL:', url); // Debugging log

  try {
    const response = await axiosInstance.get(url, { responseType: 'blob' });

    console.log('Response received:', response); // Debugging log

    return response; // Return the response instead of triggering download
  } catch (error) {
    console.error('Error in generating report:', error); // Debugging log
    throw new Error('Error generating report: ' + error.message);
  }
};






