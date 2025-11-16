import React from 'react';
import { Card, CardContent, Box, Typography, Chip } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';

/**
 * Modern statistics card component for displaying metrics.
 * Inspired by modern dashboard designs with gradient accents.
 * 
 * @param {Object} props - Component props
 * @param {string} props.title - Card title
 * @param {string|number} props.value - Main metric value
 * @param {string} props.subtitle - Subtitle/description text
 * @param {string} props.change - Percentage change (e.g., "+91%")
 * @param {string} props.changeValue - Change value (e.g., "+$201,687")
 * @param {boolean} props.isPositive - Whether change is positive
 * @param {string} props.icon - Icon component
 * @param {string} props.gradient - Gradient colors for accent
 * @returns {JSX.Element} Stat card
 */
const StatCard = ({
  title,
  value,
  subtitle,
  change,
  changeValue,
  isPositive = true,
  icon,
  gradient = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
}) => {
  return (
    <Card
      sx={{
        height: '100%',
        position: 'relative',
        overflow: 'hidden',
        transition: 'all 0.3s ease',
        '&:hover': {
          transform: 'translateY(-4px)',
          boxShadow: '0 8px 24px rgba(0,0,0,0.15)',
        },
      }}
    >
      {/* Gradient accent bar */}
      <Box
        sx={{
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          height: 4,
          background: gradient,
        }}
      />

      <CardContent sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
          <Box>
            <Typography
              variant="body2"
              color="text.secondary"
              sx={{
                fontSize: '0.875rem',
                fontWeight: 600,
                textTransform: 'uppercase',
                letterSpacing: 0.5,
                mb: 1,
              }}
            >
              {title}
            </Typography>
            <Typography
              variant="h4"
              sx={{
                fontWeight: 700,
                fontSize: '2rem',
                mb: 0.5,
                background: gradient,
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
                backgroundClip: 'text',
              }}
            >
              {value}
            </Typography>
          </Box>
          {icon && (
            <Box
              sx={{
                width: 56,
                height: 56,
                borderRadius: 3,
                background: gradient,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: '#fff',
                boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
              }}
            >
              {icon}
            </Box>
          )}
        </Box>

        {subtitle && (
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ fontSize: '0.85rem', mb: 2 }}
          >
            {subtitle}
          </Typography>
        )}

        {(change || changeValue) && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, flexWrap: 'wrap' }}>
            {change && (
              <Chip
                icon={isPositive ? <TrendingUpIcon /> : <TrendingDownIcon />}
                label={change}
                size="small"
                sx={{
                  bgcolor: isPositive ? 'success.50' : 'error.50',
                  color: isPositive ? 'success.main' : 'error.main',
                  fontWeight: 700,
                  fontSize: '0.8rem',
                  border: `1px solid ${isPositive ? 'rgba(16, 185, 129, 0.2)' : 'rgba(239, 68, 68, 0.2)'}`,
                  '& .MuiChip-icon': {
                    color: 'inherit',
                  },
                }}
              />
            )}
            {changeValue && (
              <Typography
                variant="body2"
                sx={{
                  fontSize: '0.85rem',
                  fontWeight: 600,
                  color: isPositive ? 'success.main' : 'error.main',
                }}
              >
                {changeValue}
              </Typography>
            )}
          </Box>
        )}
      </CardContent>
    </Card>
  );
};

export default StatCard;
