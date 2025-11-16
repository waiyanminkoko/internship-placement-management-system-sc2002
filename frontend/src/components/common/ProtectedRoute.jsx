import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { Box, CircularProgress, Typography } from '@mui/material';
import { useAuth } from '../../context/AuthContext';

/**
 * ProtectedRoute component to guard routes that require authentication.
 * Redirects to login page if user is not authenticated or doesn't have required role.
 * 
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - Child components to render if authorized
 * @param {string[]} props.allowedRoles - Array of roles allowed to access this route
 * @returns {JSX.Element} Protected content or redirect
 */
const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user, loading, isAuthenticated } = useAuth();
  const location = useLocation();

  // Show loading spinner while checking authentication
  if (loading) {
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        }}
      >
        <CircularProgress
          size={60}
          thickness={4}
          sx={{
            color: 'white',
            mb: 3,
          }}
        />
        <Typography
          variant="h6"
          sx={{
            color: 'white',
            fontWeight: 500,
          }}
        >
          Verifying authentication...
        </Typography>
      </Box>
    );
  }

  // Check if user is authenticated
  if (!isAuthenticated() || !user) {
    // Redirect to login page, but save the attempted location
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Check if user has required role
  if (allowedRoles && allowedRoles.length > 0) {
    if (!allowedRoles.includes(user.role)) {
      // User doesn't have permission, redirect to their appropriate dashboard
      const userDashboard = getRoleDashboard(user.role);
      return <Navigate to={userDashboard} replace />;
    }
  }

  // User is authenticated and authorized
  return children;
};

/**
 * Get the dashboard path for a given user role.
 * 
 * @param {string} role - User role
 * @returns {string} Dashboard path
 */
const getRoleDashboard = (role) => {
  switch (role) {
    case 'STUDENT':
      return '/student';
    case 'COMPANY_REPRESENTATIVE':
      return '/representative';
    case 'CAREER_CENTER_STAFF':
      return '/staff';
    default:
      return '/login';
  }
};

export default ProtectedRoute;
