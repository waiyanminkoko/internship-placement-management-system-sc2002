import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
  Alert,
  Grid,
  Avatar,
  Divider,
} from '@mui/material';
import {
  CheckCircle as ApproveIcon,
  Cancel as RejectIcon,
  Refresh as RefreshIcon,
  Assignment as AssignmentIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import representativeService from '../../services/representativeService';
import { formatDate } from '../../utils/formatters';

/**
 * Applications component for company representatives.
 * Review and manage student applications for internships.
 * 
 * @param {Object} props - Component props
 * @param {string} props.repId - Representative ID (email)
 * @returns {JSX.Element} Applications management
 */
const Applications = ({ repId }) => {
  const [loading, setLoading] = useState(true);
  const [internships, setInternships] = useState([]);
  const [selectedInternship, setSelectedInternship] = useState(null);
  const [applications, setApplications] = useState([]);
  const [selectedApp, setSelectedApp] = useState(null);
  const [detailDialog, setDetailDialog] = useState(false);

  useEffect(() => {
    fetchInternships();
  }, [repId]);

  const fetchInternships = async () => {
    try {
      setLoading(true);
      const data = await representativeService.getMyInternships(repId);
      // Only show approved internships for reviewing applications
      const approvedInternships = (data || []).filter(internship => internship.status === 'APPROVED');
      setInternships(approvedInternships);
      if (approvedInternships && approvedInternships.length > 0) {
        selectInternship(approvedInternships[0]);
      }
    } catch (error) {
      console.error('Error fetching internships:', error);
      toast.error('Failed to load internships');
    } finally {
      setLoading(false);
    }
  };

  const selectInternship = async (internship) => {
    setSelectedInternship(internship);
    try {
      const apps = await representativeService.getApplications(repId, internship.internshipId);
      setApplications(apps || []);
    } catch (error) {
      console.error('Error fetching applications:', error);
      toast.error('Failed to load applications');
    }
  };

  const handleApprove = async (applicationId) => {
    try {
      await representativeService.approveApplication(repId, applicationId);
      toast.success('Application approved successfully!');
      if (selectedInternship) {
        selectInternship(selectedInternship);
      }
      setDetailDialog(false);
    } catch (error) {
      console.error('Error approving application:', error);
      toast.error(error.response?.data?.message || 'Failed to approve application');
    }
  };

  const handleReject = async (applicationId) => {
    try {
      await representativeService.rejectApplication(repId, applicationId);
      toast.success('Application rejected');
      if (selectedInternship) {
        selectInternship(selectedInternship);
      }
      setDetailDialog(false);
    } catch (error) {
      console.error('Error rejecting application:', error);
      toast.error('Failed to reject application');
    }
  };

  const viewDetails = (application) => {
    setSelectedApp(application);
    setDetailDialog(true);
  };

  const getStatusColor = (status) => {
    const colors = {
      PENDING: 'warning',
      APPROVED: 'success',
      REJECTED: 'error',
      ACCEPTED: 'info',
    };
    return colors[status] || 'default';
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h5" sx={{ fontWeight: 700, mb: 1 }}>
            Review Applications
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Review and approve or reject student applications
          </Typography>
        </Box>
        <Button
          variant="outlined"
          startIcon={<RefreshIcon />}
          onClick={fetchInternships}
        >
          Refresh
        </Button>
      </Box>

      {/* Info alert about approved internships only */}
      <Alert severity="info" sx={{ mb: 3 }}>
        <Typography variant="body2">
          <strong>Note:</strong> Only showing applications for <strong>approved</strong> internship opportunities. 
          Pending or rejected opportunities will not appear here until they are approved by Career Center Staff.
        </Typography>
      </Alert>

      {internships.length === 0 ? (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <AssignmentIcon sx={{ fontSize: 80, color: 'text.disabled', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No approved internships available
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Create an internship opportunity and wait for Career Center Staff approval to start receiving applications
          </Typography>
        </Paper>
      ) : (
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                Internship Applications
              </Typography>
              {internships.map((internship) => {
                // Use pending application count from backend (updated live)
                const pendingCount = internship.pendingApplicationCount || 0;
                
                return (
                  <Box
                    key={internship.internshipId}
                    sx={{
                      p: 2,
                      mb: 1,
                      borderRadius: 1,
                      cursor: 'pointer',
                      bgcolor: selectedInternship?.internshipId === internship.internshipId ? 'primary.light' : 'grey.50',
                      '&:hover': { bgcolor: 'grey.100' },
                    }}
                    onClick={() => selectInternship(internship)}
                  >
                    <Typography variant="body2" sx={{ fontWeight: 600 }}>
                      {internship.title}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      {pendingCount} pending actions
                    </Typography>
                  </Box>
                );
              })}
            </Paper>
          </Grid>

          <Grid item xs={12} md={8}>
            {selectedInternship && (
              <Paper>
                <Box sx={{ p: 2, bgcolor: 'primary.light' }}>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    {selectedInternship.title}
                  </Typography>
                  <Typography variant="body2">
                    {applications.length} total applications
                  </Typography>
                </Box>
                
                {applications.length === 0 ? (
                  <Box sx={{ p: 8, textAlign: 'center' }}>
                    <Typography variant="body2" color="text.secondary">
                      No applications yet
                    </Typography>
                  </Box>
                ) : (
                  <TableContainer>
                    <Table>
                      <TableHead>
                        <TableRow>
                          <TableCell><strong>Student</strong></TableCell>
                          <TableCell><strong>Applied Date</strong></TableCell>
                          <TableCell><strong>Status</strong></TableCell>
                          <TableCell align="center"><strong>Actions</strong></TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {applications.map((app) => (
                          <TableRow key={app.applicationId} hover>
                            <TableCell>
                              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                <Avatar sx={{ width: 32, height: 32 }}>
                                  {app.studentName?.charAt(0) || 'S'}
                                </Avatar>
                                <Box>
                                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                    {app.studentName || app.studentId}
                                  </Typography>
                                  <Typography variant="caption" color="text.secondary">
                                    {app.studentId}
                                  </Typography>
                                </Box>
                              </Box>
                            </TableCell>
                            <TableCell>
                              {formatDate(app.applicationDate)}
                            </TableCell>
                            <TableCell>
                              <Chip
                                label={app.status || 'PENDING'}
                                color={getStatusColor(app.status)}
                                size="small"
                              />
                            </TableCell>
                            <TableCell align="center">
                              <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                                <Button
                                  size="small"
                                  onClick={() => viewDetails(app)}
                                >
                                  View
                                </Button>
                                {app.status === 'PENDING' && (
                                  <>
                                    <Button
                                      size="small"
                                      variant="contained"
                                      color="success"
                                      startIcon={<ApproveIcon />}
                                      onClick={() => handleApprove(app.applicationId)}
                                    >
                                      Approve
                                    </Button>
                                    <Button
                                      size="small"
                                      variant="outlined"
                                      color="error"
                                      startIcon={<RejectIcon />}
                                      onClick={() => handleReject(app.applicationId)}
                                    >
                                      Reject
                                    </Button>
                                  </>
                                )}
                              </Box>
                            </TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                )}
              </Paper>
            )}
          </Grid>
        </Grid>
      )}

      {/* Application Detail Dialog */}
      <Dialog open={detailDialog} onClose={() => setDetailDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Student Application Details</DialogTitle>
        <DialogContent>
          {selectedApp && (
            <Box sx={{ mt: 2 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
                <Avatar sx={{ width: 56, height: 56, bgcolor: 'primary.main' }}>
                  {selectedApp.studentName?.charAt(0) || 'S'}
                </Avatar>
                <Box>
                  <Typography variant="h6">{selectedApp.studentName || 'Unknown'}</Typography>
                  <Typography variant="body2" color="text.secondary">{selectedApp.studentId}</Typography>
                </Box>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <Typography variant="subtitle2" color="text.secondary">Email</Typography>
                  <Typography variant="body1" sx={{ mb: 1 }}>{selectedApp.studentEmail || 'N/A'}</Typography>
                </Grid>

                <Grid item xs={6}>
                  <Typography variant="subtitle2" color="text.secondary">Major</Typography>
                  <Typography variant="body1" sx={{ mb: 1 }}>{selectedApp.studentMajor || 'N/A'}</Typography>
                </Grid>

                <Grid item xs={6}>
                  <Typography variant="subtitle2" color="text.secondary">Year</Typography>
                  <Typography variant="body1" sx={{ mb: 1 }}>
                    Year {selectedApp.studentYear || 'N/A'}
                  </Typography>
                </Grid>

                <Grid item xs={12}>
                  <Typography variant="subtitle2" color="text.secondary">Application Date</Typography>
                  <Typography variant="body1" sx={{ mb: 1 }}>
                    {formatDate(selectedApp.applicationDate)}
                  </Typography>
                </Grid>

                <Grid item xs={12}>
                  <Typography variant="subtitle2" color="text.secondary">Status</Typography>
                  <Chip
                    label={selectedApp.status || 'PENDING'}
                    color={getStatusColor(selectedApp.status)}
                    size="small"
                    sx={{ mb: 1 }}
                  />
                </Grid>
              </Grid>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          {selectedApp?.status === 'PENDING' && (
            <>
              <Button
                onClick={() => handleReject(selectedApp.applicationId)}
                color="error"
                startIcon={<RejectIcon />}
              >
                Reject
              </Button>
              <Button
                onClick={() => handleApprove(selectedApp.applicationId)}
                variant="contained"
                startIcon={<ApproveIcon />}
              >
                Approve
              </Button>
            </>
          )}
          <Button onClick={() => setDetailDialog(false)}>Close</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Applications;
