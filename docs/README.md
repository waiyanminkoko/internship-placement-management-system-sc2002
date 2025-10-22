# Documentation

This directory contains comprehensive documentation for the Internship Placement Management System.

## 📂 Directory Structure

```
docs/
├── javadoc/              # Generated JavaDoc API documentation
├── uml/                  # UML diagrams
│   ├── class-diagrams/   # UML Class Diagrams
│   └── sequence-diagrams/# UML Sequence Diagrams
├── api/                  # API documentation and examples
└── README.md            # This file
```

## 📚 Contents

### 1. JavaDoc Documentation
Location: `docs/javadoc/`

Generated HTML documentation for all Java classes, interfaces, and methods in the system.

**To Generate JavaDocs:**
```bash
mvn javadoc:javadoc
```

The generated documentation will be available at: `target/site/apidocs/index.html`

**Coverage:**
- All model classes (User, Student, CompanyRepresentative, etc.)
- All utility classes (CSVUtil, IdGenerator, ValidationUtil, etc.)
- All enum types (UserRole, ApplicationStatus, InternshipLevel, etc.)
- All exception classes
- All DTO classes

### 2. UML Class Diagrams
Location: `docs/uml/class-diagrams/`

Visual representations of the system's class structure and relationships.

**Diagrams Included:**
- Overall system architecture
- Model layer hierarchy
- Enum relationships
- Exception hierarchy
- Utility classes structure

### 3. UML Sequence Diagrams
Location: `docs/uml/sequence-diagrams/`

Visual representations of system interactions and workflows.

**Diagrams Included:**
- Student application workflow
- Company representative internship creation
- Staff approval process
- Authentication and authorization flow
- Withdrawal request processing
- CSV data operations

### 4. API Documentation
Location: `docs/api/`

REST API endpoint documentation and usage examples.

**Contents:**
- API endpoint specifications
- Request/response examples
- Authentication requirements
- Error handling documentation

## 🔧 Generating Documentation

### Generate JavaDocs
```bash
mvn javadoc:javadoc
```
Output: `target/site/apidocs/`

### Generate All Documentation
```bash
mvn site
```
Output: `target/site/`

## 📖 Usage

1. **For Developers**: Refer to JavaDoc for detailed API information
2. **For Architects**: Use UML diagrams to understand system design
3. **For Frontend Teams**: Use API documentation for integration
4. **For Stakeholders**: Refer to sequence diagrams for workflow understanding

## 🔄 Updating Documentation

- **JavaDocs**: Updated automatically from code comments during build
- **UML Diagrams**: Update when class structure or workflows change
- **API Docs**: Update when endpoints are added, modified, or removed

## 📝 Notes

- All documentation should be kept in sync with code changes
- UML diagrams should follow standard UML 2.5 notation
- JavaDoc comments should follow Oracle Java documentation standards
- API documentation should include working examples

---

**Last Updated**: October 22, 2025  
**Project**: SC2002 Group 6 - Internship Placement Management System
