import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import { AccountCircle, Logout } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import useNotification from '../../hooks/useNotification';
import authService from '../../services/authService';
import { APP_NAME } from '../../utils/constants';
import { formatRole } from '../../utils/formatters';

/**
 * Navigation bar component displayed at the top of all authenticated pages.
 * @returns {JSX.Element} Navigation bar
 */
const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const { showSuccess, showError } = useNotification();
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = async () => {
    try {
      if (user?.userId) {
        await authService.logout(user.userId);
      }
      logout();
      showSuccess('Logged out successfully');
      navigate('/login');
    } catch (error) {
      showError('Error during logout');
      // Still logout locally even if server request fails
      logout();
      navigate('/login');
    }
    handleClose();
  };

  const handleChangePassword = () => {
    handleClose();
    navigate('./change-password');
  };

  return (
    <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          {APP_NAME}
        </Typography>
        
        {user && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Typography variant="body2">
              {user.name} ({formatRole(user.role)})
            </Typography>
            
            <IconButton
              size="large"
              aria-label="account menu"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <AccountCircle />
            </IconButton>
            
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem onClick={handleChangePassword}>Change Password</MenuItem>
              <MenuItem onClick={handleLogout}>
                <Logout fontSize="small" sx={{ mr: 1 }} />
                Logout
              </MenuItem>
            </Menu>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
