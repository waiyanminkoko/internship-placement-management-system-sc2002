import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from '@mui/material';
import PropTypes from 'prop-types';

/**
 * Reusable modal dialog component.
 * @param {Object} props - Component props
 * @returns {JSX.Element} Modal dialog
 */
const Modal = ({
  open,
  onClose,
  title,
  content,
  onConfirm,
  confirmText = 'Confirm',
  cancelText = 'Cancel',
  showCancel = true,
  maxWidth = 'sm',
  fullWidth = true,
  children,
}) => {
  const handleConfirm = () => {
    if (onConfirm) {
      onConfirm();
    }
    onClose();
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      maxWidth={maxWidth}
      fullWidth={fullWidth}
    >
      {title && <DialogTitle>{title}</DialogTitle>}
      
      <DialogContent>
        {content && <DialogContentText>{content}</DialogContentText>}
        {children}
      </DialogContent>
      
      <DialogActions>
        {showCancel && (
          <Button onClick={onClose} color="inherit">
            {cancelText}
          </Button>
        )}
        {onConfirm && (
          <Button onClick={handleConfirm} variant="contained" color="primary">
            {confirmText}
          </Button>
        )}
      </DialogActions>
    </Dialog>
  );
};

Modal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  title: PropTypes.string,
  content: PropTypes.string,
  onConfirm: PropTypes.func,
  confirmText: PropTypes.string,
  cancelText: PropTypes.string,
  showCancel: PropTypes.bool,
  maxWidth: PropTypes.string,
  fullWidth: PropTypes.bool,
  children: PropTypes.node,
};

export default Modal;
