import React, { useState, useContext, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaBars } from 'react-icons/fa';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import ListItemIcon from '@mui/material/ListItemIcon';
import Logout from '@mui/icons-material/Logout';
import Footer from "../components/Footer";
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import '../assets/css/Sidebar.css';
import logo from '../assets/images/logo1.png';
import { AuthContext } from '../security/AuthContext';
import { Avatar } from '@mui/material';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import { getProfileDetails } from './axios/services';
import { menuItem } from "../components/icons/menuItem";
//import ExpandLess from '@mui/icons-material/ExpandLess';
//import ExpandMore from '@mui/icons-material/ExpandMore';
const Sidebar = ({ children }) => {
    const { logout } = useContext(AuthContext);
    const [isOpen, setIsOpen] = useState(false);
    const [dropdown, setDropdown] = useState(null);
    const [profile, setProfile] = useState({});
    const [anchorEl, setAnchorEl] = useState(null);
    const [isProfileOpen, setIsProfileOpen] = useState(false); // State for profile dialog
    const open = Boolean(anchorEl);

    // Toggle Sidebar
    const toggle = () => setIsOpen(!isOpen);

    // Handle Logout
    const handleLogout = async () => {
        await logout();
    };

    // Handle Account Menu
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    // Handle Dropdown Menu
    const handleDropdown = (index) => {
        setDropdown(dropdown === index ? null : index);
    };

    // Fetch Profile Details
    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const data = await getProfileDetails();
                setProfile(data);
            } catch (error) {
                console.error('Failed to fetch profile details', error);
            }
        };
        fetchProfile();
    }, []);

    // Open/Close Profile Dialog
    const handleProfileOpen = () => {
        setIsProfileOpen(true);
    };

    const handleProfileClose = () => {
        setIsProfileOpen(false);
    };

    return (
        <div className="container">
            <div className={`sidebar ${isOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
                <div className="top_section">
                    <h1 className={`logo ${isOpen ? 'logo-visible' : 'logo-hidden'}`}>
                        <img className='navbar-logo' src={logo} alt="Logo" />
                    </h1>
                    <div className={`bars ${isOpen ? 'bars-open' : 'bars-closed'}`}>
                        <FaBars onClick={toggle} />
                    </div>
                </div>
                <ul>
    {menuItem.map((item, index) => (
        <li key={index}>
            <Link
                to={item.path}
                className="link"
                onClick={() => item.subItems ? handleDropdown(index) : null}
            >
                <div className="icon" data-tip={isOpen ? '' : item.name}>{item.icon}</div>
                <div className={`link_text ${isOpen ? 'link-text-visible' : 'link-text-hidden'}`}>
                    {item.name}
                </div>
                {/* {item.subItems && (
                   <div style={{ display: "flex" }}>
                   <div className="expand-icon">
                     {dropdown === index ? <ExpandLess /> : <ExpandMore />}
                   </div>
                 </div>
                 
               
                )} */}
            </Link>
            {item.subItems && dropdown === index && (
                <ul className="sub-menu">
                    {item.subItems.map((subItem, subIndex) => (
                        <li key={subIndex} className="sub-menu-item">
                            <Link className="link" to={subItem.path}>
                                <div className="icon" data-tip={isOpen ? '' : subItem.name}>{subItem.icon}</div>
                                <div className={`link_text ${isOpen ? 'link-text-visible' : 'link-text-hidden'}`}>{subItem.name}</div>
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </li>
    ))}
</ul>

            </div>

            <main className='navbar-main' style={{ marginLeft: isOpen ? "250px" : "60px" }}>
                <nav className='navbar'>
                    <h1 className='navbar-title-name'>DUAL VET</h1>
                    <React.Fragment>
                        <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center' }}>
                            <Tooltip >
                                <IconButton
                                    onClick={handleClick}
                                    size="small"
                                    sx={{ ml: 2 }}
                                    aria-controls={open ? 'account-menu' : undefined}
                                    aria-haspopup="true"
                                    aria-expanded={open ? 'true' : undefined}
                                >
                                    <Avatar sx={{ width: 32, height: 32 }}>
                                        {profile?.firstname ? profile.firstname[0] : 'U'}
                                    </Avatar>
                                </IconButton>
                            </Tooltip>
                        </Box>
                        <Menu
                            anchorEl={anchorEl}
                            id="account-menu"
                            open={open}
                            onClose={handleClose}
                            onClick={handleClose}
                            transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                            anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
                        >
                            <MenuItem onClick={handleProfileOpen}>
                         
                            <ListItemIcon>
                                    <Avatar sx={{ width: 32, height: 32,marginLeft:-1 }} fontSize="small" />
                                </ListItemIcon>
                                Profile
                            </MenuItem>
                            <MenuItem onClick={handleLogout}>
                                <ListItemIcon>
                                    <Logout fontSize="small" />
                                </ListItemIcon>
                                Logout
                            </MenuItem>
                        </Menu>
                    </React.Fragment>
                </nav>
                <div className="main-content">
                    {children}
                </div>
                <Footer />
            </main>

            {/* Profile Dialog */}
            <Dialog open={isProfileOpen} onClose={handleProfileClose}>
                <DialogTitle>Profile Details</DialogTitle>
                <DialogContent>
                    <p><strong>First Name:</strong> {profile.firstname || 'N/A'}</p>
                    <p><strong>Last Name:</strong> {profile.lastname || 'N/A'}</p>
                    <p><strong>Email:</strong> {profile.email || 'N/A'}</p>
                    <p><strong>Phone:</strong> {profile.contactNumber || 'N/A'}</p>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleProfileClose}>Close</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default Sidebar;
