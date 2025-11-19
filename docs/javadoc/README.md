# JavaDoc Documentation

## 📖 Overview

This directory contains or references the generated JavaDoc HTML documentation for the entire Internship Placement Management System codebase.

## 🚀 Generating JavaDocs

### Using Maven
```bash
# Generate JavaDocs only
mvn javadoc:javadoc

# Generate JavaDocs with full site documentation
mvn site
```

### Output Location
After generation, JavaDocs will be available at:
- **Primary Location**: `docs/javadoc/index.html` (configured in pom.xml)
- Alternative (with site): `target/site/apidocs/index.html`

**The JavaDoc files are generated directly into this folder (`docs/javadoc/`) for easy access and version control.**

## 📦 Coverage

The generated JavaDoc documentation includes **65 Java classes and interfaces** organized by package:

### 1. **Model Classes** (`model/`)
Domain entities representing core business objects:
- `User` (abstract base class)
- `Student` - Student entity with application limits and eligibility rules
- `CompanyRepresentative` - Company rep entity with internship management
- `CareerCenterStaff` - Staff entity with approval workflows
- `InternshipOpportunity` - Internship posting with slots and dates
- `Application` - Internship application with status tracking
- `WithdrawalRequest` - Withdrawal request entity

### 2. **Controller Classes** (`controller/`)
REST API endpoints for all user types (5 controllers):
- `AuthenticationController` - Login, logout, password management
- `StudentController` - Student-specific endpoints
- `CompanyRepresentativeController` - Company representative endpoints
- `StaffController` - Career center staff endpoints (with enhanced report generation)
- `HealthController` - System health check

### 3. **Service Layer** (`service/` and `service.impl/`)
Business logic implementations:
- `AuthenticationService` & `AuthenticationServiceImpl` - User authentication
- `StudentService` & `StudentServiceImpl` - Student operations
- `CompanyRepresentativeService` & `CompanyRepresentativeServiceImpl` - Company rep operations
- `StaffService` & `StaffServiceImpl` - Staff operations
- `WithdrawalRequestService` - Withdrawal processing

### 4. **Individual Contributions** (`service.indiv_contribution/`)
Demonstrates individual team member contributions:
- `StudentViewAvailInternshipService` - View available internships
- `StudentViewAppliedInternshipService` - View applied internships
- `StudentApplyInternshipService` - Apply for internships
- `StudentAcceptInternshipService` - Accept placements

### 5. **Repository Layer** (`repository/` and `repository.impl/`)
Data access with CSV-based persistence:
- `Repository<T, ID>` - Generic repository interface
- `UserRepository` - User data access
- `StudentRepository` - Student data access
- `CompanyRepresentativeRepository` - Company rep data access
- `CareerCenterStaffRepository` - Staff data access
- `InternshipOpportunityRepository` - Internship data access
- `ApplicationRepository` - Application data access
- **CSV Implementations**: All interfaces have CSV-based implementations

### 6. **DTO Classes** (`dto/`)
Data transfer objects for API requests/responses (14 DTOs):
- `ApiResponse<T>` - Standardized API response wrapper
- `LoginRequest` & `LoginResponse` - Authentication DTOs
- `ChangePasswordRequest` - Password change request
- `CreateInternshipRequest` - Internship creation request
- `InternshipDTO` - Internship data transfer object
- `ApplyInternshipRequest` - Application submission request
- `ApplyInternshipDTO` - Application with enriched internship details
- `ApplicationWithStudentDTO` - Application with student details
- `ApprovalDecisionRequest` - Approval/rejection decision
- `RegisterRepresentativeRequest` - Company rep registration
- `ReportFilter` & `ReportFilterRequest` - Report filtering with extended filters (application status, internship level, internship status)
- `WithdrawalRequestDTO` - Withdrawal request data
- `WithdrawalDetailsDTO` - Withdrawal details with enriched information

### 7. **Enum Types** (`enums/`)
Type-safe enumeration constants:
- `UserRole` - User roles (STUDENT, COMPANY_REPRESENTATIVE, CAREER_CENTER_STAFF)
- `ApplicationStatus` - Application statuses (PENDING, SUCCESSFUL, REJECTED, WITHDRAWN)
- `ApprovalStatus` - Approval statuses (PENDING_APPROVAL, APPROVED, REJECTED)
- `InternshipLevel` - Internship levels (BASIC, INTERMEDIATE, ADVANCED)
- `InternshipStatus` - Internship statuses (PENDING, APPROVED, REJECTED, FILLED)
- `Major` - Student majors (CSC, EEE, MAE, etc.)

### 8. **Exception Classes** (`exception/`)
Custom exception hierarchy:
- `BusinessRuleException` - Business logic violations
- `ResourceNotFoundException` - Entity not found errors
- `UnauthorizedException` - Authentication/authorization failures
- `InvalidInputException` - Input validation errors
- `DataPersistenceException` - CSV file operation errors
- `CsvParseException` - CSV parsing errors
- `GlobalExceptionHandler` - Global REST API exception handler

