import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import { CssBaseline } from '@mui/material';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AuthProvider } from './context/AuthContext';
import theme from './theme';
import ErrorBoundary from './components/common/ErrorBoundary';
import ProtectedRoute from './components/common/ProtectedRoute';
import Login from './pages/Login';
import StudentDashboard from './pages/StudentDashboard';
import RepresentativeDashboard from './pages/RepresentativeDashboard';
import StaffDashboard from './pages/StaffDashboard';

/**
 * Main App component for the Internship Placement Management System.
 * Configures routing, theme, and global context providers.
 * 
 * @returns {JSX.Element} The main application component
 */
function App() {
  return (
    <ErrorBoundary>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <Router>
            <div className="App">
              <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route
                  path="/student/*"
                  element={
                    <ProtectedRoute allowedRoles={['STUDENT']}>
                      <StudentDashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/representative/*"
                  element={
                    <ProtectedRoute allowedRoles={['COMPANY_REPRESENTATIVE']}>
                      <RepresentativeDashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/staff/*"
                  element={
                    <ProtectedRoute allowedRoles={['CAREER_CENTER_STAFF']}>
                      <StaffDashboard />
                    </ProtectedRoute>
                  }
                />
              </Routes>
              <ToastContainer
                position="top-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
              />
            </div>
          </Router>
        </AuthProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}

export default App;
