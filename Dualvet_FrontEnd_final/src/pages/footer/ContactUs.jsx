import React from 'react'
import { Box, Stack, Typography } from "@mui/material";

const ContactUs = () => {
  return (
    <>
    <Typography
      variant="h5"
      className="page-title text-centered"
      color="primary"
      style={{height:"50px" ,textAlign:'center'}}
    >
      Contact Us
    </Typography>
    <Stack
      direction="row"
      spacing={2}
      justifyContent="space-around"
      sx={{ my: 5 }}
    >
      <div>
        <Typography color="primary" variant="subtitle1">
          REGISTERED OFFICE
        </Typography>
        <Box>
          Tata Community Initiatives Trust,
          <br />
          c/o Tata Services Ltd Jeevan Bharti Tower I,
          <br />
          10th Floor, 124 Connaught Circus,
          <br />
          New Delhi - 110001 | Tel. No.: 23327072
        </Box>
      </div>

      <div>
        <Typography color="primary" variant="subtitle1">
          MUMBAI OFFICE
        </Typography>
        <Box>
          2nd Floor, Army Navy Building, Fort,
          <br />
          Mumbai - 400 001 | Tel. No. :+91 22 6665 7459
          <br />
          TOLL FREE NUMBER
          <br />
          1800-419-2112
        </Box>
      </div>
    </Stack>
    <Box display="grid" justifyContent="center">
      <Typography color="primary" variant="subtitle1">
        Email address
      </Typography>
      STRIVE@tatasustainability.com
    </Box>
  </>
  )
}

export default ContactUs
