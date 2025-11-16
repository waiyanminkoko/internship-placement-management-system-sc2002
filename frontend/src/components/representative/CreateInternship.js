import React, { useState } from 'react';
import {
  Box,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Alert,
  CircularProgress,
} from '@mui/material';
import { Add as AddIcon, Save as SaveIcon } from '@mui/icons-material';
import { toast } from 'react-toastify';
import representativeService from '../../services/representativeService';
import { MAJORS } from '../../utils/constants';

/**
 * Create Internship component for company representatives.
 * Allows representatives to create new internship opportunities.
 * 
 * @param {Object} props - Component props
 * @param {string} props.repId - Representative ID (email)
 * @returns {JSX.Element} Create internship form
 */
const CreateInternship = ({ repId }) => {
  const [loading, setLoading] = useState(false);
  const [checkingLimit, setCheckingLimit] = useState(true);
  const [currentCount, setCurrentCount] = useState(0);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    level: 'BASIC',
    preferredMajor: '',
    openingDate: '',
    closingDate: '',
    startDate: '',
    endDate: '',
    slots: 1,
  });

  const levels = [
    { value: 'BASIC', label: 'Entry Level' },
    { value: 'INTERMEDIATE', label: 'Intermediate' },
  ];

  const MAX_OPPORTUNITIES = 5;

  // Check current internship count on component mount
  React.useEffect(() => {
    const checkCurrentCount = async () => {
      try {
        setCheckingLimit(true);
        const internships = await representativeService.getMyInternships(repId);
        setCurrentCount(internships?.length || 0);
      } catch (error) {
        console.error('Error checking internship count:', error);
        toast.error('Failed to check current internship count');
      } finally {
        setCheckingLimit(false);
      }
    };

    if (repId) {
      checkCurrentCount();
    }
  }, [repId]);

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'number' ? parseInt(value) || 0 : value
    }));
    console.log(`Field ${name} changed to:`, value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Check maximum limit
    if (currentCount >= MAX_OPPORTUNITIES) {
      toast.error(`You have reached the maximum of ${MAX_OPPORTUNITIES} internship opportunities`);
      return;
    }

    // Validation
    if (!formData.title || !formData.description) {
      toast.error('Please fill in all required fields');
      return;
    }

    if (!formData.openingDate || !formData.closingDate || !formData.startDate || !formData.endDate) {
      toast.error('Please fill in all date fields');
      return;
    }

    if (!formData.preferredMajor) {
      toast.error('Please select a preferred major');
      return;
    }

    if (formData.slots < 1) {
      toast.error('Number of slots must be at least 1');
      return;
    }

    console.log('Submitting internship data:', formData);

      try {
      setLoading(true);
      await representativeService.createInternship(repId, formData);
      toast.success('Internship created successfully!');
      
      // Update count
      setCurrentCount(prev => prev + 1);
      
      // Reset form
      setFormData({
        title: '',
        description: '',
        level: 'BASIC',
        preferredMajor: '',
        openingDate: '',
        closingDate: '',
        startDate: '',
        endDate: '',
        slots: 1,
      });
    } catch (error) {
      console.error('Error creating internship:', error);
      toast.error(error.response?.data?.message || 'Failed to create internship');
    } finally {
      setLoading(false);
    }
  };

  if (checkingLimit) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  const hasReachedLimit = currentCount >= MAX_OPPORTUNITIES;  return (
    <Box>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5" sx={{ fontWeight: 700, mb: 1 }}>
          Create New Internship Opportunity
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Post a new internship opportunity for students to apply ({currentCount} / {MAX_OPPORTUNITIES} created)
        </Typography>
      </Box>

      {/* Maximum limit warning */}
      {hasReachedLimit && (
        <Alert severity="error" sx={{ mb: 3 }}>
          <Typography variant="body2">
            <strong>Maximum Limit Reached:</strong> You have created the maximum of {MAX_OPPORTUNITIES} internship 
            opportunities. To create a new one, please delete an existing opportunity from the "Internship Opportunities" page.
          </Typography>
        </Alert>
      )}

      {/* Near limit warning */}
      {!hasReachedLimit && currentCount >= MAX_OPPORTUNITIES - 1 && (
        <Alert severity="warning" sx={{ mb: 3 }}>
          <Typography variant="body2">
            <strong>Approaching Limit:</strong> You have {MAX_OPPORTUNITIES - currentCount} internship 
            {MAX_OPPORTUNITIES - currentCount === 1 ? ' opportunity' : ' opportunities'} remaining.
          </Typography>
        </Alert>
      )}

      <Paper sx={{ p: 4 }}>
        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                label="Internship Title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                placeholder="e.g., Software Engineering Intern"
                helperText="Provide a clear, descriptive title for the internship"
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                multiline
                rows={4}
                label="Description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Describe the internship role, responsibilities, and requirements..."
                helperText="Include key responsibilities, required skills, and what students will learn"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <FormControl fullWidth required>
                <InputLabel>Experience Level</InputLabel>
                <Select
                  name="level"
                  value={formData.level}
                  onChange={handleChange}
                  label="Experience Level"
                >
                  {levels.map(level => (
                    <MenuItem key={level.value} value={level.value}>
                      {level.label}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid item xs={12} sm={6}>
              <FormControl fullWidth required>
                <InputLabel>Preferred Major</InputLabel>
                <Select
                  name="preferredMajor"
                  value={formData.preferredMajor}
                  onChange={handleChange}
                  label="Preferred Major"
                >
                  {MAJORS.map(major => (
                    <MenuItem key={major} value={major}>
                      {major}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="date"
                label="Application Opening Date"
                name="openingDate"
                value={formData.openingDate}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                helperText="When applications open"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="date"
                label="Application Closing Date"
                name="closingDate"
                value={formData.closingDate}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                helperText="Application deadline"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="date"
                label="Internship Start Date"
                name="startDate"
                value={formData.startDate}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                helperText="When the internship begins"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="date"
                label="Internship End Date"
                name="endDate"
                value={formData.endDate}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                helperText="When the internship ends"
              />
            </Grid>

            <Grid item xs={12} sm={12}>
              <TextField
                fullWidth
                required
                type="number"
                label="Number of Slots"
                name="slots"
                value={formData.slots}
                onChange={handleChange}
                inputProps={{ min: 1, max: 20 }}
                helperText="Available positions"
              />
            </Grid>

            <Grid item xs={12}>
              <Alert severity="info">
                <Typography variant="body2">
                  <strong>Important Notes:</strong>
                </Typography>
                <Typography variant="body2" component="ul" sx={{ pl: 2, mb: 0 }}>
                  <li>Your internship will be submitted to Career Center Staff for approval before it becomes visible to students.</li>
                  <li>Once approved, the internship cannot be edited. Only pending or rejected opportunities can be modified.</li>
                  <li>Rejected opportunities can be edited and resubmitted for approval.</li>
                  <li>Maximum of {MAX_OPPORTUNITIES} internship opportunities per company representative.</li>
                </Typography>
              </Alert>
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                <Button
                  type="button"
                  variant="outlined"
                  onClick={() => setFormData({
                    title: '',
                    description: '',
                    level: 'BASIC',
                    preferredMajor: '',
                    openingDate: '',
                    closingDate: '',
                    startDate: '',
                    endDate: '',
                    slots: 1,
                  })}
                  disabled={loading || hasReachedLimit}
                >
                  Clear
                </Button>
                <Button
                  type="submit"
                  variant="contained"
                  startIcon={loading ? <CircularProgress size={20} /> : <SaveIcon />}
                  disabled={loading || hasReachedLimit}
                >
                  {loading ? 'Creating...' : 'Create Internship'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Paper>
    </Box>
  );
};

export default CreateInternship;
