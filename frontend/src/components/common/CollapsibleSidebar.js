import React, { useState } from 'react';
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  ListItemButton,
  IconButton,
  Typography,
  Divider,
  Avatar,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';

const collapsedWidth = 70;
const expandedWidth = 260;

/**
 * Modern collapsible sidebar component with smooth animations.
 * Features a dark theme design inspired by modern dashboard interfaces.
 * 
 * @param {Object} props - Component props
 * @param {Array} props.menuItems - Array of menu items {id, label, icon, badge}
 * @param {string} props.currentView - Currently active view id
 * @param {Function} props.onNavigate - Navigation handler
 * @param {Object} props.user - Current user object
 * @param {string} props.title - Sidebar title/logo text
 * @param {boolean} props.mobileOpen - Mobile drawer open state
 * @param {Function} props.onMobileToggle - Mobile drawer toggle handler
 * @param {boolean} props.useRoundedShapes - Use rounded shapes for buttons/sections (default: true for students, false for staff/reps)
 * @returns {JSX.Element} Collapsible sidebar
 */
const CollapsibleSidebar = ({
  menuItems = [],
  currentView,
  onNavigate,
  user,
  title = 'Dashboard',
  mobileOpen = false,
  onMobileToggle,
  onCollapseChange,
  useRoundedShapes = true,
}) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [collapsed, setCollapsed] = useState(false);

  const handleToggleCollapse = () => {
    const newCollapsed = !collapsed;
    setCollapsed(newCollapsed);
    if (onCollapseChange) {
      onCollapseChange(newCollapsed);
    }
  };

  const drawerContent = (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        bgcolor: '#2C3E50',
        color: '#fff',
        overflow: 'hidden',
      }}
    >
      {/* Logo/Brand Section */}
      <Box
        sx={{
          p: 2.5,
          display: 'flex',
          alignItems: 'center',
          justifyContent: collapsed && !isMobile ? 'center' : 'space-between',
          minHeight: 70,
          transition: 'all 0.3s ease',
        }}
      >
        {(!collapsed || isMobile) && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <Avatar
              sx={{
                bgcolor: theme.palette.primary.main,
                width: 36,
                height: 36,
                fontSize: '1rem',
                fontWeight: 600,
              }}
            >
              {title.charAt(0)}
            </Avatar>
            <Typography
              variant="h6"
              sx={{
                fontWeight: 700,
                fontSize: '1.25rem',
                letterSpacing: 0.5,
              }}
            >
              {title}
            </Typography>
          </Box>
        )}
        {!isMobile && (
          <IconButton
            onClick={handleToggleCollapse}
            sx={{
              color: '#fff',
              '&:hover': { bgcolor: 'rgba(255,255,255,0.1)' },
            }}
          >
            {collapsed ? <MenuIcon /> : <ChevronLeftIcon />}
          </IconButton>
        )}
      </Box>

      <Divider sx={{ bgcolor: 'rgba(255,255,255,0.1)' }} />

      {/* User Profile Section */}
      {user && (!collapsed || isMobile) && (
        <Box
          sx={{
            p: 2.5,
            display: 'flex',
            alignItems: 'center',
            gap: 1.5,
            bgcolor: 'rgba(0,0,0,0.15)',
            mx: 1.5,
            my: 2,
            borderRadius: useRoundedShapes ? 2 : 0,
          }}
        >
          <Avatar
            sx={{
              bgcolor: theme.palette.secondary.main,
              width: 44,
              height: 44,
              fontSize: '1.1rem',
              fontWeight: 600,
              borderRadius: useRoundedShapes ? '50%' : 1,
            }}
          >
            {user.name?.charAt(0) || 'U'}
          </Avatar>
          <Box sx={{ flex: 1, overflow: 'hidden' }}>
            <Typography
              variant="body1"
              sx={{
                fontWeight: 600,
                fontSize: '0.95rem',
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
              }}
            >
              {user.name}
            </Typography>
            <Typography
              variant="caption"
              sx={{
                color: 'rgba(255,255,255,0.7)',
                fontSize: '0.8rem',
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                display: 'block',
              }}
            >
              {user.email}
            </Typography>
          </Box>
        </Box>
      )}

      {user && collapsed && !isMobile && (
        <Box
          sx={{
            p: 1,
            display: 'flex',
            justifyContent: 'center',
            my: 2,
          }}
        >
          <Avatar
            sx={{
              bgcolor: theme.palette.secondary.main,
              width: 44,
              height: 44,
              fontSize: '1.1rem',
              fontWeight: 600,
              borderRadius: useRoundedShapes ? '50%' : 1,
            }}
          >
            {user.name?.charAt(0) || 'U'}
          </Avatar>
        </Box>
      )}

      {/* Navigation Menu */}
      <List sx={{ flex: 1, px: 1.5, py: 1 }}>
        {menuItems.map((item) => (
          <ListItem key={item.id} disablePadding sx={{ mb: 0.5 }}>
            <ListItemButton
              selected={currentView === item.id}
              onClick={() => onNavigate(item.id)}
              sx={{
                borderRadius: useRoundedShapes ? 2 : 0,
                minHeight: 48,
                justifyContent: collapsed && !isMobile ? 'center' : 'flex-start',
                px: 2,
                py: 1.5,
                color: 'rgba(255,255,255,0.85)',
                transition: 'all 0.2s ease',
                '&:hover': {
                  bgcolor: 'rgba(255,255,255,0.1)',
                  color: '#fff',
                  transform: 'translateX(4px)',
                },
                '&.Mui-selected': {
                  bgcolor: theme.palette.primary.main,
                  color: '#fff',
                  '&:hover': {
                    bgcolor: theme.palette.primary.dark,
                  },
                },
                position: 'relative',
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: collapsed && !isMobile ? 0 : 42,
                  color: 'inherit',
                  justifyContent: 'center',
                }}
              >
                {item.icon}
              </ListItemIcon>
              {(!collapsed || isMobile) && (
                <>
                  <ListItemText
                    primary={item.label}
                    primaryTypographyProps={{
                      fontSize: '0.9rem',
                      fontWeight: currentView === item.id ? 600 : 500,
                    }}
                  />
                </>
              )}
            </ListItemButton>
          </ListItem>
        ))}
      </List>

      {/* Footer Section */}
      {(!collapsed || isMobile) && (
        <Box
          sx={{
            p: 2,
            borderTop: '1px solid rgba(255,255,255,0.1)',
          }}
        >
          <Typography
            variant="caption"
            sx={{
              color: 'rgba(255,255,255,0.5)',
              fontSize: '0.75rem',
              textAlign: 'center',
              display: 'block',
            }}
          >
            © 2025 Internship System
          </Typography>
        </Box>
      )}
    </Box>
  );

  return (
    <>
      {/* Mobile Drawer */}
      {isMobile && (
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={onMobileToggle}
          ModalProps={{
            keepMounted: true,
          }}
          sx={{
            display: { xs: 'block', md: 'none' },
            '& .MuiDrawer-paper': {
              boxSizing: 'border-box',
              width: expandedWidth,
              border: 'none',
              borderRadius: 0,
            },
          }}
        >
          {drawerContent}
        </Drawer>
      )}

      {/* Desktop Drawer */}
      {!isMobile && (
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', md: 'block' },
            width: collapsed ? collapsedWidth : expandedWidth,
            flexShrink: 0,
            '& .MuiDrawer-paper': {
              boxSizing: 'border-box',
              width: collapsed ? collapsedWidth : expandedWidth,
              transition: theme.transitions.create('width', {
                easing: theme.transitions.easing.sharp,
                duration: theme.transitions.duration.enteringScreen,
              }),
              border: 'none',
              borderRadius: 0,
              boxShadow: '2px 0 12px rgba(0,0,0,0.08)',
            },
          }}
          open
        >
          {drawerContent}
        </Drawer>
      )}
    </>
  );
};

export default CollapsibleSidebar;
