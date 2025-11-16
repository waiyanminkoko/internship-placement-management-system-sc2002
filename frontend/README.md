# Internship Placement Management System - Frontend

Modern React-based web application for the Internship Placement Management System with complete infrastructure.

## 🚀 Quick Start

```powershell
npm install  # Install dependencies (1370 packages)
npm start    # Start on http://localhost:5173
```

**Test Credentials:**
- Student: `U2310001A` / `password`
- Company Rep: `charlielim@gmail.com` / `password`
- Staff: `tan002` / `password`

## 📦 Technology Stack

### Core Framework
- **React**: 18.2.0 - UI library
- **React DOM**: 18.2.0 - React rendering for web
- **Vite**: 6.3.6 - Fast build tool and dev server

### UI Components & Styling
- **Material-UI**: 5.14.20 - Component library
- **@mui/icons-material**: 5.14.19 - Material Design icons
- **@emotion/react**: 11.11.1 - CSS-in-JS library
- **@emotion/styled**: 11.11.0 - Styled components

### Routing & State
- **React Router DOM**: 6.20.0 - Declarative routing

### Forms & Data
- **React Hook Form**: 7.48.2 - Form management
- **Axios**: 1.6.2 - HTTP client

### Utilities
- **React Toastify**: 9.1.3 - Notifications
- **date-fns**: 2.30.0 - Date formatting
- **prop-types**: 15.8.0 - Runtime type checking

### Development Tools
- **@vitejs/plugin-react**: 4.7.0 - Vite React plugin
- **Vitest**: 4.0.8 - Unit testing framework
- **@types/react**: 18.3.26 - TypeScript definitions
- **@types/react-dom**: 18.3.7 - TypeScript definitions

## Project Structure

```
frontend/
├── public/
│   └── images/            # Public image assets
├── src/
│   ├── components/        # Reusable components
│   │   ├── auth/          # Authentication components (RegisterRepresentative)
│   │   ├── common/        # Common UI components (Navbar, Sidebar, Footer, etc.)
│   │   ├── representative/ # Company rep components (Applications, Internships, etc.)
│   │   ├── staff/         # Staff-specific components (Reports, Approvals, etc.)
│   │   ├── student/       # Student-specific components (Applications, Placements, etc.)
│   │   └── PrivateRoute.js
│   ├── pages/             # Page components
│   │   ├── Login.js
│   │   ├── StudentDashboard.js
│   │   ├── RepresentativeDashboard.js
│   │   └── StaffDashboard.js
│   ├── services/          # API services
│   │   ├── api.js
│   │   ├── authService.js
│   │   ├── studentService.js
│   │   ├── representativeService.js
│   │   └── staffService.js
│   ├── context/           # React Context
│   │   └── AuthContext.js
│   ├── hooks/             # Custom React hooks
│   │   ├── useAuth.js
│   │   ├── useFilter.js
│   │   └── useNotification.js
│   ├── utils/             # Utility functions
│   │   ├── constants.js
│   │   ├── formatters.js
│   │   ├── helpers.js
│   │   └── validation.js
│   ├── App.js             # Main app component
│   ├── index.js           # Entry point
│   ├── index.css          # Global styles
│   └── theme.js           # Material-UI theme configuration
├── build/                 # Production build output
├── package.json           # Dependencies
├── vite.config.js         # Vite configuration
└── .env                   # Environment variables
```

## Available Scripts

### `npm start`

