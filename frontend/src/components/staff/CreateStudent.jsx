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
  MenuItem,
} from '@mui/material';
import {
  PersonAdd as PersonAddIcon,
  Save as SaveIcon,
  Clear as ClearIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import staffService from '../../services/staffService';

/**
 * Component for staff to create new student accounts.
 * 
 * @param {Object} props - Component props
 * @param {string} props.staffId - Staff member's ID
 * @returns {JSX.Element} Create student form
 */
const CreateStudent = ({ staffId }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [formData, setFormData] = useState({
    studentId: '',
    name: '',
    major: '',
    year: '',
    email: '',
    password: 'password', // Default password
  });

  const majors = [
    'Computer Science',
    'Data Science & AI',
    'Computer Engineering',
    'Information Engineering & Media',
    'Business Analytics',
    'Electrical & Electronic Engineering',
  ];

  const years = [1, 2, 3, 4];

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
      studentId: '',
      name: '',
      major: '',
      year: '',
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
      if (!formData.studentId || !formData.name || !formData.email) {
        throw new Error('Student ID, Name, and Email are required');
      }

      // Email validation
      if (!formData.email.includes('@')) {
        throw new Error('Please enter a valid email address');
      }

      // Student ID format validation (e.g., U2310001A)
      const studentIdPattern = /^U\d{7}[A-Z]$/;
      if (!studentIdPattern.test(formData.studentId)) {
        throw new Error('Student ID must be in format: U followed by 7 digits and 1 letter (e.g., U2310001A)');
      }

      // Create student
      const response = await staffService.createStudent(staffId, {
        ...formData,
        year: formData.year ? parseInt(formData.year) : null,
      });

      setSuccess(`Student account created successfully for ${formData.name}!`);
      toast.success('Student account created successfully');
      
      // Clear form after successful creation
      setTimeout(() => {
        handleClear();
      }, 2000);

    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Failed to create student account';
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
          <PersonAddIcon sx={{ fontSize: 32, mr: 2, color: 'primary.main' }} />
          <Typography variant="h5" fontWeight={700}>
            Create Student Account
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
                label="Student ID"
                name="studentId"
                value={formData.studentId}
                onChange={handleChange}
                placeholder="U2310001A"
                helperText="Format: U followed by 7 digits and 1 letter"
                inputProps={{ maxLength: 9 }}
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
                placeholder="e.g., Tan Wei Ling"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                select
                label="Major"
                name="major"
                value={formData.major}
                onChange={handleChange}
                helperText="Select student's major"
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {majors.map((major) => (
                  <MenuItem key={major} value={major}>
                    {major}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                select
                label="Year"
                name="year"
                value={formData.year}
                onChange={handleChange}
                helperText="Current year of study"
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {years.map((year) => (
                  <MenuItem key={year} value={year}>
                    Year {year}
                  </MenuItem>
                ))}
              </TextField>
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
                placeholder="student@e.ntu.edu.sg"
                helperText="NTU email address"
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
                  startIcon={loading ? <CircularProgress size={20} /> : <SaveIcon />}
                  disabled={loading}
                >
                  {loading ? 'Creating...' : 'Create Student'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Paper>

      <Paper elevation={1} sx={{ p: 2, bgcolor: 'info.lighter' }}>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          <strong>Note:</strong> Created student accounts will be immediately active and added to the system.
        </Typography>
        <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 1 }}>
          • Student ID must be unique and follow the format: U + 7 digits + 1 letter<br />
          • Email must be a valid NTU email address<br />
          • Default password is "password" - students should change it after first login<br />
          • All fields except Major and Year are required
        </Typography>
      </Paper>
    </Box>
  );
};

export default CreateStudent;
