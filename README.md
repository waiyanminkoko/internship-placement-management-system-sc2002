<div align="center">
  <img src="frontend/public/images/internship-logo-png.png" alt="Internship Placement Management System Logo" width="100"/>
</div>

# SC2002 Internship Placement Management System

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Java](https://img.shields.io/badge/Java-21-blue)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)]()
[![React](https://img.shields.io/badge/React-18.2-blue)]()

> **A comprehensive web-based platform for managing internship opportunities, applications, and placement workflows.**  
> Developed as part of SC2002 Object-Oriented Design & Programming course at NTU, Singapore.

---

## 🎯 Project Overview

**Course**: SC2002 Object-Oriented Design & Programming
**Tutorial Group**: SCED, Group-6
**Institution**: Nanyang Technological University, Singapore  
**System**: Internship Placement Management System  
**Architecture**: Spring Boot REST API Backend + React Frontend  
**Data Storage**: CSV Files (No Database as per assignment requirements)  
**Status**: ✅ **COMPLETE & FULLY FUNCTIONAL**

This system demonstrates comprehensive application of Object-Oriented Programming principles including inheritance, polymorphism, encapsulation, and abstraction. It implements a role-based workflow management system for internship placements with three distinct user roles: Students, Company Representatives, and Career Center Staff.

---

## ✨ Key Features

### Core Functionality
- **Role-Based Access Control**: Three distinct user roles with specific permissions and workflows
- **Internship Management**: Create, approve, and publish internship opportunities with slot management
- **Application Workflow**: Complete application lifecycle from submission to placement acceptance
- **Approval System**: Multi-level approval workflows for representatives, internships, and withdrawals
- **Withdrawal Management**: Request and approve withdrawal from accepted placements
- **Advanced Reporting**: Generate filtered reports with downloadable PDF export
- **Account Creation**: Staff can create student and company representative accounts for testing

### Business Rules Enforced
- **Student Eligibility**: Year 1-2 students limited to BASIC level internships; Year 3-4 can apply to all levels
- **Application Limits**: Maximum 3 concurrent applications per student
- **Placement Rules**: Only 1 accepted placement per student; all other applications auto-withdrawn upon acceptance
- **Capacity Management**: Automatic status updates based on internship slot availability
- **Authorization**: Company representatives require staff approval before system access

### Additional Features
- **Thread-Safe Operations**: Concurrent CSV file access with read-write locks
- **Data Validation**: Comprehensive input validation and business rule enforcement
- **RESTful API**: 30+ well-documented API endpoints
- **Responsive UI**: Material Design interface optimized for all screen sizes
- **Real-Time Updates**: Instant feedback with toast notifications

---

## 🛠️ Technology Stack

### Backend
- **Java 21** (OpenJDK) - Core programming language
- **Spring Boot 3.2.0** - Application framework with dependency injection
- **Maven 3.8+** - Build automation and dependency management
- **OpenCSV 5.9** - CSV file operations and persistence
- **Lombok** - Reduces boilerplate code

### Frontend
- **React 18.2.0** - UI component library
- **Vite 5.4.1** - Build tool and development server
- **Material-UI 5.14** - Component library and design system
- **Axios 1.6.2** - HTTP client for API communication
- **React Router DOM 6.20** - Client-side routing
- **jsPDF & html2canvas** - PDF report generation

### Architecture Patterns
- **Layered Architecture**: Controller → Service → Repository separation
- **RESTful API Design**: Stateless HTTP-based communication
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Decoupled API contracts
- **Dependency Injection**: Spring IoC container

---

## 🚀 Quick Start

### Prerequisites
Ensure the following are installed on your system:
- **Java 21** or higher ([Download OpenJDK](https://adoptium.net/))
- **Maven 3.8+** ([Download Maven](https://maven.apache.org/download.cgi))
- **Node.js 18+** and npm ([Download Node.js](https://nodejs.org/))
- **Git** ([Download Git](https://git-scm.com/downloads))

#### Quick Install (Windows)

**Install Java 21:**
1. Download from [Adoptium](https://adoptium.net/temurin/releases/?version=21)
2. Run installer → Check "Set JAVA_HOME" and "Add to PATH"
3. Verify: `java -version`

**Install Maven:**
1. Download from [Maven Downloads](https://maven.apache.org/download.cgi) (Binary zip)
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH: `C:\Program Files\Apache\maven\bin`
4. Verify: `mvn -version`

**Install Node.js:**
1. Download from [nodejs.org](https://nodejs.org/) (LTS version)
2. Run installer → Accept defaults (includes npm)
3. Verify: `node -v` and `npm -v`

Verify all installations:
```powershell
java -version    # Should show Java 21+
mvn -version     # Should show Maven 3.8+
node -v          # Should show Node 18+
npm -v           # Should show npm version
```

### Installation Steps

1. **Clone the repository**
   ```powershell
   git clone https://github.com/waiyanminkoko/internship-placement-management-system-sc2002.git
   cd internship-placement-management-system-sc2002
   ```

2. **Compile the backend** (first time only)
   ```powershell
   mvn clean compile
   ```

3. **Start the complete system**
   ```powershell
   .\start-system.ps1
   ```

   This script will:
   - Start the Spring Boot backend server on `http://localhost:6060`
   - Start the React frontend development server on `http://localhost:5173`
   - Open both in separate terminal windows
   - Automatically open the application in your browser

4. **Access the application**
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:6060/api
   - API Health Check: http://localhost:6060/api/health

---

## 📖 Installation and Run Guide

### Running the Application

#### Option 1: Start Complete System (Recommended)
```powershell
# Start both backend and frontend servers
.\start-system.ps1
```
This will automatically:
- Start Spring Boot backend on `http://localhost:6060`
- Start React frontend on `http://localhost:5173`
- Open both in separate terminal windows

#### Option 2: Start Servers Individually

**Backend Only:**
```powershell
# Using startup script
.\start-backend.ps1

# OR manually
mvn spring-boot:run
```

**Frontend Only:**
```powershell
# Using startup script
.\start-frontend.ps1

# OR manually
cd frontend
npm install  # First time only
npm run dev
```

#### Option 3: System Health Check
```powershell
# Verify both servers are running
.\check-system.ps1
```

### Default Login Credentials

Sample users for testing (most users have default password: **`password`**):

**Students:**
| Student ID | Name | Password | Year | Major |
|------------|------|----------|------|-------|
| `U2310001A` | Tan Wei Ling | `password` | 2 | Computer Science |
| `U2310002B` | Ng Jia Hao | `password` | 3 | Data Science & AI |
| `U2310003C` | Lim Yi Xuan | `password` | 4 | Computer Engineering |
| `U2310004D` | Chong Zhi Hao | `password` | 1 | Data Science & AI |
| `U2310005E` | Wong Shu Hui | `password` | 3 | Computer Science |
| `U2310006F` | Gerald Tang | `password` | 2 | Computer Science |
| `U2310007H` | Tan Jiong Hao | `password` | 2 | Computer Science |
| `U2310008G` | Jasmine Tan | `password` | 2 | Computer Science |
| `U2310009G` | Jamie Lee | `password` | 2 | Computer Science |

**Company Representatives:**
| Email | Name | Password | Company | Status |
|-------|------|----------|---------|--------|
| `charlielim@gmail.com` | Charlie Lim | `newpassword` | Microsoft | Active |
| `henrytan@gmail.com` | Henry Tan | `password` | ST Engineering | Active |
| `boblee@gmail.com` | Bob Lee | `password` | Google | Active |
| `ryanteo@gmail.com` | Ryan Teo | `password` | OpenAI | Active |
| `alicetan@gmail.com` | Alice Tan | `password` | SAP | Active |
| `michelle@gmail.com` | Michelle Richardson | `password` | Meta | Active |

**Career Center Staff:**
| Staff ID | Name | Password | Department |
|----------|------|----------|------------|
| `tan002` | Mr. Tan Boon Kiat | `password` | CCDS |
| `lee003` | Ms. Lee Mei Ling | `password` | CCDS |
| `sng001` | Dr. Sng Hui Lin | `password` | CCDS |

### Accessing the Application

- **Frontend Interface**: http://localhost:5173
- **Backend API**: http://localhost:6060/api
- **Health Check**: http://localhost:6060/api/health

---

## 📂 CSV Data Storage

All system data is persisted in CSV files. These files are located in two places:

### Source Files (Original)
**Location**: `src/main/resources/data/`

Contains 6 CSV files:
- `student_list.csv` - Student accounts and profiles
- `company_representative_list.csv` - Company representative accounts
- `staff_list.csv` - Career center staff accounts
- `internship_opportunities.csv` - Internship postings
- `applications.csv` - Student applications
- `withdrawal_requests.csv` - Withdrawal requests

### Runtime Files (Active)
**Location**: `target/classes/data/`

During application runtime, the system reads and writes to CSV files in this location. These files are automatically copied from `src/main/resources/data/` when you build the project with Maven.

**Important Notes:**
- ✅ View/edit source files in `src/main/resources/data/` before starting the application
- ✅ Active data modifications are written to `target/classes/data/` during runtime
- ✅ After stopping the application, copy files from `target/classes/data/` back to `src/main/resources/data/` to persist changes
- ✅ Running `mvn clean` will delete the `target/` directory and reset data to source files

---

## 📁 Folder Structure

```
internship-placement-management-system/
│
├── src/
│   └── main/
│       ├── java/                    # Backend Java source code
│       │   ├── app/                 # Application entry points
│       │   ├── config/              # Spring configuration
│       │   ├── controller/          # REST API controllers
│       │   ├── dto/                 # Data Transfer Objects
│       │   ├── enums/               # Enumerations
│       │   ├── exception/           # Custom exceptions
│       │   ├── model/               # Domain models
│       │   ├── repository/          # Data access layer
│       │   │   └── impl/            # Repository implementations
│       │   ├── service/             # Business logic layer
│       │   │   └── impl/            # Service implementations
│       │   └── util/                # Utility classes
│       │
│       └── resources/
│           ├── application.properties    # Spring Boot config
│           └── data/                     # CSV data files (source)
│
├── frontend/
│   ├── public/                      # Static assets
│   ├── src/                         # React source code
│   │   ├── components/              # Reusable UI components
│   │   ├── context/                 # React Context providers
│   │   ├── hooks/                   # Custom React hooks
│   │   ├── pages/                   # Page components
│   │   ├── services/                # API service layer
│   │   └── utils/                   # Helper functions
│   ├── package.json                 # Node dependencies
│   └── vite.config.js               # Vite configuration
│
├── docs/
│   ├── api/                         # API documentation
│   ├── javadoc/                     # Generated JavaDoc
│   └── uml/                         # UML diagrams
│       ├── class-diagrams/          # Class diagrams (.puml)
│       ├── sequence-diagrams/       # Sequence diagrams (.puml)
│       └── exported-images/         # Exported diagram images
│
├── logs/                            # Application log files
│
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── start-backend.ps1                # Backend startup script
├── start-frontend.ps1               # Frontend startup script
├── start-system.ps1                 # Full system startup script
└── check-system.ps1                 # Health check script
```

---

## 🎓 Object-Oriented Design

This project demonstrates key OOP principles taught in SC2002:

### Inheritance
- Abstract `User` base class extended by `Student`, `CompanyRepresentative`, and `CareerCenterStaff`
- Promotes code reuse for common user attributes and behaviors

### Polymorphism
- Method overriding: Each user type implements `getDisplayInfo()` differently
- Dynamic binding: Authentication handles all user types uniformly

### Encapsulation
- Private fields with controlled access via getters/setters (Lombok `@Data`)
- Business logic encapsulated within model methods

### Abstraction
- Repository interfaces abstract data persistence details
- Service interfaces define business contracts
- Clients interact with abstractions, not implementations

---

## 📚 Documentation

### JavaDoc API Documentation

Comprehensive API documentation for all Java classes is available in JavaDoc format.

**Location**: `docs/javadoc/index.html`

**To View:**
```powershell
# Windows - Open in default browser
start docs\javadoc\index.html

# Or navigate to the docs/javadoc folder and open index.html
```

**What's Documented:**
- All model classes with business logic and validation rules
- Repository interfaces and implementations
- Service layer with business operations
- REST API controllers with endpoint documentation
- DTOs, enums, utilities, and exception classes
- Method parameters, return types, and exceptions
- Usage examples and code samples

**Generate Updated JavaDoc:**
```powershell
# Generate/regenerate JavaDoc documentation
mvn javadoc:javadoc

# Documentation will be created in docs/javadoc/
```

### UML Diagrams

Complete UML diagrams documenting system architecture and design.

**Location**: `docs/uml/`

**Available Diagrams:**

**Class Diagrams** (`docs/uml/class-diagrams/`):
- `02-model-hierarchy.puml` - Complete domain model relationships
- `03-backend-oop-class-diagram.puml` - Backend architecture with design patterns
- `04-backend-simplified-abstract.puml` - Simplified backend abstraction view

**Sequence Diagrams** (`docs/uml/sequence-diagrams/`):
- `01-student-application-workflow.puml` - Complete student application submission workflow
- `02-company-rep-application-review.puml` - Company representative reviews and manages applications
- `02-company-rep-approve-application-simplified.puml` - Simplified application approval process
- `03-company-rep-registration-approval.puml` - Company representative registration and approval workflow
- `04-authentication-authorization.puml` - User authentication and authorization process

**Exported Images** (`docs/uml/exported-images/`):
- All diagrams exported as PNG/SVG for easy viewing

**To View PlantUML Diagrams:**

Option 1: View Exported Images
```powershell
# Navigate to exported images folder
cd docs\uml\exported-images

# Images are ready to view in any image viewer
```

Option 2: Generate from Source (Requires PlantUML)
```powershell
# Navigate to UML directory
cd docs\uml

# Run export script to generate all diagrams
.\export-diagrams.ps1

# Generated PNG/SVG files will be in exported-images/
```

Option 3: Use VS Code Extension
1. Install "PlantUML" extension in VS Code
2. Open any `.puml` file in `docs/uml/class-diagrams/` or `docs/uml/sequence-diagrams/`
3. Press `Alt+D` to preview the diagram

**UML Documentation**: See `docs/uml/README.md` for detailed diagram descriptions and design rationale.

---

## 👥 Team & Acknowledgments

**SC2002 Group 6 - Nanyang Technological University, Singapore**

This project demonstrates comprehensive understanding of:
- Object-Oriented Programming principles
- Design patterns and best practices
- RESTful API design and development
- Full-stack web application architecture
- Thread-safe concurrent programming
- Software engineering methodologies

**Acknowledgments:**
- NTU College of Computing and Data Science (CCDS)
- Spring Boot, React, and open-source communities

---