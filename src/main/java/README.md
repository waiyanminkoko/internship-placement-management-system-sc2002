# Java Source Code Structure

This document explains the folder structure and organization of the Java source code for the Internship Placement Management System.

## Overview

The `src/main/java` directory follows a standard package-based structure, organizing code by functionality and layer responsibilities following object-oriented design principles and layered architecture patterns.

---

## Folder Structure

```
src/main/java/
├── app/                          # Application entry points
├── config/                       # Configuration classes
├── controller/                   # REST API controllers (Presentation Layer)
├── dto/                          # Data Transfer Objects (15 files)
├── enums/                        # Enumeration types (7 files)
├── exception/                    # Exception handling (8 files)
├── model/                        # Domain entities (9 files)
├── repository/                   # Data Access Layer interfaces
│   └── impl/                     # CSV-based repository implementations
├── service/                      # Service/Business Logic Layer interfaces
│   ├── impl/                     # Service implementations
│   └── indiv_contribution/       # Individual assignment submissions
└── util/                         # Utility classes (7 files)
```

---

## Why This Structure?

### 1. **Separation of Concerns**
Each package has a distinct responsibility, making the codebase easier to understand, maintain, and test:
- **Controllers** handle HTTP requests/responses
- **Services** contain business logic
- **Repositories** manage data persistence
- **Models** represent domain entities
- **DTOs** handle data transfer between layers

### 2. **Layered Architecture**
The structure follows a classic layered architecture pattern:
```
Controller Layer → Service Layer → Repository Layer → Data Storage (CSV)
        ↕              ↕                ↕
      DTOs          Models         Persistence
```
This separation allows each layer to evolve independently and makes testing easier.

### 3. **Object-Oriented Design Principles**

- **Encapsulation**: Business logic is contained within appropriate classes and packages
- **Abstraction**: Interfaces define contracts (Repository, Service)
- **Inheritance**: User hierarchy (Student, CompanyRepresentative, CareerCenterStaff extend User)
- **Polymorphism**: Different implementations of repository interfaces

### 4. **Maintainability**
- Related classes are grouped together
- Easy to locate specific functionality
- Clear dependencies between packages
- Reduces coupling between layers

### 5. **Scalability**
- Easy to add new features (add to appropriate package)
- Can swap implementations (e.g., CSV → Database) by changing repository implementations
- Service layer can be reused by different controllers or interfaces

### 6. **Industry Standards**
Follows common Java/Spring Boot conventions:
- Package naming matches functionality
- Clear separation between interface and implementation
- Configuration isolated in dedicated package
- Exception handling centralized

---

## 📁 Package Details

### 📁 `app/`
**Purpose**: Application entry points and bootstrap classes

**Why Separate?**
- Clear identification of main application classes
- Separates startup logic from business logic
- Supports multiple entry points (web vs terminal)

**Files**:
- `InternshipPlacementApplication.java` - Spring Boot web application
- `InternshipPlacementApplication_terminal.java` - Console-based version for testing

---

### 📦 `config/`
**Purpose**: Application configuration classes

**Why Separate?**
- Centralizes all configuration in one place
- Easy to modify application behavior without changing business logic
- Follows Spring Boot configuration patterns

Contains configuration files for application setup and framework initialization.

**Files**:
- `WebConfig.java` - Web application configuration (CORS, interceptors, etc.)

**Usage**: Centralized configuration management for the application, including Spring Boot settings, web security, and application-wide beans.

---

### 📦 `controller/`
**Purpose**: REST API endpoints (Presentation Layer)

**Why Separate?**
- Isolates HTTP/API concerns from business logic
- Makes API structure clear and maintainable
- Enables easy addition of new endpoints
- Follows Spring MVC pattern

Controllers handle HTTP requests, validate input, call services, and return responses.

**Files**:
- `AuthenticationController.java` - Login, logout, password change endpoints
- `StudentController.java` - Student-specific operations (applications, internships)
- `CompanyRepresentativeController.java` - Representative operations (create internships, review applications)
- `StaffController.java` - Staff/admin operations (approvals, reports)
- `HealthController.java` - System health check endpoint

**Usage**:
- Map HTTP requests to service methods
- Input validation using DTOs
- Return standardized API responses
- Handle HTTP-specific concerns (status codes, headers)

---

### 📦 `dto/` (Data Transfer Objects)
**Purpose**: Data Transfer Objects for API communication

