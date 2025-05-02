
import { FaTh, FaRegBuilding, FaUserTie, FaUniversity } from 'react-icons/fa';
import FolderIcon from '@mui/icons-material/Folder';
import PaddingIcon from '@mui/icons-material/Padding';
import ListAltIcon from '@mui/icons-material/ListAlt';
import MarginIcon from '@mui/icons-material/Margin';
export const menuItem = [
    {
        path: "/dashboard",
        name: "Dashboard",
        icon: <FaTh />
    },
    {
        path: "/itiDetails",
        name: "ITI Academics",
        icon: <FaUniversity />
    },
    {
        path: "/industryPartner",
        name: "Industry Partner",
        icon: <FaUserTie />
    },
    {
        path: "/inplantTraining",
        name: "Inplant Training",
        icon: <FaRegBuilding />
    },
// {
//     path: "/dashboard1",
//     name: "Dashboard sample",
//     icon: <FaRegBuilding />
// },
    {
        path: "",
        name: "Reports",
        icon: <FolderIcon />,
        subItems: [
            {
                path: "/itireport",
                name: "ITI Report",
                icon: <MarginIcon />
            },
            {
                path: "/industryreport",
                name: "Industry Report",
                icon: <ListAltIcon />
            },
            {
                path: "/inplanttrainingreport",
                name: "Inplant Training Report",
                icon: <PaddingIcon />
            }
        ]
    }
];