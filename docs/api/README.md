# API Documentation

## 🌐 Overview

This document contains comprehensive REST API documentation for the Internship Placement Management System backend. The API follows RESTful principles and returns standardized JSON responses.

**Current Version:** 2.0.0  
**Last Updated:** November 16, 2025  
**Status:** ✅ Fully Implemented and Operational

## 📂 Contents

- [Base Configuration](#-base-configuration)
- [Authentication Endpoints](#-authentication-endpoints)
- [Student Endpoints](#-student-endpoints)
- [Company Representative Endpoints](#-company-representative-endpoints)
- [Staff Endpoints](#-staff-endpoints)
- [Health Check Endpoints](#-health-check-endpoints)
- [Request/Response Format](#-requestresponse-format)
- [Error Handling](#-error-handling)
- [Complete Workflow Examples](#-complete-workflow-examples)
- [Testing](#-testing)

## � Base Configuration

### Base URL
```
http://localhost:6060/api
```

### CORS Settings
- **Allowed Origins:** `http://localhost:5173`, `http://localhost:3000`
- **Allowed Methods:** GET, POST, PUT, DELETE, OPTIONS
- **Allowed Headers:** All (`*`)
- **Credentials:** Enabled

---

## 🔐 Authentication Endpoints

Base Path: `/api/auth`

### 1. Register Company Representative
**POST** `/api/auth/register-representative`

Register a new company representative account (pending staff approval).

**Request Body:**
```json
{
  "representativeId": "charlielim@gmail.com",
  "name": "Charlie Lim",
  "email": "charlielim@gmail.com",
  "password": "password",
  "companyName": "Microsoft",
  "industry": "Technology",
  "position": "Sales Executive"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Registration submitted successfully. Please wait for staff approval.",
  "timestamp": "2025-11-12T14:00:00"
}
```

**Error Responses:**
- `400 Bad Request` - Invalid input (missing required fields)
- `409 Conflict` - Representative with this email already exists
- `500 Internal Server Error` - Unexpected error

**Required Fields:**
- `representativeId` - Unique identifier (typically email)
- `name` - Full name of the representative
- `email` - Valid email address
- `password` - Account password
- `companyName` - Name of the company
- `industry` - Industry sector (e.g., Technology, Finance, Healthcare)
- `position` - Job position/title (optional)

---

### 2. Login
**POST** `/api/auth/login`

Authenticates a user with their credentials.

**Request Body:**
```json
{
  "userId": "U2310001A",
  "password": "password"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "userId": "U2310001A",
    "name": "Tan Wei Ling",
    "role": "STUDENT",
    "email": "tan001@e.ntu.edu.sg",
    "token": "550e8400-e29b-41d4-a716-446655440000"
  },
  "message": "Login successful",
  "timestamp": "2025-11-10T14:30:00"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid credentials
- `404 Not Found` - User not found
- `400 Bad Request` - Invalid input

---

### 3. Logout
**POST** `/api/auth/logout?userId={userId}`

Logs out the current user and terminates their session.

**Query Parameters:**
- `userId` (required) - The user ID to logout

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Logout successful",
  "timestamp": "2025-11-10T14:35:00"
}
```

---

### 4. Change Password
**POST** `/api/auth/change-password?userId={userId}`

Changes a user's password.

**Query Parameters:**
- `userId` (required) - The user ID whose password to change

**Request Body:**
```json
{
  "oldPassword": "currentPassword",
  "newPassword": "newPassword123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Password changed successfully",
  "timestamp": "2025-11-10T14:40:00"
}
```

**Error Responses:**
- `401 Unauthorized` - Old password is incorrect
- `400 Bad Request` - New password is invalid

---

### 5. Check Authentication Status
**GET** `/api/auth/status?userId={userId}`

Checks if a user is currently authenticated.

**Query Parameters:**
- `userId` (required) - The user ID to check

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": true,
  "message": "User is authenticated",
  "timestamp": "2025-11-10T14:45:00"
}
```

---

## 👨‍🎓 Student Endpoints

Base Path: `/api/students`

### 1. Browse Available Internships
**GET** `/api/students/internships?studentId={studentId}`

Get all available internship opportunities for a student.

**Query Parameters:**
- `studentId` (required) - The student's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "internshipId": "INT-20251110-123456-a1b2c3d4",
      "title": "Software Engineer Intern",
      "description": "Full-stack development internship",
      "level": "INTERMEDIATE",
      "preferredMajor": "CSC",
      "openingDate": "2025-11-01",
      "closingDate": "2025-12-31",
      "slots": 5,
      "availableSlots": 3,
      "status": "APPROVED",
      "companyName": "TechCorp Pte Ltd"
    }
  ],
  "message": "Available internships retrieved",
  "timestamp": "2025-11-10T15:00:00"
}
```

---

### 2. View My Applications
**GET** `/api/students/applications?studentId={studentId}`

Get all applications submitted by the student.

**Query Parameters:**
- `studentId` (required) - The student's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "applicationId": "APP-20251110-001",
      "internshipId": "INT-20251110-123456-a1b2c3d4",
      "studentId": "U2345123F",
      "status": "PENDING",
      "submissionDate": "2025-11-10",
      "internshipTitle": "Software Engineer Intern",
      "companyName": "TechCorp Pte Ltd"
    }
  ],
  "message": "Applications retrieved",
  "timestamp": "2025-11-10T15:05:00"
}
```

---

### 3. Apply for Internship
**POST** `/api/students/applications?studentId={studentId}`

Submit an application for an internship opportunity.

**Query Parameters:**
- `studentId` (required) - The student's ID

**Request Body:**
```json
{
  "internshipId": "INT-20251110-123456-a1b2c3d4"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "applicationId": "APP-20251110-001",
    "internshipId": "INT-20251110-123456-a1b2c3d4",
    "studentId": "U2345123F",
    "status": "PENDING",
    "submissionDate": "2025-11-10"
  },
  "message": "Application submitted successfully",
  "timestamp": "2025-11-10T15:10:00"
}
```

**Business Rule Errors:**
- Maximum 3 active applications per student
- Cannot apply to same internship twice
- Must meet level requirements

---

### 4. Accept Placement
**POST** `/api/students/applications/{applicationId}/accept?studentId={studentId}`

Accept a placement offer.

**Path Parameters:**
- `applicationId` (required) - The application ID to accept

**Query Parameters:**
- `studentId` (required) - The student's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Placement accepted successfully",
  "timestamp": "2025-11-10T15:15:00"
}
```

---

### 5. Request Withdrawal
**POST** `/api/students/applications/withdraw?studentId={studentId}`

Request withdrawal from an accepted application.

**Query Parameters:**
- `studentId` (required) - The student's ID

**Request Body:**
```json
{
  "applicationId": "APP-20251110-001",
  "reason": "Personal circumstances"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "withdrawalId": "WD-20251110-001",
    "applicationId": "APP-20251110-001",
    "studentId": "U2345123F",
    "reason": "Personal circumstances",
    "status": "PENDING",
    "requestDate": "2025-11-10"
  },
  "message": "Withdrawal request submitted successfully",
  "timestamp": "2025-11-10T15:20:00"
}
```

---

### 6. Get Student Profile
**GET** `/api/students/profile?studentId={studentId}`

Get student profile information.

**Query Parameters:**
- `studentId` (required) - The student's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "userId": "U2310001A",
    "name": "Tan Wei Ling",
    "email": "tan001@e.ntu.edu.sg",
    "major": "Computer Science",
    "year": 2,
    "role": "STUDENT"
  },
  "message": "Profile retrieved",
  "timestamp": "2025-11-10T15:25:00"
}
```

---

### 7. Get Active Application Count
**GET** `/api/students/applications/count?studentId={studentId}`

Get the count of active applications for a student.

**Query Parameters:**
- `studentId` (required) - The student's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": 2,
  "message": "Active application count",
  "timestamp": "2025-11-10T15:30:00"
}
```

---

## 🏢 Company Representative Endpoints

Base Path: `/api/representatives`

### 1. Create Internship Opportunity
**POST** `/api/representatives/internships?repId={repId}`

Create a new internship opportunity.

**Query Parameters:**
- `repId` (required) - The representative's ID (email)

**Request Body:**
```json
{
  "title": "Software Engineer Intern",
  "description": "Full-stack development internship with modern tech stack",
  "level": "INTERMEDIATE",
  "preferredMajor": "CSC",
  "openingDate": "2025-11-01",
  "closingDate": "2025-12-31",
  "startDate": "2026-01-15",
  "endDate": "2026-06-15",
  "slots": 5
}
```

**Field Descriptions:**
- `title` - Title of the internship position (required)
- `description` - Detailed description of the role and responsibilities (required)
- `level` - Experience level: `BASIC`, `INTERMEDIATE`, or `ADVANCED` (required)
  - `BASIC`: Entry-level internships suitable for Year 1-4 students
  - `INTERMEDIATE`: Mid-level internships for Year 3-4 students only
  - `ADVANCED`: Advanced internships for Year 3-4 students only
- `preferredMajor` - Preferred student major code (e.g., CSC, EEE, MAE) (optional)
- `openingDate` - Date when applications open (YYYY-MM-DD format) (required)
- `closingDate` - Date when applications close (YYYY-MM-DD format) (required)
- `startDate` - Internship start date (YYYY-MM-DD format) (required)
- `endDate` - Internship end date (YYYY-MM-DD format) (required)
- `slots` - Number of available positions (1-10) (required)

**Success Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "internshipId": "INT-20251112-123456-a1b2c3d4",
    "title": "Software Engineer Intern",
    "description": "Full-stack development internship with modern tech stack",
    "level": "INTERMEDIATE",
    "preferredMajor": "CSC",
    "openingDate": "2025-11-01",
    "closingDate": "2025-12-31",
    "startDate": "2026-01-15",
    "endDate": "2026-06-15",
    "slots": 5,
    "status": "PENDING",
    "representativeId": "alice@techcorp.com",
    "companyName": "TechCorp Pte Ltd",
    "visible": true
  },
  "message": "Internship opportunity created successfully",
  "timestamp": "2025-11-12T15:35:00"
}
```

**Notes:**
- New internships are created with status `PENDING` and require staff approval
- The internship is visible by default but can be toggled later
- `startDate` must be after `openingDate`
- `endDate` must be after `startDate`
- `closingDate` must be before `startDate`

---

### 2. View My Internships
**GET** `/api/representatives/internships?repId={repId}`

Get all internship opportunities created by the representative.

**Query Parameters:**
- `repId` (required) - The representative's ID (email)

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "internshipId": "INT-20251110-123456-a1b2c3d4",
      "title": "Software Engineer Intern",
      "status": "APPROVED",
      "slots": 5,
      "availableSlots": 3,
      "applicationCount": 8
    }
  ],
  "message": "Internships retrieved",
  "timestamp": "2025-11-10T15:40:00"
}
```

---

### 3. Update Internship (Pending Only)
**PUT** `/api/representatives/internships/{opportunityId}?repId={repId}`

Update an existing internship opportunity (only if status is PENDING).

**Path Parameters:**
- `opportunityId` (required) - The internship opportunity ID

**Query Parameters:**
- `repId` (required) - The representative's ID (email)

**Request Body:**
```json
{
  "title": "Senior Software Engineer Intern",
  "description": "Updated description",
  "level": "ADVANCED",
  "preferredMajor": "CSC",
  "openingDate": "2025-11-01",
  "closingDate": "2025-12-31",
  "startDate": "2026-01-15",
  "endDate": "2026-06-15",
  "slots": 3
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Internship updated successfully",
  "timestamp": "2025-11-10T15:45:00"
}
```

**Note:** Only PENDING internships can be updated.

---

### 4. View Applications for Internship
**GET** `/api/representatives/internships/{opportunityId}/applications?repId={repId}`

Get all applications for a specific internship opportunity.

**Path Parameters:**
- `opportunityId` (required) - The internship opportunity ID

**Query Parameters:**
- `repId` (required) - The representative's ID (email)

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "applicationId": "APP-20251110-001",
      "studentId": "U2345123F",
      "studentName": "John Tan",
      "major": "CSC",
      "year": 2,
      "cgpa": 4.5,
      "status": "PENDING",
      "submissionDate": "2025-11-10"
    }
  ],
  "message": "Applications retrieved",
  "timestamp": "2025-11-10T15:50:00"
}
```

---

### 5. Approve/Reject Application
**POST** `/api/representatives/applications/{applicationId}/approve?repId={repId}`

Approve or reject a student application.

**Path Parameters:**
- `applicationId` (required) - The application ID

**Query Parameters:**
- `repId` (required) - The representative's ID (email)

**Request Body:**
```json
{
  "approve": true
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Application approved successfully",
  "timestamp": "2025-11-10T15:55:00"
}
```

---

### 6. Reject Application
**POST** `/api/representatives/applications/{applicationId}/reject?repId={repId}`

Reject a student application.

**Path Parameters:**
- `applicationId` (required) - The application ID

**Query Parameters:**
- `repId` (required) - The representative's ID (email)

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Application rejected successfully",
  "timestamp": "2025-11-10T16:00:00"
}
```

