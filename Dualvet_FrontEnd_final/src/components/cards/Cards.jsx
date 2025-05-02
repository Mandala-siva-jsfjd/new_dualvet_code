import React, { useEffect, useState } from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

const Cards = ({ title, icon, count }) => {
  const [displayCount, setDisplayCount] = useState(0);

  useEffect(() => {
    if (count !== null && count !== undefined) {
      let intervalId;
      let currentCount = 0;

      // Start the rolling number animation
      intervalId = setInterval(() => {
        currentCount += Math.floor(Math.random() * 100); // Increment by a random number
        setDisplayCount(currentCount);

        // Stop the animation and set the actual count after a short delay
        if (currentCount >= count) {
          clearInterval(intervalId);
          setDisplayCount(count);
        }
      }, 200); // Adjust the speed of rolling here (100ms for faster, 200ms for slower)
    }
  }, [count]);

  return (
    <Card sx={{ minWidth: 305, borderRadius: '13px', boxShadow: 3,  }}>
      <CardContent>
        <Box display="flex" justifyContent="space-evenly" alignItems="center">
          <Box display="flex" alignItems="center">
            <Box mr={2}>
              {icon}
            </Box>
            <Typography variant="h6" component="div">
              {title}
            </Typography>
          </Box>
          {count !== undefined && (
            <Typography variant="h6" component="span" >
              {displayCount}
            </Typography>
          )}
        </Box>
      </CardContent>
    </Card>
  );
};

export default Cards;
