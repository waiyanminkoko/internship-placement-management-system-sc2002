import api from './api';

/**
 * Service for career center staff operations.
 */
export const staffService = {
  /**
   * Get pending company representatives.
   * @param {string} staffId - Staff ID
   * @returns {Promise<Array>} List of pending representatives
   */
  getPendingRepresentatives: async (staffId) => {
    const response = await api.get(`/staff/representatives/pending?staffId=${staffId}`);
    return response.data.data;
  },

  /**
   * Authorize or reject a company representative.
   * @param {string} staffId - Staff ID
   * @param {string} representativeId - Representative ID
   * @param {boolean} approve - True to approve, false to reject
   * @param {string} comments - Optional comments
   * @returns {Promise} API response
   */
  authorizeRepresentative: async (staffId, representativeId, approve, comments = '') => {
    const response = await api.post(
      `/staff/representatives/${representativeId}/authorize?staffId=${staffId}`,
      { approve, comments }
    );
    return response.data;
  },

  /**
   * Get pending internship opportunities.
   * @param {string} staffId - Staff ID
   * @returns {Promise<Array>} List of pending opportunities
   */
  getPendingInternships: async (staffId) => {
    const response = await api.get(`/staff/internships/pending?staffId=${staffId}`);
    return response.data.data;
  },

  /**
   * Approve or reject an internship opportunity.
   * @param {string} staffId - Staff ID
   * @param {string} opportunityId - Internship opportunity ID
   * @param {boolean} approve - True to approve, false to reject
   * @param {string} comments - Optional comments
   * @returns {Promise} API response
   */
  approveInternship: async (staffId, opportunityId, approve, comments = '') => {
    const response = await api.post(
      `/staff/internships/${opportunityId}/approve?staffId=${staffId}`,
      { approve, comments }
    );
    return response.data;
  },

  /**
   * Get pending withdrawal requests.
   * @param {string} staffId - Staff ID
   * @returns {Promise<Array>} List of pending withdrawal requests
   */
  getPendingWithdrawals: async (staffId) => {
    const response = await api.get(`/staff/withdrawals/pending?staffId=${staffId}`);
    return response.data.data;
  },

  /**
   * Process a withdrawal request.
   * @param {string} staffId - Staff ID
   * @param {string} withdrawalId - Withdrawal request ID
   * @param {boolean} approve - True to approve, false to reject
   * @param {string} comments - Optional comments
   * @returns {Promise} API response
   */
  processWithdrawal: async (staffId, withdrawalId, approve, comments = '') => {
    const response = await api.post(
      `/staff/withdrawals/${withdrawalId}/process?staffId=${staffId}`,
      { approve, comments }
    );
    return response.data;
  },

  /**
   * Generate a report with optional filters.
   * @param {string} staffId - Staff ID
   * @param {Object} filters - Report filters
   * @returns {Promise<Object>} Report data
   */
  generateReport: async (staffId, filters = {}) => {
    const params = { staffId, ...filters };
    const response = await api.get('/staff/reports', { params });
    return response.data.data;
  },

  /**
   * Create a new student account.
   * @param {string} staffId - Staff ID
   * @param {Object} studentData - Student data (studentId, name, major, year, email, password)
   * @returns {Promise<Object>} Created student data
   */
  createStudent: async (staffId, studentData) => {
    const response = await api.post(`/staff/students/create?staffId=${staffId}`, studentData);
    return response.data;
  },

  /**
   * Create a new company representative account.
   * @param {string} staffId - Staff ID
   * @param {Object} representativeData - Representative data (companyRepId, name, companyName, industry, position, email, password)
   * @returns {Promise<Object>} Created representative data
   */
  createRepresentative: async (staffId, representativeData) => {
    const response = await api.post(`/staff/representatives/create?staffId=${staffId}`, representativeData);
    return response.data;
  },

  /**
   * Get all companies from approved representatives.
   * @param {string} staffId - Staff ID
   * @returns {Promise<Array<string>>} List of company names
   */
  getCompanies: async (staffId) => {
    const response = await api.get(`/staff/companies?staffId=${staffId}`);
    return response.data.data;
  },
};

export default staffService;
