# Java Source Code Structure

This document explains the folder structure and organization of the Java source code for the Internship Placement Management System.

## Overview

The `src/main/java` directory follows a standard package-based structure, organizing code by functionality and layer responsibilities following object-oriented design principles and MVC-like architecture patterns.

---

## Folder Structure

### 📁 Root Level

- **`InternshipPlacementApplication.java`**  
  Main application entry point for the web-based version (Spring Boot application).

- **`InternshipPlacementApplication_terminal.java`**  
  Main application entry point for the terminal/console-based version (Used during early stage backend testing).

---

### 📦 `config/`
**Purpose**: Application configuration classes

Contains configuration files for application setup and framework initialization.

**Files**:
- `WebConfig.java` - Web application configuration (CORS, interceptors, etc.)

**Usage**: Centralized configuration management for the application, including Spring Boot settings, web security, and application-wide beans.

---

### 📦 `dto/` (Data Transfer Objects)
**Purpose**: Data Transfer Objects for API communication

DTOs are used to transfer data between different layers of the application (particularly between the presentation layer and service layer) without exposing internal domain models directly.

**Files**:
- `ApiResponse.java` - Standardized API response wrapper
- `ApprovalDecisionRequest.java` - DTO for approval/rejection decisions
- `InternshipDTO.java` - Simplified internship data for API responses
- `LoginRequest.java` - DTO for authentication requests
- `ReportFilterRequest.java` - DTO for filtering report data

**Usage**: 
- Decouple internal model from external API contracts
- Validation and data transformation
- Reduce network payload by sending only necessary fields

---

### 📦 `enums/`
**Purpose**: Enumeration types for constant values

Enums provide type-safe constants for various domain concepts throughout the application.

**Files**:
- `ApplicationStatus.java` - States of internship applications (PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN, ACCEPTED)
- `ApprovalStatus.java` - Approval states for submissions (PENDING, APPROVED, REJECTED)
- `InternshipLevel.java` - Internship levels (YEAR_1, YEAR_2, YEAR_3, FINAL_YEAR)
- `InternshipStatus.java` - Internship opportunity states (DRAFT, PENDING_APPROVAL, APPROVED, REJECTED, FILLED)
- `Major.java` - Academic majors (COMPUTER_SCIENCE, BUSINESS, ENGINEERING, etc.)
- `UserRole.java` - User roles in the system (STUDENT, COMPANY_REP, STAFF)

**Usage**: 
- Enforce valid values at compile-time
- Improve code readability
- Centralized definition of domain constants

---

### 📦 `exception/`
**Purpose**: Custom exception classes and error handling

Contains custom exceptions for different error scenarios and a global exception handler for centralized error management.

**Files**:
- `BusinessRuleException.java` - Violations of business rules
- `CsvParseException.java` - Errors during CSV file parsing
- `DataPersistenceException.java` - Database/file persistence errors
- `GlobalExceptionHandler.java` - Centralized exception handling for REST APIs
- `InvalidInputException.java` - Input validation failures
- `ResourceNotFoundException.java` - Requested resource not found
- `UnauthorizedException.java` - Authentication/authorization failures

**Usage**: 
- Consistent error handling across the application
- Meaningful error messages to users
- Separation of error handling logic from business logic

---

### 📦 `model/`
**Purpose**: Domain model / Entity classes

Contains the core business objects that represent the domain model of the internship placement system. These are the main entities that encapsulate business logic and data.

**Files**:
- `User.java` - Base user entity (abstract)
- `Student.java` - Student entity with application management
  - Key Methods: `canApplyForLevel()`, `canSubmitApplication()`, `getActiveApplicationsCount()`, `canApplyMoreApplications()`, `addApplication()`, `acceptPlacement()`
  - Business Rules: Max 3 applications, year-based level restrictions (Years 1-2: BASIC only; Years 3-4: all levels)
- `CompanyRepresentative.java` - Company representative entity
  - Key Methods: `canCreateInternship()`, `canCreateMoreOpportunities()`, `addOpportunity()`, `getOpportunityCount()`, `authorize()`, `revokeAuthorization()`
  - Business Rules: Max 5 internship opportunities, requires staff authorization before login
- `CareerCenterStaff.java` - Staff entity with administrative privileges
  - Key Methods: `authorizeRepresentative()`, `approveInternship()`, `canAuthorizeRepresentatives()`, `canApproveInternships()`, `canProcessWithdrawals()`
  - Tracks: Approved representative IDs, approved internship IDs
