import React, { useRef } from 'react';
import { toast } from 'react-toastify';
import {  FormControl,  Input } from '@mui/material';
export const FileUpload = ({ onFileChange }) => {
  const fileInputRef = useRef(null);

   const handleFile = (e) => {
    const fileTypes = [
      'application/vnd.ms-excel',
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel.sheet.macroEnabled.12',
      'text/csv',
    ];
    const selectedFile = e.target.files[0];
    if (selectedFile && fileTypes.includes(selectedFile.type)) {
      const reader = new FileReader();
      reader.readAsArrayBuffer(selectedFile);
      reader.onload = (e) => {
        onFileChange(e.target.result, selectedFile.name);
      };
    } 
    else {
      toast.error('Please select only excel file types');
      // if (fileInputRef.current) fileInputRef.current.value = '';
      if (fileInputRef.current) {
        fileInputRef.current.value = ''; // Clear file input
      }
    }
    
  };

  return (
    <FormControl  margin="normal">
    <Input
      id="file-upload"
      type="file"
      inputRef={fileInputRef}
      onChange={handleFile}
      required
    />
  </FormControl>
  );
};

