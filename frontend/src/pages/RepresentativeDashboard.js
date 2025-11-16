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
  Assignment as AssignmentIcon,
  Visibility as VisibilityIcon,
  CheckCircle as CheckCircleIcon,
  Add as AddIcon,
  List as ListIcon,
  TrendingUp as TrendingUpIcon,
  ArrowForward as ArrowForwardIcon,
} from '@mui/icons-material';
import { toast } from 'react-toastify';
import CollapsibleSidebar from '../components/common/CollapsibleSidebar';
import DashboardHeader from '../components/common/DashboardHeader';
import StatCard from '../components/common/StatCard';
import CreateInternship from '../components/representative/CreateInternship';
import MyInternships from '../components/representative/MyInternships';
import Applications from '../components/representative/Applications';
import Placements from '../components/representative/Placements';
import ChangePasswordDialog from '../components/common/ChangePasswordDialog';
import Profile from '../components/common/Profile';
import representativeService from '../services/representativeService';
import LockIcon from '@mui/icons-material/Lock';
import PersonIcon from '@mui/icons-material/Person';

/**
 * Company Representative Dashboard component.
 * Main interface for company representative users with modern design.
 * 
 * @returns {JSX.Element} Representative dashboard
 */
const RepresentativeDashboard = () => {
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
    { id: 'create', label: 'Create Internship', icon: <AddIcon /> },
    { id: 'internships', label: 'Internship Opportunities', icon: <WorkIcon /> },
    { id: 'applications', label: 'Applications', icon: <AssignmentIcon /> },
    { id: 'placements', label: 'Placements', icon: <CheckCircleIcon /> },
    { id: 'profile', label: 'My Profile', icon: <PersonIcon /> },
    { id: 'change-password', label: 'Change Password', icon: <LockIcon /> },
  ];

  const renderContent = () => {
    switch (currentView) {
      case 'create':
        return <CreateInternship repId={user?.userId} />;
      case 'internships':
        return <MyInternships repId={user?.userId} />;
      case 'applications':
        return <Applications repId={user?.userId} />;
      case 'profile':
        return <Profile />;
      case 'change-password':
        setShowChangePassword(true);
        setCurrentView('home'); // Return to home
        return <RepresentativeHome user={user} onNavigate={handleNavigation} />;
      case 'placements':
        return <Placements repId={user?.userId} />;
      case 'home':
      default:
        return <RepresentativeHome user={user} onNavigate={handleNavigation} />;
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
        title="Company Portal"
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

const RepresentativeHome = ({ user, onNavigate }) => {
  const theme = useTheme();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState([
    {
      title: 'Active Internships',
      value: '0',
      subtitle: 'Out of 5 maximum',
      icon: <WorkIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    },
    {
      title: 'Pending Applications',
      value: '0',
      subtitle: 'Awaiting your review',
      icon: <AssignmentIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    },
    {
      title: 'Approved Applicants',
      value: '0',
      subtitle: 'Awaiting acceptance',
      icon: <CheckCircleIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    },
    {
      title: 'Confirmed Placements',
      value: '0',
      subtitle: 'Successfully placed',
      icon: <TrendingUpIcon sx={{ fontSize: 28 }} />,
      gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    },
  ]);

  useEffect(() => {
    const fetchRepresentativeData = async () => {
      try {
        setLoading(true);
        
        // Fetch representative's internships
        const internships = await representativeService.getMyInternships(user?.userId);
        const activeInternships = (internships || []).filter(i => i.status === 'APPROVED' && i.visibility);
        
        // Fetch applications for all internships
        let totalApps = 0;
        let approvedApps = 0;
        let confirmedPlacements = 0;
        
        for (const internship of internships || []) {
          try {
            const apps = await representativeService.getApplications(user?.userId, internship.internshipId);
            totalApps += (apps || []).filter(a => a.status === 'PENDING').length;
            approvedApps += (apps || []).filter(a => a.status === 'SUCCESSFUL').length;
            confirmedPlacements += (apps || []).filter(a => a.placementAccepted === true).length;
          } catch (error) {
            console.error(`Error fetching applications for internship ${internship.internshipId}:`, error);
          }
        }
        
        // Update stats with real data
        setStats([
          {
            title: 'Active Internships',
            value: activeInternships.length,
            subtitle: 'Out of 5 maximum',
            icon: <WorkIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          },
          {
            title: 'Pending Applications',
            value: totalApps,
            subtitle: 'Awaiting your review',
            icon: <AssignmentIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
          },
          {
            title: 'Approved Applicants',
            value: approvedApps,
            subtitle: 'Awaiting acceptance',
            icon: <CheckCircleIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
          },
          {
            title: 'Confirmed Placements',
            value: confirmedPlacements,
            subtitle: 'Successfully placed',
            icon: <TrendingUpIcon sx={{ fontSize: 28 }} />,
            gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
          },
        ]);
      } catch (error) {
        console.error('Error fetching representative data:', error);
        toast.error('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    if (user?.userId) {
      fetchRepresentativeData();
    }
  }, [user?.userId]);

  const navigationCards = [
    {
      id: 'create',
      title: 'Create New Internship',
      description: 'Post a new internship opportunity for students to apply',
      icon: <AddIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    },
    {
      id: 'internships',
      title: 'Internship Opportunities',
      description: 'View and manage your posted internship opportunities',
      icon: <ListIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    },
    {
      id: 'applications',
      title: 'Review Applications',
      description: 'Review and approve or reject student applications',
      icon: <AssignmentIcon sx={{ fontSize: 32 }} />,
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    },
    {
      id: 'placements',
      title: 'Track Placements',
      description: 'Monitor confirmed placements and accepted offers',
      icon: <CheckCircleIcon sx={{ fontSize: 32 }} />,
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
          Manage your company's internship opportunities and applicants
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
        Quick Actions
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
            💼 Company Guidelines
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Maximum of 5 active internship postings allowed
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Review applications promptly for better engagement
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Toggle visibility to pause or unpause applications
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={12} md={6}>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Provide detailed internship descriptions for clarity
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Track placement confirmations from students
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1.5, mb: 2 }}>
                <Typography sx={{ opacity: 0.9 }}>•</Typography>
                <Typography variant="body2" sx={{ opacity: 0.95, lineHeight: 1.6 }}>
                  Respond to all applications within recommended timeframe
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

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
            <strong>Company Representative ID:</strong> {user?.userId}
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

export default RepresentativeDashboard;