Runs the app in development mode on port 5173.
Open [http://localhost:5173](http://localhost:5173) to view it in your browser.

The page will reload when you make changes.

### `npm run build`

Builds the app for production to the `build` folder.
It correctly bundles React in production mode and optimizes the build for the best performance.

### `npm test`

Launches the test runner in interactive watch mode.

## Environment Variables

Create a `.env` file in the frontend directory:

```env
VITE_API_BASE_URL=http://localhost:6060/api
```

## Key Features

### Authentication
- Login for all user types
- Role-based routing
- Protected routes
- Session management

### User Roles
1. **Student**
   - Browse and filter internships
   - Submit applications
   - View application status
   - Accept placements
   - Request withdrawals
   - Change password

2. **Company Representative**
   - Create and manage internships
   - Review and approve/reject applications
   - View placements
   - Self-registration
   - Change password

3. **Career Center Staff**
   - Approve pending representatives
   - Approve pending internships
   - Create student and representative accounts
   - Process withdrawal requests
   - Generate reports
   - Change password

## API Integration

The frontend communicates with the backend API through Axios with organized service modules.

**Base URL**: `http://localhost:6060/api`

**Service Modules:**
- `authService.js` - Authentication (login, logout, password change)
- `studentService.js` - Student operations (applications, internships, placements)
- `representativeService.js` - Company rep operations (internships, application reviews)
- `staffService.js` - Staff operations (approvals, reports, account creation)
- `api.js` - Core API configuration and interceptors

Example API call:
```javascript
import { login } from './services/authService';
import { getInternships } from './services/studentService';

const response = await login({ userId, password });
const internships = await getInternships();
```

## Styling

Using Material-UI for consistent and professional UI components.

Example:
```javascript
import { Button, TextField, Container } from '@mui/material';
```

## State Management

Using React Context API for global state:
- **AuthContext**: Manages user authentication state

**Custom Hooks:**
- **useAuth**: Access authentication context
- **useFilter**: Handle filtering and searching logic
- **useNotification**: Manage toast notifications

## Routing

Using React Router for navigation:
- `/login` - Login page
- `/student/*` - Student dashboard
- `/representative/*` - Representative dashboard
- `/staff/*` - Staff dashboard

## Development Guidelines

1. **Component Structure**: Create reusable components in `src/components/` organized by role (auth, common, student, representative, staff)
2. **API Services**: Add new API endpoints in role-specific service files (`authService.js`, `studentService.js`, etc.)
3. **Custom Hooks**: Create reusable logic in `src/hooks/`
4. **Utilities**: Add helper functions in `src/utils/` (constants, formatters, helpers, validation)
5. **Styling**: Use Material-UI components and custom theme configuration in `theme.js`
6. **State**: Use Context for global state, custom hooks for shared logic, useState for local state
7. **Forms**: Use React Hook Form for form handling
8. **Notifications**: Use React Toastify via `useNotification` hook

## Building Additional Features

### Adding a New Page

1. Create component in `src/pages/`
2. Add route in `App.js`
3. Update navigation links in Sidebar/Navbar
4. Protect route with PrivateRoute if needed

### Adding API Endpoints

1. Define service function in appropriate service file (`studentService.js`, `representativeService.js`, `staffService.js`)
2. Use in components with async/await
3. Handle errors with try-catch and useNotification
4. Update API configuration in `api.js` if needed

### Adding a Custom Hook

1. Create hook file in `src/hooks/`
2. Export hook function (e.g., `useCustomLogic`)
3. Import and use in components

### Adding Utility Functions

1. Add to appropriate file in `src/utils/`:
   - `constants.js` - Application constants
   - `formatters.js` - Data formatting functions
   - `helpers.js` - General helper functions
   - `validation.js` - Form validation logic

### Adding Context

1. Create context file in `src/context/`
2. Wrap component tree in Provider
3. Use custom hook or `useContext` to access

## Common Issues

### CORS Errors
- Ensure backend is running on port 6060
- Check CORS configuration in backend WebConfig.java

### API Connection Failed
- Verify backend is running on port 6060
- Check `.env` file has correct `VITE_API_BASE_URL=http://localhost:6060/api`
- Ensure environment variable starts with `VITE_` prefix

### Build Errors
- Delete `node_modules` and `package-lock.json`
- Run `npm install` again
- Clear Vite cache: `npm run build -- --force`

### Hot Reload Not Working
- Restart Vite dev server
- Check file watcher limits on Linux/Mac
- Ensure files are saved properly

## Learn More

### Core Technologies
- [React Documentation](https://reactjs.org/)
- [Vite Documentation](https://vitejs.dev/)
- [React Router](https://reactrouter.com/)

### UI & Styling
- [Material-UI](https://mui.com/)
- [Material Icons](https://mui.com/material-ui/material-icons/)
- [Emotion](https://emotion.sh/)

### Libraries & Tools
- [Axios](https://axios-http.com/)
- [React Hook Form](https://react-hook-form.com/)
- [React Toastify](https://fkhadra.github.io/react-toastify/)
- [date-fns](https://date-fns.org/)

---

**Last Updated**: November 16, 2025
