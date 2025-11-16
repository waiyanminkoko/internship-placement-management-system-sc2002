# Documentation

This directory contains comprehensive documentation for the Internship Placement Management System.

## 📂 Directory Structure

```
docs/
├── javadoc/                        # ✅ Generated JavaDoc API documentation
│   ├── model/                      # Model class documentation
│   ├── controller/                 # Controller documentation
│   ├── service/                    # Service layer documentation
│   ├── repository/                 # Repository documentation
│   ├── dto/                        # Data Transfer Objects
│   ├── enums/                      # Enum types documentation
│   ├── util/                       # Utility classes
│   ├── exception/                  # Exception classes
│   ├── config/                     # Configuration classes
│   ├── resources/                  # Resources documentation
│   ├── legal/                      # Legal information
│   ├── script-dir/                 # Script directory
│   └── class-use/                  # Class usage documentation
├── uml/                            # ✅ UML Diagrams (PlantUML)
│   ├── class-diagrams/             # Class diagram source files (.puml)
│   ├── sequence-diagrams/          # Sequence diagram source files (.puml)
│   └── exported-images/            # Generated diagram images (PNG/SVG)
└── api/                            # ✅ REST API Documentation
```

## 📚 Contents

### 1. JavaDoc Documentation
**Location:** `docs/javadoc/`  
**Entry Point:** `docs/javadoc/index.html`  
**Status:** ✅ Fully Generated and Up-to-Date

Comprehensive HTML documentation for all Java classes, interfaces, and methods in the system.

**Coverage:**
- **Main Application Classes**: `InternshipPlacementApplication.java`, `InternshipPlacementApplication_terminal.java`
- **Model Layer**: User hierarchy (Student, CompanyRepresentative, CareerCenterStaff), InternshipOpportunity, Application, WithdrawalRequest
- **Controller Layer**: AuthenticationController, StudentController, CompanyRepresentativeController, StaffController, HealthController
- **Service Layer**: StudentService, CompanyRepService, StaffService, AuthenticationService
- **Repository Layer**: UserRepository, InternshipRepository, ApplicationRepository, WithdrawalRepository
- **DTO Classes**: ApiResponse, LoginRequest, ApplyInternshipRequest, ApprovalDecisionRequest, and more
- **Enums**: UserRole, ApplicationStatus, InternshipLevel, InternshipStatus, Major, ApprovalStatus
- **Utilities**: CSVUtil, IdGenerator, ValidationUtil, DateTimeUtil, PasswordUtil
- **Exceptions**: Custom exception classes for error handling
- **Configuration**: WebConfig (CORS configuration)

**How to Generate:**
```bash
# Generate JavaDoc
mvn javadoc:javadoc

# Output location
target/site/apidocs/index.html
```

**How to View:**
```bash
# Open in default browser (Windows)
start docs/javadoc/index.html

# Or navigate to
docs/javadoc/index.html
```

📖 **Detailed Guide:** See [`docs/javadoc/README.md`](./javadoc/README.md)

---

### 2. UML Diagrams
**Location:** `docs/uml/`  
**Status:** ✅ Complete with Export Functionality  

Comprehensive UML diagrams documenting both static structure (class diagrams) and dynamic behavior (sequence diagrams).

#### 2.1 Class Diagrams (`uml/class-diagrams/`)

Visual representations of the system's architecture, class structure, and relationships.

**Available Diagrams:**
- **`02-model-hierarchy.puml`** - User model hierarchy and inheritance
- **`03-backend-oop-class-diagram.puml`** - Complete backend OOP class structure
- **`04-backend-simplified-abstract.puml`** - Simplified abstract view of backend architecture

**Key Features:**
- Inheritance relationships (User → Student, CompanyRepresentative, CareerCenterStaff)
- Design patterns (Repository, Service Layer, MVC, DTO)
- Detailed class attributes and methods
- Relationship cardinalities and types

