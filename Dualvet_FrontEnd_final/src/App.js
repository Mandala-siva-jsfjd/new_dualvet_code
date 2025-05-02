import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

// Import pages
import Login from './pages/login/Login';
import Dashboard from './pages/Dashboard';
import IndustryPartner from './pages/IndustryPartner';
import ItiDetails from './pages/ItiDetails';
import InplantTraining from './pages/InplantTraining';
import Setting from './pages/Setting';
import ContactUs from './pages/footer/ContactUs';
import Disclaimer from './pages/footer/Disclaimer';
import PrivacyPolicy from './pages/footer/PrivacyPolicy';
import Faq from './pages/footer/Faq';
// import Dashboardsample from "./pages/Dashboard1"
// Import security components
import PrivateRoutes from './security/PrivateRoutes';
import { AuthProvider } from './security/AuthContext';

// Import reports
import InplantTriningReport from './pages/Reports/InplantTriningReport';
import IndustryReport from './pages/Reports/IndustryReport';
import ItiReport from './pages/Reports/ItiReport';

// Route configuration
const routes = [
  { path: "/", element: <Login /> },
  { path: "/dashboard", element: <PrivateRoutes><Dashboard /></PrivateRoutes> },
  { path: "/industryPartner", element: <PrivateRoutes><IndustryPartner /></PrivateRoutes> },
  { path: "/itiDetails", element: <PrivateRoutes><ItiDetails /></PrivateRoutes> },
  { path: "/inplantTraining", element: <PrivateRoutes><InplantTraining /></PrivateRoutes> },
  { path: "/setting", element: <PrivateRoutes><Setting /></PrivateRoutes> },
  { path: "/contact-us", element: <PrivateRoutes><ContactUs /></PrivateRoutes> },
  { path: "/disclaimer", element: <PrivateRoutes><Disclaimer /></PrivateRoutes> },
  { path: "/privacy-policy", element: <PrivateRoutes><PrivacyPolicy /></PrivateRoutes> },
  { path: "/faq", element: <PrivateRoutes><Faq /></PrivateRoutes> },
  { path: "/industryreport", element: <PrivateRoutes><IndustryReport /></PrivateRoutes> },
  { path: "/itireport", element: <PrivateRoutes><ItiReport /></PrivateRoutes> },
  { path: "/inplanttrainingreport", element: <PrivateRoutes><InplantTriningReport /></PrivateRoutes> },
  // { path: "/dashboard1", element: <PrivateRoutes><Dashboardsample/></PrivateRoutes> },

];

// Main App component
const App = () => {
  return (
    <BrowserRouter>
      {/* Wrap AuthProvider inside BrowserRouter */}
      <AuthProvider>
        <Routes>
          {routes.map(({ path, element }) => (
            <Route key={path} path={path} element={element} />
          ))}
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
