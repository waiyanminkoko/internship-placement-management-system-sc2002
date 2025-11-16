import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Typography,
  Grid,
  Avatar,
  Divider,
  Chip,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Person as PersonIcon,
  Email as EmailIcon,
  School as SchoolIcon,
  Business as BusinessIcon,
  Badge as BadgeIcon,
  WorkOutline as WorkIcon,
} from '@mui/icons-material';
import { useAuth } from '../../context/AuthContext';

/**
 * Profile component that displays user information based on their role.
 * Shows different information for students, representatives, and staff.
 * 
 * @returns {JSX.Element} Profile page
 */
const Profile = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  if (!user) {
    return (
      <Alert severity="error">
        Unable to load profile information. Please try logging in again.
      </Alert>
    );
  }

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) {
      return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
  };

  const getRoleColor = (role) => {
    const colors = {
      STUDENT: 'primary',
      COMPANY_REPRESENTATIVE: 'secondary',
      CAREER_CENTER_STAFF: 'success',
    };
    return colors[role] || 'default';
  };

  const getRoleLabel = (role) => {
    const labels = {
      STUDENT: 'Student',
      COMPANY_REPRESENTATIVE: 'Company Representative',
      CAREER_CENTER_STAFF: 'Career Center Staff',
    };
    return labels[role] || role;
  };

  const renderStudentProfile = () => (
    <Grid container spacing={3}>
      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <BadgeIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Student ID:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.userId}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <EmailIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Email:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.email}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <SchoolIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Major:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.major}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <SchoolIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Year of Study:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            Year {user.year}
          </Typography>
        </Box>
      </Grid>
    </Grid>
  );

  const renderRepresentativeProfile = () => (
    <Grid container spacing={3}>
      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <EmailIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Email:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.email || user.userId}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <BusinessIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Company:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.companyName || 'N/A'}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <WorkIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Department:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.department || 'N/A'}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <BadgeIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Position:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.position || 'N/A'}
          </Typography>
        </Box>
      </Grid>
    </Grid>
  );

  const renderStaffProfile = () => (
    <Grid container spacing={3}>
      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <BadgeIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Staff ID:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.userId}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <EmailIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Email:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.email}
          </Typography>
        </Box>
      </Grid>

      <Grid item xs={12} md={6}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <BusinessIcon sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
            Department:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {user.department || 'Career Services'}
          </Typography>
        </Box>
      </Grid>
    </Grid>
  );

  return (
    <Box>
      <Typography variant="h4" gutterBottom sx={{ mb: 4 }}>
        My Profile
      </Typography>

      <Paper sx={{ p: 4 }}>
        {/* Header Section with Avatar */}
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
          <Avatar
            sx={{
              width: 100,
              height: 100,
              fontSize: '2rem',
              bgcolor: 'primary.main',
              mr: 3,
            }}
          >
            {getInitials(user.name)}
          </Avatar>
          <Box sx={{ flex: 1 }}>
            <Typography variant="h4" gutterBottom>
              {user.name}
            </Typography>
            <Chip
              label={getRoleLabel(user.role)}
              color={getRoleColor(user.role)}
              sx={{ fontWeight: 500 }}
            />
          </Box>
        </Box>

        <Divider sx={{ my: 3 }} />

        {/* Profile Details Based on Role */}
        <Box sx={{ mt: 3 }}>
          <Typography variant="h6" gutterBottom sx={{ mb: 3 }}>
            Personal Information
          </Typography>

          {user.role === 'STUDENT' && renderStudentProfile()}
          {user.role === 'COMPANY_REPRESENTATIVE' && renderRepresentativeProfile()}
          {user.role === 'CAREER_CENTER_STAFF' && renderStaffProfile()}
        </Box>

        <Divider sx={{ my: 3 }} />

        {/* Security Notice */}
        <Alert severity="info" sx={{ mt: 3 }}>
          <Typography variant="body2">
            <strong>Security Notice:</strong> Your password is encrypted and cannot be displayed. 
            You can change your password from the account settings.
          </Typography>
        </Alert>
      </Paper>
    </Box>
  );
};

export default Profile;