#### 2.2 Sequence Diagrams (`uml/sequence-diagrams/`)

Visual representations of system workflows and component interactions.

**Available Diagrams:**
- **`01-student-application-workflow.puml`** - Student application submission process
- **`02-company-rep-application-review.puml`** - Application review by company representative
- **`02-company-rep-approve-application-simplified.puml`** - Simplified application approval process
- **`03-company-rep-registration-approval.puml`** - Company representative registration workflow
- **`04-authentication-authorization.puml`** - User authentication and role-based access

**Key Features:**
- Actor-to-system interactions
- Controller → Service → Repository call chains
- Data persistence operations
- Error handling flows
- Business rule validations

#### 2.3 Exported Images (`uml/exported-images/`)

Pre-generated PNG and SVG format images of all diagrams for easy viewing and documentation.

**How to Export Diagrams:**
```powershell
# Navigate to UML directory
cd docs\uml

# Run export script (auto-downloads PlantUML if needed)
.\export-diagrams.ps1

# Output: exported-images/*.png and exported-images/*.svg
```

**Prerequisites:**
- Java Runtime Environment (JRE) - Required to run PlantUML
- PlantUML JAR - Auto-downloaded by export script or place in `docs/` folder
- Graphviz (optional) - For enhanced rendering

**How to View/Edit:**
- **VS Code** (Recommended): Install PlantUML extension (`jebbs.plantuml`), open `.puml` files, press `Alt+D` to preview
- **Online**: Visit https://www.plantuml.com/plantuml/uml/ and paste diagram code
- **Command Line**: `java -jar docs/plantuml.jar docs/uml/class-diagrams/*.puml`

📖 **Comprehensive Guide:** See [`docs/uml/README.md`](./uml/README.md)  
📖 **Class Diagrams Guide:** See [`docs/uml/class-diagrams/README.md`](./uml/class-diagrams/README.md)  
📖 **Sequence Diagrams Guide:** See [`docs/uml/sequence-diagrams/README.md`](./uml/sequence-diagrams/README.md)

---

### 3. API Documentation
**Location:** `docs/api/README.md`  
**Base URL:** `http://localhost:6060/api`  
**Version:** 2.0.0  
**Status:** ✅ Complete and Operational

Comprehensive REST API documentation with detailed endpoint specifications, request/response examples, and complete workflow guides.

**Contents:**
- **Authentication Endpoints** - Login, logout, registration, password management
- **Student Endpoints** - Browse internships, apply, view applications, accept placements, request withdrawals
- **Company Representative Endpoints** - Create/manage internships, review applications, approve/reject
- **Staff Endpoints** - Approve internships, authorize representatives, process withdrawals, generate reports, create accounts
- **Health Check Endpoints** - Server status and API information
- **Request/Response Format** - Standardized JSON responses using `ApiResponse<T>` wrapper
- **Error Handling** - HTTP status codes, exception types, error examples
- **Complete Workflow Examples** - End-to-end scenarios with actual requests/responses
- **Testing Guide** - cURL, PowerShell, Postman, JavaScript/Fetch, React Query examples

**Key Features:**
- All 30+ endpoints fully documented
- Request body schemas with field descriptions
- Success and error response examples
- Business rules and constraints
- CORS configuration details
- Security considerations
- Data model overview
- API versioning strategy

**Quick Access:**
```bash
# View API documentation
start docs/api/README.md

# Or open in browser
http://localhost:6060/api/health
```

📖 **Full API Reference:** See [`docs/api/README.md`](./api/README.md)

## 🔧 Generating Documentation

### 1. Generate JavaDocs

**Using Maven:**
```bash
# Generate JavaDoc (recommended)
mvn javadoc:javadoc

# Output: target/site/apidocs/
# View: target/site/apidocs/index.html
```

**Note:** Current JavaDocs are already generated and available at `docs/javadoc/index.html`

---

### 2. Export UML Diagrams

