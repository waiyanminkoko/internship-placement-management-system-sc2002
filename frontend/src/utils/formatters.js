import { format, parseISO, formatDistanceToNow } from 'date-fns';

/**
 * Formatting utility functions for displaying data.
 */

/**
 * Format a date string to readable format.
 * @param {string|Date} date - Date to format
 * @param {string} formatStr - Format string (default: 'dd-MM-yyyy')
 * @returns {string} Formatted date
 */
export const formatDate = (date, formatStr = 'dd-MM-yyyy') => {
  if (!date) return 'N/A';
  try {
    const dateObj = typeof date === 'string' ? parseISO(date) : date;
    return format(dateObj, formatStr);
  } catch (error) {
    return 'Invalid date';
  }
};

/**
 * Format a date to relative time (e.g., "2 days ago").
 * @param {string|Date} date - Date to format
 * @returns {string} Relative time string
 */
export const formatRelativeTime = (date) => {
  if (!date) return 'N/A';
  try {
    const dateObj = typeof date === 'string' ? parseISO(date) : date;
    return formatDistanceToNow(dateObj, { addSuffix: true });
  } catch (error) {
    return 'Invalid date';
  }
};

/**
 * Format a date and time to readable format.
 * @param {string|Date} dateTime - DateTime to format
 * @returns {string} Formatted date and time
 */
export const formatDateTime = (dateTime) => {
  return formatDate(dateTime, 'dd-MM-yyyy HH:mm');
};

/**
 * Format internship level to display format.
 * @param {string} level - Internship level
 * @returns {string} Formatted level
 */
export const formatLevel = (level) => {
  if (!level) return 'N/A';
  return level.charAt(0).toUpperCase() + level.slice(1).toLowerCase();
};

/**
 * Format application status to display format with color.
 * @param {string} status - Application status
 * @returns {Object} Formatted status with color
 */
export const formatStatus = (status) => {
  const statusMap = {
    PENDING: { label: 'Pending', color: 'warning' },
    SUCCESSFUL: { label: 'Successful', color: 'success' },
    UNSUCCESSFUL: { label: 'Unsuccessful', color: 'error' },
    WITHDRAWN: { label: 'Withdrawn', color: 'default' },
    APPROVED: { label: 'Approved', color: 'success' },
    REJECTED: { label: 'Rejected', color: 'error' },
  };
  return statusMap[status] || { label: status, color: 'default' };
};

/**
 * Format user role to display format.
 * @param {string} role - User role
 * @returns {string} Formatted role
 */
export const formatRole = (role) => {
  const roleMap = {
    STUDENT: 'Student',
    COMPANY_REPRESENTATIVE: 'Company Representative',
    CAREER_CENTER_STAFF: 'Career Center Staff',
  };
  return roleMap[role] || role;
};

/**
 * Format user ID for display (mask middle characters).
 * @param {string} userId - User ID
 * @returns {string} Masked user ID
 */
export const formatUserId = (userId) => {
  if (!userId || userId.length < 4) return userId;
  const start = userId.slice(0, 2);
  const end = userId.slice(-2);
  const middle = '*'.repeat(userId.length - 4);
  return `${start}${middle}${end}`;
};

/**
 * Truncate text to specified length.
 * @param {string} text - Text to truncate
 * @param {number} maxLength - Maximum length
 * @returns {string} Truncated text
 */
export const truncateText = (text, maxLength = 100) => {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return `${text.slice(0, maxLength)}...`;
};

/**
 * Format number with commas.
 * @param {number} num - Number to format
 * @returns {string} Formatted number
 */
export const formatNumber = (num) => {
  if (num === null || num === undefined) return '0';
  return num.toLocaleString();
};

/**
 * Format percentage.
 * @param {number} value - Value (0-100)
 * @param {number} decimals - Decimal places
 * @returns {string} Formatted percentage
 */
export const formatPercentage = (value, decimals = 1) => {
  if (value === null || value === undefined) return '0%';
  return `${value.toFixed(decimals)}%`;
};

export default {
  formatDate,
  formatRelativeTime,
  formatDateTime,
  formatLevel,
  formatStatus,
  formatRole,
  formatUserId,
  truncateText,
  formatNumber,
  formatPercentage,
};
