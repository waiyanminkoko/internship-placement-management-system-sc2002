/**
 * Application constants and configuration values.
 */

export const APP_NAME = import.meta.env.VITE_APP_NAME || 'Internship Placement Management System';

export const USER_ROLES = {
  STUDENT: 'STUDENT',
  COMPANY_REPRESENTATIVE: 'COMPANY_REPRESENTATIVE',
  CAREER_CENTER_STAFF: 'CAREER_CENTER_STAFF',
};

export const INTERNSHIP_LEVELS = {
  BASIC: 'BASIC',
  INTERMEDIATE: 'INTERMEDIATE',
  ADVANCED: 'ADVANCED',
};

export const APPLICATION_STATUS = {
  PENDING: 'PENDING',
  SUCCESSFUL: 'SUCCESSFUL',
  REJECTED: 'REJECTED',
  WITHDRAWN: 'WITHDRAWN',
};

export const INTERNSHIP_STATUS = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  FILLED: 'FILLED',
};

export const APPROVAL_STATUS = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
};

export const STUDENT_YEARS = [1, 2, 3, 4];

export const MAJORS = [
  'Computer Science',
  'Computer Engineering',
  'Data Science & AI',
  'Information Engineering & Media',
  'Information Systems',
  'Data Science',
  'Business Analytics',
  'Electrical Engineering',
  'Mechanical Engineering',
  'Aerospace Engineering',
  'Business Administration',
  'Accounting',
  'Economics',
];

export const MAX_CONCURRENT_APPLICATIONS = 3;
export const MAX_INTERNSHIPS_PER_REPRESENTATIVE = 5;

export const SORT_OPTIONS = {
  TITLE_ASC: { value: 'title', label: 'Title (A-Z)' },
  TITLE_DESC: { value: '-title', label: 'Title (Z-A)' },
  DATE_ASC: { value: 'openingDate', label: 'Opening Date (Earliest)' },
  DATE_DESC: { value: '-openingDate', label: 'Opening Date (Latest)' },
  LEVEL: { value: 'level', label: 'Level' },
};

export const FILTER_STORAGE_KEY = 'internship_filters';

export default {
  APP_NAME,
  USER_ROLES,
  INTERNSHIP_LEVELS,
  APPLICATION_STATUS,
  INTERNSHIP_STATUS,
  APPROVAL_STATUS,
  STUDENT_YEARS,
  MAJORS,
  MAX_CONCURRENT_APPLICATIONS,
  MAX_INTERNSHIPS_PER_REPRESENTATIVE,
  SORT_OPTIONS,
  FILTER_STORAGE_KEY,
};
