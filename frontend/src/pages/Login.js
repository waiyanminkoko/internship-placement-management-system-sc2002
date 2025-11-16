import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Alert,
  Avatar,
  InputAdornment,
  IconButton,
  Divider,
  Fade,
  useTheme,
  CircularProgress,
  Link,
} from '@mui/material';
import {
  Login as LoginIcon,
  Visibility,
  VisibilityOff,
  Person,
  Lock,
  Business,
  School,
  AdminPanelSettings,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';
import RegisterRepresentative from '../components/auth/RegisterRepresentative';

/**
 * Login page component.
 * Provides user authentication interface with role-based routing.
 * 
 * @returns {JSX.Element} Login page
 */
const Login = () => {
  const [userId, setUserId] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showRegisterDialog, setShowRegisterDialog] = useState(false);
  const navigate = useNavigate();
  const { login, user, isAuthenticated } = useAuth();
  const theme = useTheme();

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated() && user && user.role) {
      console.log('useEffect - Redirecting user with role:', user.role);
      // Get the role - handle both string and object cases
      const userRole = typeof user.role === 'string' ? user.role : user.role?.name || user.role;
      
      // Navigate to appropriate dashboard
      switch (userRole) {
        case 'STUDENT':
          navigate('/student', { replace: true });
          break;
        case 'COMPANY_REPRESENTATIVE':
          navigate('/representative', { replace: true });
          break;
        case 'CAREER_CENTER_STAFF':
          navigate('/staff', { replace: true });
          break;
        default:
          console.error('useEffect - Unknown user role:', userRole);
          break;
      }
    }
  }, [user, isAuthenticated, navigate]);

  /**
   * Handle login form submission.
   * @param {Event} e - Form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Attempt login
      const userData = await login(userId.trim(), password);
      
      // Debug: Log the response data
      console.log('Login response data:', JSON.stringify(userData, null, 2));
      console.log('User role:', userData?.role);
      
      toast.success('Login successful!');

      // Check if userData and role exist
      if (!userData || !userData.role) {
        console.error('User data or role is missing:', userData);
        toast.error('Login successful but user role is missing. Please contact support.');
        setLoading(false);
        return;
      }

      // Navigate to appropriate dashboard based on role
      switch (userData.role) {
        case 'STUDENT':
          navigate('/student', { replace: true });
          break;
        case 'COMPANY_REPRESENTATIVE':
          navigate('/representative', { replace: true });
          break;
        case 'CAREER_CENTER_STAFF':
          navigate('/staff', { replace: true });
          break;
        default:
          console.error('Unknown user role:', userData.role, 'Full userData:', JSON.stringify(userData, null, 2));
          toast.error(`Unknown user role: ${userData.role}. Please contact support.`);
          setLoading(false);
      }
    } catch (err) {
      console.error('Login error:', err);
      setError(err.message || 'Invalid credentials. Please try again.');
      toast.error('Login failed');
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: `linear-gradient(135deg, ${theme.palette.primary.main} 0%, ${theme.palette.secondary.main} 100%)`,
        py: 4,
      }}
    >
      <Container maxWidth="sm">
        <Fade in={true} timeout={800}>
          <Paper
            elevation={8}
            sx={{
              p: { xs: 3, sm: 5 },
              borderRadius: 4,
              backdropFilter: 'blur(10px)',
              background: 'rgba(255, 255, 255, 0.98)',
              boxShadow: '0 8px 32px rgba(0, 0, 0, 0.15)',
            }}
          >
            {/* Logo and Title Section */}
            <Box sx={{ textAlign: 'center', mb: 3 }}>
              <Avatar
                src="/images/internship-logo.jpg"
                alt="Internship Logo"
                sx={{
                  width: 80,
                  height: 80,
                  mx: 'auto',
                  mb: 2,
                  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
                  border: `3px solid ${theme.palette.primary.main}`,
                }}
              />
              <Typography
                component="h1"
                variant="h4"
                sx={{
                  fontWeight: 800,
                  background: `linear-gradient(135deg, ${theme.palette.primary.main}, ${theme.palette.secondary.main})`,
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                  mb: 1,
                }}
              >
                Internship Placement System
              </Typography>
              <Typography
                variant="body1"
                color="text.secondary"
                sx={{ fontWeight: 500 }}
              >
                Welcome back! Please login to your account
              </Typography>
            </Box>

            {/* Error Alert */}
            {error && (
              <Fade in={!!error}>
                <Alert
                  severity="error"
                  sx={{
                    mb: 3,
                    borderRadius: 2,
                    '& .MuiAlert-message': { fontWeight: 500 },
                  }}
                  onClose={() => setError('')}
                >
                  {error}
                </Alert>
              </Fade>
            )}

            {/* Login Form */}
            <Box component="form" onSubmit={handleSubmit}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="userId"
                label="User ID / Email"
                name="userId"
                autoComplete="username"
                autoFocus
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                disabled={loading}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Person color="action" />
                    </InputAdornment>
                  ),
                }}
                sx={{ mb: 2 }}
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type={showPassword ? 'text' : 'password'}
                id="password"
                autoComplete="current-password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                disabled={loading}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Lock color="action" />
                    </InputAdornment>
                  ),
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={() => setShowPassword(!showPassword)}
                        edge="end"
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                sx={{ mb: 1 }}
              />

              <Button
                type="submit"
                fullWidth
                variant="contained"
                size="large"
                disabled={loading}
                startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <LoginIcon />}
                sx={{
                  mt: 3,
                  mb: 2,
                  py: 1.5,
                  fontSize: '1rem',
                  fontWeight: 700,
                  background: `linear-gradient(135deg, ${theme.palette.primary.main}, ${theme.palette.secondary.main})`,
                  '&:hover': {
                    background: `linear-gradient(135deg, ${theme.palette.primary.dark}, ${theme.palette.secondary.dark})`,
                    transform: 'translateY(-1px)',
                    boxShadow: '0 6px 16px rgba(0, 0, 0, 0.2)',
                  },
                }}
              >
                {loading ? 'Logging in...' : 'Sign In'}
              </Button>

              {/* Company Representative Registration Link */}
              <Box sx={{ textAlign: 'center' }}>
                <Typography variant="body2" color="text.secondary">
                  Company Representative?{' '}
                  <Link
                    component="button"
                    type="button"
                    variant="body2"
                    onClick={() => setShowRegisterDialog(true)}
                    sx={{
                      fontWeight: 600,
                      color: theme.palette.secondary.main,
                      textDecoration: 'none',
                      cursor: 'pointer',
                      '&:hover': {
                        textDecoration: 'underline',
                      },
                    }}
                  >
                    Register Here
                  </Link>
                </Typography>
              </Box>
            </Box>

            <Divider sx={{ my: 3 }}>
              <Typography variant="body2" color="text.secondary">
                User Types
              </Typography>
            </Divider>

            {/* User Type Info Cards */}
            <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 3 }}>
              <Box
                sx={{
                  flex: 1,
                  minWidth: 120,
                  p: 1.5,
                  borderRadius: 2,
                  bgcolor: theme.palette.primary.light + '15',
                  border: `1px solid ${theme.palette.primary.light}30`,
                  textAlign: 'center',
                }}
              >
                <School sx={{ color: theme.palette.primary.main, mb: 0.5 }} />
                <Typography variant="caption" display="block" fontWeight={600}>
                  Student
                </Typography>
                <Typography variant="caption" color="text.secondary" display="block">
                  Student ID
                </Typography>
              </Box>
              <Box
                sx={{
                  flex: 1,
                  minWidth: 120,
                  p: 1.5,
                  borderRadius: 2,
                  bgcolor: theme.palette.secondary.light + '15',
                  border: `1px solid ${theme.palette.secondary.light}30`,
                  textAlign: 'center',
                }}
              >
                <Business sx={{ color: theme.palette.secondary.main, mb: 0.5 }} />
                <Typography variant="caption" display="block" fontWeight={600}>
                  Company Rep
                </Typography>
                <Typography variant="caption" color="text.secondary" display="block">
                  Email
                </Typography>
              </Box>
              <Box
                sx={{
                  flex: 1,
                  minWidth: 120,
                  p: 1.5,
                  borderRadius: 2,
                  bgcolor: theme.palette.success.light + '15',
                  border: `1px solid ${theme.palette.success.light}30`,
                  textAlign: 'center',
                }}
              >
                <AdminPanelSettings sx={{ color: theme.palette.success.main, mb: 0.5 }} />
                <Typography variant="caption" display="block" fontWeight={600}>
                  Staff
                </Typography>
                <Typography variant="caption" color="text.secondary" display="block">
                  Staff ID
                </Typography>
              </Box>
            </Box>

            {/* Footer */}
            <Box sx={{ mt: 3, pt: 2, borderTop: `1px solid ${theme.palette.divider}` }}>
              <Typography
                variant="caption"
                color="text.secondary"
                align="center"
                display="block"
                sx={{ fontWeight: 500 }}
              >
                SC2002 Object-Oriented Design & Programming
              </Typography>
              <Typography
                variant="caption"
                color="text.secondary"
                align="center"
                display="block"
              >
                AY 2025/2026 Semester 1
              </Typography>
              <Typography
                variant="caption"
                color="text.secondary"
                align="center"
                display="block"
                sx={{ mt: 1, fontSize: '0.7rem' }}
              >
                Default password: <strong>password</strong>
              </Typography>
            </Box>
          </Paper>
        </Fade>
      </Container>

      {/* Registration Dialog */}
      <RegisterRepresentative 
        open={showRegisterDialog} 
        onClose={() => setShowRegisterDialog(false)} 
      />
    </Box>
  );
};

export default Login;
