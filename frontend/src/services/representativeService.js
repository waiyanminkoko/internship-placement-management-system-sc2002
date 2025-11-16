import api from './api';

/**
 * Service for company representative operations.
 */
export const representativeService = {
  /**
   * Create a new internship opportunity.
   * @param {string} repId - Representative ID
   * @param {Object} internshipData - Internship details
   * @returns {Promise} API response
   */
  createInternship: async (repId, internshipData) => {
    const response = await api.post(
      `/representatives/internships?repId=${repId}`,
      internshipData
    );
    return response.data;
  },

  /**
   * Get representative's internship opportunities.
   * @param {string} repId - Representative ID
   * @returns {Promise<Array>} List of internship opportunities
   */
  getMyInternships: async (repId) => {
    const response = await api.get(`/representatives/internships?repId=${repId}`);
    return response.data.data;
  },

  /**
   * Update an internship opportunity.
   * @param {string} repId - Representative ID
   * @param {string} opportunityId - Internship opportunity ID
   * @param {Object} internshipData - Updated internship details
   * @returns {Promise} API response
   */
  updateInternship: async (repId, opportunityId, internshipData) => {
    const response = await api.put(
      `/representatives/internships/${opportunityId}?repId=${repId}`,
      internshipData
    );
    return response.data;
  },

  /**
   * Delete an internship opportunity.
   * @param {string} repId - Representative ID
   * @param {string} opportunityId - Internship opportunity ID
   * @returns {Promise} API response
   */
  deleteInternship: async (repId, opportunityId) => {
    const response = await api.delete(
      `/representatives/internships/${opportunityId}?repId=${repId}`
    );
    return response.data;
  },

  /**
   * Get applications for an internship opportunity.
   * @param {string} repId - Representative ID
   * @param {string} opportunityId - Internship opportunity ID
   * @returns {Promise<Array>} List of applications
   */
  getApplications: async (repId, opportunityId) => {
    const response = await api.get(
      `/representatives/internships/${opportunityId}/applications?repId=${repId}`
    );
    return response.data.data;
  },

  /**
   * Approve an application.
   * @param {string} repId - Representative ID
   * @param {string} applicationId - Application ID
   * @param {string} comments - Optional comments
   * @returns {Promise} API response
   */
  approveApplication: async (repId, applicationId, comments = '') => {
    const response = await api.post(
      `/representatives/applications/${applicationId}/approve?repId=${repId}`,
      { approve: true, comments }
    );
    return response.data;
  },

  /**
   * Reject an application.
   * @param {string} repId - Representative ID
   * @param {string} applicationId - Application ID
   * @param {string} comments - Optional comments
   * @returns {Promise} API response
   */
  rejectApplication: async (repId, applicationId, comments = '') => {
    const response = await api.post(
      `/representatives/applications/${applicationId}/reject?repId=${repId}`,
      { approve: false, comments }
    );
    return response.data;
  },

  /**
   * Toggle internship visibility.
   * @param {string} repId - Representative ID
   * @param {string} opportunityId - Internship opportunity ID
   * @param {boolean} visibility - New visibility status
   * @returns {Promise} API response
   */
  toggleVisibility: async (repId, opportunityId, visibility) => {
    const response = await api.patch(
      `/representatives/internships/${opportunityId}/visibility?repId=${repId}&visibility=${visibility}`
    );
    return response.data;
  },
};

export default representativeService;
