import api from './api';

/**
 * Service for student-related operations.
 */
export const studentService = {
  /**
   * Get available internship opportunities for a student.
   * @param {string} studentId - Student ID
   * @param {Object} filters - Optional filters
   * @returns {Promise<Array>} List of internship opportunities
   */
  getAvailableInternships: async (studentId, filters = {}) => {
    const params = { studentId, ...filters };
    const response = await api.get('/students/internships', { params });
    return response.data.data;
  },

  /**
   * Get student's applications.
   * @param {string} studentId - Student ID
   * @returns {Promise<Array>} List of applications
   */
  getMyApplications: async (studentId) => {
    const response = await api.get(`/students/applications?studentId=${studentId}`);
    return response.data.data;
  },

  /**
   * Apply for an internship.
   * @param {string} studentId - Student ID
   * @param {string} internshipId - Internship ID
   * @returns {Promise} API response
   */
  applyForInternship: async (studentId, internshipId) => {
    const response = await api.post(
      `/students/applications?studentId=${studentId}`,
      { internshipId }
    );
    return response.data;
  },

  /**
   * Accept a placement offer.
   * @param {string} studentId - Student ID
   * @param {string} applicationId - Application ID
   * @returns {Promise} API response
   */
  acceptPlacement: async (studentId, applicationId) => {
    const response = await api.post(
      `/students/applications/${applicationId}/accept?studentId=${studentId}`
    );
    return response.data;
  },

  /**
   * Request withdrawal from an application.
   * @param {string} studentId - Student ID
   * @param {string} applicationId - Application ID
   * @param {string} reason - Withdrawal reason
   * @returns {Promise} API response
   */
  requestWithdrawal: async (studentId, applicationId, reason) => {
    const response = await api.post(
      `/students/applications/withdraw?studentId=${studentId}`,
      { applicationId, reason }
    );
    return response.data;
  },

  /**
   * Get student profile.
   * @param {string} studentId - Student ID
   * @returns {Promise<Object>} Student profile data
   */
  getProfile: async (studentId) => {
    const response = await api.get(`/students/profile?studentId=${studentId}`);
    return response.data.data;
  },

  /**
   * Get count of active applications.
   * @param {string} studentId - Student ID
   * @returns {Promise<number>} Count of active applications
   */
  getActiveApplicationCount: async (studentId) => {
    const response = await api.get(`/students/applications/count?studentId=${studentId}`);
    return response.data.data;
  },

  /**
   * Get all withdrawal requests for a student.
   * @param {string} studentId - Student ID
   * @returns {Promise<Array>} List of withdrawal requests
   */
  getWithdrawalRequests: async (studentId) => {
    const response = await api.get(`/students/withdrawal-requests?studentId=${studentId}`);
    return response.data.data;
  },

  /**
   * Update a pending withdrawal request.
   * @param {string} studentId - Student ID
   * @param {string} withdrawalId - Withdrawal request ID
   * @param {string} reason - Updated withdrawal reason
   * @returns {Promise} API response
   */
  updateWithdrawalRequest: async (studentId, withdrawalId, reason) => {
    const response = await api.put(
      `/students/withdrawal-requests/${withdrawalId}?studentId=${studentId}`,
      { reason }
    );
    return response.data;
  },

  /**
   * Cancel a pending withdrawal request.
   * @param {string} studentId - Student ID
   * @param {string} withdrawalId - Withdrawal request ID
   * @param {string} cancellationReason - Reason for cancelling the withdrawal request
   * @returns {Promise} API response
   */
  cancelWithdrawalRequest: async (studentId, withdrawalId, cancellationReason) => {
    const response = await api.delete(
      `/students/withdrawal-requests/${withdrawalId}?studentId=${studentId}`,
      { data: { reason: cancellationReason } }
    );
    return response.data;
  },
};

export default studentService;
