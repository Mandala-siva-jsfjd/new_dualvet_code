import { Navigate } from 'react-router-dom';
import React, { useContext } from 'react';
import Sidebar from '../components/Sidebar';
import { AuthContext } from '../security/AuthContext';

const PrivateRoutes = ({ children }) => {
  const { auth, loading } = useContext(AuthContext);

  if (loading) {
    return <div>Loading...</div>;
  }

  // console.log("Auth state in PrivateRoutes:", auth);

  return auth.isAuthenticated ? <Sidebar>{children}</Sidebar> : <Navigate to="/" />;
};

export default PrivateRoutes;