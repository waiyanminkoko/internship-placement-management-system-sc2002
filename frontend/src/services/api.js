import axios from 'axios';

/**
 * Axios instance configured for API communication.
 * Base URL is set from environment variable.
 * Vite uses import.meta.env instead of process.env
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:6060/api',
  headers: {
    'Content-Type': 'application/json',
    'Cache-Control': 'no-cache, no-store, must-revalidate',
    'Pragma': 'no-cache',
    'Expires': '0',
  },
});

/**
 * Request interceptor to add auth token to requests.
 */
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Response interceptor for centralized error handling.
 */
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle 401 Unauthorized - only clear storage, let React Router handle redirect
    if (error.response?.status === 401) {
      // Don't clear auth on login endpoint failures
      if (!error.config?.url?.includes('/auth/login')) {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        // Don't redirect here - let ProtectedRoute handle it
      }
    }
    return Promise.reject(error);
  }
);

export default api;
