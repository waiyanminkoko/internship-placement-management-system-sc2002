/**
 * Validation utility functions for form inputs.
 */

/**
 * Validate email format.
 * @param {string} email - Email address
 * @returns {boolean} True if valid
 */
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

/**
 * Validate NTU student ID format (e.g., U1234567A).
 * @param {string} userId - User ID
 * @returns {boolean} True if valid
 */
export const isValidStudentId = (userId) => {
  const studentIdRegex = /^U\d{7}[A-Z]$/;
  return studentIdRegex.test(userId);
};

/**
 * Validate password strength.
 * @param {string} password - Password
 * @returns {Object} Validation result with isValid and message
 */
export const validatePassword = (password) => {
  if (!password) {
    return { isValid: false, message: 'Password is required' };
  }
  if (password.length < 8) {
    return { isValid: false, message: 'Password must be at least 8 characters' };
  }
  if (!/[A-Z]/.test(password)) {
    return { isValid: false, message: 'Password must contain at least one uppercase letter' };
  }
  if (!/[a-z]/.test(password)) {
    return { isValid: false, message: 'Password must contain at least one lowercase letter' };
  }
  if (!/[0-9]/.test(password)) {
    return { isValid: false, message: 'Password must contain at least one number' };
  }
  return { isValid: true, message: 'Password is strong' };
};

/**
 * Validate required field.
 * @param {any} value - Field value
 * @param {string} fieldName - Field name for error message
 * @returns {string|null} Error message or null if valid
 */
export const validateRequired = (value, fieldName = 'This field') => {
  if (!value || (typeof value === 'string' && value.trim() === '')) {
    return `${fieldName} is required`;
  }
  return null;
};

/**
 * Validate date is not in the past.
 * @param {string|Date} date - Date to validate
 * @returns {boolean} True if date is today or future
 */
export const isValidFutureDate = (date) => {
  const selectedDate = new Date(date);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return selectedDate >= today;
};

/**
 * Validate date range (start before end).
 * @param {string|Date} startDate - Start date
 * @param {string|Date} endDate - End date
 * @returns {boolean} True if start is before end
 */
export const isValidDateRange = (startDate, endDate) => {
  return new Date(startDate) < new Date(endDate);
};

/**
 * Validate number is positive integer.
 * @param {any} value - Value to validate
 * @returns {boolean} True if positive integer
 */
export const isPositiveInteger = (value) => {
  const num = parseInt(value, 10);
  return !isNaN(num) && num > 0 && num === parseFloat(value);
};

/**
 * Validate text length.
 * @param {string} text - Text to validate
 * @param {number} minLength - Minimum length
 * @param {number} maxLength - Maximum length
 * @returns {Object} Validation result
 */
export const validateTextLength = (text, minLength = 0, maxLength = Infinity) => {
  if (!text) {
    return { isValid: false, message: 'Text is required' };
  }
  if (text.length < minLength) {
    return { isValid: false, message: `Text must be at least ${minLength} characters` };
  }
  if (text.length > maxLength) {
    return { isValid: false, message: `Text must not exceed ${maxLength} characters` };
  }
  return { isValid: true, message: 'Valid' };
};

export default {
  isValidEmail,
  isValidStudentId,
  validatePassword,
  validateRequired,
  isValidFutureDate,
  isValidDateRange,
  isPositiveInteger,
  validateTextLength,
};
