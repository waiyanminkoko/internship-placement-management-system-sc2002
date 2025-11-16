import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  useTheme,
  useMediaQuery,
  Container,
  Paper,
  Grid,
  Card,
  CardContent,
  Button,
  Chip,
  CircularProgress,
} from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import WorkIcon from '@mui/icons-material/Work';
import AssignmentIcon from '@mui/icons-material/Assignment';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import PendingIcon from '@mui/icons-material/Pending';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import CollapsibleSidebar from '../components/common/CollapsibleSidebar';
import DashboardHeader from '../components/common/DashboardHeader';
import StatCard from '../components/common/StatCard';
import InternshipList from '../components/student/InternshipList';
import MyApplications from '../components/student/MyApplications';
import AcceptPlacement from '../components/student/AcceptPlacement';
import WithdrawalRequest from '../components/student/WithdrawalRequest';
import ChangePasswordDialog from '../components/common/ChangePasswordDialog';
import Profile from '../components/common/Profile';
import studentService from '../services/studentService';
import LockIcon from '@mui/icons-material/Lock';
import PersonIcon from '@mui/icons-material/Person';

const StudentHome = ({ onNavigate, user }) => {
  const theme = useTheme();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalApplications: 0,
    pendingApplications: 0,
    approvedApplications: 0,
    availableInternships: 0,
  });

  useEffect(() => {
    const fetchDashboardData = async () => {
      if (!user?.userId) return;
      
      try {
        setLoading(true);
        
        // Fetch all data in parallel
        const [applications, internships] = await Promise.all([
          studentService.getMyApplications(user.userId),
          studentService.getAvailableInternships(user.userId),
        ]);

        // Calculate statistics from fetched data
        const totalApps = applications?.length || 0;
        const pendingApps = applications?.filter(app => app.status === 'PENDING')?.length || 0;
        const approvedApps = applications?.filter(app => app.status === 'SUCCESSFUL')?.length || 0;
        const availableInts = internships?.length || 0;

        setStats({
          totalApplications: totalApps,
          pendingApplications: pendingApps,
          approvedApplications: approvedApps,
          availableInternships: availableInts,
        });
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
        toast.error('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [user?.userId]);

  const statsCards = [
    {
      title: 'Total Applications',
      value: loading ? '...' : stats.totalApplications.toString(),
      subtitle: 'Active applications this semester',
      icon: <AssignmentIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    },
    {
      title: 'Pending Review',
      value: loading ? '...' : stats.pendingApplications.toString(),
      subtitle: 'Applications awaiting review',
      icon: <PendingIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    },
    {
      title: 'Approved',
      value: loading ? '...' : stats.approvedApplications.toString(),
      subtitle: 'Applications approved',
      icon: <ThumbUpIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    },
    {
      title: 'Available Internships',
      value: loading ? '...' : stats.availableInternships.toString(),
      subtitle: 'All Internship opportunities',
      icon: <TrendingUpIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    },
  ];

  const quickActions = [
    {
      title: 'Browse Internships',
      description: 'Explore and apply for available internship opportunities',
      icon: <WorkIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      action: () => onNavigate('internships'),
    },
    {
      title: 'My Applications',
      description: 'Track application status and manage submissions',
      icon: <AssignmentIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
      action: () => onNavigate('applications'),
    },
    {
      title: 'Accept Placement',
      description: 'Review and confirm approved placement offers',
      icon: <CheckCircleIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
      action: () => onNavigate('accept'),
    },
    {
      title: 'Request Withdrawal',
      description: 'Submit withdrawal requests for active applications',
      icon: <RemoveCircleOutlineIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
      action: () => onNavigate('withdraw'),
    },
  ];

  return (
    <Box>
      {/* Welcome section */}
      <Box sx={{ mb: 4 }}>
        <Typography
          variant="h4"
          sx={{
            fontWeight: 700,
            mb: 1,
            color: 'grey.900',
          }}
        >
          Welcome back, {user?.name}! 👋
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ fontSize: '1rem' }}>
          Here's an overview of your internship journey
        </Typography>
      </Box>

      {/* Statistics cards */}
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
          <CircularProgress />
        </Box>
      ) : (
        <Grid container spacing={3} sx={{ mb: 4 }}>
          {statsCards.map((stat, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <StatCard {...stat} />
            </Grid>
          ))}
        </Grid>
      )}

      {/* Quick actions */}
      <Typography
        variant="h5"
        sx={{
          fontWeight: 700,
          mb: 3,
          color: 'grey.900',
        }}
      >
        Quick Actions
      </Typography>
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {quickActions.map((action, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card
              sx={{
                height: '100%',
                cursor: 'pointer',
                position: 'relative',
                overflow: 'hidden',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-8px)',
                  boxShadow: '0 12px 24px rgba(0,0,0,0.15)',
                  '& .action-icon': {
                    transform: 'scale(1.1)',
                  },
                  '& .arrow-icon': {
                    transform: 'translateX(4px)',
                  },
                },
              }}
              onClick={action.action}
            >
              {/* Gradient accent */}
              <Box
                sx={{
                  position: 'absolute',
                  top: 0,
                  left: 0,
                  right: 0,
                  height: 4,
                  background: action.gradient,
                }}
              />
              
              <CardContent sx={{ p: 3 }}>
                <Box
                  className="action-icon"
                  sx={{
                    width: 60,
                    height: 60,
                    borderRadius: 3,
                    background: action.gradient,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: '#fff',
                    mb: 2.5,
                    transition: 'transform 0.3s ease',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                  }}
                >
                  {action.icon}
                </Box>
                
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    mb: 1,
                    fontSize: '1.1rem',
                    color: 'grey.900',
                  }}
                >
                  {action.title}
                </Typography>
                
                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{
                    fontSize: '0.875rem',
                    lineHeight: 1.6,
                    mb: 2,
                  }}
                >
                  {action.description}
                </Typography>

                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                  <Typography
                    variant="body2"
                    sx={{
                      fontWeight: 600,
                      color: 'primary.main',
                      fontSize: '0.875rem',
                    }}
                  >
                    Get started
                  </Typography>
                  <ArrowForwardIcon
                    className="arrow-icon"
                    sx={{
                      fontSize: 18,
                      color: 'primary.main',
                      transition: 'transform 0.3s ease',
                    }}
                  />
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Important information */}
      <Card
        sx={{
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          color: '#fff',
          mb: 3,
        }}
      >
        <CardContent sx={{ p: 3 }}>
          <Typography
            variant="h6"
            sx={{
              fontWeight: 700,
              mb: 2,
              display: 'flex',
              alignItems: 'center',
              gap: 1,
            }}
          >
            📋 Important Guidelines
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Maximum of 3 active applications allowed at any time
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Application statuses: Pending, Approved, or Rejected
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={12} md={6}>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Accept approved placements promptly to secure your position
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Withdrawal requests require staff approval for accepted placements
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* User info footer */}
      <Paper
        sx={{
          p: 2,
          bgcolor: 'grey.50',
          border: '1px solid',
          borderColor: 'grey.200',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          flexWrap: 'wrap',
          gap: 2,
        }}
      >
        <Box>
          <Typography variant="body2" color="text.secondary" sx={{ fontSize: '0.85rem' }}>
            <strong>Student ID:</strong> {user?.userId}
          </Typography>
        </Box>
        <Chip
          label={user?.email}
          size="small"
          sx={{
            fontWeight: 600,
            bgcolor: 'white',
            border: '1px solid',
            borderColor: 'grey.300',
          }}
        />
      </Paper>
    </Box>
  );
};

