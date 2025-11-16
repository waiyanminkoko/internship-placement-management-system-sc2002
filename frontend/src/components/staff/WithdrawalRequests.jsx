import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  CircularProgress,
  Alert,
  Grid,
} from '@mui/material';
import { toast } from 'react-toastify';
import staffService from '../../services/staffService';
import { formatDate } from '../../utils/formatters';

/**
 * Component to manage pending withdrawal requests.
 */
const WithdrawalRequests = ({ staffId }) => {
  const [withdrawals, setWithdrawals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedWithdrawal, setSelectedWithdrawal] = useState(null);
  const [comments, setComments] = useState('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
  const [actionType, setActionType] = useState(null);

  useEffect(() => {
    loadWithdrawals();
  }, [staffId]);

  const loadWithdrawals = async () => {
    try {
      setLoading(true);
      const data = await staffService.getPendingWithdrawals(staffId);
      setWithdrawals(data || []);
    } catch (error) {
      toast.error('Failed to load withdrawal requests');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = (withdrawal) => {
    setSelectedWithdrawal(withdrawal);
    setDetailsDialogOpen(true);
  };

  const handleOpenActionDialog = (withdrawal, action) => {
    setSelectedWithdrawal(withdrawal);
    setActionType(action);
    setComments('');
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedWithdrawal(null);
    setComments('');
    setActionType(null);
  };

  const handleConfirmAction = async () => {
    if (!selectedWithdrawal) return;

    try {
      const approve = actionType === 'approve';
      await staffService.processWithdrawal(
        staffId,
        selectedWithdrawal.withdrawalId,
        approve,
        comments
      );
      toast.success(
        `Withdrawal request ${approve ? 'approved' : 'rejected'} successfully`
      );
      handleCloseDialog();
      loadWithdrawals();
    } catch (error) {
      toast.error(`Failed to ${actionType} withdrawal request`);
      console.error(error);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" p={4}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Pending Withdrawal Requests
        </Typography>

        {withdrawals.length === 0 ? (
          <Alert severity="info">No pending withdrawal requests.</Alert>
        ) : (
          <TableContainer component={Paper} sx={{ mt: 2 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Request ID</TableCell>
                  <TableCell>Student ID</TableCell>
                  <TableCell>Student Name</TableCell>
                  <TableCell>Major</TableCell>
                  <TableCell>Year</TableCell>
                  <TableCell>Internship</TableCell>
                  <TableCell>Company</TableCell>
                  <TableCell>Request Date</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {withdrawals.map((withdrawal) => (
                  <TableRow key={withdrawal.withdrawalId}>
                    <TableCell>{withdrawal.withdrawalId}</TableCell>
                    <TableCell>{withdrawal.studentId}</TableCell>
                    <TableCell>{withdrawal.studentName || 'N/A'}</TableCell>
                    <TableCell>{withdrawal.studentMajor || 'N/A'}</TableCell>
                    <TableCell>{withdrawal.studentYear || 'N/A'}</TableCell>
                    <TableCell>{withdrawal.internshipTitle || 'N/A'}</TableCell>
                    <TableCell>{withdrawal.companyName || 'N/A'}</TableCell>
                    <TableCell>{formatDate(withdrawal.requestDate)}</TableCell>
                    <TableCell>
                      <Chip
                        label={withdrawal.status}
                        color="warning"
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Button
                        variant="outlined"
                        size="small"
                        onClick={() => handleViewDetails(withdrawal)}
                        sx={{ mr: 1, mb: 0.5 }}
                      >
                        View
                      </Button>
                      <Button
                        variant="contained"
                        color="success"
                        size="small"
                        onClick={() => handleOpenActionDialog(withdrawal, 'approve')}
                        sx={{ mr: 1, mb: 0.5 }}
                      >
                        Approve
                      </Button>
                      <Button
                        variant="contained"
                        color="error"
                        size="small"
                        onClick={() => handleOpenActionDialog(withdrawal, 'reject')}
                        sx={{ mb: 0.5 }}
                      >
                        Reject
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}

        {/* Details Dialog */}
        <Dialog
          open={detailsDialogOpen}
          onClose={() => setDetailsDialogOpen(false)}
          maxWidth="sm"
          fullWidth
        >
          <DialogTitle>Withdrawal Request Details</DialogTitle>
          <DialogContent>
            {selectedWithdrawal && (
              <Grid container spacing={2} sx={{ mt: 1 }}>
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Request ID
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.withdrawalId}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Student ID
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.studentId}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Student Name
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.studentName || 'N/A'}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Major
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.studentMajor || 'N/A'}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Year
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.studentYear || 'N/A'}</Typography>
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Internship
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.internshipTitle || 'N/A'}</Typography>
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Company
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.companyName || 'N/A'}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Request Date
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(selectedWithdrawal.requestDate)}
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Status
                  </Typography>
                  <Typography variant="body1">{selectedWithdrawal.status}</Typography>
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Reason
                  </Typography>
                  <Typography variant="body1">
                    {selectedWithdrawal.reason || 'No reason provided'}
                  </Typography>
                </Grid>
              </Grid>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setDetailsDialogOpen(false)}>Close</Button>
          </DialogActions>
        </Dialog>

        {/* Action Confirmation Dialog */}
        <Dialog open={dialogOpen} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
          <DialogTitle>
            {actionType === 'approve' ? 'Approve' : 'Reject'} Withdrawal Request
          </DialogTitle>
          <DialogContent>
            {selectedWithdrawal && (
              <>
                <Typography variant="body1" gutterBottom>
                  <strong>Student:</strong> {selectedWithdrawal.studentName || 'N/A'} (
                  {selectedWithdrawal.studentId})
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>Major:</strong> {selectedWithdrawal.studentMajor || 'N/A'} | <strong>Year:</strong> {selectedWithdrawal.studentYear || 'N/A'}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>Internship:</strong> {selectedWithdrawal.internshipTitle || 'N/A'}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>Company:</strong> {selectedWithdrawal.companyName || 'N/A'}
                </Typography>
                <Typography variant="body1" gutterBottom sx={{ mb: 2 }}>
                  <strong>Reason:</strong> {selectedWithdrawal.reason || 'Not provided'}
                </Typography>
                <TextField
                  fullWidth
                  label="Comments (Optional)"
                  multiline
                  rows={3}
                  value={comments}
                  onChange={(e) => setComments(e.target.value)}
                  placeholder="Add any comments or feedback..."
                />
              </>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button
              variant="contained"
              color={actionType === 'approve' ? 'success' : 'error'}
              onClick={handleConfirmAction}
            >
              Confirm {actionType === 'approve' ? 'Approval' : 'Rejection'}
            </Button>
          </DialogActions>
        </Dialog>
      </CardContent>
    </Card>
  );
};

export default WithdrawalRequests;