---

### 7. Toggle Internship Visibility
**PATCH** `/api/representatives/internships/{opportunityId}/visibility?repId={repId}&visibility={boolean}`

Toggle the visibility of an internship opportunity.

**Path Parameters:**
- `opportunityId` (required) - The internship opportunity ID

**Query Parameters:**
- `repId` (required) - The representative's ID (email)
- `visibility` (required) - Boolean value (true/false)

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Visibility updated successfully",
  "timestamp": "2025-11-10T16:05:00"
}
```

---

## 👔 Staff Endpoints

Base Path: `/api/staff`

### 1. View Pending Representatives
**GET** `/api/staff/representatives/pending?staffId={staffId}`

Get all company representatives awaiting authorization.

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "userId": "haku@gmail.com",
      "name": "Haku",
      "email": "haku@gmail.com",
      "companyName": "Unknown",
      "authorized": false,
      "registrationDate": "2025-11-09"
    }
  ],
  "message": "Pending representatives retrieved",
  "timestamp": "2025-11-10T16:10:00"
}
```

---

### 2. Authorize/Reject Representative
**POST** `/api/staff/representatives/{representativeId}/authorize?staffId={staffId}`

Authorize or reject a company representative.

**Path Parameters:**
- `representativeId` (required) - The representative's ID (email)

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Request Body:**
```json
{
  "approve": true
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Representative authorized successfully",
  "timestamp": "2025-11-10T16:15:00"
}
```

