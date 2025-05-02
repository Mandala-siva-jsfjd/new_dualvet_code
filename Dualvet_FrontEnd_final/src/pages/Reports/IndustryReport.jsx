import React, { useState, } from 'react';
import { IndustryReportForm } from '../Reports/IndustryReportform';
import { IndustrygenerateReport } from '../../components/axios/services';
import { Container, Typography, Box } from '@mui/material';
const IndustryReport = () => {
  const [result, setResult] = useState('');
  const [key, setKey] = useState(0); // Key to force re-render

  const handleGenerateReport = async (format, states, districts) => {
    //console.log('Generating report with params:', format, states, districts); // Debugging log
    // Ensure states and districts are arrays
    if (!Array.isArray(states)) {
      states = [states];
    }
    if (!Array.isArray(districts)) {
      districts = [districts];
    }

    try {
      const message = await IndustrygenerateReport(format, states, districts);
      if (!message.includes('Error')) {
        // Reset form by changing the key
        setKey(prevKey => prevKey + 1);
        alert('Report downloaded successfully');
      }
    } catch (error) {
     // console.error('Error generating report:', error); // Debugging log
      setResult('Error generating report: ' + error.message);
    }
  };

  return (
    <Container>
      <Box sx={{ textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>
          Industry Report
        </Typography>
      </Box>
      <IndustryReportForm key={key} onGenerateReport={handleGenerateReport} />
      <Box mt={2}>
        <Typography variant="body1" color={result.includes('Error') ? 'error' : 'textPrimary'}>
          {result}
        </Typography>
      </Box>
      
    </Container>
  );
};

export default IndustryReport;