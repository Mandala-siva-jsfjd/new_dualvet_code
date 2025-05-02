import React, { useEffect, useState } from 'react';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import GroupsIcon from '@mui/icons-material/Groups';
import Groups3Icon from '@mui/icons-material/Groups3';
import Card from '../components/cards/Cards';
import {
  getItiRegisteredCount,
  getIndustryPartnersCount,
  getStudentsOfferedTrainingCount
} from '../components/axios/services';
import { Box, Typography, Grid,  } from '@mui/material';

const Dashboard = () => {
  const [itiRegisteredCount, setItiRegisteredCount] = useState(null);
  const [industryPartnersCount, setIndustryPartnersCount] = useState(null);
  const [studentsOfferedTrainingCount, setStudentsOfferedTrainingCount] = useState(null);

  useEffect(() => {
    const fetchCounts = async () => {
      try {
        const itiCount = await getItiRegisteredCount();
        setItiRegisteredCount(itiCount);
        const industryCount = await getIndustryPartnersCount();
        setIndustryPartnersCount(industryCount);
        const studentsCount = await getStudentsOfferedTrainingCount();
        setStudentsOfferedTrainingCount(studentsCount);
      } catch (error) {
        console.error('Error fetching counts:', error);
      }
    };

    fetchCounts();
  }, []);

  return (
    <Box >
    <Box sx={{ backgroundColor: '#DDEBF6', padding: 2, borderRadius: 2 }}>
  <Typography variant="h5" component="h3" gutterBottom>
    Dashboard
  </Typography>
  <Typography variant="subtitle1" component="h4">
    Dual VET in Development Cooperation: Key Elements, Success Factors, Opportunities and Limitations.
  </Typography>
</Box> 
<br />
<Grid container  sx={{ backgroundColor: '#DDEBF6', padding: 2, borderRadius: 2, boxShadow: 1 }}>
  <Grid container spacing={2}>
        <Grid  item xs={12} sm={6} md={4}>
          <Box >
            <Card 
              title="Registered ITIs" 
              icon={<PeopleAltIcon fontSize="large" />} 
              count={itiRegisteredCount} 
            />
          </Box>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Box >
            <Card 
              title="Industry Partners" 
              icon={<GroupsIcon fontSize="large" />} 
              count={industryPartnersCount} 
            />
          </Box>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Box >
            <Card 
              title="No of Trainees" 
              icon={<Groups3Icon fontSize="large" />} 
              count={studentsOfferedTrainingCount} 
            />
          </Box>
        </Grid>
        </Grid>
      </Grid>
  </Box>
  );
};

export default Dashboard;