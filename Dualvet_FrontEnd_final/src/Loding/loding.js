import { Backdrop, CircularProgress } from "@mui/material";
import React from "react";

const Loading = ({ open }) => {
  return (
    <Backdrop sx={{ color: "#fff", zIndex: 30000 }} open={open}>
      <CircularProgress color="inherit" />
    </Backdrop>
  );
};

export default Loading;