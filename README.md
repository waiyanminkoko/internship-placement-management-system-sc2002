# SC2002 Internship Placement Management System - Project Skeleton

## 🎯 Project Overview

**Course**: SC2002 Object-Oriented Design & Programming  
**Group**: Group 6  
**System**: Internship Placement Management System  
**Architecture**: Spring Boot REST API Backend + React Frontend  
**Data Storage**: CSV Files (No Database as per assignment requirements)

---

## 📦 Installation and Run Guide

### Prerequisites

Ensure you have the following installed on your system:

- **Java Development Kit (JDK) 21** or higher
  - Download: [OpenJDK 21](https://adoptium.net/) or [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/)
  - Verify installation: `java -version`
  
- **Apache Maven 3.8+**
  - Download: [Maven Download](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn -version`
  
- **Git** (for version control)
  - Download: [Git for Windows](https://git-scm.com/download/win)
  
### Initial Setup

1. **Clone or Navigate to Project Directory**
   ```powershell
   git clone https://github.com/waiyanminkoko/internship-placement-management-system-sc2002
   ```
2. **Verify Project Structure**
   ```powershell
   # Ensure pom.xml exists in the root directory
   dir pom.xml
   ```

3. **Download Dependencies**
   ```powershell
   mvn clean install
   ```
   This will:
   - Download all required dependencies (Spring Boot, OpenCSV, Lombok)
   - Compile the project
   - Run tests (when available)
   - Package the application

### Running the Application

#### Important Note
Ensure you are at the **root** of your project directory. **Compile** the project before running any application mode:
```powershell
mvn compile
```

#### Option 1: Terminal-Based Testing Application (Currently Available)

The **terminal-based test application** allows you to test all backend logic without needing a web server or frontend.

```powershell
# Run the terminal test application
mvn exec:java "-Dexec.mainClass=InternshipPlacementApplication_terminal"
```

**What You Can Test:**
- ✅ User Management (Students, Company Representatives, Staff)
- ✅ Internship Opportunity Creation and Management
- ✅ Application Workflow Testing
- ✅ Business Rules Validation
- ✅ CSV Data Operations
- ✅ Exception Handling
- ✅ Demo Scenarios

**Location:** `src/main/java/InternshipPlacementApplication_terminal.java`

#### Option 2: Spring Boot Web Server (Pending Implementation)

Once the REST API controllers are implemented, you can run the web server:

```powershell
# Run Spring Boot application (NOT YET FUNCTIONAL - Controllers not implemented)
mvn spring-boot:run
```

The server will start on: `http://localhost:7070`

**Note:** This will only work after the following components are implemented:
- ⏳ Controllers (5 files)
- ⏳ Services (8 files)
- ⏳ Repositories (13 files)
- ⏳ DTOs (9 files)

#### Option 3: Run Compiled JAR

```powershell
# Package the application
mvn clean package

# Run the JAR file (once Spring Boot is fully implemented)
java -jar target/internship-placement-system-1.0.0.jar
```

### Testing the Application

#### Manual Testing via Terminal

```powershell
# Start the terminal test application
mvn exec:java "-Dexec.mainClass=InternshipPlacementApplication_terminal"
```

Follow the interactive menu to test different features:
1. User Management Testing
2. Internship Management Testing
3. Application Workflow Testing
4. Business Rules Validation
5. CSV Operations Testing
6. Exception Handling Testing
7. Demo Scenarios
8. System Status Overview

#### Unit Tests (To Be Implemented)

```powershell
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=StudentTest

# Run tests with coverage
mvn test jacoco:report
```

### Development Workflow for Team Members

#### Finding Backend Logic/Code to Work On

All backend logic is organized in the following directories:

1. **Domain Models** (⏳ TODO)
   - Location: `src/main/java/model/`
   - Files: `User.java`, `Student.java`, `CompanyRepresentative.java`, `CareerCenterStaff.java`, `InternshipOpportunity.java`, `Application.java`, `WithdrawalRequest.java`
   - Contains: All business logic, validation rules, and entity relationships

2. **Enumerations** (✅ COMPLETED)
   - Location: `src/main/java/enums/`
   - Files: `UserRole.java`, `ApplicationStatus.java`, `InternshipStatus.java`, `InternshipLevel.java`, `ApprovalStatus.java`, `Major.java`
   - Contains: All system constants and status types

3. **Utilities** (✅ COMPLETED)
   - Location: `src/main/java/util/`
   - Files: `CSVUtil.java`, `ValidationUtil.java`, `DateUtil.java`, `IdGenerator.java`, `CSVReader.java`, `CSVWriter.java`
   - Contains: Helper functions for CSV operations, validation, and ID generation

4. **Exceptions** (✅ COMPLETED)
   - Location: `src/main/java/exception/`
   - Files: `ResourceNotFoundException.java`, `InvalidInputException.java`, `UnauthorizedException.java`, `BusinessRuleException.java`, `GlobalExceptionHandler.java`
   - Contains: Custom exception classes and global error handling

5. **DTOs** (⏳ TODO)
   - Location: `src/main/java/dto/`
   - To Create: 9 DTO files for request/response objects

6. **Repositories** (⏳ TODO)
   - Location: `src/main/java/repository/` (interfaces)
   - Location: `src/main/java/repository/impl/` (implementations)
   - To Create: 7 interfaces + 6 CSV-based implementations

7. **Services** (⏳ TODO)
   - Location: `src/main/java/service/` (interfaces)
   - Location: `src/main/java/service/impl/` (implementations)
   - To Create: 4 service interfaces + 4 implementations

8. **Controllers** (⏳ TODO)
   - Location: `src/main/java/controller/`
   - To Create: 5 REST API controllers

#### Building and Compiling After Changes

```powershell
# Clean and compile
mvn clean compile

# Check for compilation errors
mvn validate

# Build without running tests
mvn package -DskipTests

# Full build with tests
mvn clean install
```

#### Viewing Compiled Classes

```powershell
# Compiled classes are in:
cd target\classes

# View compiled model classes
dir target\classes\model

# View all compiled packages
tree target\classes /F
```

### Common Issues and Solutions

#### Issue 1: Maven Build Fails
```powershell
# Clear Maven cache and rebuild
mvn clean
mvn dependency:purge-local-repository
mvn clean install
```

#### Issue 2: Java Version Mismatch
```powershell
# Check Java version
java -version

# Ensure JAVA_HOME is set correctly
echo $env:JAVA_HOME

# Set JAVA_HOME if needed (adjust path to your JDK installation)
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
```

#### Issue 3: Port 7070 Already in Use
```powershell
# Find process using port 7070
netstat -ano | findstr :7070

# Kill the process (replace <PID> with actual process ID)
taskkill /PID <PID> /F

# Or change port in src/main/resources/application.properties
# server.port=8080
```

#### Issue 4: CSV Files Not Found
```powershell
# Verify CSV files exist
dir "src\main\resources\data\*.csv"

# Files should be automatically copied to target/classes/data/ during build
# If missing, run:
mvn clean compile
```
### Next Steps After Installation

1. **Run the terminal test application** to verify setup:
   ```powershell
   mvn exec:java "-Dexec.mainClass=InternshipPlacementApplication_terminal"
   ```

2. **Explore the codebase** in this order:
   - Review domain models in `src/main/java/model/`
   - Understand enums in `src/main/java/enums/`
   - Check utilities in `src/main/java/util/`
   - Study the terminal test app to see how components interact

3. **Start implementing your assigned components** (see Team Work Allocation)

4. **Test your changes** using the terminal application

5. **Commit and push** your changes regularly

---

## ✅ Implementation Status

### Backend Components - **IN-PROGRESS**

#### ✓ Core Configuration (3 files)
- `pom.xml` - Maven configuration with Spring Boot 3.2.0, OpenCSV, Lombok
- `application.properties` - Server port 7070, CSV paths, CORS, logging
- `InternshipPlacementApplication.java` - Main Spring Boot application class

#### ✓ Enumerations (5 files)
- `InternshipLevel.java` - BASIC, INTERMEDIATE, ADVANCED
- `ApplicationStatus.java` - PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN
- `InternshipStatus.java` - PENDING, APPROVED, REJECTED, FILLED
- `UserRole.java` - STUDENT, COMPANY_REPRESENTATIVE, CAREER_CENTER_STAFF
- `ApprovalStatus.java` - PENDING, APPROVED, REJECTED

#### ✓ Domain Models (7 files)
- `User.java` - Abstract base class with inheritance hierarchy
- `Student.java` - extends User, manages applications and placement
- `CompanyRepresentative.java` - extends User, manages internship postings
- `CareerCenterStaff.java` - extends User, administrative functions
- `InternshipOpportunity.java` - Internship posting with slots management
- `Application.java` - Student application with status workflow
- `WithdrawalRequest.java` - Withdrawal request with approval workflow

#### ✓ Utilities (2 files)
- `CSVUtil.java` - Complete CSV operations (read, write, append, update, delete)
- `ValidationUtil.java` - ID generation, validation, date utilities

#### ✓ Exceptions (5 files)
- `ResourceNotFoundException.java` - 404 errors
- `InvalidInputException.java` - 400 errors
- `UnauthorizedException.java` - 401 errors
- `BusinessRuleException.java` - 422 errors
- `GlobalExceptionHandler.java` - Centralized exception handling

#### ✓ CSV Data Files (6 files)
- `student_list.csv` - 5 sample students with default passwords
- `staff_list.csv` - 3 sample staff members
- `company_representative_list.csv` - Empty, ready for registrations
- `internship_opportunities.csv` - Empty with headers
- `applications.csv` - Empty with headers
- `withdrawal_requests.csv` - Empty with headers

### Backend Components - **TO BE IMPLEMENTED**

#### ⏳ DTOs (9 files needed)
Located in: `src/main/java/dto/`
- `ApiResponse.java` - Generic API response wrapper
- `LoginRequest.java` / `LoginResponse.java`
- `ChangePasswordRequest.java`
- `CreateInternshipRequest.java`
- `ApplyInternshipRequest.java`
- `WithdrawalRequestDTO.java`
- `ApprovalDecisionRequest.java`
- `ReportFilterRequest.java`
- `InternshipDTO.java`

#### ⏳ Repository Interfaces (7 files needed)
Located in: `src/main/java/repository/`
- `UserRepository.java`
- `StudentRepository.java`
- `CompanyRepresentativeRepository.java`
- `CareerCenterStaffRepository.java`
- `InternshipOpportunityRepository.java`
- `ApplicationRepository.java`
- `WithdrawalRequestRepository.java`

#### ⏳ Repository Implementations (6 files needed)
Located in: `src/main/java/repository/impl/`
- `CsvStudentRepository.java` - Thread-safe with ReentrantReadWriteLock
- `CsvCompanyRepresentativeRepository.java`
- `CsvCareerCenterStaffRepository.java`
- `CsvInternshipOpportunityRepository.java`
- `CsvApplicationRepository.java`
- `CsvWithdrawalRequestRepository.java`

#### ⏳ Service Interfaces (4 files needed)
Located in: `src/main/java/service/`
- `AuthenticationService.java`
- `StudentService.java`
- `CompanyRepresentativeService.java`
- `StaffService.java`

#### ⏳ Service Implementations (4 files needed)
Located in: `src/main/java/service/impl/`
- `AuthenticationServiceImpl.java`
- `StudentServiceImpl.java`
- `CompanyRepresentativeServiceImpl.java`
- `StaffServiceImpl.java`

#### ⏳ Controllers (5 files needed)
Located in: `src/main/java/controller/`
- `AuthenticationController.java` - Login/logout/change password
- `StudentController.java` - 7 endpoints for student operations
- `CompanyRepresentativeController.java` - 7 endpoints for representative operations
- `StaffController.java` - 7 endpoints for staff operations
- `HealthController.java` - Health check endpoint

#### ⏳ Configuration (1 file needed)
Located in: `src/main/java/config/`
- `WebConfig.java` - CORS configuration for http://localhost:5173

### Frontend Components - **NOT YET STARTED**

All frontend files need to be created in `frontend/` directory following React + Vite structure.

## 🏗️ Project Structure

```
Internship Placement Management System/
├── pom.xml                                   ✓ DONE
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── InternshipPlacementApplication.java  ✓ DONE
│   │   │   ├── enums/                        ✓ DONE (5 files)
│   │   │   ├── model/                        ⏳ TODO (7 files)
│   │   │   ├── util/                         ✓ DONE (2 files)
│   │   │   ├── exception/                    ✓ DONE (5 files)
│   │   │   ├── dto/                          ⏳ TODO (9 files)
│   │   │   ├── repository/                   ⏳ TODO (7 interfaces)
│   │   │   ├── repository/impl/              ⏳ TODO (6 implementations)
│   │   │   ├── service/                      ⏳ TODO (4 interfaces)
│   │   │   ├── service/impl/                 ⏳ TODO (4 implementations)
│   │   │   ├── controller/                   ⏳ TODO (5 files)
│   │   │   └── config/                       ⏳ TODO (1 file)
│   │   └── resources/
│   │       ├── application.properties        ✓ DONE
│   │       └── data/                         ✓ DONE (6 CSV files)
│   └── test/                                  ⏳ TODO (Test structure)
├── frontend/                                  ⏳ TODO (React application)
```

## 🚀 Quick Start Guide

### Prerequisites
- Java 21 (OpenJDK 21.0.8 or higher)
- Maven 3.8+
- Node.js 18+ (for frontend)
- VS Code or IntelliJ IDEA

### Building the Backend

```powershell
# Navigate to project root
cd "d:\My Workspace\Studies\NTU\Year-2\Semester-1\SC2002 OBJECT ORIENTED DESIGN & PROGRAMMING\Internship Placement Management System"

# Clean and compile
mvn clean compile

# Run tests (once test files are created)
mvn test

# Package application
mvn package

# Run application
mvn spring-boot:run
```

The backend will start on http://localhost:7070

### API Base URL
```
http://localhost:7070/api
```

## 📋 Key Business Rules Implemented in Models

### Student Rules
- ✓ Maximum 3 concurrent applications
- ✓ Year 1-2: BASIC level internships only
- ✓ Year 3-4: All levels allowed
- ✓ Only 1 accepted placement
- ✓ Cannot apply after accepting placement
- ✓ Business logic methods: `canApplyForLevel()`, `hasMaxApplications()`, `canSubmitApplication()`

### Company Representative Rules
- ✓ Maximum 5 internship opportunities
- ✓ Must be authorized before login
- ✓ Business logic methods: `canCreateInternship()`, `canLogin()`, `authorize()`

### Internship Opportunity Rules
- ✓ Maximum 10 slots per internship
- ✓ Auto-mark FILLED when all slots confirmed
- ✓ Visibility toggle functionality
- ✓ Business logic methods: `isAcceptingApplications()`, `confirmPlacement()`, `releasePlacement()`

### Application Rules
- ✓ Default status: PENDING
- ✓ Status workflow validation
- ✓ Placement acceptance logic
- ✓ Business logic methods: `canAcceptPlacement()`, `approve()`, `reject()`, `withdraw()`

## 🔑 Default Credentials

All users have default password: `password`

### Sample Students
- U2345123F - John Tan (Year 3, CSC)
- U3456789G - Mary Lee (Year 2, EEE)
- U4567890H - David Wong (Year 4, MAE)
- U5678901I - Sarah Lim (Year 1, CSC)
- U6789012J - James Chen (Year 3, EEE)

### Sample Staff
- staff001 - Dr. Emily Ng (Career Services)
- staff002 - Prof. Michael Tan (Student Affairs)
- staff003 - Ms. Rachel Koh (Internship Coordination)

## 🎓 OOP Principles Demonstrated

### Inheritance
- `User` abstract base class
- `Student`, `CompanyRepresentative`, `CareerCenterStaff` extend `User`

### Polymorphism
- `getDisplayInfo()` abstract method overridden in each subclass

### Encapsulation
- Private fields with getter/setter methods (Lombok `@Data`)
- Business logic encapsulated in model methods

### Abstraction
- Repository interfaces abstract CSV operations
- Service interfaces abstract business logic

## 📖 Documentation

### JavaDoc Generation

Generate comprehensive API documentation for all Java classes:

```powershell
# Generate JavaDoc documentation
mvn javadoc:javadoc
```

**Output Location:** `docs/javadoc/index.html`

**View Documentation:**
```powershell
# Windows - Open in default browser
start docs\javadoc\index.html

# Or navigate directly
cd docs\javadoc
start index.html
```

**What's Documented:**
- ✅ All model classes with business logic
- ✅ All enum types and constants
- ✅ All utility classes and helper methods
- ✅ All exception classes
- ✅ DTOs and configuration classes (when implemented)
- ✅ Service and repository interfaces (when implemented)
- ✅ REST controllers (when implemented)

**Documentation Standards:**
- Every public class includes purpose, features, and examples
- Every method includes parameters, return values, and exceptions
- Business rules are clearly highlighted
- Cross-references between related classes
- Usage examples where applicable

See `docs/javadoc/README.md` for more details about documentation standards.

## ⚠️ Important Notes

1. **No Database**: All data stored in CSV files as per assignment requirements
2. **Thread Safety**: CSV operations are synchronized
3. **Default Passwords**: All users start with password "password"
4. **ID Formats**:
   - Students: U followed by 7 digits and letter (U2345123F)
   - Company Reps: Email address
   - Staff: NTU account
5. **Business Rules**: Implemented in model classes with validation methods

## 🐛 Known Issues / TODO
- [ ] Frontend not yet implemented
- [ ] Repository layer not yet implemented
- [ ] Service layer not yet implemented
- [ ] Controller layer not yet implemented
- [ ] Test classes not yet implemented
- [ ] UML diagrams not yet created

## 📄 License
Academic project for SC2002 - NTU Singapore
