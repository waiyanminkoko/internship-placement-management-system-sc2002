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
  Grid,
} from '@mui/material';
import { studentService } from '../../services/studentService';
import { useAuth } from '../../context/AuthContext';
import { formatDate } from '../../utils/formatters';

const MyApplications = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedApplication, setSelectedApplication] = useState(null);
  const [detailsOpen, setDetailsOpen] = useState(false);

  useEffect(() => {
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await studentService.getMyApplications(user.userId);
      setApplications(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch applications');
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = (application) => {
    setSelectedApplication(application);
    setDetailsOpen(true);
  };

  const getStatusColor = (status) => {
    const colors = {
      PENDING: 'warning',
      SUCCESSFUL: 'success',
      UNSUCCESSFUL: 'error',
      ACCEPTED: 'info',
      WITHDRAWN: 'default',
    };
    return colors[status] || 'default';
  };

  const getStatusDescription = (status) => {
    const descriptions = {
      PENDING: 'Your application is under review by staff',
      SUCCESSFUL: 'Your application has been approved! You can now accept the placement',
      UNSUCCESSFUL: 'Unfortunately, your application was not approved',
      ACCEPTED: 'You have accepted this placement',
      WITHDRAWN: 'You have withdrawn from this application',
    };
    return descriptions[status] || '';
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
        My Applications
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

      {/* Summary Cards */}
      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h4" color="warning.main">
              {applications.filter(app => app.status === 'PENDING').length}
            </Typography>
            <Typography color="textSecondary">Pending</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h4" color="success.main">
              {applications.filter(app => app.status === 'SUCCESSFUL').length}
            </Typography>
            <Typography color="textSecondary">Approved</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h4" color="info.main">
              {applications.filter(app => app.status === 'ACCEPTED').length}
            </Typography>
            <Typography color="textSecondary">Accepted</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h4" color="error.main">
              {applications.filter(app => app.status === 'UNSUCCESSFUL').length}
            </Typography>
            <Typography color="textSecondary">Rejected</Typography>
          </Paper>
        </Grid>
      </Grid>

      {/* Applications Table */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Internship Title</TableCell>
              <TableCell>Company</TableCell>
              <TableCell>Applied Date</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {applications.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} align="center">
                  <Box sx={{ py: 3 }}>
                    <Typography color="textSecondary">
                      You haven't applied to any internships yet
                    </Typography>
                    <Typography variant="body2" color="textSecondary" sx={{ mt: 1 }}>
                      Go to "Browse Internships" to find opportunities
                    </Typography>
                  </Box>
                </TableCell>
              </TableRow>
            ) : (
              applications.map((application) => (
                <TableRow key={application.applicationId}>
                  <TableCell>{application.internshipTitle || 'N/A'}</TableCell>
                  <TableCell>{application.companyName || 'N/A'}</TableCell>
                  <TableCell>
                    {formatDate(application.appliedDate)}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={application.status}
                      color={getStatusColor(application.status)}
                      size="small"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <Button
                      size="small"
                      variant="outlined"
                      onClick={() => handleViewDetails(application)}
                    >
                      View Details
                    </Button>
                  </TableCell>
                </TableRow>
              ))
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
        <DialogTitle>Application Details</DialogTitle>
        <DialogContent>
          {selectedApplication && (
            <Box sx={{ pt: 2 }}>
              {/* Status Alert */}
              <Alert 
                severity={
                  selectedApplication.status === 'SUCCESSFUL' ? 'success' :
                  selectedApplication.status === 'UNSUCCESSFUL' ? 'error' :
                  selectedApplication.status === 'ACCEPTED' ? 'info' :
                  'warning'
                }
                sx={{ mb: 3 }}
              >
                <Typography variant="subtitle2" gutterBottom>
                  Status: {selectedApplication.status}
                </Typography>
                <Typography variant="body2">
                  {getStatusDescription(selectedApplication.status)}
                </Typography>
              </Alert>

              {/* Application Info */}
              <Typography variant="h6" gutterBottom>
                {selectedApplication.internshipTitle}
              </Typography>
              <Typography color="textSecondary" gutterBottom>
                {selectedApplication.companyName}
              </Typography>

              <Grid container spacing={2} sx={{ mt: 2 }}>
                <Grid item xs={12} sm={6}>
                  <Typography variant="subtitle2">Application ID:</Typography>
                  <Typography variant="body2">
                    {selectedApplication.applicationId}
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <Typography variant="subtitle2">Applied Date:</Typography>
                  <Typography variant="body2">
                    {formatDate(selectedApplication.appliedDate)}
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <Typography variant="subtitle2">Internship ID:</Typography>
                  <Typography variant="body2">
                    {selectedApplication.internshipId}
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <Typography variant="subtitle2">Student ID:</Typography>
                  <Typography variant="body2">
                    {selectedApplication.studentId}
                  </Typography>
                </Grid>
              </Grid>

              {/* Staff Comments */}
              {selectedApplication.staffComments && (
                <Box sx={{ mt: 3 }}>
                  <Typography variant="subtitle2" gutterBottom>
                    Staff Comments:
                  </Typography>
                  <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                    <Typography variant="body2">
                      {selectedApplication.staffComments}
                    </Typography>
                  </Paper>
                </Box>
              )}

              {/* Next Steps */}
              {selectedApplication.status === 'SUCCESSFUL' && (
                <Alert severity="info" sx={{ mt: 3 }}>
                  <Typography variant="subtitle2" gutterBottom>
                    Next Steps:
                  </Typography>
                  <Typography variant="body2">
                    Go to "Accept Placement" to confirm your acceptance of this internship
                  </Typography>
                </Alert>
              )}

              {selectedApplication.status === 'PENDING' && (
                <Alert severity="info" sx={{ mt: 3 }}>
                  <Typography variant="subtitle2" gutterBottom>
                    Next Steps:
                  </Typography>
                  <Typography variant="body2">
                    Your application is being reviewed. You can request withdrawal from "Request Withdrawal" if needed.
                  </Typography>
                </Alert>
              )}
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDetailsOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default MyApplications;