**Using PowerShell Script (Recommended):**
```powershell
# Navigate to UML directory
cd docs\uml

# Run the export script
.\export-diagrams.ps1

# This will:
# 1. Check for PlantUML JAR (auto-download if missing)
# 2. Export all .puml files to PNG and SVG
# 3. Save to exported-images/ directory
# 4. Open the output directory
```

**Manual Export:**
```powershell
# Export specific diagram
java -jar docs\plantuml.jar docs\uml\class-diagrams\02-model-hierarchy.puml

# Export all class diagrams
java -jar docs\plantuml.jar docs\uml\class-diagrams\*.puml

# Export all sequence diagrams
java -jar docs\plantuml.jar docs\uml\sequence-diagrams\*.puml

# Export to SVG format
java -jar docs\plantuml.jar -tsvg docs\uml\sequence-diagrams\*.puml
```

**Prerequisites:**
- Java Runtime Environment (JRE) installed
- PlantUML JAR (auto-downloaded by script or place in `docs/` folder)
- Graphviz (optional, for better rendering)

**Verify Java Installation:**
```powershell
java -version
```

---

### 3. Generate Complete Site

**Using Maven Site Plugin:**
```bash
# Generate complete site with JavaDoc and reports
mvn site

# Output: target/site/
# View: target/site/index.html
```

---

## 📖 Documentation Usage Guide

### For Different Audiences

#### 👨‍💻 **For Developers**
- **JavaDoc**: Browse `docs/javadoc/index.html` for detailed API information
  - Find class methods, parameters, return types
  - Understand method behavior and exceptions
  - View inheritance hierarchies
- **UML Class Diagrams**: Understand system architecture and relationships
- **API Documentation**: Reference REST endpoints for backend integration

#### 🏗️ **For System Architects**
- **UML Class Diagrams**: Analyze static structure and design patterns
- **UML Sequence Diagrams**: Understand dynamic behavior and workflows
- **Model Hierarchy**: Review `02-model-hierarchy.puml` for user model structure
- **OOP Principles**: See `03-backend-oop-class-diagram.puml` for OOP design

#### 🎨 **For Frontend Developers**
- **API Documentation**: Essential reference for REST API integration
  - Endpoint URLs and HTTP methods
  - Request/response formats
  - Authentication requirements
  - Error handling
- **Workflow Examples**: Complete integration scenarios with actual code
- **Testing Guide**: cURL, Postman, Fetch API, React Query examples

#### 📊 **For Project Stakeholders**
- **Sequence Diagrams**: Understand business workflows
  - Student application process
  - Company representative registration and approval
  - Staff administrative tasks
- **API Overview**: High-level understanding of system capabilities

#### 🧪 **For QA/Testers**
- **API Documentation**: Complete endpoint specifications for testing
- **Sequence Diagrams**: Expected system behavior and flows
- **Business Rules**: Constraints and validation rules to verify

---

## 🔄 Updating Documentation

### When to Update

| Trigger | Documentation to Update |
|---------|------------------------|
| Add/modify Java class | Regenerate JavaDoc |
| Change class relationships | Update UML class diagrams |
| Add/modify workflow | Update UML sequence diagrams |
| Add/modify REST endpoint | Update API documentation |
| Change business rules | Update API docs and sequence diagrams |
| Modify data models | Update JavaDoc, UML diagrams, API docs |

### How to Update Each Type

#### ✏️ **Updating JavaDoc**

1. Add/update JavaDoc comments in source code:
   ```java
   /**
    * Applies for an internship opportunity.
    *
    * @param studentId the student's unique identifier
    * @param internshipId the internship opportunity ID
    * @return ApplicationDTO the created application
    * @throws MaxApplicationsReachedException if student has 3 active applications
    * @throws DuplicateApplicationException if already applied to this internship
    */
   public ApplicationDTO applyForInternship(String studentId, String internshipId) {
       // Implementation
   }
   ```

2. Regenerate JavaDoc:
   ```bash
   mvn javadoc:javadoc
   ```

