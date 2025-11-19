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
  TextField,
  Grid,
  Divider,
  Chip,
  Tabs,
  Tab,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import {
  WarningAmber as WarningAmberIcon,
  Edit as EditIcon,
  History as HistoryIcon,
  Info as InfoIcon,
} from '@mui/icons-material';
import { studentService } from '../../services/studentService';
import { useAuth } from '../../context/AuthContext';
import { formatDate } from '../../utils/formatters';

const WithdrawalRequest = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [withdrawalRequests, setWithdrawalRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedApplication, setSelectedApplication] = useState(null);
  const [withdrawDialogOpen, setWithdrawDialogOpen] = useState(false);
  const [editWithdrawDialogOpen, setEditWithdrawDialogOpen] = useState(false);
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [withdrawalReason, setWithdrawalReason] = useState('');
  const [cancellationReason, setCancellationReason] = useState('');
  const [selectedWithdrawal, setSelectedWithdrawal] = useState(null);
  const [processing, setProcessing] = useState(false);
  const [activeTab, setActiveTab] = useState(0);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError('');
      const [applicationsData, withdrawalsData] = await Promise.all([
        studentService.getMyApplications(user.userId),
        studentService.getWithdrawalRequests(user.userId),
      ]);
      
      // Filter for applications that can be withdrawn:
      // 1. PENDING applications (not yet reviewed by company)
      // 2. SUCCESSFUL applications that have NOT been accepted yet
      // Note: Students cannot withdraw from accepted placements - they must contact career staff directly
      const withdrawableApps = applicationsData.filter(
        app => app.status === 'PENDING' || (app.status === 'SUCCESSFUL' && !app.placementAccepted)
      );
      setApplications(withdrawableApps);
      setWithdrawalRequests(withdrawalsData || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  // Check if application has an existing withdrawal request
  // Returns the most recent (latest) withdrawal request for the application
  const getWithdrawalForApplication = (applicationId) => {
    const requests = withdrawalRequests.filter(wr => wr.applicationId === applicationId);
    if (requests.length === 0) return null;
    
    // Priority 1: If there's a PENDING request, always return it (it's the active one)
    // If multiple PENDING (shouldn't happen), return most recent
    const pendingRequests = requests.filter(wr => wr.status === 'PENDING');
    if (pendingRequests.length > 0) {
      if (pendingRequests.length === 1) return pendingRequests[0];
      // Sort by requestId timestamp if multiple pending requests
      pendingRequests.sort((a, b) => (b.requestId || '').localeCompare(a.requestId || ''));
      return pendingRequests[0];
    }
    
    // Priority 2: If there's an APPROVED request, return it
    const approvedRequest = requests.find(wr => wr.status === 'APPROVED');
    if (approvedRequest) return approvedRequest;
    
    // Priority 3: If there's a CANCELLED request, return the most recent one
    const cancelledRequests = requests.filter(wr => wr.status === 'CANCELLED');
    if (cancelledRequests.length > 0) {
      cancelledRequests.sort((a, b) => (b.requestId || '').localeCompare(a.requestId || ''));
      return cancelledRequests[0];
    }
    
    // Priority 4: Return the most recent REJECTED request (sort by requestId which contains timestamp)
    // requestId format: WDR-YYYYMMDD-HHMMSS-randomhex
    const rejectedRequests = requests.filter(wr => wr.status === 'REJECTED');
    if (rejectedRequests.length > 0) {
      rejectedRequests.sort((a, b) => {
        // requestId contains timestamp, sort descending (newest first)
        return (b.requestId || '').localeCompare(a.requestId || '');
      });
      return rejectedRequests[0];
    }
    
    // Fallback: return first request
    return requests[0];
  };

  // Check if student can submit new withdrawal request for this application
  const canSubmitNewWithdrawal = (application) => {
    const existing = getWithdrawalForApplication(application.applicationId);
    if (!existing) return true; // No existing request
    if (existing.status === 'REJECTED') return true; // Rejected - can submit new
    if (existing.status === 'CANCELLED') return true; // Cancelled - can submit new
    return false; // Pending or Approved - cannot submit new
  };

  const handleWithdrawClick = (application) => {
    const existing = getWithdrawalForApplication(application.applicationId);
    if (existing && existing.status === 'PENDING') {
      // Edit existing pending request
      setSelectedWithdrawal(existing);
      setWithdrawalReason(existing.reason);
      setEditWithdrawDialogOpen(true);
    } else {
      // Create new withdrawal request
      setSelectedApplication(application);
      setWithdrawalReason('');
      setWithdrawDialogOpen(true);
    }
  };

  const handleWithdrawConfirm = async () => {
    if (!withdrawalReason.trim()) {
      setError('Please provide a reason for withdrawal');
      return;
    }

    try {
      setProcessing(true);
      setError('');
      setSuccess('');

      await studentService.requestWithdrawal(
        user.userId,
        selectedApplication.applicationId,
        withdrawalReason
      );

      setSuccess('Withdrawal request submitted successfully and is pending staff approval');
      setWithdrawDialogOpen(false);
      setSelectedApplication(null);
      setWithdrawalReason('');
      
      // Refresh data
      await fetchData();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit withdrawal request');
      setWithdrawDialogOpen(false);
    } finally {
      setProcessing(false);
    }
  };

  const handleEditWithdrawConfirm = async () => {
    if (!withdrawalReason.trim()) {
      setError('Please provide a reason for withdrawal');
      return;
    }

    try {
      setProcessing(true);
      setError('');
      setSuccess('');

      await studentService.updateWithdrawalRequest(
        user.userId,
        selectedWithdrawal.requestId,
        withdrawalReason
      );

      setSuccess('Withdrawal request updated successfully');
      setEditWithdrawDialogOpen(false);
      setSelectedWithdrawal(null);
      setWithdrawalReason('');
      
      // Refresh data
      await fetchData();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update withdrawal request');
      setEditWithdrawDialogOpen(false);
    } finally {
      setProcessing(false);
    }
  };

  const handleCancelWithdrawalClick = (withdrawalRequest) => {
    setSelectedWithdrawal(withdrawalRequest);
    setCancellationReason('');
    setCancelDialogOpen(true);
  };

  const handleCancelWithdrawalConfirm = async () => {
    if (!cancellationReason.trim()) {
      setError('Please provide a reason for cancelling the withdrawal request');
      return;
    }

    try {
      setProcessing(true);
      setError('');
      setSuccess('');

      await studentService.cancelWithdrawalRequest(
        user.userId,
        selectedWithdrawal.requestId,
        cancellationReason
      );

      setSuccess('Withdrawal request cancelled successfully. You can now accept the placement.');
      setCancelDialogOpen(false);
      setSelectedWithdrawal(null);
      setCancellationReason('');
      
      // Refresh data
      await fetchData();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to cancel withdrawal request');
      setCancelDialogOpen(false);
    } finally {
      setProcessing(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      PENDING: 'warning',
      APPROVED: 'success',
      REJECTED: 'error',
      CANCELLED: 'default',
    };
    return colors[status] || 'default';
  };

  const getButtonText = (application) => {
    const existing = getWithdrawalForApplication(application.applicationId);
    if (!existing) return 'Request Withdrawal';
    if (existing.status === 'PENDING') return 'Edit Withdrawal Request';
    if (existing.status === 'REJECTED') return 'Submit New Request';
    return 'View Request'; // APPROVED
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
        Withdrawal Requests
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

      <Paper sx={{ mb: 3 }}>
        <Tabs value={activeTab} onChange={(e, newValue) => setActiveTab(newValue)}>
          <Tab label="Request Withdrawal" icon={<WarningAmberIcon />} />
          <Tab label="Withdrawal History" icon={<HistoryIcon />} />
        </Tabs>
      </Paper>

      {/* Request Withdrawal Tab */}
      {activeTab === 0 && (
        <Box>
          <Alert severity="warning" sx={{ mb: 3 }}>
            <Typography variant="subtitle2" gutterBottom>
              Important Information
            </Typography>
            <Typography variant="body2">
              • You can request withdrawal from PENDING applications or SUCCESSFUL applications before acceptance<br />
              • Once you accept a placement, you CANNOT withdraw through this system<br />
              • If you need to withdraw from an accepted placement, contact your Career Center Staff directly<br />
              • All withdrawal requests require Career Center Staff approval<br />
              • You can edit pending withdrawal requests before they are processed<br />
              • If rejected, you can submit a new withdrawal request
            </Typography>
          </Alert>

          {applications.length === 0 ? (
            <Paper sx={{ p: 4, textAlign: 'center' }}>
              <Typography variant="h6" color="textSecondary" gutterBottom>
                No Withdrawable Applications
              </Typography>
              <Typography color="textSecondary">
                You don't have any PENDING or approved (not-yet-accepted) applications that can be withdrawn.
              </Typography>
              <Typography variant="body2" color="textSecondary" sx={{ mt: 2 }}>
                Note: Accepted placements cannot be withdrawn through this system. Contact Career Center Staff if needed.
              </Typography>
              <Typography variant="body2" color="textSecondary" sx={{ mt: 1 }}>
                Check "My Applications" to see all your applications.
              </Typography>
            </Paper>
          ) : (
            <Grid container spacing={3}>
              {applications.map((application) => {
                const existing = getWithdrawalForApplication(application.applicationId);
                const canSubmit = canSubmitNewWithdrawal(application);
                
                return (
                  <Grid item xs={12} key={application.applicationId}>
                    <Paper sx={{ p: 3 }}>
                      <Grid container spacing={2}>
                        <Grid item xs={12} md={8}>
                          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                            <Typography variant="h6">
                              {application.internshipTitle}
                            </Typography>
                            <Chip
                              label={
                                application.status === 'PENDING' ? 'PENDING' :
                                application.placementAccepted ? 'ACCEPTED' : 'APPROVED'
                              }
                              color={
                                application.status === 'PENDING' ? 'warning' :
                                application.placementAccepted ? 'success' : 'primary'
                              }
                              size="small"
                              sx={{ ml: 2 }}
                            />
                          </Box>
                          
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
                                Applied Date:
                              </Typography>
                              <Typography variant="body2">
                                {formatDate(application.appliedDate)}
                              </Typography>
                            </Grid>
                          </Grid>

                          {existing && (
                            <Alert 
                              severity={existing.status === 'PENDING' ? 'info' : existing.status === 'APPROVED' ? 'success' : 'warning'}
                              sx={{ mt: 2 }}
                            >
                              <Typography variant="subtitle2">
                                Withdrawal Request Status: <Chip label={existing.status} size="small" color={getStatusColor(existing.status)} />
                              </Typography>
                              <Typography variant="body2" sx={{ mt: 1 }}>
                                {existing.status === 'PENDING' && 'Your withdrawal request is pending staff approval. You may edit it by clicking the "Edit Withdrawal Request" button.'}
                                {existing.status === 'APPROVED' && 'Your withdrawal request has been approved. No further action needed.'}
                                {existing.status === 'REJECTED' && 'Your withdrawal request was rejected. You can submit a new request.'}
                                {existing.status === 'CANCELLED' && 'You cancelled this withdrawal request. You can submit a new request if needed.'}
                              </Typography>
                              {existing.staffComments && (
                                <Typography variant="body2" sx={{ mt: 1 }}>
                                  <strong>Staff Comments:</strong> {existing.staffComments}
                                </Typography>
                              )}
                            </Alert>
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
                          {canSubmit || (existing && existing.status === 'PENDING') ? (
                            <>
                              <Button
                                variant={existing && existing.status === 'PENDING' ? 'contained' : 'outlined'}
                                color={existing && existing.status === 'PENDING' ? 'primary' : 'error'}
                                size="large"
                                fullWidth
                                onClick={() => handleWithdrawClick(application)}
                                startIcon={existing && existing.status === 'PENDING' ? <EditIcon /> : <WarningAmberIcon />}
                                disabled={processing}
                              >
                                {getButtonText(application)}
                              </Button>

                              {existing && existing.status === 'PENDING' && (
                                <Button
                                  variant="outlined"
                                  color="secondary"
                                  size="medium"
                                  fullWidth
                                  onClick={() => handleCancelWithdrawalClick(existing)}
                                  disabled={processing}
                                  sx={{ mt: 2 }}
                                >
                                  Cancel Withdrawal Request
                                </Button>
                              )}

                              <Typography 
                                variant="caption" 
                                color="textSecondary" 
                                sx={{ mt: 2, textAlign: 'center' }}
                              >
                                {existing && existing.status === 'PENDING' 
                                  ? 'Cancel the request to accept this placement'
                                  : 'You must provide a valid reason for withdrawal'}
                              </Typography>
                            </>
                          ) : existing && existing.status === 'APPROVED' ? (
                            <Alert severity="info" sx={{ width: '100%' }}>
                              <Typography variant="body2">
                                Withdrawal approved. No further action allowed.
                              </Typography>
                            </Alert>
                          ) : null}
                        </Grid>
                      </Grid>
                    </Paper>
                  </Grid>
                );
              })}
            </Grid>
          )}
        </Box>
      )}

      {/* Withdrawal History Tab */}
      {activeTab === 1 && (
        <Box>
          <Alert severity="info" sx={{ mb: 3 }}>
            <Typography variant="body2">
              View all your withdrawal requests and their current status
            </Typography>
          </Alert>

          {withdrawalRequests.length === 0 ? (
            <Paper sx={{ p: 4, textAlign: 'center' }}>
              <InfoIcon sx={{ fontSize: 60, color: 'text.disabled', mb: 2 }} />
              <Typography variant="h6" color="textSecondary" gutterBottom>
                No Withdrawal History
              </Typography>
              <Typography color="textSecondary">
                You haven't submitted any withdrawal requests yet.
              </Typography>
            </Paper>
          ) : (
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell><strong>Request ID</strong></TableCell>
                    <TableCell><strong>Application ID</strong></TableCell>
                    <TableCell><strong>Internship</strong></TableCell>
                    <TableCell><strong>Reason</strong></TableCell>
                    <TableCell><strong>Status</strong></TableCell>
                    <TableCell><strong>Requested Date</strong></TableCell>
                    <TableCell><strong>Processed Date</strong></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {[...withdrawalRequests]
                    .sort((a, b) => {
                      // Sort by requestId (contains timestamp) descending - most recent first
                      return (b.requestId || '').localeCompare(a.requestId || '');
                    })
                    .map((request) => {
                      const application = applications.find(app => app.applicationId === request.applicationId);
                      return (
                        <TableRow 
                          key={request.requestId}
                          sx={{
                            // Highlight PENDING requests
                            backgroundColor: request.status === 'PENDING' ? 'action.hover' : 'inherit'
                          }}
                        >
                          <TableCell>
                            <Typography variant="body2" sx={{ fontWeight: request.status === 'PENDING' ? 600 : 400 }}>
                              {request.requestId}
                            </Typography>
                          </TableCell>
                          <TableCell>{request.applicationId}</TableCell>
                          <TableCell>
                            {application ? application.internshipTitle : request.internshipId || 'N/A'}
                          </TableCell>
                          <TableCell>
                            <Typography variant="body2" noWrap sx={{ maxWidth: 200 }}>
                              {request.reason}
                            </Typography>
                          </TableCell>
                          <TableCell>
                            <Chip 
                              label={request.status} 
                              color={getStatusColor(request.status)}
                              size="small"
                            />
                          </TableCell>
                          <TableCell>{formatDate(request.requestDate)}</TableCell>
                          <TableCell>{request.processedDate ? formatDate(request.processedDate) : '-'}</TableCell>
                        </TableRow>
                      );
                    })}
                </TableBody>
              </Table>
            </TableContainer>
          )}

          {/* Summary Statistics */}
          {withdrawalRequests.length > 0 && (
            <Paper sx={{ p: 3, mt: 3 }}>
              <Typography variant="h6" gutterBottom>
                Summary
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={6} sm={2.4}>
                  <Box sx={{ textAlign: 'center' }}>
                    <Typography variant="h4" color="warning.main">
                      {withdrawalRequests.filter(r => r.status === 'PENDING').length}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Pending
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={6} sm={2.4}>
                  <Box sx={{ textAlign: 'center' }}>
                    <Typography variant="h4" color="success.main">
                      {withdrawalRequests.filter(r => r.status === 'APPROVED').length}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Approved
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={6} sm={2.4}>
                  <Box sx={{ textAlign: 'center' }}>
                    <Typography variant="h4" color="error.main">
                      {withdrawalRequests.filter(r => r.status === 'REJECTED').length}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Rejected
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={6} sm={2.4}>
                  <Box sx={{ textAlign: 'center' }}>
                    <Typography variant="h4" color="text.secondary">
                      {withdrawalRequests.filter(r => r.status === 'CANCELLED').length}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Cancelled
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={2.4}>
                  <Box sx={{ textAlign: 'center' }}>
                    <Typography variant="h4" color="primary.main">
                      {withdrawalRequests.length}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Total
                    </Typography>
                  </Box>
                </Grid>
              </Grid>
            </Paper>
          )}
        </Box>
      )}

      {/* New Withdrawal Dialog */}
      <Dialog
        open={withdrawDialogOpen}
        onClose={() => !processing && setWithdrawDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <WarningAmberIcon color="error" sx={{ mr: 1 }} />
            Request Withdrawal
          </Box>
        </DialogTitle>
        <DialogContent>
          {selectedApplication && (
            <Box sx={{ pt: 2 }}>
              <Alert severity="warning" sx={{ mb: 3 }}>
                <Typography variant="subtitle2" gutterBottom>
                  Withdrawal Process
                </Typography>
                <Typography variant="body2">
                  Your withdrawal request will be reviewed by Career Center Staff. 
                  Approval is required before withdrawal is finalized.<br /><br />
                  <strong>Note:</strong> This process is only for applications you have not yet accepted. 
                  If you have already accepted a placement, you must contact your Career Center Staff directly.
                </Typography>
              </Alert>

              <Typography variant="h6" gutterBottom>
                {selectedApplication.internshipTitle}
              </Typography>
              <Typography color="textSecondary" gutterBottom>
                {selectedApplication.companyName}
              </Typography>

              <Divider sx={{ my: 2 }} />

              <TextField
                fullWidth
                multiline
                rows={4}
                label="Reason for Withdrawal *"
                placeholder="Please provide a detailed reason for your withdrawal request..."
                value={withdrawalReason}
                onChange={(e) => setWithdrawalReason(e.target.value)}
                helperText="A clear and valid reason is required for processing your request"
                sx={{ mt: 2 }}
              />

              <Typography variant="body2" color="textSecondary" sx={{ mt: 2 }}>
                Application ID: {selectedApplication.applicationId}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => setWithdrawDialogOpen(false)}
            disabled={processing}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            color="error"
            onClick={handleWithdrawConfirm}
            disabled={processing || !withdrawalReason.trim()}
            startIcon={processing ? <CircularProgress size={20} /> : <WarningAmberIcon />}
          >
            {processing ? 'Submitting...' : 'Submit Withdrawal Request'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Edit Withdrawal Dialog */}
      <Dialog
        open={editWithdrawDialogOpen}
        onClose={() => !processing && setEditWithdrawDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <EditIcon color="primary" sx={{ mr: 1 }} />
            Edit Withdrawal Request
          </Box>
        </DialogTitle>
        <DialogContent>
          {selectedWithdrawal && (
            <Box sx={{ pt: 2 }}>
              <Alert severity="info" sx={{ mb: 3 }}>
                <Typography variant="subtitle2" gutterBottom>
                  Edit Pending Request
                </Typography>
                <Typography variant="body2">
                  You can update the reason for your pending withdrawal request.
                  The request will remain pending until processed by staff.
                </Typography>
              </Alert>

              <Typography variant="body2" color="textSecondary" gutterBottom>
                <strong>Request ID:</strong> {selectedWithdrawal.requestId}
              </Typography>
              <Typography variant="body2" color="textSecondary" gutterBottom>
                <strong>Application ID:</strong> {selectedWithdrawal.applicationId}
              </Typography>
              <Typography variant="body2" color="textSecondary" gutterBottom>
                <strong>Status:</strong> <Chip label={selectedWithdrawal.status} size="small" color="warning" />
              </Typography>

              <Divider sx={{ my: 2 }} />

              <TextField
                fullWidth
                multiline
                rows={4}
                label="Reason for Withdrawal *"
                placeholder="Update your reason for withdrawal..."
                value={withdrawalReason}
                onChange={(e) => setWithdrawalReason(e.target.value)}
                helperText="A clear and valid reason is required"
                sx={{ mt: 2 }}
              />
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => setEditWithdrawDialogOpen(false)}
            disabled={processing}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={handleEditWithdrawConfirm}
            disabled={processing || !withdrawalReason.trim()}
            startIcon={processing ? <CircularProgress size={20} /> : <EditIcon />}
          >
            {processing ? 'Updating...' : 'Update Request'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Cancel Withdrawal Dialog */}
      <Dialog
        open={cancelDialogOpen}
        onClose={() => !processing && setCancelDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <WarningAmberIcon color="warning" sx={{ mr: 1 }} />
            Cancel Withdrawal Request
          </Box>
        </DialogTitle>
        <DialogContent>
          {selectedWithdrawal && (
            <Box sx={{ pt: 2 }}>
              <Alert severity="warning" sx={{ mb: 3 }}>
                <Typography variant="subtitle2" gutterBottom>
                  Important Notice
                </Typography>
                <Typography variant="body2">
                  By cancelling this withdrawal request, you will be able to accept the internship placement.
                  Please provide a reason for cancelling your withdrawal request.
                </Typography>
              </Alert>

              <Typography variant="body2" color="textSecondary" gutterBottom>
                <strong>Request ID:</strong> {selectedWithdrawal.requestId}
              </Typography>
              <Typography variant="body2" color="textSecondary" gutterBottom>
                <strong>Application ID:</strong> {selectedWithdrawal.applicationId}
              </Typography>
              <Typography variant="body2" color="textSecondary" gutterBottom>
                <strong>Current Status:</strong> <Chip label={selectedWithdrawal.status} size="small" color="warning" />
              </Typography>

              <Divider sx={{ my: 2 }} />

              <TextField
                fullWidth
                multiline
                rows={3}
                label="Reason for Cancellation *"
                placeholder="Please explain why you want to cancel this withdrawal request..."
                value={cancellationReason}
                onChange={(e) => setCancellationReason(e.target.value)}
                helperText="This will be recorded in the request history"
                sx={{ mt: 2 }}
              />
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => setCancelDialogOpen(false)}
            disabled={processing}
          >
            Close
          </Button>
          <Button
            variant="contained"
            color="warning"
            onClick={handleCancelWithdrawalConfirm}
            disabled={processing || !cancellationReason.trim()}
            startIcon={processing ? <CircularProgress size={20} /> : <WarningAmberIcon />}
          >
            {processing ? 'Cancelling...' : 'Confirm Cancellation'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default WithdrawalRequest;