---

### 3. View Pending Internships
**GET** `/api/staff/internships/pending?staffId={staffId}`

Get all internship opportunities awaiting approval.

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "internshipId": "INT-20251110-123456-a1b2c3d4",
      "title": "Software Engineer Intern",
      "companyName": "TechCorp Pte Ltd",
      "level": "INTERMEDIATE",
      "slots": 5,
      "status": "PENDING",
      "submittedDate": "2025-11-10"
    }
  ],
  "message": "Pending opportunities retrieved",
  "timestamp": "2025-11-10T16:20:00"
}
```

---

### 4. Approve/Reject Internship
**POST** `/api/staff/internships/{opportunityId}/approve?staffId={staffId}`

Approve or reject an internship opportunity.

**Path Parameters:**
- `opportunityId` (required) - The internship opportunity ID

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Request Body:**
```json
{
  "approve": true
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Opportunity approved successfully",
  "timestamp": "2025-11-10T16:25:00"
}
```

---

### 5. View Pending Withdrawals
**GET** `/api/staff/withdrawals/pending?staffId={staffId}`

Get all withdrawal requests awaiting processing.

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "withdrawalId": "WD-20251110-001",
      "applicationId": "APP-20251110-001",
      "studentId": "U2345123F",
      "studentName": "John Tan",
      "reason": "Personal circumstances",
      "status": "PENDING",
      "requestDate": "2025-11-10"
    }
  ],
  "message": "Pending withdrawals retrieved",
  "timestamp": "2025-11-10T16:30:00"
}
```

