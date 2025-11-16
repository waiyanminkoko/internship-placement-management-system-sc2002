import { toast } from 'react-toastify';
import { getErrorMessage } from '../utils/helpers';

/**
 * Custom hook for showing notifications using react-toastify.
 * @returns {Object} Notification functions
 */
const useNotification = () => {
  /**
   * Show success notification.
   * @param {string} message - Success message
   * @param {Object} options - Toast options
   */
  const showSuccess = (message, options = {}) => {
    toast.success(message, {
      position: 'top-right',
      autoClose: 3000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      ...options,
    });
  };

  /**
   * Show error notification.
   * @param {string|Error} error - Error message or Error object
   * @param {Object} options - Toast options
   */
  const showError = (error, options = {}) => {
    const message = typeof error === 'string' ? error : getErrorMessage(error);
    toast.error(message, {
      position: 'top-right',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      ...options,
    });
  };

  /**
   * Show warning notification.
   * @param {string} message - Warning message
   * @param {Object} options - Toast options
   */
  const showWarning = (message, options = {}) => {
    toast.warning(message, {
      position: 'top-right',
      autoClose: 4000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      ...options,
    });
  };

  /**
   * Show info notification.
   * @param {string} message - Info message
   * @param {Object} options - Toast options
   */
  const showInfo = (message, options = {}) => {
    toast.info(message, {
      position: 'top-right',
      autoClose: 3000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      ...options,
    });
  };

  /**
   * Show loading notification.
   * @param {string} message - Loading message
   * @returns {number} Toast ID for updating/dismissing
   */
  const showLoading = (message = 'Loading...') => {
    return toast.loading(message, {
      position: 'top-right',
    });
  };

  /**
   * Update an existing notification.
   * @param {number} toastId - Toast ID to update
   * @param {Object} options - Update options
   */
  const updateNotification = (toastId, options) => {
    toast.update(toastId, options);
  };

  /**
   * Dismiss a notification.
   * @param {number} toastId - Toast ID to dismiss
   */
  const dismiss = (toastId) => {
    toast.dismiss(toastId);
  };

  /**
   * Dismiss all notifications.
   */
  const dismissAll = () => {
    toast.dismiss();
  };

  return {
    showSuccess,
    showError,
    showWarning,
    showInfo,
    showLoading,
    updateNotification,
    dismiss,
    dismissAll,
  };
};

export default useNotification;