3. Verify output at `target/site/apidocs/index.html`

#### ✏️ **Updating UML Class Diagrams**

1. Open the relevant `.puml` file in `docs/uml/class-diagrams/`
2. Edit using PlantUML syntax (see examples in existing files)
3. Preview in VS Code (Alt+D) or online at https://www.plantuml.com/plantuml/uml/
4. Save changes
5. Re-export diagrams:
   ```powershell
   cd docs\uml
   .\export-diagrams.ps1
   ```

**Example Edit:**
```plantuml
' Add a new method to Student class
class Student {
    - studentId: String
    - name: String
    + newMethod(): void  ' <- Added this line
}
```

#### ✏️ **Updating UML Sequence Diagrams**

1. Open the relevant `.puml` file in `docs/uml/sequence-diagrams/`
2. Add/modify interactions:
   ```plantuml
   Student -> StudentController: submitApplication()
   StudentController -> StudentService: createApplication()
   StudentService -> ValidationService: validateEligibility()  ' <- New interaction
   ValidationService --> StudentService: validationResult
   StudentService -> ApplicationRepository: save()
   ```
3. Preview and save
4. Re-export diagrams

#### ✏️ **Updating API Documentation**

1. Open `docs/api/README.md`
2. Add/modify endpoint documentation following the existing format:
   ```markdown
   ### X. New Endpoint Name
   **POST** `/api/path/to/endpoint`
   
   Description of what the endpoint does.
   
   **Request Body:**
   ```json
   {
     "field": "value"
   }
   ```
   
   **Success Response (200 OK):**
   ```json
   {
     "success": true,
     "data": { ... },
     "message": "Success message"
   }
   ```
   ```
3. Add to table of contents
4. Update version number if needed
5. Update "Last Updated" date

---

## 📝 Documentation Standards

### JavaDoc Standards
- Follow Oracle Java documentation guidelines
- Include `@param`, `@return`, `@throws` tags
- Provide clear, concise descriptions
- Document all public and protected members
- Include usage examples for complex methods

