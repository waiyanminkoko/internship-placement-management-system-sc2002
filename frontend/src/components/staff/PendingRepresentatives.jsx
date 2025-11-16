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
} from '@mui/material';
import { toast } from 'react-toastify';
import staffService from '../../services/staffService';

/**
 * Component to manage pending company representative registrations.
 */
const PendingRepresentatives = ({ staffId }) => {
  const [representatives, setRepresentatives] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedRep, setSelectedRep] = useState(null);
  const [comments, setComments] = useState('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [actionType, setActionType] = useState(null); // 'approve' or 'reject'

  useEffect(() => {
    loadRepresentatives();
  }, [staffId]);

  const loadRepresentatives = async () => {
    try {
      setLoading(true);
      const data = await staffService.getPendingRepresentatives(staffId);
      setRepresentatives(data || []);
    } catch (error) {
      toast.error('Failed to load representatives');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (rep, action) => {
    setSelectedRep(rep);
    setActionType(action);
    setComments('');
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedRep(null);
    setComments('');
    setActionType(null);
  };

  const handleConfirmAction = async () => {
    if (!selectedRep) return;

    try {
      const approve = actionType === 'approve';
      await staffService.authorizeRepresentative(
        staffId,
        selectedRep.userId,
        approve,
        comments
      );
      toast.success(
        `Representative ${approve ? 'approved' : 'rejected'} successfully`
      );
      handleCloseDialog();
      loadRepresentatives();
    } catch (error) {
      toast.error(`Failed to ${actionType} representative`);
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
          Pending Company Representatives
        </Typography>

        {representatives.length === 0 ? (
          <Alert severity="info">No pending representative registrations.</Alert>
        ) : (
          <TableContainer component={Paper} sx={{ mt: 2 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Company</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Position</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {representatives.map((rep) => (
                  <TableRow key={rep.userId}>
                    <TableCell>{rep.userId}</TableCell>
                    <TableCell>{rep.name}</TableCell>
                    <TableCell>{rep.companyName}</TableCell>
                    <TableCell>{rep.email}</TableCell>
                    <TableCell>{rep.position}</TableCell>
                    <TableCell>
                      <Chip
                        label={rep.status}
                        color={rep.status === 'PENDING' ? 'warning' : 'default'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Button
                        variant="contained"
                        color="success"
                        size="small"
                        onClick={() => handleOpenDialog(rep, 'approve')}
                        sx={{ mr: 1 }}
                      >
                        Approve
                      </Button>
                      <Button
                        variant="contained"
                        color="error"
                        size="small"
                        onClick={() => handleOpenDialog(rep, 'reject')}
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

        {/* Confirmation Dialog */}
        <Dialog open={dialogOpen} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
          <DialogTitle>
            {actionType === 'approve' ? 'Approve' : 'Reject'} Representative
          </DialogTitle>
          <DialogContent>
            {selectedRep && (
              <>
                <Typography variant="body1" gutterBottom>
                  <strong>Name:</strong> {selectedRep.name}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>Company:</strong> {selectedRep.companyName}
                </Typography>
                <Typography variant="body1" gutterBottom sx={{ mb: 2 }}>
                  <strong>Email:</strong> {selectedRep.email}
                </Typography>
                <TextField
                  fullWidth
                  label="Comments (Optional)"
                  multiline
                  rows={3}
                  value={comments}
                  onChange={(e) => setComments(e.target.value)}
                  placeholder="Add any comments or reasons..."
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

export default PendingRepresentatives;
