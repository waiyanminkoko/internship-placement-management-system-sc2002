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
 * Component to manage pending internship opportunities.
 */
const PendingInternships = ({ staffId }) => {
  const [internships, setInternships] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedInternship, setSelectedInternship] = useState(null);
  const [comments, setComments] = useState('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
  const [actionType, setActionType] = useState(null);

  useEffect(() => {
    loadInternships();
  }, [staffId]);

  const loadInternships = async () => {
    try {
      setLoading(true);
      const data = await staffService.getPendingInternships(staffId);
      setInternships(data || []);
    } catch (error) {
      toast.error('Failed to load internships');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = (internship) => {
    setSelectedInternship(internship);
    setDetailsDialogOpen(true);
  };

  const handleOpenActionDialog = (internship, action) => {
    setSelectedInternship(internship);
    setActionType(action);
    setComments('');
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedInternship(null);
    setComments('');
    setActionType(null);
  };

  const handleConfirmAction = async () => {
    if (!selectedInternship) return;

    try {
      const approve = actionType === 'approve';
      await staffService.approveInternship(
        staffId,
        selectedInternship.opportunityId,
        approve,
        comments
      );
      toast.success(
        `Internship ${approve ? 'approved' : 'rejected'} successfully`
      );
      handleCloseDialog();
      loadInternships();
    } catch (error) {
      toast.error(`Failed to ${actionType} internship`);
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
          Pending Internship Opportunities
        </Typography>

        {internships.length === 0 ? (
          <Alert severity="info">No pending internship opportunities.</Alert>
        ) : (
          <TableContainer component={Paper} sx={{ mt: 2 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Title</TableCell>
                  <TableCell>Company</TableCell>
                  <TableCell>Opening Date</TableCell>
                  <TableCell>Closing Date</TableCell>
                  <TableCell>Start Date</TableCell>
                  <TableCell>Slots</TableCell>
                  <TableCell>Level</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {internships.map((internship) => (
                  <TableRow key={internship.opportunityId}>
                    <TableCell>{internship.opportunityId}</TableCell>
                    <TableCell>{internship.title}</TableCell>
                    <TableCell>{internship.companyName}</TableCell>
                    <TableCell>{formatDate(internship.openingDate)}</TableCell>
                    <TableCell>{formatDate(internship.closingDate)}</TableCell>
                    <TableCell>{formatDate(internship.startDate)}</TableCell>
                    <TableCell>{internship.availableSlots}</TableCell>
                    <TableCell>
                      <Chip
                        label={internship.level}
                        color={
                          internship.level === 'BASIC' ? 'primary' : 
                          internship.level === 'INTERMEDIATE' ? 'secondary' : 
                          internship.level === 'ADVANCED' ? 'error' : 
                          'default'
                        }
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={internship.status}
                        color="warning"
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Button
                        variant="outlined"
                        size="small"
                        onClick={() => handleViewDetails(internship)}
                        sx={{ mr: 1, mb: 0.5 }}
                      >
                        View
                      </Button>
                      <Button
                        variant="contained"
                        color="success"
                        size="small"
                        onClick={() => handleOpenActionDialog(internship, 'approve')}
                        sx={{ mr: 1, mb: 0.5 }}
                      >
                        Approve
                      </Button>
                      <Button
                        variant="contained"
                        color="error"
                        size="small"
                        onClick={() => handleOpenActionDialog(internship, 'reject')}
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
          maxWidth="md"
          fullWidth
        >
          <DialogTitle>Internship Details</DialogTitle>
          <DialogContent>
            {selectedInternship && (
              <Grid container spacing={2} sx={{ mt: 1 }}>
                <Grid item xs={12}>
                  <Typography variant="h6">{selectedInternship.title}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Company
                  </Typography>
                  <Typography variant="body1">{selectedInternship.companyName}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Level
                  </Typography>
                  <Typography variant="body1">{selectedInternship.level}</Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Opening Date
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(selectedInternship.openingDate)}
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Closing Date
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(selectedInternship.closingDate)}
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Start Date
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(selectedInternship.startDate)}
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    End Date
                  </Typography>
                  <Typography variant="body1">
                    {formatDate(selectedInternship.endDate)}
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="body2" color="textSecondary">
                    Available Slots
                  </Typography>
                  <Typography variant="body1">{selectedInternship.availableSlots}</Typography>
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Description
                  </Typography>
                  <Typography variant="body1">
                    {selectedInternship.description || 'No description provided'}
                  </Typography>
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Requirements
                  </Typography>
                  <Typography variant="body1">
                    {selectedInternship.requirements || 'No specific requirements'}
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
            {actionType === 'approve' ? 'Approve' : 'Reject'} Internship
          </DialogTitle>
          <DialogContent>
            {selectedInternship && (
              <>
                <Typography variant="body1" gutterBottom>
                  <strong>Title:</strong> {selectedInternship.title}
                </Typography>
                <Typography variant="body1" gutterBottom sx={{ mb: 2 }}>
                  <strong>Company:</strong> {selectedInternship.companyName}
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

export default PendingInternships;
