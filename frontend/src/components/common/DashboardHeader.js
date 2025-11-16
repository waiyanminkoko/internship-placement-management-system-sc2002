import React from 'react';
import {
  Box,
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Avatar,
  Badge,
  Button,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import NotificationsIcon from '@mui/icons-material/Notifications';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LogoutIcon from '@mui/icons-material/Logout';

/**
 * Modern dashboard header component with user profile and notifications.
 * 
 * @param {Object} props - Component props
 * @param {Object} props.user - Current user object
 * @param {Function} props.onLogout - Logout handler
 * @param {Function} props.onMenuClick - Mobile menu toggle handler
 * @param {Function} props.onProfileClick - Profile click handler
 * @param {string} props.title - Page title
 * @param {number} props.notificationCount - Number of unread notifications
 * @returns {JSX.Element} Dashboard header
 */
const DashboardHeader = ({
  user,
  onLogout,
  onMenuClick,
  onProfileClick,
  title = 'Dashboard',
  notificationCount = 0,
}) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        bgcolor: '#fff',
        borderBottom: '1px solid',
        borderColor: 'grey.200',
        boxShadow: '0 1px 3px rgba(0,0,0,0.05)',
      }}
    >
      <Toolbar sx={{ py: 1 }}>
        {/* Mobile menu icon */}
        {isMobile && (
          <IconButton
            color="default"
            edge="start"
            onClick={onMenuClick}
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
        )}

        {/* Page title */}
        <Typography
          variant="h5"
          sx={{
            flexGrow: 1,
            color: 'grey.800',
            fontWeight: 700,
            fontSize: { xs: '1.25rem', md: '1.5rem' },
          }}
        >
          {title}
        </Typography>

        {/* User actions */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
          {/* Notifications */}
          <IconButton
            sx={{
              color: 'grey.600',
              '&:hover': {
                bgcolor: 'grey.100',
              },
            }}
          >
            <Badge badgeContent={notificationCount} color="error">
              <NotificationsIcon />
            </Badge>
          </IconButton>

          {/* User profile */}
          <Box
            onClick={onProfileClick}
            sx={{
              display: { xs: 'none', sm: 'flex' },
              alignItems: 'center',
              gap: 1.5,
              px: 2,
              py: 0.5,
              borderRadius: 2,
              bgcolor: 'grey.50',
              border: '1px solid',
              borderColor: 'grey.200',
              cursor: 'pointer',
              transition: 'all 0.2s ease',
              '&:hover': {
                bgcolor: 'grey.100',
                borderColor: 'grey.300',
                transform: 'translateY(-1px)',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              },
            }}
          >
            <Avatar
              sx={{
                width: 36,
                height: 36,
                bgcolor: theme.palette.primary.main,
                fontSize: '0.95rem',
                fontWeight: 600,
              }}
            >
              {user?.name?.charAt(0) || 'U'}
            </Avatar>
            <Box sx={{ minWidth: 0 }}>
              <Typography
                variant="body2"
                sx={{
                  fontWeight: 600,
                  fontSize: '0.9rem',
                  color: 'grey.800',
                  whiteSpace: 'nowrap',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                }}
              >
                {user?.name || 'User'}
              </Typography>
              <Typography
                variant="caption"
                sx={{
                  fontSize: '0.75rem',
                  color: 'grey.500',
                  whiteSpace: 'nowrap',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  display: 'block',
                }}
              >
                {user?.email || ''}
              </Typography>
            </Box>
          </Box>

          {/* Logout button */}
          <Button
            variant="outlined"
            color="error"
            onClick={onLogout}
            startIcon={<LogoutIcon />}
            sx={{
              display: { xs: 'none', md: 'flex' },
              borderRadius: 2,
              fontWeight: 600,
              textTransform: 'none',
            }}
          >
            Logout
          </Button>

          {/* Mobile logout icon */}
          <IconButton
            color="error"
            onClick={onLogout}
            sx={{
              display: { xs: 'flex', md: 'none' },
            }}
          >
            <LogoutIcon />
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default DashboardHeader;