---

### 6. Process Withdrawal Request
**POST** `/api/staff/withdrawals/{withdrawalId}/process?staffId={staffId}`

Approve or reject a withdrawal request.

**Path Parameters:**
- `withdrawalId` (required) - The withdrawal request ID

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Request Body:**
```json
{
  "approve": true
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "Withdrawal approved successfully",
  "timestamp": "2025-11-10T16:35:00"
}
```

---

### 7. Generate Report
**GET** `/api/staff/reports?staffId={staffId}&major={major}&year={year}&companyName={company}&startDate={date}&endDate={date}`

Generate a report with optional filtering.

**Query Parameters:**
- `staffId` (required) - The staff member's ID
- `major` (optional) - Filter by major (CSC, EEE, MAE, etc.)
- `year` (optional) - Filter by student year
- `companyName` (optional) - Filter by company name
- `startDate` (optional) - Filter by start date (YYYY-MM-DD)
- `endDate` (optional) - Filter by end date (YYYY-MM-DD)

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalStudents": 150,
    "totalInternships": 45,
    "totalApplications": 280,
    "placedStudents": 120,
    "pendingApplications": 35,
    "byMajor": {
      "CSC": 80,
      "EEE": 40,
      "MAE": 30
    },
    "byCompany": {
      "TechCorp Pte Ltd": 25,
      "DataSoft Inc": 20
    },
    "generatedDate": "2025-11-10T16:40:00"
  },
  "message": "Report generated successfully",
  "timestamp": "2025-11-10T16:40:00"
}
```

---

### 8. Create Student Account
**POST** `/api/staff/students/create?staffId={staffId}`

Create a new student account.

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Request Body:**
```json
{
  "userId": "U2345678K",
  "name": "Jane Doe",
  "email": "jane.doe@e.ntu.edu.sg",
  "password": "password",
  "major": "CSC",
  "year": 2,
  "cgpa": 4.2
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "userId": "U2345678K",
    "name": "Jane Doe",
    "email": "jane.doe@e.ntu.edu.sg",
    "role": "STUDENT"
  },
  "message": "Student account created successfully",
  "timestamp": "2025-11-10T16:45:00"
}
```

---

### 9. Create Company Representative Account
**POST** `/api/staff/representatives/create?staffId={staffId}`

Create a new company representative account.

**Query Parameters:**
- `staffId` (required) - The staff member's ID

**Request Body:**
```json
{
  "userId": "bob@datasoft.com",
  "name": "Bob Lee",
  "email": "bob@datasoft.com",
  "password": "password",
  "companyName": "DataSoft Inc"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "userId": "bob@datasoft.com",
    "name": "Bob Lee",
    "email": "bob@datasoft.com",
    "companyName": "DataSoft Inc",
    "role": "COMPANY_REPRESENTATIVE"
  },
  "message": "Company representative account created successfully",
  "timestamp": "2025-11-10T16:50:00"
}
```

---

## 💚 Health Check Endpoints

Base Path: `/api`

### 1. Health Check
**GET** `/api/health`

Check if the server is running and healthy.

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "status": "UP",
    "timestamp": "2025-11-10T17:00:00",
    "application": "Internship Placement Management System",
    "version": "1.0.0"
  },
  "message": "Server is running",
  "timestamp": "2025-11-10T17:00:00"
}
```

