import React from 'react';
import { Box, Typography, Container } from '@mui/material';
import PropTypes from 'prop-types';

/**
 * Footer component displayed at the bottom of pages.
 * @param {Object} props - Component props
 * @returns {JSX.Element} Footer
 */
const Footer = ({ sx = {} }) => {
  const currentYear = new Date().getFullYear();

  return (
    <Box
      component="footer"
      sx={{
        py: 3,
        px: 2,
        mt: 'auto',
        backgroundColor: (theme) =>
          theme.palette.mode === 'light'
            ? theme.palette.grey[200]
            : theme.palette.grey[800],
        ...sx,
      }}
    >
      <Container maxWidth="lg">
        <Typography variant="body2" color="text.secondary" align="center">
          © {currentYear} Internship Placement Management System. All rights reserved.
        </Typography>
        <Typography variant="caption" color="text.secondary" align="center" display="block">
          Developed for SC2002 Assignment
        </Typography>
      </Container>
    </Box>
  );
};

Footer.propTypes = {
  sx: PropTypes.object,
};

export default Footer;