### 9. **Utility Classes** (`util/`)
Helper classes for common operations:
- `CSVUtil` - CSV file operations (read, write, update, delete)
- `CSVReader` - CSV reading utilities with header support
- `CSVWriter` - CSV writing utilities
- `IdGenerator` - Unique ID generation
- `ValidationUtil` - Input validation utilities
- `DateUtil` - Date/time utilities

### 10. **Configuration** (`config/`)
Spring Boot configuration:
- `WebConfig` - Web MVC and CORS configuration

### 11. **Application Entry Points** (`app/`)
Main entry points for the system (3 classes):
- `InternshipPlacementApplication` - Spring Boot REST API (port 6060)
- `InternshipPlacementApplication_terminal` - Console/terminal version
- `package-info.java` - Package documentation

## 📋 JavaDoc Standards

All classes follow Oracle Java documentation standards:

### Class-Level Documentation
```java
/**
 * Brief description of the class.
 * 
 * <p>Detailed description with usage information.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Feature 1</li>
 *   <li>Feature 2</li>
 * </ul>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
```

### Method-Level Documentation
```java
/**
 * Brief description of what the method does.
 * 
 * <p>Detailed description if needed.</p>
 * 
 * @param paramName description of parameter
 * @return description of return value
 * @throws ExceptionType when this exception occurs
 */
```

### Field-Level Documentation
```java
/**
 * Description of the field's purpose.
 * Additional details about valid values or constraints.
 */
```

## 🔍 Viewing JavaDocs

### Option 1: Local Browser
```bash
# Windows
start docs\javadoc\index.html

# macOS
open docs/javadoc/index.html

# Linux
xdg-open docs/javadoc/index.html
```

### Option 2: IDE Integration
Most IDEs (Eclipse, IntelliJ, VS Code with Java extensions) can display JavaDocs inline:
- Hover over a class/method name
- Press `F1` or `Ctrl+Q` (IDE-specific)

### Option 3: Command Line
```bash
# Extract specific class documentation
javadoc -d temp-docs src/main/java/model/Student.java
```

## 📊 JavaDoc Statistics

| Package | Classes/Interfaces | Enums | Total |
|---------|-------------------|-------|-------|
| model | 8 classes | - | 8 |
| controller | 5 classes | - | 5 |
| service | 4 interfaces | - | 4 |
| service.impl | 5 classes | - | 5 |
| service.indiv_contribution | 4 classes | - | 4 |
| repository | 7 interfaces | - | 7 |
| repository.impl | 5 classes | - | 5 |
| dto | 14 classes | - | 14 |
| enums | - | 6 enums | 6 |
| exception | 7 classes | - | 7 |
| util | 6 classes | - | 6 |
| config | 1 class | - | 1 |
| app | 3 classes | - | 3 |
| **Total** | **54 classes + 11 interfaces** | **6 enums** | **74** |

## 🎯 Key Features in JavaDocs

### 1. Comprehensive Class Descriptions
Every class includes:
- Purpose and responsibility
- Key business rules
- Usage examples
- Related classes
- Since version

### 2. Detailed Method Documentation
Every public method includes:
- What it does
- Parameters and their constraints
- Return value description
- Exceptions that may be thrown
- Usage examples (where applicable)

### 4. Business Rule Documentation
Important business rules are highlighted:
- Student application limits (max 3 active applications)
- Year-level restrictions (Year 1-2 = BASIC only; Year 3-4 = BASIC + INTERMEDIATE)
- Application status workflow (PENDING → SUCCESSFUL/REJECTED/WITHDRAWN)
- Internship status workflow (PENDING → APPROVED → FILLED)
- Company representative limits (max 5 internships)
- Internship slot limits (max 10)
- Authorization requirements
- Placement acceptance rules (one accepted placement per student)
- Report filtering capabilities (by major, year, company, dates, application status, internship level, internship status)

### 4. Cross-References
- Related classes linked
- Inherited methods documented
- Interface implementations noted
- See-also tags for related functionality

## 🛠️ Maven Configuration

The project's `pom.xml` includes JavaDoc plugin configuration:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.5.0</version>
    <configuration>
        <show>private</show>
        <nohelp>true</nohelp>
        <encoding>UTF-8</encoding>
        <docencoding>UTF-8</docencoding>
        <charset>UTF-8</charset>
    </configuration>
</plugin>
```

## 📚 Additional Resources

- [Oracle JavaDoc Guide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)
- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)
- [Java Documentation Standards](https://www.oracle.com/java/technologies/javase/javadoc-tool.html)

---

**Generated By**: Maven JavaDoc Plugin 3.6.2  
**Java Version**: 21  
**Last Updated**: November 19, 2025  
**Total Documentation**: 74 classes/interfaces/enums
