import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Button, Box } from "@mui/material";
import { GridToolbar, GridToolbarQuickFilter } from "@mui/x-data-grid";

export const HistoryComponent = ({ loading, users, historyDownload }) => {
  // Table Columns Configuration
  const columns = [
    { field: "regId", headerName: "Register ID", flex: 1 },
    { field: "createdBy", headerName: "Created By", flex: 1 },
    { field: "fileName", headerName: "File Name", flex: 1 },
    { field: "records", headerName: "No of Records", flex: 1 },
    { field: "successRecords", headerName: "Success Records", flex: 1 },
    { field: "failedRecords", headerName: "Failed Records", flex: 1 },
    { field: "status", headerName: "Status", flex: 1 },
    { field: "uploadDate", headerName: "Upload Date", flex: 1 },
    {
      field: "actions",
      headerName: "Actions",
      flex: 1,
      renderCell: (params) => (
        <Button
          variant="contained"
          color="primary"
          onClick={() => historyDownload(params.row.regId)}
        >
          Download
        </Button>
      ),
    },
  ];

  // Custom Toolbar with Quick Filter
  function MyCustomToolbar(props) {
    return (
      <>
        <Box id="filter-panel">
          <GridToolbarQuickFilter />
        </Box>
        <GridToolbar {...props} />
      </>
    );
  }

  return (
    <div>
      {loading ? (
        <div>Loading...</div>
      ) : (
        <div>
          {/* DataGrid Table */}
          <DataGrid
            rows={users} // Fixed the undefined variable
            columns={columns}
            getRowId={(row) => row.regId}
            pageSizeOptions={[5, 10, 25]}
            initialState={{
              pagination: { paginationModel: { pageSize: 10 } },
              filter: {
                filterModel: {
                  items: [],
                  quickFilterExcludeHiddenColumns: true,
                },
              },
            }}
            loading={loading}
            autoHeight
            disableSelectionOnClick
            slots={{
              toolbar: MyCustomToolbar,
            }}
          />
        </div>
      )}
    </div>
  );
};