/**
 * Student Dashboard component.
 * Main interface for student users with modern design and collapsible sidebar.
 * 
 * @returns {JSX.Element} Student dashboard
 */
const StudentDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const [mobileOpen, setMobileOpen] = useState(false);
  const [currentView, setCurrentView] = useState('dashboard');
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [showChangePassword, setShowChangePassword] = useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const handleCollapseChange = (collapsed) => {
    setSidebarCollapsed(collapsed);
  };

  const handleLogout = async () => {
    await logout();
    toast.success('Logged out successfully');
    navigate('/login', { replace: true });
  };

  const handleNavigate = (view) => {
    setCurrentView(view);
    if (isMobile) {
      setMobileOpen(false);
    }
  };

  const menuItems = [
    { id: 'dashboard', label: 'Overview', icon: <DashboardIcon /> },
    { id: 'internships', label: 'Internships', icon: <WorkIcon /> },
    { id: 'applications', label: 'Applications', icon: <AssignmentIcon /> },
    { id: 'accept', label: 'Accept Placement', icon: <CheckCircleIcon /> },
    { id: 'withdraw', label: 'Withdraw Request', icon: <RemoveCircleOutlineIcon /> },
    { id: 'profile', label: 'My Profile', icon: <PersonIcon /> },
    { id: 'change-password', label: 'Change Password', icon: <LockIcon /> },
  ];

  const renderContent = () => {
    switch (currentView) {
      case 'internships':
        return <InternshipList />;
      case 'applications':
        return <MyApplications />;
      case 'accept':
        return <AcceptPlacement />;
      case 'withdraw':
        return <WithdrawalRequest />;
      case 'profile':
        return <Profile />;
      case 'change-password':
        setShowChangePassword(true);
        setCurrentView('dashboard'); // Return to dashboard
        return <StudentHome onNavigate={handleNavigate} user={user} />;
      case 'dashboard':
      default:
        return <StudentHome onNavigate={handleNavigate} user={user} />;
    }
  };

  // Get page title based on current view
  const getPageTitle = () => {
    const item = menuItems.find(m => m.id === currentView);
    return item ? item.label : 'Dashboard';
  };

  // Calculate content width based on sidebar state
  const getContentWidth = () => {
    if (isMobile) return '100%';
    return sidebarCollapsed ? 'calc(100% - 70px)' : 'calc(100% - 260px)';
  };

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* Collapsible Sidebar */}
      <CollapsibleSidebar
        menuItems={menuItems}
        currentView={currentView}
        onNavigate={handleNavigate}
        user={user}
        title="Student Portal"
        mobileOpen={mobileOpen}
        onMobileToggle={handleDrawerToggle}
        onCollapseChange={handleCollapseChange}
      />

      {/* Main Content Area */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { xs: '100%', md: getContentWidth() },
          minHeight: '100vh',
          transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
          }),
        }}
      >
        {/* Modern Header */}
        <DashboardHeader
          user={user}
          onLogout={handleLogout}
          onMenuClick={handleDrawerToggle}
          onProfileClick={() => handleNavigate('profile')}
          title={getPageTitle()}
          notificationCount={0}
        />

        {/* Content with proper spacing */}
        <Box
          sx={{
            px: { xs: 2, sm: 3, md: 4 },
            py: 4,
          }}
        >
          <Container maxWidth="xl" disableGutters>
            {renderContent()}
          </Container>
        </Box>
      </Box>

      {/* Change Password Dialog */}
      <ChangePasswordDialog
        open={showChangePassword}
        onClose={() => setShowChangePassword(false)}
        userId={user?.userId}
      />
    </Box>
  );
};

export default StudentDashboard;
