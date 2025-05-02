import React, { useState, useEffect } from 'react';
import { FormControl, InputLabel, Select, MenuItem, Checkbox, ListItemText, CircularProgress, Typography, Grid, RadioGroup, FormControlLabel, Radio, Box, Button } from '@mui/material';
//import { STATES_LIST } from '../../constant/Constant';
import { getItiName, getItiDistricts, itigenerateReport,getItiStates } from "../../components/axios/services";
import Loading from '../../Loding/loding';

const ItiReport = () => {
  const [loading, setLoading] = useState(false);
  const [selectedStates, setSelectedStates] = useState([]);
  const [selectedDistricts, setSelectedDistricts] = useState([]);
  const [selectedItiNames, setSelectedItiNames] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [itiNames, setItiNames] = useState([]);
  const [loadingDistricts, setLoadingDistricts] = useState(false);
  const [loadingStates, setLoadingStates] = useState(false);
  const [states, setStates] = useState([]);





  const [loadingItiNames, setLoadingItiNames] = useState(false);
  const [format, setFormat] = useState('xlsx'); // Default format

  // Handle State Change
  const handleStateChange = (event) => {
    const { value } = event.target;
    setSelectedStates(value);
    setSelectedDistricts([]);
    setSelectedItiNames([]);
    setDistricts([]);
    setItiNames([]);
  };

  // Handle District Change
  const handleDistrictChange = (event) => {
    const { value } = event.target;
    setSelectedDistricts(value);
    setSelectedItiNames([]);
    setItiNames([]);
  };

  // Handle ITI Name Change
  const handleItiNameChange = (event) => {
    const { value } = event.target;
    setSelectedItiNames(value);
  };

  // Handle Format Change
  const handleFormatChange = (event) => {
    setFormat(event.target.value);
  };

  // Fetch Districts whenever states change
  useEffect(() => {
    const fetchDistricts = async () => {
      if (selectedStates.length > 0) {
        setLoadingDistricts(true);
        try {
          const promises = selectedStates.map(state => getItiDistricts(state));
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



  //FETCH ITI STATES DYNAMICALLY 
  useEffect(() => {
    const fetchStates = async () => {
      setLoadingStates(true);
      try {
        const response = await getItiStates(); // Replace with your API function
        setStates(response);
      } catch (error) {
        console.error('Error fetching states:', error);
        setStates([]);
      } finally {
        setLoadingStates(false);
      }
    };
  
    fetchStates();
  }, []);

  
  // Fetch ITI Names whenever states or districts change
  useEffect(() => {
    const fetchItiNames = async () => {
      if (selectedStates.length > 0 && selectedDistricts.length > 0) {
        setLoadingItiNames(true);
        try {
          const validCombinations = selectedStates.flatMap(state =>
            selectedDistricts
              .filter(district => districts.includes(district))
              .map(district => ({ state, district }))
          );
  
          if (validCombinations.length === 0) {
            //console.warn('No valid state-district combinations found');
            setItiNames([]);
            setLoadingItiNames(false);
            return;
          }
          const promises = validCombinations.map(({ state, district }) => getItiName(state, district));
          const results = await Promise.all(promises);
          const allItiNames = Array.from(new Set(results.flat()));
          setItiNames(allItiNames);
        } catch (error) {
          console.error('Failed to fetch ITI names:', error);
          setItiNames([]);
        } finally {
          setLoadingItiNames(false);
        }
      }
    };
    fetchItiNames();
  }, [selectedStates, selectedDistricts, districts]);
  

  // Generate Report
  const generateReport = async () => {
    if (selectedStates.length === 0 || selectedDistricts.length === 0 || selectedItiNames.length === 0) {
      alert("Please select all required fields.");
      return;
    }
  
    try {
      setLoading(true);
      const response = await itigenerateReport(format, selectedStates, selectedDistricts, selectedItiNames);
  
      // Trigger the download directly using the returned blob URL
      const link = document.createElement('a');
      link.href = response.data;
      link.setAttribute('download', `ITIReport.${format}`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      alert('Report downloaded successfully');
      // Reset the dropdown selections
      setSelectedStates([]);
      setSelectedDistricts([]);
      setSelectedItiNames([]);
      setDistricts([]);
      setItiNames([]);
    } catch (error) {
      //console.error('Error generating report:', error);
      alert("Failed to generate report. Please try again.");
    } finally {
      setLoading(false);
    }
  };
  

  return (
    <Box p={1}>
      <Box sx={{ textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>
          ITI Report
        </Typography>
      </Box>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <FormControl fullWidth margin="normal">
            <RadioGroup
              row
              aria-label="format"
              name="format"
              value={format}
              onChange={handleFormatChange}
            >
              <FormControlLabel value="xlsx" control={<Radio />} label="XLSX" />
              <FormControlLabel value="pdf" control={<Radio />} label="PDF" />
            </RadioGroup>
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={4}>
  <FormControl fullWidth>
    <InputLabel id="select-state-label">Select State</InputLabel>
    <Select
      label="Select State"
      multiple
      value={selectedStates}
      onChange={handleStateChange}
      renderValue={(selected) => selected.join(', ')}
    >
      {states.map((state, index) => (
        <MenuItem key={index} value={state}>
          <Checkbox checked={selectedStates.indexOf(state) > -1} />
          <ListItemText primary={state} />
        </MenuItem>
      ))}
    </Select>
  </FormControl>
</Grid>

        <Grid item xs={12} sm={4}>
          <FormControl fullWidth>
            <InputLabel id="select-district-label">Select District</InputLabel>
            {loadingDistricts ? (
              <CircularProgress size={24} />
            ) : (
              <Select
                label="Select District"
                multiple
                value={selectedDistricts}
                onChange={handleDistrictChange}
                renderValue={(selected) => selected.join(', ')}
                disabled={districts.length === 0}
              >
                {districts.map((district, index) => (
                  <MenuItem key={index} value={district}>
                    <Checkbox checked={selectedDistricts.indexOf(district) > -1} />
                    <ListItemText primary={district} />
                  </MenuItem>
                ))}
              </Select>
            )}
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={4}>
          <FormControl fullWidth>
            <InputLabel id="select-iti-name-label">Select ITI Name</InputLabel>
            {loadingItiNames ? (
              <CircularProgress size={24} />
            ) : (
              <Select
                label="Select ITI Name"
                multiple
                value={selectedItiNames}
                onChange={handleItiNameChange}
                renderValue={(selected) => selected.join(', ')}
                disabled={itiNames.length === 0}
              >
                {itiNames.map((iti, index) => (
                  <MenuItem key={index} value={iti}>
                    <Checkbox checked={selectedItiNames.indexOf(iti) > -1} />
                    <ListItemText primary={iti} />
                  </MenuItem>
                ))}
              </Select>
            )}
          </FormControl>
        </Grid>
      </Grid>

      <Box mt={3}>
        <Button
          type="submit"
          variant="contained"
          color="primary"
          onClick={generateReport}
          disabled={loading}
        >
          {loading ? 'Generating...' : 'Generate Report'}
        </Button>
      </Box>
      <Loading open={loading} /> 
    </Box>
  );
};

export default ItiReport;