**Why Separate?**
- Decouples API contracts from internal domain models
- Prevents exposing sensitive internal data structures
- Allows API to evolve independently from domain model
- Enables validation at API boundary
- Optimizes data transfer (send only what's needed)

DTOs are used to transfer data between different layers of the application (particularly between the presentation layer and service layer) without exposing internal domain models directly.

**Files**:
- `ApiResponse.java` - Standardized API response wrapper
- `LoginRequest.java` / `LoginResponse.java` - Authentication DTOs
- `ApplyInternshipRequest.java` / `ApplyInternshipDTO.java` - Application DTOs
- `InternshipDTO.java` - Simplified internship data for API responses
- `ApprovalDecisionRequest.java` - DTO for approval/rejection decisions
- `ReportFilterRequest.java` / `ReportFilter.java` - Report filtering DTOs
- `ChangePasswordRequest.java` - Password change DTO
- `CreateInternshipRequest.java` - Internship creation DTO
- `RegisterRepresentativeRequest.java` - Representative registration DTO
- `WithdrawalRequestDTO.java` / `WithdrawalDetailsDTO.java` - Withdrawal DTOs
- `ApplicationWithStudentDTO.java` - Application with student information

**Usage**: 
- Decouple internal model from external API contracts
- Validation and data transformation
- Reduce network payload by sending only necessary fields
- Version API independently from domain model

---

### 📦 `enums/`
**Purpose**: Enumeration types for constant values

**Why Separate?**
- Type-safe constants prevent invalid values
- Centralized definition avoids duplication
- Easy to extend with new values
- Compile-time validation
- Self-documenting code

Enums provide type-safe constants for various domain concepts throughout the application.

**Files**:
- `ApplicationStatus.java` - States of internship applications (PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN, ACCEPTED)
- `ApprovalStatus.java` - Approval states for submissions (PENDING, APPROVED, REJECTED)
- `InternshipLevel.java` - Internship levels (YEAR_1, YEAR_2, YEAR_3, FINAL_YEAR)
- `InternshipStatus.java` - Internship opportunity states (DRAFT, PENDING_APPROVAL, APPROVED, REJECTED, FILLED)
- `Major.java` - Academic majors (COMPUTER_SCIENCE, DATA_SCIENCE_AI, COMPUTER_ENGINEERING, BUSINESS, etc.)
- `UserRole.java` - User roles in the system (STUDENT, COMPANY_REPRESENTATIVE, CAREER_CENTER_STAFF)

**Usage**: 
- Enforce valid values at compile-time
- Improve code readability and maintainability
- Centralized definition of domain constants
- Prevent typos and invalid values

---

### 📦 `exception/`
**Purpose**: Custom exception classes and error handling

**Why Separate?**
- Centralizes error handling logic
- Provides meaningful, domain-specific error messages
- Enables consistent error responses across API
- Separates error handling from business logic
- Makes debugging easier with specific exception types

Contains custom exceptions for different error scenarios and a global exception handler for centralized error management.

**Files**:
- `GlobalExceptionHandler.java` - Centralized exception handling for REST APIs (Spring @ControllerAdvice)
- `BusinessRuleException.java` - Violations of business rules (e.g., max applications exceeded)
- `ResourceNotFoundException.java` - Requested resource not found (404 errors)
- `UnauthorizedException.java` - Authentication/authorization failures (401/403 errors)
- `InvalidInputException.java` - Input validation failures (400 errors)
- `CsvParseException.java` - Errors during CSV file parsing
- `DataPersistenceException.java` - Database/file persistence errors

**Usage**: 
- Consistent error handling across the application
- Meaningful error messages to users and developers
- Separation of error handling logic from business logic
- HTTP status code mapping in GlobalExceptionHandler

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

**Why Separate?**
- Abstracts data storage mechanism (currently CSV, easily changeable to database)
- Isolates persistence logic from business logic
- Enables testing with mock repositories
- Follows Repository pattern for clean architecture
- Single Responsibility: only handles data access

Repository interfaces and implementations provide abstraction over data storage, currently using CSV files as the persistence mechanism.

**Interfaces**:
- `Repository.java` - Base repository interface with CRUD operations
- `UserRepository.java` - User-specific repository operations
- `StudentRepository.java` - Student data access
- `CompanyRepresentativeRepository.java` - Company representative data access
- `CareerCenterStaffRepository.java` - Staff data access
- `InternshipOpportunityRepository.java` - Internship opportunity data access
- `ApplicationRepository.java` - Application data access

**Implementations** (`impl/` subfolder):
- `CsvStudentRepository.java` - CSV-based student persistence
- `CsvCompanyRepresentativeRepository.java` - CSV-based representative persistence
- `CsvCareerCenterStaffRepository.java` - CSV-based staff persistence
- `CsvInternshipOpportunityRepository.java` - CSV-based internship persistence
- `CsvApplicationRepository.java` - CSV-based application persistence
- `CsvWithdrawalRepository.java` - CSV-based withdrawal request persistence
- `CsvUserRepository.java` - CSV-based user operations (shared across all user types)
- Additional CSV repository implementations as needed

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

**Why Separate?**
- Encapsulates complex business workflows
- Coordinates between multiple repositories and models
- Transaction-like operations with validation
- Reusable business logic (can be called from different controllers)
- Testable independently from presentation layer

**Why `impl/` subfolder?**
- Separates interface contracts from implementations
- Allows for multiple implementations (e.g., different business rule versions)
- Follows dependency inversion principle
- Makes testing easier with mock implementations

**Why `indiv_contribution/` subfolder?**
- Contains individual student assignment submissions
- Shows individual work for grading purposes
- Not integrated into main application flow
- Demonstrates understanding of specific functionality

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

**Why Separate?**
- Avoids code duplication across the application
- Provides reusable, stateless helper functions
- Cross-cutting concerns that don't belong to specific layers
- Easy to test independently
- Single place to update common functionality

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
┌─────────────────────┐
│  Controller Layer   │ ← Handles HTTP requests/responses
│      (+ DTOs)       │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│   Service Layer     │ ← Business logic and workflows
│    (+ DTOs)         │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│   Repository Layer  │ ← Data access abstraction
│ (+ Persistence)     │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│   Data Storage      │ ← CSV files
│       (CSV)         │
└─────────────────────┘

Cross-cutting concerns:
- Models (Domain Layer) ← Used by all layers
- Enums ← Used by all layers
- Exceptions ← Used by all layers
- Utils ← Used by all layers
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
