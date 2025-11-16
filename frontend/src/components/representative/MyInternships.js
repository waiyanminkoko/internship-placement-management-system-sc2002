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
  IconButton,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Grid,
  CircularProgress,
  Alert,
  Tooltip,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import {
  Edit as EditIcon,
  Visibility as VisibilityIcon,
  VisibilityOff as VisibilityOffIcon,
  Refresh as RefreshIcon,
  Assignment as AssignmentIcon,
  Delete as DeleteIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import representativeService from '../../services/representativeService';
import { formatDate } from '../../utils/formatters';
import { MAJORS } from '../../utils/constants';

/**
 * My Internships component for company representatives.
 * Displays and manages representative's internship opportunities.
 * 
 * @param {Object} props - Component props
 * @param {string} props.repId - Representative ID (email)
 * @returns {JSX.Element} Internships list and management
 */
const MyInternships = ({ repId }) => {
  const [loading, setLoading] = useState(true);
  const [internships, setInternships] = useState([]);
  const [editDialog, setEditDialog] = useState(false);
  const [deleteDialog, setDeleteDialog] = useState(false);
  const [selectedInternship, setSelectedInternship] = useState(null);
  const [editForm, setEditForm] = useState({});

  useEffect(() => {
    fetchInternships();
  }, [repId]);

  const fetchInternships = async () => {
    try {
      setLoading(true);
      const data = await representativeService.getMyInternships(repId);
      setInternships(data || []);
    } catch (error) {
      console.error('Error fetching internships:', error);
      toast.error('Failed to load internships');
    } finally {
      setLoading(false);
    }
  };

  // Check if internship can be edited (only PENDING and REJECTED)
  const canEditInternship = (internship) => {
    return internship.status === 'PENDING' || internship.status === 'REJECTED';
  };

  // Check if internship can be deleted
  // Can delete if: PENDING, REJECTED, or APPROVED but after closing date
  const canDeleteInternship = (internship) => {
    if (internship.status === 'PENDING' || internship.status === 'REJECTED') {
      return true;
    }
    
    // For approved opportunities, allow deletion if closing date has passed
    if (internship.status === 'APPROVED') {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // Reset time for date-only comparison
      const closingDate = new Date(internship.closingDate);
      closingDate.setHours(0, 0, 0, 0);
      return today >= closingDate; // Allow deletion from closing date onwards
    }
    
    return false;
  };

  const handleDelete = (internship) => {
    setSelectedInternship(internship);
    setDeleteDialog(true);
  };

  const handleDeleteConfirm = async () => {
    try {
      await representativeService.deleteInternship(repId, selectedInternship.internshipId);
      toast.success('Internship deleted successfully!');
      setDeleteDialog(false);
      setSelectedInternship(null);
      fetchInternships();
    } catch (error) {
      console.error('Error deleting internship:', error);
      toast.error(error.response?.data?.message || 'Failed to delete internship');
    }
  };

  const handleEdit = (internship) => {
    setSelectedInternship(internship);
    // Convert dates to YYYY-MM-DD format for date inputs
    const formatDateForInput = (dateString) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toISOString().split('T')[0];
    };
    
    setEditForm({
      title: internship.title || '',
      description: internship.description || '',
      level: internship.level || 'BASIC',
      preferredMajor: internship.preferredMajor || '',
      openingDate: formatDateForInput(internship.openingDate),
      closingDate: formatDateForInput(internship.closingDate),
      startDate: formatDateForInput(internship.startDate),
      endDate: formatDateForInput(internship.endDate),
      slots: internship.slots || 1,
    });
    setEditDialog(true);
  };

  const handleEditClose = () => {
    setEditDialog(false);
    setSelectedInternship(null);
    setEditForm({});
  };

  const handleEditSubmit = async () => {
    try {
      await representativeService.updateInternship(repId, selectedInternship.internshipId, editForm);
      const wasRejected = selectedInternship.status === 'REJECTED';
      toast.success(
        wasRejected 
          ? 'Internship updated and resubmitted for approval!' 
          : 'Internship updated successfully!'
      );
      handleEditClose();
      fetchInternships();
    } catch (error) {
      console.error('Error updating internship:', error);
      toast.error(error.response?.data?.message || 'Failed to update internship');
    }
  };

  const handleToggleVisibility = async (internshipId, currentVisibility) => {
    try {
      await representativeService.toggleVisibility(repId, internshipId, !currentVisibility);
      toast.success(`Internship ${!currentVisibility ? 'published' : 'hidden'} successfully!`);
      fetchInternships();
    } catch (error) {
      console.error('Error toggling visibility:', error);
      toast.error('Failed to update visibility');
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      PENDING: 'warning',
      APPROVED: 'success',
      REJECTED: 'error',
      CLOSED: 'default',
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
            Internship Opportunities
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Manage your posted internship opportunities ({internships.length} / 5 maximum)
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

      {/* Maximum limit alert */}
      {internships.length >= 5 && (
        <Alert severity="warning" sx={{ mb: 3 }}>
          <Typography variant="body2">
            <strong>Maximum Limit Reached:</strong> You have reached the maximum of 5 internship opportunities. 
            To create a new one, please delete an existing opportunity.
          </Typography>
        </Alert>
      )}

      {/* Info alert about editing and deletion restrictions */}
      {internships.length > 0 && (
        <Alert severity="info" sx={{ mb: 3 }}>
          <Typography variant="body2" component="div">
            <strong>Editing & Deletion Restrictions:</strong>
            <ul style={{ margin: '8px 0', paddingLeft: '20px' }}>
              <li>✅ <strong>Pending</strong> and <strong>Rejected</strong> opportunities can be edited, deleted, and resubmitted</li>
              <li>❌ <strong>Approved</strong> opportunities cannot be edited while accepting applications</li>
              <li>📝 Editing a rejected opportunity will automatically resubmit it for staff approval</li>
              <li>🗑️ <strong>Approved</strong> opportunities can be deleted after their closing date</li>
              <li>🔒 Opportunities automatically become hidden after the closing date</li>
            </ul>
          </Typography>
        </Alert>
      )}

      {internships.length === 0 ? (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <AssignmentIcon sx={{ fontSize: 80, color: 'text.disabled', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No internships posted yet
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Create your first internship opportunity to get started
          </Typography>
        </Paper>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell><strong>Title</strong></TableCell>
                <TableCell><strong>Level</strong></TableCell>
                <TableCell><strong>Status</strong></TableCell>
                <TableCell><strong>Visibility</strong></TableCell>
                <TableCell><strong>Opening Date</strong></TableCell>
                <TableCell><strong>Closing Date</strong></TableCell>
                <TableCell><strong>Start Date</strong></TableCell>
                <TableCell><strong>Slots</strong></TableCell>
                <TableCell><strong>Applications</strong></TableCell>
                <TableCell align="center"><strong>Actions</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {internships.map((internship) => (
                <TableRow key={internship.internshipId} hover>
                  <TableCell>
                    <Typography variant="body2" sx={{ fontWeight: 600 }}>
                      {internship.title}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      {internship.companyName}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Chip label={internship.level || 'BASIC'} size="small" />
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={internship.status || 'PENDING'}
                      color={getStatusColor(internship.status)}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    {internship.visibility ? (
                      <Chip label="Visible" color="success" size="small" />
                    ) : (
                      <Chip label="Hidden" color="default" size="small" />
                    )}
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
                  <TableCell>
                    {internship.filledSlots || 0} / {internship.slots || 0}
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">
                      {internship.applicationCount || 0} pending
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Box sx={{ display: 'flex', gap: 0.5, justifyContent: 'center' }}>
                      <Tooltip 
                        title={
                          !canEditInternship(internship) 
                            ? "Can only edit pending or rejected opportunities" 
                            : "Edit Internship"
                        }
                      >
                        <span>
                          <IconButton
                            size="small"
                            onClick={() => handleEdit(internship)}
                            disabled={!canEditInternship(internship)}
                          >
                            <EditIcon fontSize="small" />
                          </IconButton>
                        </span>
                      </Tooltip>
                      <Tooltip title={internship.visibility ? 'Hide from students' : 'Show to students'}>
                        <span>
                          <IconButton
                            size="small"
                            onClick={() => handleToggleVisibility(internship.internshipId, internship.visibility)}
                            disabled={internship.status !== 'APPROVED'}
                          >
                            {internship.visibility ? (
                              <VisibilityOffIcon fontSize="small" />
                            ) : (
                              <VisibilityIcon fontSize="small" />
                            )}
                          </IconButton>
                        </span>
                      </Tooltip>
                      <Tooltip 
                        title={
                          !canDeleteInternship(internship) 
                            ? "Can only delete pending, rejected, or approved opportunities after closing date" 
                            : "Delete Internship"
                        }
                      >
                        <span>
                          <IconButton
                            size="small"
                            color="error"
                            onClick={() => handleDelete(internship)}
                            disabled={!canDeleteInternship(internship)}
                          >
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        </span>
                      </Tooltip>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {/* Edit Dialog */}
      <Dialog open={editDialog} onClose={handleEditClose} maxWidth="md" fullWidth>
        <DialogTitle>Edit Internship</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Title"
                value={editForm.title || ''}
                onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={4}
                label="Description"
                value={editForm.description || ''}
                onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                select
                label="Level"
                value={editForm.level || 'BASIC'}
                onChange={(e) => setEditForm({ ...editForm, level: e.target.value })}
                SelectProps={{ native: true }}
              >
                <option value="BASIC">Entry Level</option>
                <option value="INTERMEDIATE">Intermediate</option>
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                select
                label="Preferred Major"
                value={editForm.preferredMajor || ''}
                onChange={(e) => setEditForm({ ...editForm, preferredMajor: e.target.value })}
                SelectProps={{ native: true }}
              >
                <option value="">Select Major</option>
                {MAJORS.map(major => (
                  <option key={major} value={major}>{major}</option>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                type="date"
                label="Application Opening Date"
                value={editForm.openingDate || ''}
                onChange={(e) => setEditForm({ ...editForm, openingDate: e.target.value })}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                type="date"
                label="Application Closing Date"
                value={editForm.closingDate || ''}
                onChange={(e) => setEditForm({ ...editForm, closingDate: e.target.value })}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                type="date"
                label="Internship Start Date"
                value={editForm.startDate || ''}
                onChange={(e) => setEditForm({ ...editForm, startDate: e.target.value })}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                type="date"
                label="Internship End Date"
                value={editForm.endDate || ''}
                onChange={(e) => setEditForm({ ...editForm, endDate: e.target.value })}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                type="number"
                label="Slots"
                value={editForm.slots || 1}
                onChange={(e) => setEditForm({ ...editForm, slots: parseInt(e.target.value) })}
                inputProps={{ min: 1 }}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleEditClose}>Cancel</Button>
          <Button onClick={handleEditSubmit} variant="contained">
            Save Changes
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <Dialog open={deleteDialog} onClose={() => setDeleteDialog(false)} maxWidth="sm">
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <Alert severity="warning" sx={{ mb: 2 }}>
            <Typography variant="body2">
              Are you sure you want to delete this internship opportunity?
            </Typography>
          </Alert>
          {selectedInternship && (
            <Box>
              <Typography variant="body2" sx={{ mb: 1 }}>
                <strong>Title:</strong> {selectedInternship.title}
              </Typography>
              <Typography variant="body2" sx={{ mb: 1 }}>
                <strong>Status:</strong> <Chip label={selectedInternship.status} size="small" />
              </Typography>
              <Typography variant="body2" color="error" sx={{ mt: 2 }}>
                This action cannot be undone.
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialog(false)}>Cancel</Button>
          <Button onClick={handleDeleteConfirm} variant="contained" color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default MyInternships;