### UML Diagram Standards
- Follow UML 2.5 notation
- Use consistent naming conventions
- Include visibility modifiers (+, -, #, ~)
- Show multiplicities for relationships
- Add comments for complex relationships

### API Documentation Standards
- Include all required and optional parameters
- Provide complete request/response examples
- Document all possible error responses
- Include HTTP status codes
- Add usage examples for complex workflows
- Keep response format consistent

---

## 🔗 Quick Links

### Documentation Files
- **Main README**: [`../../README.md`](../../README.md)
- **JavaDoc Entry**: [`javadoc/index.html`](./javadoc/index.html)
- **JavaDoc Guide**: [`javadoc/README.md`](./javadoc/README.md)
- **UML Guide**: [`uml/README.md`](./uml/README.md)
- **Class Diagrams**: [`uml/class-diagrams/README.md`](./uml/class-diagrams/README.md)
- **Sequence Diagrams**: [`uml/sequence-diagrams/README.md`](./uml/sequence-diagrams/README.md)
- **API Reference**: [`api/README.md`](./api/README.md)

### External Resources
- **PlantUML Official**: https://plantuml.com/
- **PlantUML Class Diagrams**: https://plantuml.com/class-diagram
- **PlantUML Sequence Diagrams**: https://plantuml.com/sequence-diagram
- **Oracle JavaDoc Guide**: https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html
- **UML 2.5 Specification**: https://www.omg.org/spec/UML/2.5/
- **REST API Best Practices**: https://restfulapi.net/

---

## 🚨 Troubleshooting

### Issue: JavaDoc Generation Fails

**Solution:**
```bash
# Clean and regenerate
mvn clean
mvn javadoc:javadoc

# Check for compilation errors first
mvn compile
```

### Issue: PlantUML Export Script Doesn't Run

**Solution:**
```powershell
# Enable script execution (run as Administrator)
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser

# Verify Java is installed
java -version

# Download PlantUML manually if needed
# Place plantuml.jar in docs/ folder
```

### Issue: Diagrams Don't Render in VS Code

**Solution:**
1. Install PlantUML extension: `Ctrl+Shift+X` → Search "PlantUML"
2. Install Graphviz (optional but recommended):
   ```powershell
   choco install graphviz
   ```
3. Restart VS Code

### Issue: "Java not found" Error

**Solution:**
```powershell
# Install Java (using Chocolatey)
choco install openjdk

# Or download from: https://www.java.com/download/

# Verify installation
java -version
```

---

## 📊 Documentation Metrics

**Current Status (as of November 16, 2025):**

| Documentation Type | Status | Completeness | Last Updated |
|-------------------|--------|--------------|--------------|
| JavaDoc | ✅ Complete | 100% | Nov 16, 2025 |
| Class Diagrams | ✅ Complete | 100% | Nov 16, 2025 |
| Sequence Diagrams | ✅ Complete | 100% | Nov 16, 2025 |
| API Documentation | ✅ Complete | 100% | Nov 16, 2025 |
| Exported Images | ✅ Available | 100% | Nov 16, 2025 |

**Coverage:**
- **Classes Documented**: 74 classes/interfaces/enums
- **UML Diagrams**: 8 diagrams (3 class + 5 sequence)
- **API Endpoints**: 30+ endpoints
- **Exported Images**: 16 files (8 PNG + 8 SVG)

---

## 🤝 Contributing to Documentation

When adding or modifying documentation:

1. ✅ Follow existing format and style
2. ✅ Test that code examples work
3. ✅ Verify diagrams render correctly
4. ✅ Update table of contents if needed
5. ✅ Update "Last Updated" dates
6. ✅ Increment version numbers for breaking changes
7. ✅ Run generation scripts to verify output

**Commit Message Format:**
```bash
git commit -m "docs: [type] brief description"

# Examples:
git commit -m "docs: update API documentation for new endpoint"
git commit -m "docs: add sequence diagram for withdrawal workflow"
git commit -m "docs: regenerate JavaDoc after class changes"
```

---

## 📄 Additional Notes

### File Locations

**Documentation Source Files:**
- JavaDoc source: `src/main/java/**/*.java` (inline comments)
- UML source: `docs/uml/**/*.puml` (PlantUML files)
- API docs: `docs/api/README.md` (Markdown file)

**Generated/Output Files:**
- JavaDoc output: `docs/javadoc/` (already generated) and `target/site/apidocs/` (Maven output)
- UML images: `docs/uml/exported-images/` (PNG and SVG)
- Maven site: `target/site/` (when generated)

**Tools:**
- PlantUML JAR: `docs/plantuml.jar`
- Export script: `docs/uml/export-diagrams.ps1`

### Version Control

**What to Commit:**
- ✅ Source files (`.java`, `.puml`, `.md`)
- ✅ Generated JavaDoc in `docs/javadoc/`
- ✅ Exported UML images in `docs/uml/exported-images/`
- ✅ PlantUML JAR file `docs/plantuml.jar`

**What to Ignore:**
- ❌ `target/` directory (Maven build output)
- ❌ Temporary PlantUML files

### Maintenance Schedule

**Regular Updates:**
- After every significant code change
- Before each milestone delivery
- When adding new features
- When modifying existing workflows

**Quality Checks:**
- Verify all links work
- Ensure code examples compile and run
- Check that diagrams render correctly
- Validate API endpoint responses

---

**Last Updated**: November 16, 2025  
**Documentation Version**: 2.0.0  
**Project**: SC2002 Group 6 - Internship Placement Management System  
**Course**: Object-Oriented Design & Programming  
**Institution**: Nanyang Technological University

---

**Need Help?**
- 📖 Check individual README files in subdirectories
- 🔍 Search the JavaDoc for specific classes or methods
- 📧 Refer to project repository issues