- `InternshipOpportunity.java` - Internship posting/opportunity entity
  - Key Methods: `isAcceptingApplications()`, `canAcceptApplications()`, `isEligibleForStudent()`, `incrementFilledSlots()`, `isFilled()`, `confirmPlacement()`, `releasePlacement()`
  - Business Rules: Slot management, visibility control, date-based application windows, major matching
- `Application.java` - Student application to an internship
  - Key Methods: `approve()`, `reject()`, `withdraw()`, `acceptPlacement()`, `isPending()`, `isSuccessful()`, `isActive()`, `canBeWithdrawn()`
  - Lifecycle: PENDING → SUCCESSFUL/UNSUCCESSFUL/WITHDRAWN → ACCEPTED
- `WithdrawalRequest.java` - Request to withdraw from an accepted placement
  - Key Methods: `approve()`, `reject()`, `isPending()`, `isApproved()`, `isRejected()`
  - Tracks: Request date, processed by staff ID, approval status, comments
- `ModelDemo.java` - Comprehensive demonstration and testing of model classes
  - Showcases all business rules and entity interactions
  - Serves as living documentation and manual testing tool
  - Demonstrates complete lifecycle scenarios

**Usage**: 
- Encapsulate business logic and validation rules
- Represent real-world entities in the system
- Maintain data integrity through encapsulation
- Foundation for persistence layer
- Business rule enforcement at the entity level

---

### 📦 `repository/`
**Purpose**: Data access layer for persistence operations

Repository interfaces and implementations provide abstraction over data storage, currently using CSV files as the persistence mechanism.

**Interfaces**:
- `Repository.java` - Base repository interface with CRUD operations
- `UserRepository.java` - User-specific repository operations
- `StudentRepository.java` - Student data access
- `CompanyRepresentativeRepository.java` - Company representative data access
- `CareerCenterStaffRepository.java` - Staff data access
- `InternshipOpportunityRepository.java` - Internship opportunity data access
- `ApplicationRepository.java` - Application data access

**Implementations** (`impl/`):
- `CsvStudentRepository.java` - CSV-based student persistence
- `CsvCompanyRepresentativeRepository.java` - CSV-based representative persistence
- `CsvCareerCenterStaffRepository.java` - CSV-based staff persistence
- `CsvInternshipOpportunityRepository.java` - CSV-based internship persistence
- `CsvApplicationRepository.java` - CSV-based application persistence

**Key Features**:
- Thread-safe operations using ReentrantReadWriteLock
- In-memory caching with lazy loading
- Atomic file writes for data consistency
- Custom query methods for complex filtering
- Support for header-based CSV reading/writing

**Usage**:
- Abstraction over data storage mechanism
- Easy migration to database in the future
- Thread-safe concurrent access
- Automatic ID generation and management

---

### 📦 `service/`
**Purpose**: Business logic and service layer

Service classes contain the core business logic of the application. They orchestrate operations between models and handle complex business workflows.

**Core Services** (`impl/`):
- `AuthenticationServiceImpl.java` - User authentication and session management
  - Login/logout functionality
  - Password validation
  - Role-based access control
  
- `StudentServiceImpl.java` - Standalone service for all student operations
  - View available internships (with filtering by major/level)
  - View applied internships
  - Apply for internships (with eligibility validation)
  - Accept internship placements
  - Business rule enforcement (max 3 applications, year-based levels)
  
- `CompanyRepresentativeServiceImpl.java` - Company representative operations
  - Create/edit/delete internship opportunities
  - View applications to posted internships
  - Approve/reject student applications
  - Toggle internship visibility
  
- `StaffServiceImpl.java` - Staff/admin operations (implemented as interface with default methods)
  - Authorize/reject company representatives
  - Approve/reject internship opportunities
  - Process withdrawal requests
  - Generate system reports
  
- `WithdrawalRequestService.java` - Withdrawal request management
  - Submit withdrawal requests
  - Process approvals/rejections
  - Track withdrawal history
  - CSV persistence integration

**Individual Contribution Services** (`indiv_contribution/`):
- `StudentAcceptInternshipService.java` - Individual contribution: Accept placement functionality
- `StudentApplyInternshipService.java` - Individual contribution: Apply for internship functionality
- `StudentViewAppliedInternshipService.java` - Individual contribution: View applied internships
- `StudentViewAvailInternshipService.java` - Individual contribution: View available internships

**Note**: Individual contribution services exist for grading purposes but are not integrated into the main application. The core functionality is implemented in `StudentServiceImpl.java`.

