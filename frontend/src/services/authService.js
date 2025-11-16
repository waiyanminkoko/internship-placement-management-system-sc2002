import api from './api';

/**
 * Authentication service for handling user authentication operations.
 */
export const authService = {
  /**
   * Login a user with credentials.
   * @param {string} userId - User ID
   * @param {string} password - Password
   * @returns {Promise<Object>} User data with token
   */
  login: async (userId, password) => {
    const response = await api.post('/auth/login', { userId, password });
    const { data } = response.data;
    return data;
  },

  /**
   * Logout the current user.
   * @param {string} userId - User ID
   * @returns {Promise} API response
   */
  logout: async (userId) => {
    try {
      await api.post(`/auth/logout?userId=${userId}`);
    } finally {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
    }
  },

  /**
   * Change user password.
   * @param {string} userId - User ID
   * @param {string} oldPassword - Current password
   * @param {string} newPassword - New password
   * @returns {Promise} API response
   */
  changePassword: async (userId, oldPassword, newPassword) => {
    const response = await api.post(`/auth/change-password?userId=${userId}`, {
      oldPassword,
      newPassword,
    });
    return response.data;
  },

  /**
   * Check authentication status.
   * @param {string} userId - User ID
   * @returns {Promise<boolean>} True if authenticated
   */
  checkStatus: async (userId) => {
    const response = await api.get(`/auth/status?userId=${userId}`);
    return response.data.data;
  },

  /**
   * Get current user from localStorage.
   * @returns {Object|null} User data or null
   */
  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  /**
   * Check if user is authenticated.
   * @returns {boolean} True if authenticated
   */
  isAuthenticated: () => {
    return !!localStorage.getItem('authToken');
  },
};

export default authService;
