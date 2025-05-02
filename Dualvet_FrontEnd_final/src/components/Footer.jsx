import { Divider, Stack } from "@mui/material";
import React from "react";
import { Link } from "react-router-dom";
import "../assets/css/Footer.css";

const Footer = () => {
  return (
    <footer className="footer-main">
      <Stack direction="row" spacing={6} flexWrap="wrap"  justifyContent="center">
        <Link to="/contact-us" className="footer-link">
          Contact Us
        </Link>
        <Divider orientation="vertical" flexItem className="divider" />
        <Link to="/disclaimer" className="footer-link">
          Disclaimer
        </Link>
        <Divider orientation="vertical" flexItem className="divider" />
        <Link to="/privacy-policy" className="footer-link">
          Privacy Policy
        </Link>
        <Divider orientation="vertical" flexItem className="divider" />
        <Link to="https://tatastrive.com" target="_blank" className="footer-link">
          @Tata STRIVE
        </Link>
        <Divider orientation="vertical" flexItem className="divider" />
      </Stack>
    </footer>
  );
};

export default Footer;