---

### 2. Root/Welcome
**GET** `/api`

Get API information and welcome message.

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "application": "Internship Placement Management System",
    "version": "1.0.0",
    "description": "Web-based platform for managing internship opportunities and applications",
    "apiBasePath": "/api"
  },
  "message": "Welcome to the Internship Placement Management System",
  "timestamp": "2025-11-10T17:05:00"
}
```

---

## 📋 Request/Response Format

All API endpoints follow a standardized JSON response format powered by the `ApiResponse<T>` wrapper class.

### Standard Success Response Structure
```json
{
  "success": true,
  "data": {
    // Response data (type varies by endpoint)
  },
  "message": "Operation completed successfully",
  "timestamp": "2025-11-10T14:30:00"
}
```

### Standard Error Response Structure
```json
{
  "success": false,
  "data": null,
  "message": "Error description with details",
  "timestamp": "2025-11-10T14:30:00"
}
```

### Response Field Descriptions

| Field | Type | Description |
|-------|------|-------------|
| `success` | Boolean | Indicates if the operation was successful |
| `data` | Generic | The response payload (null for errors) |
| `message` | String | Human-readable message describing the result |
| `timestamp` | LocalDateTime | ISO 8601 timestamp when response was created |

---

## 🚨 Error Handling

### HTTP Status Codes

| Status Code | Meaning | Usage |
|-------------|---------|-------|
| **200 OK** | Success | Request completed successfully |
| **201 Created** | Created | Resource created successfully |
| **400 Bad Request** | Client Error | Invalid input or business rule violation |
| **401 Unauthorized** | Auth Error | Invalid credentials or authentication required |
| **403 Forbidden** | Permission Error | User lacks required permissions |
| **404 Not Found** | Not Found | Requested resource does not exist |
| **500 Internal Server Error** | Server Error | Unexpected server-side error |

### Common Error Examples

**Invalid Input:**
```json
{
  "success": false,
  "data": null,
  "message": "User ID cannot be empty",
  "timestamp": "2025-11-10T14:30:00"
}
```

**Authentication Error:**
```json
{
  "success": false,
  "data": null,
  "message": "Invalid credentials",
  "timestamp": "2025-11-10T14:30:00"
}
```

**Resource Not Found:**
```json
{
  "success": false,
  "data": null,
  "message": "Student not found",
  "timestamp": "2025-11-10T14:30:00"
}
```

**Business Rule Violation:**
```json
{
  "success": false,
  "data": null,
  "message": "Maximum applications limit reached (3)",
  "timestamp": "2025-11-10T14:30:00"
}
```

### Exception Types

The backend throws the following custom exceptions:

- **`InvalidInputException`** → 400 Bad Request
- **`UnauthorizedException`** → 401 Unauthorized
- **`ResourceNotFoundException`** → 404 Not Found
- **`BusinessRuleViolationException`** → 400 Bad Request
- **`MaxApplicationsReachedException`** → 400 Bad Request
- **`DuplicateApplicationException`** → 400 Bad Request

---

## 📝 Complete Workflow Examples

### Example 1: Student Applies for Internship

#### Step 1: Login
```bash
POST http://localhost:6060/api/auth/login
Content-Type: application/json

{
  "userId": "U2310001A",
  "password": "password"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "userId": "U2310001A",
    "name": "Tan Wei Ling",
    "role": "STUDENT",
    "email": "tan001@e.ntu.edu.sg",
    "token": "550e8400-e29b-41d4-a716-446655440000"
  },
  "message": "Login successful",
  "timestamp": "2025-11-10T14:00:00"
}
```

#### Step 2: Browse Available Internships
```bash
GET http://localhost:6060/api/students/internships?studentId=U2310001A
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "internshipId": "INT-20251110-123456-a1b2c3d4",
      "title": "Software Engineer Intern",
      "companyName": "TechCorp Pte Ltd",
      "level": "INTERMEDIATE",
      "slots": 5,
      "availableSlots": 3
    }
  ],
  "message": "Available internships retrieved",
  "timestamp": "2025-11-10T14:05:00"
}
```

#### Step 3: Submit Application
```bash
POST http://localhost:6060/api/students/applications?studentId=U2310001A
Content-Type: application/json

{
  "internshipId": "INT-20251110-123456-a1b2c3d4"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "applicationId": "APP-20251110-001",
    "status": "PENDING",
    "submissionDate": "2025-11-10"
  },
  "message": "Application submitted successfully",
  "timestamp": "2025-11-10T14:10:00"
}
```

---

### Example 2: Company Creates and Manages Internship

#### Step 1: Login as Company Representative
```bash
POST http://localhost:6060/api/auth/login
Content-Type: application/json

{
  "userId": "charlielim@gmail.com",
  "password": "password"
}
```

#### Step 2: Create Internship Opportunity
```bash
POST http://localhost:6060/api/representatives/internships?repId=charlielim@gmail.com
Content-Type: application/json

