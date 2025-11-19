import React, { useState, useEffect, useRef } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Grid,
  TextField,
  MenuItem,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  CircularProgress,
  Alert,
  Divider,
  Chip,
} from '@mui/material';
import { Download as DownloadIcon } from '@mui/icons-material';
import { toast } from 'react-toastify';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import staffService from '../../services/staffService';
import { formatDate } from '../../utils/formatters';

/**
 * Component to generate and view system reports.
 */
const Reports = ({ staffId }) => {
  const [loading, setLoading] = useState(false);
  const [downloadingPDF, setDownloadingPDF] = useState(false);
  const [reportData, setReportData] = useState(null);
  const [companies, setCompanies] = useState([]);
  const [loadingCompanies, setLoadingCompanies] = useState(true);
  const [filters, setFilters] = useState({
    major: '',
    year: '',
    companyName: '',
    startDate: '',
    endDate: '',
    applicationStatus: '',
    internshipLevel: '',
    internshipStatus: '',
  });
  const reportContentRef = useRef(null);

  const majors = [
    'Computer Science',
    'Data Science & AI',
    'Computer Engineering',
    'Information Engineering & Media',
  ];

  const years = ['1', '2', '3', '4'];

  const applicationStatuses = ['PENDING', 'SUCCESSFUL', 'REJECTED', 'WITHDRAWN'];

  const internshipLevels = ['BASIC', 'INTERMEDIATE'];

  const internshipStatuses = ['PENDING', 'APPROVED', 'REJECTED', 'FILLED'];

  // Fetch companies on mount
  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        setLoadingCompanies(true);
        const companyList = await staffService.getCompanies(staffId);
        setCompanies(companyList);
      } catch (error) {
        console.error('Failed to fetch companies:', error);
        toast.error('Failed to load companies');
      } finally {
        setLoadingCompanies(false);
      }
    };

    if (staffId) {
      fetchCompanies();
    }
  }, [staffId]);

  const handleFilterChange = (field, value) => {
    setFilters((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleGenerateReport = async () => {
    try {
      setLoading(true);
      const data = await staffService.generateReport(staffId, filters);
      setReportData(data);
      toast.success('Report generated successfully');
    } catch (error) {
      toast.error('Failed to generate report');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleClearFilters = () => {
    setFilters({
      major: '',
      year: '',
      companyName: '',
      startDate: '',
      endDate: '',
      applicationStatus: '',
      internshipLevel: '',
      internshipStatus: '',
    });
    setReportData(null);
  };

  const handleDownloadPDF = async () => {
    if (!reportContentRef.current) {
      toast.error('No report to download');
      return;
    }

    try {
      setDownloadingPDF(true);
      toast.info('Preparing PDF for download...');

      // Create a clone of the report content to avoid modifying the original
      const reportElement = reportContentRef.current;
      
      // Use html2canvas to capture the report content
      const canvas = await html2canvas(reportElement, {
        scale: 2, // Higher quality
        useCORS: true,
        logging: false,
        backgroundColor: '#ffffff',
      });

      // Calculate PDF dimensions
      const imgWidth = 210; // A4 width in mm
      const pageHeight = 297; // A4 height in mm
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      let heightLeft = imgHeight;

      // Create PDF
      const pdf = new jsPDF('p', 'mm', 'a4');
      let position = 0;

      // Add image to PDF
      const imgData = canvas.toDataURL('image/png');
      pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;

      // Add new pages if content is longer than one page
      while (heightLeft > 0) {
        position = heightLeft - imgHeight;
        pdf.addPage();
        pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;
      }

      // Generate filename with timestamp
      const timestamp = new Date().toISOString().slice(0, 10);
      const filename = `Internship_System_Report_${timestamp}.pdf`;

      // Save the PDF
      pdf.save(filename);

      toast.success('Report downloaded successfully');
    } catch (error) {
      console.error('Error generating PDF:', error);
      toast.error('Failed to download report as PDF');
    } finally {
      setDownloadingPDF(false);
    }
  };

  return (
    <Card>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h5" fontWeight={700}>
            System Reports
          </Typography>
          {reportData && (
            <Button
              variant="contained"
              color="success"
              startIcon={downloadingPDF ? <CircularProgress size={20} color="inherit" /> : <DownloadIcon />}
              onClick={handleDownloadPDF}
              disabled={downloadingPDF || loading}
            >
              {downloadingPDF ? 'Generating PDF...' : 'Download PDF'}
            </Button>
          )}
        </Box>

        {/* Filters Section */}
        <Paper elevation={2} sx={{ p: 3, mt: 2 }}>
          <Typography variant="h6" gutterBottom>
            Report Filters
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                select
                value={filters.major}
                onChange={(e) => handleFilterChange('major', e.target.value)}
                SelectProps={{
                  displayEmpty: true,
                  renderValue: (selected) => {
                    if (!selected || selected === '') {
                      return 'All Majors';
                    }
                    return selected;
                  },
                }}
              >
                <MenuItem value="">All Majors</MenuItem>
                {majors.map((major) => (
                  <MenuItem key={major} value={major}>
                    {major}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                select
                value={filters.year}
                onChange={(e) => handleFilterChange('year', e.target.value)}
                SelectProps={{
                  displayEmpty: true,
                  renderValue: (selected) => {
                    if (!selected || selected === '') {
                      return 'All Years';
                    }
                    return `Year ${selected}`;
                  },
                }}
              >
                <MenuItem value="">All Years</MenuItem>
                {years.map((year) => (
                  <MenuItem key={year} value={year}>
                    Year {year}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                select
                value={filters.companyName}
                onChange={(e) => handleFilterChange('companyName', e.target.value)}
                disabled={loadingCompanies}
                SelectProps={{
                  displayEmpty: true,
                  renderValue: (selected) => {
                    if (!selected || selected === '') {
                      return 'All Companies';
                    }
                    return selected;
                  },
                }}
              >
                <MenuItem value="">All Companies</MenuItem>
                {companies.map((company) => (
                  <MenuItem key={company} value={company}>
                    {company}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                select
                value={filters.applicationStatus}
                onChange={(e) => handleFilterChange('applicationStatus', e.target.value)}
                SelectProps={{
                  displayEmpty: true,
                  renderValue: (selected) => {
                    if (!selected || selected === '') {
                      return 'All Application Statuses';
                    }
                    return selected;
                  },
                }}
              >
                <MenuItem value="">All Application Statuses</MenuItem>
                {applicationStatuses.map((status) => (
                  <MenuItem key={status} value={status}>
                    {status}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                select
                value={filters.internshipLevel}
                onChange={(e) => handleFilterChange('internshipLevel', e.target.value)}
                SelectProps={{
                  displayEmpty: true,
                  renderValue: (selected) => {
                    if (!selected || selected === '') {
                      return 'All Internship Levels';
                    }
                    return selected;
                  },
                }}
              >
                <MenuItem value="">All Internship Levels</MenuItem>
                {internshipLevels.map((level) => (
                  <MenuItem key={level} value={level}>
                    {level}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                select
                value={filters.internshipStatus}
                onChange={(e) => handleFilterChange('internshipStatus', e.target.value)}
                SelectProps={{
                  displayEmpty: true,
                  renderValue: (selected) => {
                    if (!selected || selected === '') {
                      return 'All Internship Statuses';
                    }
                    return selected;
                  },
                }}
              >
                <MenuItem value="">All Internship Statuses</MenuItem>
                {internshipStatuses.map((status) => (
                  <MenuItem key={status} value={status}>
                    {status}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                type="date"
                label="Internship Opening Date"
                value={filters.startDate}
                onChange={(e) => handleFilterChange('startDate', e.target.value)}
                InputLabelProps={{ shrink: true }}
                helperText="Optional: Leave empty to ignore start date"
              />
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <TextField
                fullWidth
                type="date"
                label="Internship Closing Date"
                value={filters.endDate}
                onChange={(e) => handleFilterChange('endDate', e.target.value)}
                InputLabelProps={{ shrink: true }}
                helperText="Optional: Leave empty to ignore end date"
              />
            </Grid>
            <Grid item xs={12} sm={6} md={4} sx={{ display: 'flex', alignItems: 'center' }}>
              <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={handleGenerateReport}
                disabled={loading}
                sx={{ mr: 1 }}
              >
                {loading ? <CircularProgress size={24} /> : 'Generate Report'}
              </Button>
              <Button variant="outlined" fullWidth onClick={handleClearFilters}>
                Clear
              </Button>
            </Grid>
          </Grid>
        </Paper>

        {/* Report Results */}
        {loading && (
          <Box display="flex" justifyContent="center" p={4}>
            <CircularProgress />
          </Box>
        )}

        {!loading && reportData && (
          <Box ref={reportContentRef} sx={{ mt: 3, bgcolor: 'white', p: 2 }}>
            {/* Summary Statistics */}
            <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Summary Statistics
              </Typography>
              <Grid container spacing={3}>
                <Grid item xs={12} sm={6} md={3}>
                  <Typography variant="body2" color="textSecondary">
                    Total Applications
                  </Typography>
                  <Typography variant="h4">
                    {reportData.totalApplications || 0}
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                  <Typography variant="body2" color="textSecondary">
                    Total Internships
                  </Typography>
                  <Typography variant="h4">
                    {reportData.totalInternships || 0}
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                  <Typography variant="body2" color="textSecondary">
                    Approved Representatives
                  </Typography>
                  <Typography variant="h4">
                    {reportData.approvedRepresentatives || 0}
                  </Typography>
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                  <Typography variant="body2" color="textSecondary">
                    Pending Withdrawals
                  </Typography>
                  <Typography variant="h4">
                    {reportData.pendingWithdrawals || 0}
                  </Typography>
                </Grid>
              </Grid>
            </Paper>

            {/* Applications by Major */}
            {reportData.applicationsByMajor && (
              <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Applications by Major
                </Typography>
                <TableContainer>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Major</TableCell>
                        <TableCell align="right">Count</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {Object.entries(reportData.applicationsByMajor).map(
                        ([major, count]) => (
                          <TableRow key={major}>
                            <TableCell>{major}</TableCell>
                            <TableCell align="right">{count}</TableCell>
                          </TableRow>
                        )
                      )}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            )}

            {/* Applications by Year */}
            {reportData.applicationsByYear && (
              <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Applications by Year
                </Typography>
                <TableContainer>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Year</TableCell>
                        <TableCell align="right">Count</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {Object.entries(reportData.applicationsByYear).map(
                        ([year, count]) => (
                          <TableRow key={year}>
                            <TableCell>Year {year}</TableCell>
                            <TableCell align="right">{count}</TableCell>
                          </TableRow>
                        )
                      )}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            )}

            {/* Internships by Company */}
            {reportData.internshipsByCompany && (
              <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Internships by Company
                </Typography>
                <TableContainer>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Company</TableCell>
                        <TableCell align="right">Count</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {Object.entries(reportData.internshipsByCompany).map(
                        ([company, count]) => (
                          <TableRow key={company}>
                            <TableCell>{company}</TableCell>
                            <TableCell align="right">{count}</TableCell>
                          </TableRow>
                        )
                      )}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            )}

            {/* Detailed Applications Table */}
            {reportData.applications && reportData.applications.length > 0 && (
              <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Application Details
                </Typography>
                <TableContainer sx={{ maxHeight: 440 }}>
                  <Table stickyHeader>
                    <TableHead>
                      <TableRow>
                        <TableCell>Application ID</TableCell>
                        <TableCell>Student Name</TableCell>
                        <TableCell>Major</TableCell>
                        <TableCell>Year</TableCell>
                        <TableCell>Internship</TableCell>
                        <TableCell>Company</TableCell>
                        <TableCell>Status</TableCell>
                        <TableCell>Submission Date</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {reportData.applications.map((app) => (
                        <TableRow key={app.applicationId} hover>
                          <TableCell>{app.applicationId}</TableCell>
                          <TableCell>{app.studentName || 'N/A'}</TableCell>
                          <TableCell>{app.studentMajor || 'N/A'}</TableCell>
                          <TableCell>{app.studentYear || 'N/A'}</TableCell>
                          <TableCell>{app.internshipTitle || 'N/A'}</TableCell>
                          <TableCell>{app.companyName || 'N/A'}</TableCell>
                          <TableCell>
                            <Chip
                              label={app.status}
                              color={
                                app.status === 'SUCCESSFUL'
                                  ? 'success'
                                  : app.status === 'PENDING'
                                  ? 'warning'
                                  : app.status === 'REJECTED'
                                  ? 'error'
                                  : app.status === 'WITHDRAWN'
                                  ? 'default'
                                  : 'info'
                              }
                              size="small"
                              sx={{
                                fontWeight: 'medium',
                              }}
                            />
                          </TableCell>
                          <TableCell>
                            {formatDate(app.submissionDate)}
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            )}

            {/* Student Status Summary */}
            <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Students by Application Status
              </Typography>
              <Grid container spacing={2} sx={{ mb: 3 }}>
                <Grid item xs={12} sm={4}>
                  <Box sx={{ textAlign: 'center', p: 2, bgcolor: 'success.light', borderRadius: 1 }}>
                    <Typography variant="h3" color="success.dark">
                      {reportData.successfulStudentsCount || 0}
                    </Typography>
                    <Typography variant="body2" color="success.dark">
                      Successful Applications
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={4}>
                  <Box sx={{ textAlign: 'center', p: 2, bgcolor: 'grey.200', borderRadius: 1 }}>
                    <Typography variant="h3" color="text.primary">
                      {reportData.withdrawnStudentsCount || 0}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Withdrawn Applications
                    </Typography>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={4}>
                  <Box sx={{ textAlign: 'center', p: 2, bgcolor: 'error.light', borderRadius: 1 }}>
                    <Typography variant="h3" color="error.dark">
                      {reportData.rejectedStudentsCount || 0}
                    </Typography>
                    <Typography variant="body2" color="error.dark">
                      Rejected Applications
                    </Typography>
                  </Box>
                </Grid>
              </Grid>

              {/* Successful Students Details */}
              {reportData.successfulStudents && reportData.successfulStudents.length > 0 && (
                <Box sx={{ mb: 3 }}>
                  <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 600, color: 'success.dark' }}>
                    Students with Successful Applications ({reportData.successfulStudentsCount})
                  </Typography>
                  <TableContainer sx={{ maxHeight: 300 }}>
                    <Table size="small" stickyHeader>
                      <TableHead>
                        <TableRow>
                          <TableCell>Student Name</TableCell>
                          <TableCell>Major</TableCell>
                          <TableCell>Year</TableCell>
                          <TableCell>Internship</TableCell>
                          <TableCell>Company</TableCell>
                          <TableCell>Placement Accepted</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {reportData.successfulStudents.map((student) => (
                          <TableRow key={student.applicationId} hover>
                            <TableCell>{student.studentName || student.studentId}</TableCell>
                            <TableCell>{student.studentMajor || 'N/A'}</TableCell>
                            <TableCell>{student.studentYear || 'N/A'}</TableCell>
                            <TableCell>{student.internshipTitle || 'N/A'}</TableCell>
                            <TableCell>{student.companyName || 'N/A'}</TableCell>
                            <TableCell>
                              <Chip
                                label={student.placementAccepted ? 'Yes' : 'No'}
                                color={student.placementAccepted ? 'success' : 'default'}
                                size="small"
                              />
                            </TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Box>
              )}

              {/* Withdrawn Students Details */}
              {reportData.withdrawnStudents && reportData.withdrawnStudents.length > 0 && (
                <Box sx={{ mb: 3 }}>
                  <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 600, color: 'text.secondary' }}>
                    Students with Withdrawn Applications ({reportData.withdrawnStudentsCount})
                  </Typography>
                  <TableContainer sx={{ maxHeight: 300 }}>
                    <Table size="small" stickyHeader>
                      <TableHead>
                        <TableRow>
                          <TableCell>Student Name</TableCell>
                          <TableCell>Major</TableCell>
                          <TableCell>Year</TableCell>
                          <TableCell>Internship</TableCell>
                          <TableCell>Company</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {reportData.withdrawnStudents.map((student) => (
                          <TableRow key={student.applicationId} hover>
                            <TableCell>{student.studentName || student.studentId}</TableCell>
                            <TableCell>{student.studentMajor || 'N/A'}</TableCell>
                            <TableCell>{student.studentYear || 'N/A'}</TableCell>
                            <TableCell>{student.internshipTitle || 'N/A'}</TableCell>
                            <TableCell>{student.companyName || 'N/A'}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Box>
              )}

              {/* Rejected Students Details */}
              {reportData.rejectedStudents && reportData.rejectedStudents.length > 0 && (
                <Box sx={{ mb: 0 }}>
                  <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 600, color: 'error.dark' }}>
                    Students with Rejected Applications ({reportData.rejectedStudentsCount})
                  </Typography>
                  <TableContainer sx={{ maxHeight: 300 }}>
                    <Table size="small" stickyHeader>
                      <TableHead>
                        <TableRow>
                          <TableCell>Student Name</TableCell>
                          <TableCell>Major</TableCell>
                          <TableCell>Year</TableCell>
                          <TableCell>Internship</TableCell>
                          <TableCell>Company</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {reportData.rejectedStudents.map((student) => (
                          <TableRow key={student.applicationId} hover>
                            <TableCell>{student.studentName || student.studentId}</TableCell>
                            <TableCell>{student.studentMajor || 'N/A'}</TableCell>
                            <TableCell>{student.studentYear || 'N/A'}</TableCell>
                            <TableCell>{student.internshipTitle || 'N/A'}</TableCell>
                            <TableCell>{student.companyName || 'N/A'}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Box>
              )}
            </Paper>

            {/* Detailed Internships Table */}
            {reportData.internships && reportData.internships.length > 0 && (
              <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Internship Details
                </Typography>
                <TableContainer sx={{ maxHeight: 440 }}>
                  <Table stickyHeader>
                    <TableHead>
                      <TableRow>
                        <TableCell>Internship ID</TableCell>
                        <TableCell>Title</TableCell>
                        <TableCell>Company</TableCell>
                        <TableCell>Level</TableCell>
                        <TableCell>Preferred Major</TableCell>
                        <TableCell>Slots</TableCell>
                        <TableCell>Filled</TableCell>
                        <TableCell>Opening Date</TableCell>
                        <TableCell>Closing Date</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {reportData.internships.map((internship) => (
                        <TableRow key={internship.internshipId} hover>
                          <TableCell>{internship.internshipId}</TableCell>
                          <TableCell>{internship.title}</TableCell>
                          <TableCell>{internship.companyName}</TableCell>
                          <TableCell>
                            <Chip label={internship.level} size="small" />
                          </TableCell>
                          <TableCell>{internship.preferredMajor}</TableCell>
                          <TableCell align="center">{internship.slots}</TableCell>
                          <TableCell align="center">{internship.filledSlots}</TableCell>
                          <TableCell>
                            {formatDate(internship.openingDate)}
                          </TableCell>
                          <TableCell>
                            {formatDate(internship.closingDate)}
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            )}

            {/* Detailed Withdrawal Requests Table */}
            {reportData.withdrawals && reportData.withdrawals.length > 0 && (
              <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Withdrawal Request Details
                </Typography>
                <TableContainer sx={{ maxHeight: 440 }}>
                  <Table stickyHeader>
                    <TableHead>
                      <TableRow>
                        <TableCell>Withdrawal ID</TableCell>
                        <TableCell>Student Name</TableCell>
                        <TableCell>Major</TableCell>
                        <TableCell>Year</TableCell>
                        <TableCell>Internship</TableCell>
                        <TableCell>Company</TableCell>
                        <TableCell>Reason</TableCell>
                        <TableCell>Status</TableCell>
                        <TableCell>Request Date</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {reportData.withdrawals.map((withdrawal) => (
                        <TableRow key={withdrawal.withdrawalId} hover>
                          <TableCell>{withdrawal.withdrawalId}</TableCell>
                          <TableCell>{withdrawal.studentName || 'N/A'}</TableCell>
                          <TableCell>{withdrawal.studentMajor || 'N/A'}</TableCell>
                          <TableCell>{withdrawal.studentYear || 'N/A'}</TableCell>
                          <TableCell>{withdrawal.internshipTitle || 'N/A'}</TableCell>
                          <TableCell>{withdrawal.companyName || 'N/A'}</TableCell>
                          <TableCell sx={{ maxWidth: 200 }}>
                            <Typography
                              variant="body2"
                              sx={{
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                display: '-webkit-box',
                                WebkitLineClamp: 2,
                                WebkitBoxOrient: 'vertical',
                              }}
                              title={withdrawal.reason}
                            >
                              {withdrawal.reason || 'N/A'}
                            </Typography>
                          </TableCell>
                          <TableCell>
                            <Chip
                              label={withdrawal.status}
                              color={
                                withdrawal.status === 'APPROVED'
                                  ? 'success'
                                  : withdrawal.status === 'PENDING'
                                  ? 'warning'
                                  : withdrawal.status === 'REJECTED'
                                  ? 'error'
                                  : 'default'
                              }
                              size="small"
                              sx={{
                                fontWeight: 'medium',
                              }}
                            />
                          </TableCell>
                          <TableCell>
                            {formatDate(withdrawal.requestDate)}
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            )}
          </Box>
        )}

        {!loading && !reportData && (
          <Alert severity="info" sx={{ mt: 3 }}>
            Select filters and click "Generate Report" to view statistics.
          </Alert>
        )}
      </CardContent>
    </Card>
  );
};

export default Reports;
