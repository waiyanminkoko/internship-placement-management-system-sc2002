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
  CircularProgress,
  Avatar,
  Button,
} from '@mui/material';
import {
  CheckCircle as CheckCircleIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import representativeService from '../../services/representativeService';
import { formatDate } from '../../utils/formatters';

/**
 * Placements component for company representatives.
 * View confirmed placements and accepted internship offers.
 * 
 * @param {Object} props - Component props
 * @param {string} props.repId - Representative ID (email)
 * @returns {JSX.Element} Placements list
 */
const Placements = ({ repId }) => {
  const [loading, setLoading] = useState(true);
  const [placements, setPlacements] = useState([]);

  useEffect(() => {
    fetchPlacements();
  }, [repId]);

  const fetchPlacements = async () => {
    try {
      setLoading(true);
      // Fetch all internships and filter for accepted applications
      const internships = await representativeService.getMyInternships(repId);
      
      // Fetch applications for each internship and filter accepted ones
      const allPlacements = [];
      for (const internship of internships || []) {
        try {
          const apps = await representativeService.getApplications(repId, internship.internshipId);
          const acceptedApps = (apps || []).filter(app => app.placementAccepted === true);
          acceptedApps.forEach(app => {
            allPlacements.push({
              ...app,
              internshipTitle: internship.title,
              internshipId: internship.internshipId,
            });
          });
        } catch (error) {
          console.error(`Error fetching applications for internship ${internship.internshipId}:`, error);
        }
      }
      
      setPlacements(allPlacements);
    } catch (error) {
      console.error('Error fetching placements:', error);
      toast.error('Failed to load placements');
    } finally {
      setLoading(false);
    }
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
            Confirmed Placements
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Students who have accepted internship offers ({placements.length} total)
          </Typography>
        </Box>
        <Button
          variant="outlined"
          startIcon={<RefreshIcon />}
          onClick={fetchPlacements}
        >
          Refresh
        </Button>
      </Box>

      {placements.length === 0 ? (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <CheckCircleIcon sx={{ fontSize: 80, color: 'text.disabled', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No confirmed placements yet
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Students will appear here once they accept your internship offers
          </Typography>
        </Paper>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell><strong>Student</strong></TableCell>
                <TableCell><strong>Internship</strong></TableCell>
                <TableCell><strong>Applied Date</strong></TableCell>
                <TableCell><strong>Approved Date</strong></TableCell>
                <TableCell><strong>Status</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {placements.map((placement) => (
                <TableRow key={placement.applicationId} hover>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <Avatar sx={{ width: 40, height: 40 }}>
                        {placement.studentName?.charAt(0) || 'S'}
                      </Avatar>
                      <Box>
                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                          {placement.studentName || placement.studentId}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          {placement.studentId}
                        </Typography>
                      </Box>
                    </Box>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2" sx={{ fontWeight: 600 }}>
                      {placement.internshipTitle}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      ID: {placement.internshipId}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    {formatDate(placement.applicationDate)}
                  </TableCell>
                  <TableCell>
                    {formatDate(placement.statusUpdateDate)}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label="Confirmed"
                      color="success"
                      size="small"
                      icon={<CheckCircleIcon />}
                    />
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {placements.length > 0 && (
        <Paper sx={{ p: 2, mt: 3, bgcolor: 'success.light' }}>
          <Typography variant="body2" sx={{ color: 'success.dark' }}>
            ✓ Total confirmed placements: <strong>{placements.length}</strong>
          </Typography>
        </Paper>
      )}
    </Box>
  );
};

export default Placements;
