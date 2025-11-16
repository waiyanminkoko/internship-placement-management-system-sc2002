import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  Typography,
  Container,
  Box,
  Paper,
  Grid,
  Card,
  CardContent,
  useTheme,
  useMediaQuery,
  Chip,
  CircularProgress,
} from '@mui/material';
import {
  Home as HomeIcon,
  Work as WorkIcon,
  ExitToApp as ExitToAppIcon,
  Assessment as AssessmentIcon,
  Person as PersonIcon,
  CheckCircle as CheckCircleIcon,
  HourglassEmpty as PendingIcon,
  TrendingUp as TrendingUpIcon,
  ArrowForward as ArrowForwardIcon,
  PersonAdd as PersonAddIcon,
  Business as BusinessIcon,
  HourglassEmpty as HourglassEmptyIcon,
  Lock as LockIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import CollapsibleSidebar from '../components/common/CollapsibleSidebar';
import DashboardHeader from '../components/common/DashboardHeader';
import StatCard from '../components/common/StatCard';
import PendingRepresentatives from '../components/staff/PendingRepresentatives';
import PendingInternships from '../components/staff/PendingInternships';
import WithdrawalRequests from '../components/staff/WithdrawalRequests';
import Reports from '../components/staff/Reports';
import CreateStudent from '../components/staff/CreateStudent';
import CreateRepresentative from '../components/staff/CreateRepresentative';
import ChangePasswordDialog from '../components/common/ChangePasswordDialog';
import Profile from '../components/common/Profile';
import staffService from '../services/staffService';

/**
 * Career Center Staff Dashboard component.
 * Main interface for staff users with modern design and collapsible sidebar.
 * 
 * @returns {JSX.Element} Staff dashboard
 */
const StaffDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  
  const [mobileOpen, setMobileOpen] = useState(false);
  const [currentView, setCurrentView] = useState('home');
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

  const handleNavigation = (view) => {
    setCurrentView(view);
    if (isMobile) {
      setMobileOpen(false);
    }
  };

  const menuItems = [
    { id: 'home', label: 'Overview', icon: <HomeIcon /> },
    { id: 'create-student', label: 'Create Student', icon: <PersonAddIcon /> },
    { id: 'create-representative', label: 'Create Representative', icon: <BusinessIcon /> },
    { id: 'pending-representatives', label: 'Pending Representatives', icon: <HourglassEmptyIcon /> },
    { id: 'internships', label: 'Internships', icon: <WorkIcon /> },
    { id: 'withdrawals', label: 'Withdrawals', icon: <ExitToAppIcon /> },
    { id: 'reports', label: 'Reports', icon: <AssessmentIcon /> },
    { id: 'profile', label: 'My Profile', icon: <PersonIcon /> },
    { id: 'change-password', label: 'Change Password', icon: <LockIcon /> },
  ];

  const renderContent = () => {
    switch (currentView) {
      case 'create-student':
        return <CreateStudent staffId={user?.userId} />;
      case 'create-representative':
        return <CreateRepresentative staffId={user?.userId} />;
      case 'pending-representatives':
        return <PendingRepresentatives staffId={user?.userId} />;
      case 'internships':
        return <PendingInternships staffId={user?.userId} />;
      case 'withdrawals':
        return <WithdrawalRequests staffId={user?.userId} />;
      case 'reports':
        return <Reports staffId={user?.userId} />;
      case 'profile':
        return <Profile />;
      case 'change-password':
        setShowChangePassword(true);
        setCurrentView('home'); // Return to home
        return <StaffHome user={user} onNavigate={handleNavigation} />;
      case 'home':
      default:
        return <StaffHome user={user} onNavigate={handleNavigation} />;
    }
  };

  const getPageTitle = () => {
    const item = menuItems.find(m => m.id === currentView);
    return item ? item.label : 'Dashboard';
  };

  const getContentWidth = () => {
    if (isMobile) return '100%';
    return sidebarCollapsed ? 'calc(100% - 70px)' : 'calc(100% - 260px)';
  };

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      <CollapsibleSidebar
        menuItems={menuItems}
        currentView={currentView}
        onNavigate={handleNavigation}
        user={user}
        title="Staff Portal"
        mobileOpen={mobileOpen}
        onMobileToggle={handleDrawerToggle}
        onCollapseChange={handleCollapseChange}
        useRoundedShapes={false}
      />

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
        <DashboardHeader
          user={user}
          onLogout={handleLogout}
          onMenuClick={handleDrawerToggle}
          onProfileClick={() => handleNavigation('profile')}
          title={getPageTitle()}
          notificationCount={0}
        />

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

const StaffHome = ({ user, onNavigate }) => {
  const theme = useTheme();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState([
    {
      title: 'Pending Representatives',
      value: '0',
      subtitle: 'Awaiting approval',
      icon: <PersonIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    },
    {
      title: 'Pending Internships',
      value: '0',
      subtitle: 'Pending review',
      icon: <WorkIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    },
    {
      title: 'Withdrawal Requests',
      value: '0',
      subtitle: 'Requiring attention',
      icon: <ExitToAppIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    },
    {
      title: 'Total Placements',
      value: '0',
      subtitle: 'This semester',
      icon: <CheckCircleIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    },
  ]);

  useEffect(() => {
    const fetchStaffData = async () => {
      try {
        setLoading(true);
        
        // Fetch pending representatives
        const pendingReps = await staffService.getPendingRepresentatives(user?.userId);
        
        // Fetch pending internships
        const pendingInternships = await staffService.getPendingInternships(user?.userId);
        
        // Fetch pending withdrawals
        const pendingWithdrawals = await staffService.getPendingWithdrawals(user?.userId);
        
        // Generate report to get total placements
        const reportData = await staffService.generateReport(user?.userId, {});
        
        // Update stats with real data
        setStats([
          {
            title: 'Pending Representatives',
            value: pendingReps?.length || 0,
            subtitle: 'Awaiting approval',
            icon: <PersonIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          },
          {
            title: 'Pending Internships',
            value: pendingInternships?.length || 0,
            subtitle: 'Pending review',
            icon: <WorkIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
          },
          {
            title: 'Withdrawal Requests',
            value: pendingWithdrawals?.length || 0,
            subtitle: 'Requiring attention',
            icon: <ExitToAppIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
          },
          {
            title: 'Total Placements',
            value: reportData?.totalPlacements || 0,
            subtitle: 'This semester',
            icon: <CheckCircleIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
          },
        ]);
      } catch (error) {
        console.error('Error fetching staff data:', error);
        toast.error('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    if (user?.userId) {
      fetchStaffData();
    }
  }, [user?.userId]);

  const navigationCards = [
    {
      id: 'pending-representatives',
      title: 'Company Representatives',
      description: 'Approve or reject company representative registrations and manage accounts',
      icon: <PersonIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    },
    {
      id: 'internships',
      title: 'Internship Opportunities',
      description: 'Review and approve internship postings from companies',
      icon: <WorkIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    },
    {
      id: 'withdrawals',
      title: 'Withdrawal Requests',
      description: 'Manage student withdrawal requests for accepted placements',
      icon: <ExitToAppIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    },
    {
      id: 'reports',
      title: 'System Reports',
      description: 'Generate comprehensive reports and analytics for the system',
      icon: <AssessmentIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    },
  ];

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  return (
    <Box>
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
          Manage the internship placement system and oversee all operations
        </Typography>
      </Box>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <StatCard {...stat} />
          </Grid>
        ))}
      </Grid>

      <Typography
        variant="h5"
        sx={{
          fontWeight: 700,
          mb: 3,
          color: 'grey.900',
        }}
      >
        Management Tools
      </Typography>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        {navigationCards.map((card) => (
          <Grid item xs={12} sm={6} md={6} key={card.id}>
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
              onClick={() => onNavigate(card.id)}
            >
              <Box
                sx={{
                  position: 'absolute',
                  top: 0,
                  left: 0,
                  right: 0,
                  height: 4,
                  background: card.gradient,
                }}
              />
              
              <CardContent sx={{ p: 3 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                  <Box
                    className="action-icon"
                    sx={{
                      width: 60,
                      height: 60,
                      borderRadius: 3,
                      background: card.gradient,
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: '#fff',
                      transition: 'transform 0.3s ease',
                      boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                    }}
                  >
                    {card.icon}
                  </Box>
                </Box>
                
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    mb: 1,
                    fontSize: '1.15rem',
                    color: 'grey.900',
                  }}
                >
                  {card.title}
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
                  {card.description}
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
                    Manage
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
            <strong>Staff ID:</strong> {user?.userId} | <strong>Department:</strong> {user?.department || 'CCDS'}
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

export default StaffDashboard;
