import React, { useState } from 'react';
import {
  Box,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  Alert,
  CircularProgress,
} from '@mui/material';
import {
  Business as BusinessIcon,
  Save as SaveIcon,
  Clear as ClearIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import staffService from '../../services/staffService';

/**
 * Component for staff to create new company representative accounts.
 * 
 * @param {Object} props - Component props
 * @param {string} props.staffId - Staff member's ID
 * @returns {JSX.Element} Create representative form
 */
const CreateRepresentative = ({ staffId }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [formData, setFormData] = useState({
    companyRepId: '',
    name: '',
    companyName: '',
    industry: '',
    position: '',
    email: '',
    password: 'password', // Default password
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError('');
    setSuccess('');
  };

  const handleClear = () => {
    setFormData({
      companyRepId: '',
      name: '',
      companyName: '',
      industry: '',
      position: '',
      email: '',
      password: 'password',
    });
    setError('');
    setSuccess('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      // Validate form
      if (!formData.companyRepId || !formData.name || !formData.companyName || !formData.email) {
        throw new Error('Company Rep ID, Name, Company Name, and Email are required');
      }

      // Email validation
      if (!formData.email.includes('@')) {
        throw new Error('Please enter a valid email address');
      }

      // Company Rep ID format validation (e.g., C00001)
      const repIdPattern = /^C\d{5}$/;
      if (!repIdPattern.test(formData.companyRepId)) {
        throw new Error('Company Rep ID must be in format: C followed by 5 digits (e.g., C00001)');
      }

      // Create representative
      const response = await staffService.createRepresentative(staffId, formData);

      setSuccess(`Company representative account created successfully for ${formData.name}!`);
      toast.success('Company representative account created successfully');
      
      // Clear form after successful creation
      setTimeout(() => {
        handleClear();
      }, 2000);

    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Failed to create representative account';
      setError(errorMessage);
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box>
      <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <BusinessIcon sx={{ fontSize: 32, mr: 2, color: 'secondary.main' }} />
          <Typography variant="h5" fontWeight={700}>
            Create Company Representative Account
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError('')}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 3 }} onClose={() => setSuccess('')}>
            {success}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Company Rep ID"
                name="companyRepId"
                value={formData.companyRepId}
                onChange={handleChange}
                placeholder="C00001"
                helperText="Format: C followed by 5 digits"
                inputProps={{ maxLength: 6 }}
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Full Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="e.g., Alice Tan"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Company Name"
                name="companyName"
                value={formData.companyName}
                onChange={handleChange}
                placeholder="e.g., Google"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Industry"
                name="industry"
                value={formData.industry}
                onChange={handleChange}
                placeholder="e.g., Technology"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Position"
                name="position"
                value={formData.position}
                onChange={handleChange}
                placeholder="e.g., HR Manager"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                type="email"
                label="Email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="representative@company.com"
                helperText="Company email address"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                helperText="Default: password"
              />
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                <Button
                  variant="outlined"
                  startIcon={<ClearIcon />}
                  onClick={handleClear}
                  disabled={loading}
                >
                  Clear
                </Button>
                <Button
                  type="submit"
                  variant="contained"
                  color="secondary"
                  startIcon={loading ? <CircularProgress size={20} /> : <SaveIcon />}
                  disabled={loading}
                >
                  {loading ? 'Creating...' : 'Create Representative'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Paper>

      <Paper elevation={1} sx={{ p: 2, bgcolor: 'info.lighter' }}>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          <strong>Note:</strong> Created representative accounts will be automatically approved and immediately active.
        </Typography>
        <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 1 }}>
          • Company Rep ID must be unique and follow the format: C + 5 digits<br />
          • Email must be a valid company email address<br />
          • Default password is "password" - representatives should change it after first login<br />
          • Company Rep ID, Name, Company Name, and Email are required<br />
          • Industry and Position are optional but recommended
        </Typography>
      </Paper>
    </Box>
  );
};

export default CreateRepresentative;
