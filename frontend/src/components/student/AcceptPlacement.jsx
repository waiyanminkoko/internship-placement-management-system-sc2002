import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Button,
  Alert,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Grid,
  Divider,
} from '@mui/material';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import { studentService } from '../../services/studentService';
import { useAuth } from '../../context/AuthContext';
import { formatDate } from '../../utils/formatters';

const AcceptPlacement = () => {
  const { user, updateUser } = useAuth();
  const [applications, setApplications] = useState([]);
  const [withdrawalRequests, setWithdrawalRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedApplication, setSelectedApplication] = useState(null);
  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);
  const [processing, setProcessing] = useState(false);

  useEffect(() => {
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    try {
      setLoading(true);
      setError('');
      const [applicationsData, withdrawalsData] = await Promise.all([
        studentService.getMyApplications(user.userId),
        studentService.getWithdrawalRequests(user.userId),
      ]);
      // Filter for successful applications only (approved by company representative)
      const approvedApps = applicationsData.filter(app => app.status === 'SUCCESSFUL');
      setApplications(approvedApps);
      setWithdrawalRequests(withdrawalsData || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch applications');
    } finally {
      setLoading(false);
    }
  };

  // Check if application has a pending withdrawal request
  const hasPendingWithdrawal = (applicationId) => {
    return withdrawalRequests.some(
      wr => wr.applicationId === applicationId && wr.status === 'PENDING'
    );
  };

  const handleAcceptClick = (application) => {
    setSelectedApplication(application);
    setConfirmDialogOpen(true);
  };

  const handleAcceptConfirm = async () => {
    try {
      setProcessing(true);
      setError('');
      setSuccess('');

      await studentService.acceptPlacement(
        user.userId,
        selectedApplication.applicationId
      );

      setSuccess('Placement accepted successfully! Congratulations!');
      setConfirmDialogOpen(false);
      setSelectedApplication(null);
      
      // Update user context to reflect accepted placement
      const updatedUser = { ...user, hasAcceptedPlacement: true };
      updateUser(updatedUser);
      
      // Refresh applications
      await fetchApplications();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to accept placement');
      setConfirmDialogOpen(false);
    } finally {
      setProcessing(false);
    }
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
        Accept Placement
      </Typography>

      <Typography color="textSecondary" paragraph>
        Review and accept your approved internship placements below.
      </Typography>

      <Alert severity="warning" sx={{ mb: 3 }}>
        <Typography variant="subtitle2" gutterBottom>
          Important Notice
        </Typography>
        <Typography variant="body2">
          <strong>Once you accept a placement, you cannot withdraw through the system.</strong><br />
          If you need to withdraw from an accepted placement, you must contact your respective school's Career Support Staff directly.
        </Typography>
      </Alert>

      {error && (
        <Alert severity="error" onClose={() => setError('')} sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {success && (
        <Alert 
          severity="success" 
          onClose={() => setSuccess('')} 
          sx={{ mb: 2 }}
          icon={<CheckCircleOutlineIcon />}
        >
          {success}
        </Alert>
      )}

      {applications.length === 0 ? (
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6" color="textSecondary" gutterBottom>
            No Approved Placements
          </Typography>
          <Typography color="textSecondary">
            You don't have any approved placements to accept at this time.
          </Typography>
          <Typography variant="body2" color="textSecondary" sx={{ mt: 2 }}>
            Check "My Applications" to see the status of your applications.
          </Typography>
        </Paper>
      ) : (
        <Grid container spacing={3}>
          {applications.map((application) => (
            <Grid item xs={12} key={application.applicationId}>
              <Paper sx={{ p: 3 }}>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={8}>
                    <Typography variant="h6" gutterBottom>
                      {application.internshipTitle}
                    </Typography>
                    <Typography color="textSecondary" gutterBottom>
                      {application.companyName}
                    </Typography>

                    <Divider sx={{ my: 2 }} />

                    <Grid container spacing={2}>
                      <Grid item xs={6}>
                        <Typography variant="subtitle2" color="textSecondary">
                          Application ID:
                        </Typography>
                        <Typography variant="body2">
                          {application.applicationId}
                        </Typography>
                      </Grid>
                      <Grid item xs={6}>
                        <Typography variant="subtitle2" color="textSecondary">
                          Internship ID:
                        </Typography>
                        <Typography variant="body2">
                          {application.internshipId}
                        </Typography>
                      </Grid>
                      <Grid item xs={6}>
                        <Typography variant="subtitle2" color="textSecondary">
                          Applied Date:
                        </Typography>
                        <Typography variant="body2">
                          {formatDate(application.appliedDate)}
                        </Typography>
                      </Grid>
                      <Grid item xs={6}>
                        <Typography variant="subtitle2" color="textSecondary">
                          Status:
                        </Typography>
                        <Typography variant="body2" color="success.main">
                          APPROVED
                        </Typography>
                      </Grid>
                    </Grid>

                    {application.staffComments && (
                      <Box sx={{ mt: 2 }}>
                        <Typography variant="subtitle2" color="textSecondary">
                          Staff Comments:
                        </Typography>
                        <Paper sx={{ p: 1.5, bgcolor: 'grey.50', mt: 1 }}>
                          <Typography variant="body2">
                            {application.staffComments}
                          </Typography>
                        </Paper>
                      </Box>
                    )}
                  </Grid>

                  <Grid 
                    item 
                    xs={12} 
                    md={4} 
                    sx={{ 
                      display: 'flex', 
                      flexDirection: 'column',
                      justifyContent: 'center',
                      alignItems: 'center',
                    }}
                  >
                    <Alert severity="success" sx={{ mb: 2, width: '100%' }}>
                      <Typography variant="subtitle2" gutterBottom>
                        Congratulations!
                      </Typography>
                      <Typography variant="body2">
                        Your application has been approved
                      </Typography>
                    </Alert>

                    {hasPendingWithdrawal(application.applicationId) ? (
                      <>
                        <Alert severity="warning" sx={{ mb: 2, width: '100%' }}>
                          <Typography variant="subtitle2" gutterBottom>
                            Withdrawal Request Pending
                          </Typography>
                          <Typography variant="body2">
                            You have a pending withdrawal request for this internship
                          </Typography>
                        </Alert>
                        <Button
                          variant="outlined"
                          color="warning"
                          size="large"
                          fullWidth
                          disabled
                        >
                          Cannot Accept - Withdrawal Pending
                        </Button>
                        <Typography 
                          variant="caption" 
                          color="textSecondary" 
                          sx={{ mt: 2, textAlign: 'center' }}
                        >
                          Cancel your withdrawal request to accept this placement
                        </Typography>
                      </>
                    ) : application.placementAccepted ? (
                      <>
                        <Button
                          variant="contained"
                          color="success"
                          size="large"
                          fullWidth
                          disabled
                          startIcon={<CheckCircleOutlineIcon />}
                          sx={{ 
                            bgcolor: 'success.light',
                            '&.Mui-disabled': {
                              bgcolor: 'success.light',
                              color: 'white',
                            }
                          }}
                        >
                          Placement Accepted
                        </Button>
                        <Typography 
                          variant="caption" 
                          color="textSecondary" 
                          sx={{ mt: 2, textAlign: 'center' }}
                        >
                          You have accepted this internship placement
                        </Typography>
                      </>
                    ) : (
                      <>
                        <Button
                          variant="contained"
                          color="success"
                          size="large"
                          fullWidth
                          onClick={() => handleAcceptClick(application)}
                          startIcon={<CheckCircleOutlineIcon />}
                        >
                          Accept Placement
                        </Button>
                        <Typography 
                          variant="caption" 
                          color="textSecondary" 
                          sx={{ mt: 2, textAlign: 'center' }}
                        >
                          By accepting, you confirm your commitment to this internship
                        </Typography>
                      </>
                    )}
                  </Grid>
                </Grid>
              </Paper>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Confirmation Dialog */}
      <Dialog
        open={confirmDialogOpen}
        onClose={() => !processing && setConfirmDialogOpen(false)}
      >
        <DialogTitle>Confirm Placement Acceptance</DialogTitle>
        <DialogContent>
          {selectedApplication && (
            <Box sx={{ pt: 2 }}>
              <Alert severity="warning" sx={{ mb: 2 }}>
                <Typography variant="subtitle2" gutterBottom>
                  Important Notice
                </Typography>
                <Typography variant="body2">
                  By accepting this placement, you are making a commitment to complete this internship.<br />
                  <strong>Once accepted, you cannot withdraw through the system.</strong> If you need to withdraw later, 
                  you must contact your school's Career Support Staff directly.
                </Typography>
              </Alert>

              <Typography variant="h6" gutterBottom>
                {selectedApplication.internshipTitle}
              </Typography>
              <Typography color="textSecondary" gutterBottom>
                {selectedApplication.companyName}
              </Typography>

              <Divider sx={{ my: 2 }} />

              <Typography variant="body2" paragraph>
                Application ID: {selectedApplication.applicationId}
              </Typography>

              <Typography variant="body2" color="textSecondary">
                Are you sure you want to accept this placement?
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => setConfirmDialogOpen(false)}
            disabled={processing}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            color="success"
            onClick={handleAcceptConfirm}
            disabled={processing}
            startIcon={processing ? <CircularProgress size={20} /> : <CheckCircleOutlineIcon />}
          >
            {processing ? 'Accepting...' : 'Confirm Acceptance'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AcceptPlacement;