{
  "title": "Software Engineer Intern",
  "description": "Full-stack development internship with React and Spring Boot",
  "level": "INTERMEDIATE",
  "preferredMajor": "CSC",
  "openingDate": "2025-11-15",
  "closingDate": "2025-12-31",
  "startDate": "2026-01-15",
  "endDate": "2026-06-15",
  "slots": 5
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "internshipId": "INT-20251110-123456-a1b2c3d4",
    "status": "PENDING",
    "title": "Software Engineer Intern"
  },
  "message": "Internship opportunity created successfully",
  "timestamp": "2025-11-10T14:15:00"
}
```

#### Step 3: View Applications (After Approval)
```bash
GET http://localhost:6060/api/representatives/internships/INT-20251110-123456-a1b2c3d4/applications?repId=charlielim@gmail.com
```

#### Step 4: Approve an Application
```bash
POST http://localhost:6060/api/representatives/applications/APP-20251110-001/approve?repId=charlielim@gmail.com
Content-Type: application/json

{
  "approve": true
}
```

---

### Example 3: Staff Approves Internship and Processes Withdrawal

#### Step 1: Login as Staff
```bash
POST http://localhost:6060/api/auth/login
Content-Type: application/json

{
  "userId": "tan002",
  "password": "password"
}
```

#### Step 2: View Pending Internships
```bash
GET http://localhost:6060/api/staff/internships/pending?staffId=tan002
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "internshipId": "INT-20251110-123456-a1b2c3d4",
      "title": "Software Engineer Intern",
      "companyName": "Microsoft",
      "status": "PENDING"
    }
  ],
  "message": "Pending opportunities retrieved",
  "timestamp": "2025-11-10T14:20:00"
}
```

#### Step 3: Approve Internship
```bash
POST http://localhost:6060/api/staff/internships/INT-20251110-123456-a1b2c3d4/approve?staffId=tan002
Content-Type: application/json

{
  "approve": true
}
```

#### Step 4: View Pending Withdrawals
```bash
GET http://localhost:6060/api/staff/withdrawals/pending?staffId=tan002
```

#### Step 5: Process Withdrawal Request
```bash
POST http://localhost:6060/api/staff/withdrawals/WD-20251110-001/process?staffId=tan002
Content-Type: application/json

{
  "approve": true
}
```

#### Step 6: Generate Report
```bash
GET http://localhost:6060/api/staff/reports?staffId=tan002&major=Computer Science&year=2
```

---

## 🔧 Testing

### Prerequisites
- Backend server running on port 6060
- Frontend (optional) running on port 5173

### Using cURL

#### Login Example
```bash
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"U2310001A\",\"password\":\"password\"}"
```

#### Get Available Internships
```bash
curl -X GET "http://localhost:6060/api/students/internships?studentId=U2310001A"
```

#### Submit Application
```bash
curl -X POST "http://localhost:6060/api/students/applications?studentId=U2310001A" \
  -H "Content-Type: application/json" \
  -d "{\"internshipId\":\"INT-20251110-123456-a1b2c3d4\"}"
```

#### Health Check
```bash
curl -X GET http://localhost:6060/api/health
```

---

### Using PowerShell (Windows)

#### Login Example
```powershell
$body = @{
    userId = "U2310001A"
    password = "password"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:6060/api/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

#### Get Internships
```powershell
Invoke-RestMethod -Uri "http://localhost:6060/api/students/internships?studentId=U2310001A" `
    -Method GET
```

---

### Using Postman

1. **Set Base URL**: Create environment variable `baseUrl = http://localhost:6060`
2. **Import Collection**: Use the endpoints documented above to create requests
3. **Test Workflow**:
   - Login → Save token from response
   - Use token (if implementing token-based auth in future)
   - Test CRUD operations

**Recommended Postman Tests:**
```javascript
// Test successful response
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has success field", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
});

pm.test("Response has data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.data).to.not.be.null;
});
```

---

### Using JavaScript/Fetch (Frontend Integration)

#### API Service Example
```javascript
const API_BASE_URL = 'http://localhost:6060/api';

// Login
async function login(userId, password) {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include', // Include cookies
    body: JSON.stringify({ userId, password })
  });
  
  if (!response.ok) {
    throw new Error('Login failed');
  }
  
  return await response.json();
}

// Get Available Internships
async function getInternships(studentId) {
  const response = await fetch(
    `${API_BASE_URL}/students/internships?studentId=${studentId}`,
    {
      method: 'GET',
      credentials: 'include'
    }
  );
  
  if (!response.ok) {
    throw new Error('Failed to fetch internships');
  }
  
  return await response.json();
}

// Submit Application
async function applyForInternship(studentId, internshipId) {
  const response = await fetch(
    `${API_BASE_URL}/students/applications?studentId=${studentId}`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ internshipId })
    }
  );
  
  if (!response.ok) {
    throw new Error('Application submission failed');
  }
  
  return await response.json();
}
```

---

### Using React Query (Recommended for React Frontend)

```javascript
import { useQuery, useMutation } from '@tanstack/react-query';

// Query: Get Internships
function useInternships(studentId) {
  return useQuery({
    queryKey: ['internships', studentId],
    queryFn: async () => {
      const response = await fetch(
        `http://localhost:6060/api/students/internships?studentId=${studentId}`
      );
      if (!response.ok) throw new Error('Failed to fetch');
      return response.json();
    }
  });
}

