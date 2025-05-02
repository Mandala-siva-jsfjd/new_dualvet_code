import React, { useState, useEffect } from 'react';
import { 
  Button, 
  FormControl, 
  FormControlLabel, 
  CircularProgress, 
  Radio, 
  RadioGroup, 
  InputLabel, 
  Select, 
  Grid, 
  MenuItem, 
  Checkbox, 
  ListItemText, 
  Box 
} from '@mui/material';
import { STATES_LIST } from '../../constant/Constant';
import Loading from '../../Loding/loding'; // Adjust the import path as needed
import { getIndustryDistricts } from "../../components/axios/services";
export const IndustryReportForm = ({ onGenerateReport }) => {
  const [format, setFormat] = useState('xlsx');
  const [selectedStates, setSelectedStates] = useState([]);
  const [selectedDistricts, setSelectedDistricts] = useState([]);
  const [stateOptions, setStateOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingDistricts, setLoadingDistricts] = useState(false);
  const [districts, setDistricts] = useState([]);
  useEffect(() => {
    // Initialize states dropdown options
    setStateOptions(
      STATES_LIST.map(state => ({ value: state, label: state }))
    );
  }, []);

  useEffect(() => {
    const fetchDistricts = async () => {
      if (selectedStates.length > 0) {
        setLoadingDistricts(true);
        try {
          const data = await getIndustryDistricts(selectedStates.join(','));
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
  }, [selectedStates]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    await onGenerateReport(format, selectedStates, selectedDistricts);
    setLoading(false);
  };

  const handleDistrictChange = (event) => {
    setSelectedDistricts(event.target.value);
  };

  return (
    <Box component="form" onSubmit={handleSubmit}>

      <Grid container spacing={2}>
      <FormControl fullWidth >
        <RadioGroup
          row
          aria-label="format"
          name="format"
          value={format}
          onChange={(e) => setFormat(e.target.value)}
          sx={{ paddingLeft: "16px" }}

        >
          <FormControlLabel value="xlsx" control={<Radio />} label="XLSX" />
          <FormControlLabel value="pdf" control={<Radio />} label="PDF" />
        </RadioGroup>
      </FormControl>
      <Grid item xs={12} sm={6} md={4}>
      <FormControl fullWidth >
        <InputLabel>Select State</InputLabel>
        <Select
          multiple
          label="Select-State"
          value={selectedStates}
          onChange={(e) => setSelectedStates(e.target.value)}
          renderValue={(selected) => selected.join(', ')}
        >
          {stateOptions.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              <Checkbox checked={selectedStates.indexOf(option.value) > -1} />
              <ListItemText primary={option.label} />
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      </Grid>
      <Grid item xs={12} sm={6} md={4}>
        <FormControl fullWidth >
          <InputLabel id="select-district-label">Select District</InputLabel>
          {loadingDistricts ? (
            <CircularProgress size={24} />
          ) : (
            <Select
              multiple
              value={selectedDistricts}
              label="Select-District"
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
      </Grid>
      <Box mt={2}>
        <Button 
          type="submit" 
          variant="contained" 
          color="primary" 
          disabled={loading}
        >
          Generate Report
        </Button>
      </Box>
      <Loading open={loading} /> {/* Display the loading component */}
    </Box>
  );
};