**Usage**: 
- Implement business rules and workflows
- Coordinate between multiple models and repositories
- Transaction-like operations with rollback support
- Data validation before persistence
- Cross-cutting concerns (logging, authorization)

---

### 📦 `util/`
**Purpose**: Utility classes and helper functions

Contains reusable utility classes that provide common functionality across the application.

**Files**:
- `CSVReader.java` - Read and parse CSV files
  - `readCSVWithHeaders()` - Returns List<Map<String, String>> with column headers as keys
  - `readCSV()` - Returns List<String[]> for raw CSV data
  - Handles missing columns gracefully with empty string defaults
  
- `CSVWriter.java` - Write data to CSV files
  - `writeCSV()` - Legacy method for backward compatibility
  - `writeAllRecords()` - Modern method with atomic file operations
  - Temporary file creation for data integrity
  
- `CSVUtil.java` - General CSV utility functions
  - CSV formatting and parsing helpers
  - Header validation
  
- `DateUtil.java` - Date parsing, formatting, and validation
  - `formatDate(LocalDate)` - Format LocalDate for CSV storage
  - `formatDateTime(LocalDateTime)` - Format LocalDateTime for display
  - `formatDateTimeForCsv(LocalDateTime)` - Format LocalDateTime for CSV storage
  - `parseDate(String)` - Parse date strings with multiple format support
  
- `IdGenerator.java` - Generate unique identifiers
  - UUID generation for entities
  - Timestamp-based ID generation
  - Format: PREFIX-YYYYMMDD-XXXXX
  
- `ValidationUtil.java` - Input validation helper methods
  - Email validation
  - Password strength checking
  - Required field validation
  - Format validation

**Usage**: 
- Reusable functions to avoid code duplication
- Centralized implementations of common operations
- Data format conversions and validations
- Thread-safe ID generation
- Consistent date/time handling across the application

---

## Architecture Layers

The folder structure follows a layered architecture:

```
┌─────────────────────────────────────┐
│     Application Entry Points        │  InternshipPlacementApplication.java
│  (InternshipPlacementApplication_*) │  InternshipPlacementApplication_terminal.java
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         DTO Layer                   │  dto/
│    (Data Transfer Objects)          │  API request/response objects
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       Service Layer                 │  service/ & service/impl/
│     (Business Logic)                │  Business rules and workflows
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Repository Layer               │  repository/ & repository/impl/
│   (Data Access / Persistence)       │  CSV-based data storage
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│        Model Layer                  │  model/
│     (Domain Entities)               │  Business objects and logic
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Utility & Support              │  util/, enums/, exception/
│     (Cross-cutting Concerns)        │  Helpers and shared components
└─────────────────────────────────────┘
```

**Layer Responsibilities**:
- **Application**: Bootstrap and configuration
- **DTO**: Data transformation and API contracts
- **Service**: Business logic orchestration and validation
- **Repository**: Data persistence and retrieval (CSV files)
- **Model**: Domain entities with encapsulated business rules
- **Utility**: Reusable helpers and cross-cutting concerns

---

## Design Principles Applied

1. **Separation of Concerns**: Each package has a specific responsibility
2. **Single Responsibility Principle**: Classes focus on one area of functionality
3. **Encapsulation**: Business logic is encapsulated in appropriate layers
4. **Reusability**: Common functionality extracted to utility classes
5. **Type Safety**: Enums used instead of magic strings/numbers
6. **Error Handling**: Centralized exception management

---

## Package Naming Conventions

- **config**: Configuration and setup
- **dto**: Data Transfer Objects for API communication
- **enums**: Type-safe enumeration constants
- **exception**: Custom exceptions and error handling
- **model**: Domain entities and business objects
- **service**: Business logic and workflows
- **util**: Helper functions and utilities

---

## Notes for Developers

- Always place new business logic in the appropriate service class
- Use DTOs when exposing data through APIs to avoid tight coupling
- Add new domain entities to the `model/` package with proper business rule encapsulation
- Create custom exceptions in `exception/` for specific error scenarios
- Utility functions should be stateless and reusable
- Follow the existing package structure when adding new features
- When adding methods to models, ensure CSV repositories are updated accordingly
- Use `DateUtil.formatDateTime()` for LocalDateTime and `formatDate()` for LocalDate
- Always catch `Exception` (not `IOException`) in repository CSV operations
- Run `ModelDemo.java` after model changes to verify business logic integrity
- Maintain thread-safety in repository implementations using appropriate locks
