import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Alert,
  CircularProgress,
  TextField,
  MenuItem,
  Grid,
} from '@mui/material';
import { studentService } from '../../services/studentService';
import { useAuth } from '../../context/AuthContext';
import { formatDate } from '../../utils/formatters';
import { MAJORS } from '../../utils/constants';

const InternshipList = () => {
  const { user } = useAuth();
  const [internships, setInternships] = useState([]);
  const [myApplications, setMyApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedInternship, setSelectedInternship] = useState(null);
  const [detailsOpen, setDetailsOpen] = useState(false);
  const [applyDialogOpen, setApplyDialogOpen] = useState(false);
  const [hasAcceptedPlacement, setHasAcceptedPlacement] = useState(false);
  const [filters, setFilters] = useState({
    major: '',
  });

  useEffect(() => {
    fetchData();
  }, [filters]);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError('');
      const [internshipsData, applicationsData] = await Promise.all([
        studentService.getAvailableInternships(user.userId, {}),
        studentService.getMyApplications(user.userId),
      ]);
      
      // Check if any application has been accepted (for backward compatibility with cached user data)
      const hasAcceptedApp = applicationsData.some(app => app.placementAccepted === true);
      
      // Update state - use either user.hasAcceptedPlacement or check applications
      setHasAcceptedPlacement(user?.hasAcceptedPlacement === true || hasAcceptedApp);
      
      // Apply client-side filtering based on user selections
      let filteredInternships = internshipsData;
      
      // Automatic level filtering based on student's year
      const studentYear = user?.year || 1;
      if (studentYear <= 2) {
        // Year 1-2 students can ONLY see Basic-level internships
        filteredInternships = filteredInternships.filter(
          internship => internship.level === 'BASIC'
        );
      } else {
        // Year 3-4 students can see Basic and Intermediate internships
        filteredInternships = filteredInternships.filter(
          internship => internship.level === 'BASIC' || internship.level === 'INTERMEDIATE'
        );
      }
      
      // Filter by major if specified (not "All Majors")
      if (filters.major) {
        filteredInternships = filteredInternships.filter(
          internship => internship.preferredMajor === filters.major
        );
      }
      
      setInternships(filteredInternships);
      setMyApplications(applicationsData);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch internships');
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = (internship) => {
    setSelectedInternship(internship);
    setDetailsOpen(true);
  };

  const handleApplyClick = (internship) => {
    setSelectedInternship(internship);
    setApplyDialogOpen(true);
  };

  const handleApplyConfirm = async () => {
    try {
      setError('');
      setSuccess('');
      
      // Check if student has already accepted a placement
      if (hasAcceptedPlacement) {
        setError('You have already accepted an internship placement and cannot apply for new internships.');
        setApplyDialogOpen(false);
        return;
      }
      
      // Check eligibility
      if (!isEligibleForInternship(selectedInternship)) {
        setError('You are not eligible for this internship. Please check the level and major requirements.');
        setApplyDialogOpen(false);
        return;
      }
      
      // Check application limit
      const activeApps = myApplications.filter(
        app => app.status === 'PENDING' || app.status === 'SUCCESSFUL'
      );
      if (activeApps.length >= 3) {
        setError('You have reached the maximum of 3 active applications');
        setApplyDialogOpen(false);
        return;
      }

      await studentService.applyForInternship(user.userId, selectedInternship.internshipId);
      setSuccess('Application submitted successfully!');
      setApplyDialogOpen(false);
      fetchData(); // Refresh data
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit application');
      setApplyDialogOpen(false);
    }
  };

  const hasApplied = (internshipId) => {
    return myApplications.some(
      app => app.internshipId === internshipId && 
      (app.status === 'PENDING' || app.status === 'SUCCESSFUL' || app.status === 'ACCEPTED')
    );
  };

  const getLevelColor = (level) => {
    const colors = {
      BASIC: 'primary',
      INTERMEDIATE: 'secondary',
      ADVANCED: 'error',
    };
    return colors[level] || 'default';
  };

  const getStatusColor = (status) => {
    const colors = {
      APPROVED: 'success',
      PENDING: 'warning',
      REJECTED: 'error',
      FILLED: 'default',
    };
    return colors[status] || 'default';
  };

  // Check if student is eligible for an internship based on year, level, and major
  const isEligibleForInternship = (internship) => {
    const studentYear = user?.year || 1;
    const studentMajor = user?.major || '';
    const internshipLevel = internship?.level || '';
    const preferredMajor = internship?.preferredMajor || '';
    
    // Check level eligibility based on year FIRST
    // Year 1 & 2 students can ONLY apply for Basic-level internships
    if (studentYear <= 2 && internshipLevel !== 'BASIC') {
      return false;
    }
    
    // Check major eligibility - student's major must match the internship's preferred major
    // This applies to all students regardless of year
    if (preferredMajor && studentMajor !== preferredMajor) {
      return false;
    }
    
    // Year 3 & above students can apply for any level (Basic, Intermediate, Advanced)
    // as long as their major matches
    return true;
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Available Internships
      </Typography>

      {error && (
        <Alert severity="error" onClose={() => setError('')} sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {success && (
        <Alert severity="success" onClose={() => setSuccess('')} sx={{ mb: 2 }}>
          {success}
        </Alert>
      )}

      {/* Filters */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              select
              fullWidth
              value={filters.major}
              onChange={(e) => setFilters({ ...filters, major: e.target.value })}
              SelectProps={{
                displayEmpty: true,
              }}
            >
              <MenuItem value="">All Majors</MenuItem>
              {MAJORS.map((major) => (
                <MenuItem key={major} value={major}>
                  {major}
                </MenuItem>
              ))}
            </TextField>
          </Grid>
        </Grid>
      </Paper>

      {/* Application Count and Eligibility Info */}
      <Box sx={{ mb: 2 }}>
        <Alert severity="info" sx={{ mb: 1 }}>
          Active Applications: {myApplications.filter(app => 
            app.status === 'PENDING' || app.status === 'SUCCESSFUL'
          ).length} / 3
        </Alert>
        {hasAcceptedPlacement ? (
          <Alert severity="warning" sx={{ mb: 1 }}>
            You have already accepted an internship placement and are not eligible to apply for any new internships.
          </Alert>
        ) : null}
        {user?.year <= 2 ? (
          <Alert severity="info">
            As a Year {user.year} student, you can view and apply for Basic-level internships only.
          </Alert>
        ) : (
          <Alert severity="info">
            As a Year {user.year} student, you can view and apply for Basic and Intermediate-level internships.
          </Alert>
        )}
      </Box>

      {/* Internships Table */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Title</TableCell>
              <TableCell>Company</TableCell>
              <TableCell>Level</TableCell>
              <TableCell>Eligibility</TableCell>
              <TableCell>Opening Date</TableCell>
              <TableCell>Closing Date</TableCell>
              <TableCell>Internship Start Date</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {internships.length === 0 ? (
              <TableRow>
                <TableCell colSpan={8} align="center">
                  No internships available
                </TableCell>
              </TableRow>
            ) : (
              internships.map((internship) => {
                const isEligible = isEligibleForInternship(internship);
                return (
                  <TableRow 
                    key={internship.internshipId}
                    sx={{ 
                      bgcolor: isEligible ? 'inherit' : 'action.hover',
                      opacity: isEligible ? 1 : 0.7
                    }}
                  >
                    <TableCell>{internship.title}</TableCell>
                    <TableCell>{internship.companyName}</TableCell>
                    <TableCell>
                      <Chip
                        label={internship.level}
                        color={getLevelColor(internship.level)}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={isEligible ? 'Eligible' : 'Not Eligible'}
                        color={isEligible ? 'success' : 'default'}
                        size="small"
                        variant={isEligible ? 'filled' : 'outlined'}
                      />
                    </TableCell>
                    <TableCell>
                      {formatDate(internship.openingDate)}
                    </TableCell>
                    <TableCell>
                      {formatDate(internship.closingDate)}
                    </TableCell>
                    <TableCell>
                      {formatDate(internship.startDate)}
                    </TableCell>
                    <TableCell align="center">
                      <Button
                        size="small"
                        variant="outlined"
                        onClick={() => handleViewDetails(internship)}
                        sx={{ mr: 1 }}
                      >
                        Details
                      </Button>
                      {hasAcceptedPlacement ? (
                        <Chip label="Ineligible" color="error" size="small" variant="outlined" />
                      ) : hasApplied(internship.internshipId) ? (
                        <Chip label="Applied" color="default" size="small" />
                      ) : internship.status === 'APPROVED' && isEligible ? (
                        <Button
                          size="small"
                          variant="contained"
                          color="primary"
                          onClick={() => handleApplyClick(internship)}
                        >
                          Apply
                        </Button>
                      ) : !isEligible ? (
                        <Chip label="Ineligible" color="error" size="small" variant="outlined" />
                      ) : null}
                    </TableCell>
                  </TableRow>
                );
              })
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Details Dialog */}
      <Dialog
        open={detailsOpen}
        onClose={() => setDetailsOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Internship Details</DialogTitle>
        <DialogContent>
          {selectedInternship && (
            <Box sx={{ pt: 2 }}>
              <Typography variant="h6" gutterBottom>
                {selectedInternship.title}
              </Typography>
              <Typography color="textSecondary" gutterBottom>
                {selectedInternship.companyName}
              </Typography>
              
              <Box sx={{ mt: 2 }}>
                <Typography variant="subtitle2" gutterBottom>
                  Description:
                </Typography>
                <Typography variant="body2" paragraph>
                  {selectedInternship.description || 'No description available'}
                </Typography>

                <Grid container spacing={2} sx={{ mt: 1 }}>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Level:</Typography>
                    <Typography variant="body2">
                      {selectedInternship.level}
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Eligibility:</Typography>
                    <Chip
                      label={isEligibleForInternship(selectedInternship) ? 'Eligible' : 'Not Eligible'}
                      color={isEligibleForInternship(selectedInternship) ? 'success' : 'error'}
                      size="small"
                    />
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Status:</Typography>
                    <Typography variant="body2">
                      {selectedInternship.status}
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Preferred Major:</Typography>
                    <Typography variant="body2">
                      {selectedInternship.preferredMajor || 'Any'}
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Opening Date:</Typography>
                    <Typography variant="body2">
                      {formatDate(selectedInternship.openingDate)}
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Closing Date:</Typography>
                    <Typography variant="body2">
                      {formatDate(selectedInternship.closingDate)}
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2">Start Date:</Typography>
                    <Typography variant="body2">
                      {formatDate(selectedInternship.startDate)}
                    </Typography>
                  </Grid>
                </Grid>

                {selectedInternship.requirements && (
                  <Box sx={{ mt: 2 }}>
                    <Typography variant="subtitle2" gutterBottom>
                      Requirements:
                    </Typography>
                    <Typography variant="body2">
                      {selectedInternship.requirements}
                    </Typography>
                  </Box>
                )}
              </Box>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDetailsOpen(false)}>Close</Button>
          {selectedInternship && hasAcceptedPlacement && (
            <Typography variant="caption" color="error" sx={{ mr: 2 }}>
              You have already accepted a placement and cannot apply for new internships
            </Typography>
          )}
          {selectedInternship && !hasAcceptedPlacement && !hasApplied(selectedInternship.internshipId) && 
           selectedInternship.status === 'APPROVED' && 
           isEligibleForInternship(selectedInternship) && (
            <Button
              variant="contained"
              color="primary"
              onClick={() => {
                setDetailsOpen(false);
                handleApplyClick(selectedInternship);
              }}
            >
              Apply Now
            </Button>
          )}
          {selectedInternship && !hasAcceptedPlacement && !hasApplied(selectedInternship.internshipId) && 
           selectedInternship.status === 'APPROVED' && 
           !isEligibleForInternship(selectedInternship) && (
            <Typography variant="caption" color="error" sx={{ mr: 2 }}>
              You are not eligible for this internship (check level and major requirements)
            </Typography>
          )}
        </DialogActions>
      </Dialog>

      {/* Apply Confirmation Dialog */}
      <Dialog
        open={applyDialogOpen}
        onClose={() => setApplyDialogOpen(false)}
      >
        <DialogTitle>Confirm Application</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to apply for this internship?
          </Typography>
          {selectedInternship && (
            <Box sx={{ mt: 2 }}>
              <Typography variant="subtitle2">
                {selectedInternship.title}
              </Typography>
              <Typography color="textSecondary">
                {selectedInternship.companyName}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setApplyDialogOpen(false)}>Cancel</Button>
          <Button
            variant="contained"
            color="primary"
            onClick={handleApplyConfirm}
          >
            Confirm Application
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default InternshipList;
