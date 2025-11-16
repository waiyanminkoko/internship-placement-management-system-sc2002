import React, { createContext, useState, useEffect } from 'react';
import authService from '../services/authService';

/**
 * Authentication context for managing user authentication state.
 * Provides authentication functions and user data throughout the application.
 */
export const AuthContext = createContext();

/**
 * AuthProvider component that wraps the application and provides authentication context.
 * 
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - Child components
 * @returns {JSX.Element} Authentication provider
 */
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Initialize auth state from localStorage on mount
  useEffect(() => {
    const initializeAuth = () => {
      try {
        const storedUser = authService.getCurrentUser();
        const token = localStorage.getItem('authToken');
        
        if (storedUser && token) {
          setUser(storedUser);
        }
      } catch (error) {
        console.error('Error initializing auth:', error);
        // Clear potentially corrupted data
        localStorage.removeItem('user');
        localStorage.removeItem('authToken');
      } finally {
        setLoading(false);
      }
    };

    initializeAuth();
  }, []);

  /**
   * Login function to authenticate user.
   * 
   * @param {string} userId - User ID (student ID, staff ID, or email)
   * @param {string} password - User password
   * @returns {Promise<Object>} Login response with user data
   * @throws {Error} If login fails
   */
  const login = async (userId, password) => {
    try {
      // Call authentication service
      const response = await authService.login(userId, password);
      
      // Store auth data
      localStorage.setItem('authToken', response.token);
      localStorage.setItem('user', JSON.stringify(response));
      
      // Update state
      setUser(response);
      
      return response;
    } catch (error) {
      console.error('Login error:', error);
      
      // Clear any existing auth data
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      setUser(null);
      
      // Extract error message
      const errorMessage = error.response?.data?.message || error.message || 'Login failed';
      throw new Error(errorMessage);
    }
  };

  /**
   * Logout function to clear authentication state.
   */
  const logout = async () => {
    try {
      // Call logout service if user exists
      if (user?.userId) {
        await authService.logout(user.userId);
      }
    } catch (error) {
      console.error('Logout error:', error);
      // Continue with logout even if API call fails
    } finally {
      // Clear auth data
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      setUser(null);
    }
  };

  /**
   * Check if user is authenticated.
   * 
   * @returns {boolean} True if user is authenticated
   */
  const isAuthenticated = () => {
    return !!user && !!localStorage.getItem('authToken');
  };

  /**
   * Get the current user's role.
   * 
   * @returns {string|null} User role or null
   */
  const getUserRole = () => {
    return user?.role || null;
  };

  /**
   * Check if current user has a specific role.
   * 
   * @param {string} role - Role to check
   * @returns {boolean} True if user has the role
   */
  const hasRole = (role) => {
    return user?.role === role;
  };

  /**
   * Check if current user has any of the specified roles.
   * 
   * @param {string[]} roles - Array of roles to check
   * @returns {boolean} True if user has any of the roles
   */
  const hasAnyRole = (roles) => {
    return roles.includes(user?.role);
  };

  /**
   * Update user data in context and localStorage.
   * Useful after profile updates.
   * 
   * @param {Object} updatedUser - Updated user data
   */
  const updateUser = (updatedUser) => {
    setUser(updatedUser);
    localStorage.setItem('user', JSON.stringify(updatedUser));
  };

  // Context value provided to consumers
  const value = {
    user,
    loading,
    login,
    logout,
    isAuthenticated,
    getUserRole,
    hasRole,
    hasAnyRole,
    updateUser,
  };

  // Don't render children until auth state is initialized
  if (loading) {
    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        }}
      >
        <div style={{ textAlign: 'center', color: 'white' }}>
          <div
            style={{
              width: '50px',
              height: '50px',
              border: '5px solid rgba(255, 255, 255, 0.3)',
              borderTop: '5px solid white',
              borderRadius: '50%',
              animation: 'spin 1s linear infinite',
              margin: '0 auto 20px',
            }}
          />
          <style>
            {`
              @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
              }
            `}
          </style>
          <p style={{ fontSize: '18px', fontWeight: 500 }}>Loading...</p>
        </div>
      </div>
    );
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * Custom hook to use the AuthContext.
 * This is a convenience export that wraps the useContext hook.
 * 
 * @returns {Object} Authentication context value
 * @throws {Error} If used outside AuthProvider
 */
export const useAuth = () => {
  const context = React.useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext;
