import React from 'react';
import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Divider,
  Box,
} from '@mui/material';
import {
  Dashboard,
  Work,
  Description,
  Person,
  Assessment,
  People,
  Approval,
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';
import PropTypes from 'prop-types';

const drawerWidth = 240;

/**
 * Get icon component based on label.
 * @param {string} label - Menu item label
 * @returns {JSX.Element} Icon component
 */
const getIcon = (label) => {
  const iconMap = {
    'Dashboard': <Dashboard />,
    'Browse Internships': <Work />,
    'My Applications': <Description />,
    'Profile': <Person />,
    'My Internships': <Work />,
    'Review Applications': <Description />,
    'Approval Center': <Approval />,
    'Representatives': <People />,
    'Internships': <Work />,
    'Withdrawals': <Description />,
    'Reports': <Assessment />,
  };
  return iconMap[label] || <Dashboard />;
};

/**
 * Sidebar navigation component.
 * @param {Object} props - Component props
 * @param {Array<Object>} props.menuItems - Menu items with label and path
 * @returns {JSX.Element} Sidebar
 */
const Sidebar = ({ menuItems }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
        },
      }}
    >
      <Toolbar />
      <Box sx={{ overflow: 'auto' }}>
        <List>
          {menuItems.map((item, index) => (
            <React.Fragment key={item.path}>
              {item.divider && <Divider sx={{ my: 1 }} />}
              <ListItem disablePadding>
                <ListItemButton
                  selected={location.pathname === item.path}
                  onClick={() => handleNavigation(item.path)}
                  sx={{
                    '&.Mui-selected': {
                      backgroundColor: 'primary.light',
                      color: 'primary.contrastText',
                      '&:hover': {
                        backgroundColor: 'primary.main',
                      },
                      '& .MuiListItemIcon-root': {
                        color: 'primary.contrastText',
                      },
                    },
                  }}
                >
                  <ListItemIcon>
                    {getIcon(item.label)}
                  </ListItemIcon>
                  <ListItemText primary={item.label} />
                </ListItemButton>
              </ListItem>
            </React.Fragment>
          ))}
        </List>
      </Box>
    </Drawer>
  );
};

Sidebar.propTypes = {
  menuItems: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      path: PropTypes.string.isRequired,
      divider: PropTypes.bool,
    })
  ).isRequired,
};

export default Sidebar;
