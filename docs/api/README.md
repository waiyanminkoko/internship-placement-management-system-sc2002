# API Documentation

## 🌐 Overview

This directory contains REST API documentation for the Internship Placement Management System backend.

## 📂 Contents

### API Documentation Files (To Be Created)

1. **`endpoints.md`** - Complete API endpoint reference
2. **`authentication.md`** - Authentication and authorization details
3. **`error-codes.md`** - Error response codes and messages
4. **`examples.md`** - Request/response examples
5. **`postman-collection.json`** - Postman collection for testing

## 🔌 API Endpoints (Draft)

### Base URL
```
http://localhost:7070/api
```

### Authentication Endpoints
```
POST   /api/auth/login           - User login
POST   /api/auth/logout          - User logout
POST   /api/auth/change-password - Change password
GET    /api/auth/session         - Check session status
```

### Student Endpoints
```
GET    /api/students/{id}                    - Get student details
GET    /api/students/{id}/applications       - Get student's applications
POST   /api/students/{id}/applications       - Submit application
DELETE /api/students/{id}/applications/{appId} - Withdraw application
POST   /api/students/{id}/accept-placement   - Accept placement
GET    /api/internships                      - Browse internships
GET    /api/internships/{id}                 - View internship details
```

### Company Representative Endpoints
```
GET    /api/representatives/{id}                - Get representative details
GET    /api/representatives/{id}/internships    - Get rep's internships
POST   /api/representatives/{id}/internships    - Create internship
PUT    /api/representatives/{id}/internships/{intId} - Update internship
GET    /api/representatives/{id}/applications   - View applications
PUT    /api/representatives/{id}/applications/{appId} - Approve/reject application
```

### Staff Endpoints
```
GET    /api/staff/{id}                        - Get staff details
GET    /api/staff/pending-internships         - Get pending internships
PUT    /api/staff/internships/{id}/approve    - Approve internship
PUT    /api/staff/internships/{id}/reject     - Reject internship
GET    /api/staff/pending-representatives     - Get unauthorized reps
PUT    /api/staff/representatives/{id}/authorize - Authorize representative
GET    /api/staff/withdrawal-requests         - Get withdrawal requests
PUT    /api/staff/withdrawal-requests/{id}    - Process withdrawal
GET    /api/staff/reports                     - Generate reports
```

## 📋 Request/Response Format

### Standard Success Response
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {
    // Response data here
  },
  "timestamp": "2025-10-22T14:30:00"
}
```

### Standard Error Response
```json
{
  "success": false,
  "message": "Error description",
  "error": {
    "code": "ERROR_CODE",
    "details": "Detailed error message"
  },
  "timestamp": "2025-10-22T14:30:00"
}
```

## 🔐 Authentication

### Login Request
```json
POST /api/auth/login
Content-Type: application/json

{
  "userId": "U2345123F",
  "password": "password123"
}
```

### Login Response
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "userId": "U2345123F",
    "name": "John Tan",
    "role": "STUDENT",
    "sessionToken": "abc123...",
    "expiresAt": "2025-10-22T16:30:00"
  }
}
```

## 📝 Example Workflows

### 1. Student Applies for Internship

**Step 1: Login**
```bash
POST /api/auth/login
{
  "userId": "U2345123F",
  "password": "password"
}
```

**Step 2: Browse Internships**
```bash
GET /api/internships?major=CSC&level=INTERMEDIATE
```

**Step 3: Submit Application**
```bash
POST /api/students/U2345123F/applications
{
  "internshipId": "INT-20251022-123456-a1b2c3d4"
}
```

### 2. Company Creates Internship

**Step 1: Login**
```bash
POST /api/auth/login
{
  "userId": "alice@techcorp.com",
  "password": "password"
}
```

**Step 2: Create Internship**
```bash
POST /api/representatives/alice@techcorp.com/internships
{
  "title": "Software Engineer Intern",
  "description": "Full-stack development internship",
  "level": "INTERMEDIATE",
  "preferredMajor": "CSC",
  "openingDate": "2025-11-01",
  "closingDate": "2025-12-31",
  "totalSlots": 5
}
```

### 3. Staff Approves Internship

**Step 1: Login**
```bash
POST /api/auth/login
{
  "userId": "staff001",
  "password": "password"
}
```

**Step 2: View Pending Internships**
```bash
GET /api/staff/pending-internships
```

**Step 3: Approve Internship**
```bash
PUT /api/staff/internships/INT-20251022-123456-a1b2c3d4/approve
{
  "comments": "Approved - meets university standards"
}
```

## 🚨 Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `AUTH_001` | 401 | Invalid credentials |
| `AUTH_002` | 401 | Session expired |
| `AUTH_003` | 403 | Insufficient permissions |
| `VAL_001` | 400 | Missing required field |
| `VAL_002` | 400 | Invalid input format |
| `BIZ_001` | 400 | Max applications reached |
| `BIZ_002` | 400 | Not eligible for level |
| `BIZ_003` | 400 | Internship not accepting applications |
| `BIZ_004` | 400 | Max internships reached |
| `RES_001` | 404 | User not found |
| `RES_002` | 404 | Internship not found |
| `RES_003` | 404 | Application not found |
| `SYS_001` | 500 | Internal server error |
| `SYS_002` | 500 | Database error |

## 🔧 Testing

### Using cURL
```bash
# Login
curl -X POST http://localhost:7070/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"userId":"U2345123F","password":"password"}'

# Get internships
curl -X GET http://localhost:7070/api/internships \
  -H "Authorization: Bearer {token}"
```

### Using Postman
1. Import `postman-collection.json`
2. Set environment variable `baseUrl` = `http://localhost:7070`
3. Run collection tests

### Using JavaScript/Fetch
```javascript
// Login
const response = await fetch('http://localhost:7070/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    userId: 'U2345123F',
    password: 'password'
  })
});
const data = await response.json();
```

## 📚 Additional Documentation

### To Be Created:
- [ ] `endpoints.md` - Detailed endpoint documentation
- [ ] `authentication.md` - Auth flow and security
- [ ] `error-codes.md` - Complete error code reference
- [ ] `examples.md` - More request/response examples
- [ ] `postman-collection.json` - Postman test collection
- [ ] `swagger.yaml` - OpenAPI/Swagger specification

### Tools for API Documentation:
- **Swagger/OpenAPI** - Interactive API documentation
- **Postman** - API testing and documentation
- **ReDoc** - OpenAPI documentation renderer
- **API Blueprint** - API description language

## 🔄 CORS Configuration

Current CORS settings (from `WebConfig.java`):
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## 📊 API Versioning

Current version: `v1`

Future versioning strategy:
```
/api/v1/...  - Current version
/api/v2/...  - Future version
```

---

**Base URL**: http://localhost:7070/api  
**Last Updated**: October 22, 2025  
**Status**: 📝 Draft structure ready for implementation
