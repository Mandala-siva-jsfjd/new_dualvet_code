import React, { useState, useEffect } from 'react';
import { getIptReportIndustryName, getIptreportDistricts, generateIptReport } from "../../components/axios/services";
import { FormControl, InputLabel, Select, MenuItem, FormGroup, Box, Button, Typography, CircularProgress, FormLabel, Grid, Checkbox, ListItemText, RadioGroup, FormControlLabel, Radio } from '@mui/material';
import { STATES_LIST } from '../../constant/Constant';
import Loading from '../../Loding/loding';

const InplantTrainingReport = () => {
  const [selectedYear, setSelectedYear] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedStates, setSelectedStates] = useState([]);
  const [selectedDistricts, setSelectedDistricts] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [industries, setIndustries] = useState([]);
  const [loadingDistricts, setLoadingDistricts] = useState(false);
  const [loadingIndustries, setLoadingIndustries] = useState(false);
  const [selectedIndustries, setSelectedIndustries] = useState([]);
  const [format, setFormat] = useState('xlsx'); // Default format

  // Handle State Change
  const handleStateChange = (event) => {
    const { value } = event.target;
    setSelectedStates(value);
    setSelectedDistricts([]);
    setDistricts([]);
    setIndustries([]);
    setSelectedIndustries([]);
    setSelectedYear([]);
  };

  // Handle District Change
  const handleDistrictChange = (event) => {
    const { value } = event.target;
    setSelectedDistricts(value);
    setIndustries([]);
    setSelectedIndustries([]);
  };

  // Handle Industry Change
  const handleIndustryChange = (event) => {
    const { value } = event.target;
    setSelectedIndustries(value); // Storing only industryName
  };

  // Handle Year Change
  const handleYearChange = (event) => {
    const { value, checked } = event.target;
    setSelectedYear(prev => {
      if (checked) {
        return [...prev, value];
      } else {
        return prev.filter(year => year !== value);
      }
    });
  };
  
  const handleFormatChange = (event) => {
    setFormat(event.target.value);
  };

  // Fetch districts based on selected states
  useEffect(() => {
    const fetchDistricts = async () => {
      if (selectedStates.length > 0) {
        setLoadingDistricts(true);
        try {
          const promises = selectedStates.map(state => getIptreportDistricts(state));
          const results = await Promise.all(promises);
          const allDistricts = Array.from(new Set(results.flat()));
          setDistricts(allDistricts);
        } catch (error) {
          console.error('Error fetching districts:', error);
          setDistricts([]);
        } finally {
          setLoadingDistricts(false);
        }
      }
    };
    fetchDistricts();
  }, [selectedStates]);

  // Fetch industries based on selected districts and years
  useEffect(() => {
    const fetchData = async () => {
      if (selectedDistricts.length > 0 && Array.isArray(selectedYear) && selectedYear.length > 0) {
        setLoadingIndustries(true);
        try {
          // Fetch data based on selected districts and years
          const data = await getIptReportIndustryName(
            selectedDistricts.join(','), 
            selectedYear.join(',')
          );
          // Filter out duplicate industry names
          const uniqueIndustries = Array.from(new Set(data.map(industry => industry.industryName)));
          setIndustries(uniqueIndustries); // Set unique industry names
        } catch (error) {
          console.error('Error fetching industry names:', error);
          setIndustries([]);
        } finally {
          setLoadingIndustries(false);
        }
      } else {
        setIndustries([]);
      }
    };
    fetchData();
  }, [selectedDistricts, selectedYear]);

  // Generate the report
  const generateReport = async () => {
    if (selectedStates.length === 0 || selectedDistricts.length === 0 || selectedIndustries.length === 0 || selectedYear.length === 0) {
      alert("Please select all required fields.");
      return;
    }
    try {
      setLoading(true);
      const response = await generateIptReport(format, selectedIndustries.join(','), selectedYear.join(',')); // No industry IDs here
      const mimeType = response.headers && response.headers['content-type']
        ? response.headers['content-type']
        : 'application/octet-stream';
      const blob = new Blob([response.data], { type: mimeType });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `InPlantTrainingReport.${format}`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      window.URL.revokeObjectURL(url);
      alert('Report downloaded successfully');
      setSelectedStates([]);
      setSelectedDistricts([]);
      setSelectedIndustries([]);
      setDistricts([]);
      setIndustries([]);
      setSelectedYear([]);
    } catch (error) {
      console.error('Error generating report:', error);
      alert("Failed to generate report. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box p={1}>
      <Box sx={{ textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>
          In-Plant Training Report
        </Typography>
      </Box>
      <Grid container spacing={1}>
        <FormControl fullWidth margin="normal">
          <RadioGroup
            row
            aria-label="format"
            name="format"
            value={format}
            onChange={handleFormatChange}
            sx={{ paddingLeft: "10px" }}
          >
            <FormControlLabel value="xlsx" control={<Radio />} label="XLSX" />
            <FormControlLabel value="pdf" control={<Radio />} label="PDF" />
          </RadioGroup>
        </FormControl>

        <Grid item xs={12} sm={6} md={4}>
          <FormControl fullWidth>
            <InputLabel id="select-state-label">Select State</InputLabel>
            <Select
              label="Select State"
              multiple
              value={selectedStates}
              onChange={handleStateChange}
              renderValue={(selected) => selected.join(', ')}
            >
              {STATES_LIST.map((state, index) => (
                <MenuItem key={index} value={state}>
                  <Checkbox checked={selectedStates.indexOf(state) > -1} />
                  <ListItemText primary={state} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={6} md={4}>
          <FormControl fullWidth>
            <InputLabel id="select-district-label">Select District</InputLabel>
            {loadingDistricts ? (
              <CircularProgress size={24} />
            ) : (
              <Select
                label="Select-District"
                multiple
                value={selectedDistricts}
                onChange={handleDistrictChange}
                renderValue={(selected) => selected.join(', ')}
                disabled={districts.length === 0}
              >
                {districts.length > 0 ? (
                  districts.map((district, index) => (
                    <MenuItem key={index} value={district}>
                      <Checkbox checked={selectedDistricts.indexOf(district) > -1} />
                      <ListItemText primary={district} />
                    </MenuItem>
                  ))
                ) : (
                  <MenuItem disabled>No districts available</MenuItem>
                )}
              </Select>
            )}
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={6} md={4}>
          <FormControl component="fieldset" sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
            <FormLabel component="legend" sx={{ marginRight: 2 }}>Inplant Training Year:</FormLabel>
            <FormGroup sx={{ display: 'flex', flexDirection: 'row' }}>
              <FormControlLabel
                control={
                  <Checkbox 
                    checked={selectedYear.indexOf('1') > -1} 
                    onChange={handleYearChange} 
                    value="1" 
                  />
                }
                label="1st Year"
              />
              <FormControlLabel
                control={
                  <Checkbox 
                    checked={selectedYear.indexOf('2') > -1} 
                    onChange={handleYearChange} 
                    value="2" 
                  />
                }
                label="2nd Year"
              />
            </FormGroup>
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={6} md={4}>
          <FormControl fullWidth>
            <InputLabel id="select-industry-label">Select Industry</InputLabel>
            {loadingIndustries ? (
              <CircularProgress size={24} />
            ) : (
              <Select
                label="Select-Industry"
                multiple
                value={selectedIndustries}
                onChange={handleIndustryChange}
                renderValue={(selected) => selected.join(', ')}
                disabled={industries.length === 0}
              >
                {industries.length > 0 ? (
                  industries.map((industry, index) => (
                    <MenuItem key={index} value={industry}>
                      <Checkbox checked={selectedIndustries.indexOf(industry) > -1} />
                      <ListItemText primary={industry} />
                    </MenuItem>
                  ))
                ) : (
                  <MenuItem disabled>No industries available</MenuItem>
                )}
              </Select>
            )}
          </FormControl>
        </Grid>
      </Grid>
<Box mt={2}>
        <Button 
          type="submit" 
          variant="contained" 
          color="primary" 
          onClick={generateReport}
          disabled={loading}
        >
          Generate Report
        </Button>
      </Box>
      <Loading open={loading} /> 
    </Box>
  );
};

export default InplantTrainingReport;