// Mutation: Apply for Internship
function useApplyInternship() {
  return useMutation({
    mutationFn: async ({ studentId, internshipId }) => {
      const response = await fetch(
        `http://localhost:6060/api/students/applications?studentId=${studentId}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ internshipId })
        }
      );
      if (!response.ok) throw new Error('Application failed');
      return response.json();
    }
  });
}
```

---

## 🔄 CORS Configuration

The API is configured to allow cross-origin requests from the frontend application.

**Configuration (from `application.properties`):**
```properties
cors.allowed.origins=http://localhost:5173,http://localhost:3000
cors.allowed.methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed.headers=*
cors.allow.credentials=true
```

**Implementation (WebConfig.java):**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

---

## 🔒 Security Considerations

### Current Implementation
- **Password Storage**: Plain text (for development only)
- **Session Management**: Server-side session with 30-minute timeout
- **Authentication**: Simple user ID + password validation
- **Token**: UUID-based token (returned on login)

### Recommended Production Enhancements
1. **Password Hashing**: Implement BCrypt or similar
2. **JWT Tokens**: Replace UUID tokens with JWT
3. **HTTPS**: Use SSL/TLS encryption
4. **Rate Limiting**: Prevent brute force attacks
5. **Input Validation**: Sanitize all user inputs
6. **CSRF Protection**: Enable Spring Security CSRF tokens
7. **SQL Injection Prevention**: Use parameterized queries (already using CSV storage)

---

## 📊 Business Rules & Constraints

### Student Constraints
- **Maximum Applications**: 3 active applications at a time
- **Eligibility Based on Year**:
  - Year 1-2 students: Can ONLY apply for BASIC level internships
  - Year 3-4 students: Can apply for all levels (BASIC, INTERMEDIATE, ADVANCED)
- **Cannot Apply**: To same internship twice
- **Cannot Apply**: After closing date or when internship is FILLED
- **Withdrawal**: Requires staff approval

### Company Representative Constraints
- **Registration**: New representatives require staff authorization before they can post internships
- **Maximum Internships**: 5 active internships per representative
- **Update Restrictions**: Can only update PENDING internships
- **Visibility Control**: Can toggle visibility of APPROVED internships only

### Staff Privileges
- Approve/reject internship opportunities
- Authorize/reject company representatives
- Process withdrawal requests
- Create student and representative accounts
- Generate comprehensive reports with filtering options

### Internship Constraints
- **Maximum Slots**: 10 slots per internship (MAX_SLOTS constant)
- **Status Flow**: PENDING → APPROVED → FILLED (or REJECTED)
- **Visibility**: Only APPROVED internships can be visible to students
- **Date Validation**:
  - Opening date must be before closing date
  - Start date must be after opening date
  - End date must be after start date
  - Closing date should be before start date
- **Auto-Fill**: Status automatically changes to FILLED when all slots are confirmed

---

## 📈 Data Models Overview

### Core Entities

#### User (Abstract)
- `userId` (String) - Primary key
- `name` (String)
- `email` (String)
- `password` (String)
- `role` (UserRole enum)

#### Student extends User
- `major` (Major enum)
- `year` (Integer)
- `cgpa` (Double)

#### CompanyRepresentative extends User
- `companyName` (String)
- `industry` (String) - Industry sector
- `position` (String) - Job position/title
- `authorized` (Boolean) - Authorization status (requires staff approval)

#### CareerCenterStaff extends User
- No additional fields

#### InternshipOpportunity
- `opportunityId` (String) - Primary key (also returned as `internshipId`)
- `title` (String) - Title of the internship position
- `description` (String) - Detailed description of the role
- `level` (InternshipLevel enum) - Experience level (BASIC, INTERMEDIATE, ADVANCED)
- `preferredMajor` (String) - Preferred student major (e.g., CSC, EEE) or "Any"
- `openingDate` (LocalDate) - Date when applications open
- `closingDate` (LocalDate) - Date when applications close
- `startDate` (LocalDate) - Internship start date
- `endDate` (LocalDate) - Internship end date
- `totalSlots` (Integer) - Total number of available positions (max 10)
- `filledSlots` (Integer) - Number of confirmed placements
- `status` (InternshipStatus enum) - Current status of the opportunity
- `representativeId` (String) - Foreign key to company representative
- `createdBy` (String) - Email of the representative who created it
- `companyRepEmail` (String) - Representative email for this opportunity
- `companyName` (String) - Associated company name
- `visible` (Boolean) - Visibility toggle (can be controlled by representative)

#### Application
- `applicationId` (String) - Primary key
- `studentId` (String) - Foreign key
- `internshipId` (String) - Foreign key
- `status` (ApplicationStatus enum)
- `submissionDate` (LocalDate)

#### WithdrawalRequest
- `withdrawalId` (String) - Primary key
- `applicationId` (String) - Foreign key
- `studentId` (String) - Foreign key
- `reason` (String)
- `status` (ApprovalStatus enum)
- `requestDate` (LocalDate)

### Enums

**UserRole**: STUDENT, COMPANY_REPRESENTATIVE, CAREER_CENTER_STAFF

**InternshipLevel**: BASIC, INTERMEDIATE, ADVANCED
- `BASIC`: Entry-level internships suitable for Year 1-4 students
- `INTERMEDIATE`: Mid-level internships for Year 3-4 students only
- `ADVANCED`: Advanced internships for Year 3-4 students only

**InternshipStatus**: PENDING, APPROVED, REJECTED, OPEN, CLOSED

**ApplicationStatus**: PENDING, APPROVED, REJECTED, ACCEPTED, WITHDRAWN

**ApprovalStatus**: PENDING, APPROVED, REJECTED

**Major**: CSC, EEE, MAE, CEE, MSE, ADM, SCSE, etc.

---

## 📊 API Versioning

**Current Version:** 2.0.0

**Version History:**
- **2.0.0** (2025-11-16): Added `startDate` and `endDate` fields to internship opportunities, added company representative registration endpoint, updated InternshipLevel from BEGINNER to BASIC
- **1.0.0** (2025-11-10): Initial release with all core endpoints

**Versioning Strategy:**
- Current approach: No version prefix in URL
- Future approach (if needed):
  ```
  /api/v1/...  - Version 1
  /api/v2/...  - Version 2
  ```

**Backward Compatibility:**
- All breaking changes will require version increment
- Deprecated endpoints will be marked and documented
- Minimum support period: 6 months for deprecated endpoints

---

## 📚 Additional Resources

### Related Documentation
- [Main Documentation](../README.md)
- [JavaDoc API Reference](../javadoc/index.html)
- [Project README](../../README.md)

### External Tools
- **Postman**: API testing and documentation
- **Swagger/OpenAPI**: Interactive API documentation (future enhancement)
- **curl**: Command-line HTTP client
- **HTTPie**: User-friendly HTTP client

### Sample Credentials (Development Only)

**Students:**
- `U2310001A` / `password` - Tan Wei Ling (Computer Science, Year 2)
- `U2310002B` / `password` - Ng Jia Hao (Data Science & AI, Year 3)
- `U2310003C` / `password` - Lim Yi Xuan (Computer Engineering, Year 4)
- `U2310004D` / `password` - Chong Zhi Hao (Data Science & AI, Year 1)
- `U2310005E` / `newpassword` - Wong Shu Hui (Computer Science, Year 3)
- `U2310006F` / `password` - Gerald Tang (Computer Science, Year 2)

**Company Representatives:**
- `charlielim@gmail.com` / `password` - Charlie Lim (Microsoft)
- `henrytan@gmail.com` / `password` - Henry Tan (ST Engineering)
- `boblee@gmail.com` / `password` - Bob Lee (Google)
- `alicetan@gmail.com` / `password` - Alice Tan (SAP)
- `michelle@gmail.com` / `password` - Michelle Richardson (Meta)

**Staff:**
- `tan002` / `password` - Mr. Tan Boon Kiat (CCDS)
- `lee003` / `password` - Ms. Lee Mei Ling (CCDS)
- `sng001` / `password` - Dr. Sng Hui Lin (CCDS)

---

## 🔍 Quick Reference

### Common Query Parameters
- `studentId` - Student user ID (e.g., U2345123F)
- `repId` - Company representative email (e.g., alice@techcorp.com)
- `staffId` - Staff user ID (e.g., staff001)
- `applicationId` - Application identifier
- `internshipId` - Internship opportunity identifier
- `opportunityId` - Alias for internshipId
- `withdrawalId` - Withdrawal request identifier

### HTTP Methods Used
- **GET** - Retrieve data (no side effects)
- **POST** - Create new resource or perform action
- **PUT** - Update existing resource (complete replacement)
- **PATCH** - Partial update of resource
- **DELETE** - Remove resource (not currently used)

### Content Types
- **Request**: `application/json`
- **Response**: `application/json`
- **Character Encoding**: UTF-8

---

**API Base URL**: `http://localhost:6060/api`  
**Server Port**: 6060  
**Context Path**: `/`  
**Last Updated**: November 16, 2025  
**Status**: ✅ Fully Implemented and Operational  
**Version**: 2.0.0  

---

*For issues or questions, please refer to the project repository or contact the development team.*
