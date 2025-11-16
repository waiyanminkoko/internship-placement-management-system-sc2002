import React from 'react';
import { Box, CircularProgress, Typography } from '@mui/material';
import PropTypes from 'prop-types';

/**
 * Loading spinner component with optional message.
 * @param {Object} props - Component props
 * @param {string} props.message - Optional loading message
 * @param {string} props.size - Spinner size (default: 40)
 * @returns {JSX.Element} Loading spinner
 */
const LoadingSpinner = ({ message = 'Loading...', size = 40 }) => {
  return (
    <Box
      display="flex"
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      minHeight="200px"
      gap={2}
    >
      <CircularProgress size={size} />
      {message && (
        <Typography variant="body2" color="textSecondary">
          {message}
        </Typography>
      )}
    </Box>
  );
};

LoadingSpinner.propTypes = {
  message: PropTypes.string,
  size: PropTypes.number,
};

export default LoadingSpinner;
