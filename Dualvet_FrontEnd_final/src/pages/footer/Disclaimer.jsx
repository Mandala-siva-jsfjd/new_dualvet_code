import React from 'react'
import { Box, Typography } from "@mui/material";

const Disclaimer = () => {
  return (
    <>
    <Typography
      variant="h5"
      className="page-title text-centered"
      color="primary"
      style={{height:"22px",}}

    >
      Legal Disclaimer
    </Typography>

    <Box sx={{ my: 5,textAlign:"justify" }}>
      Tata Community Initiatives Trust retains copyright on all the text,
      graphics and trademarks created on this site for Tata STRIVE. Images
      taken from External Sources are used with appropriate licensing and may
      not be modified, reconfigured or repurposed for any use. You may print
      copies of the information on this site for your personal use and store
      the files on your computer for personal use. You may not distribute text
      or graphics to others without the express written consent of Tata
      Community Initiatives Trust. Also, you may not, without our permission,
      copy and distribute this information on any other server, or modify or
      reuse text or graphics on this or any another system. No reproduction of
      any part of the site may be sold or distributed for commercial gain, nor
      shall it be modified or incorporated in any other work, publication or
      site, whether in hard copy or electronic format, including postings to
      any other site. Tata Community Initiatives Trust reserves all other
      rights. The information on this site has been included in good faith and
      is for general purposes only. It should not be relied upon for any
      specific purpose and no representation or warranty is given as regards
      its accuracy or completeness. No information on this site shall
      constitute an invitation or assurance of employment in any Tata company
      or any other company. Neither Tata Community Initiatives Trust, Tata
      Sons and Tata affiliates, nor their or their affiliates' officers,
      employees or agents shall be liable for any loss, damage or expense
      arising out of any access to or use of this site or any site linked to
      it for offering employment, including, without limitation, any loss of
      profit, indirect, incidental or consequential loss.
    </Box>
  </>
  )
}

export default Disclaimer