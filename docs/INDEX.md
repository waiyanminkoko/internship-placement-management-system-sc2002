# Documentation Index

## 📚 Complete Documentation Structure

Welcome to the comprehensive documentation for the SC2002 Internship Placement Management System.

---

## 📂 Quick Navigation

### 1. [JavaDoc Documentation](javadoc/README.md)
**Location**: `docs/javadoc/`

Generated API documentation for all Java classes.

**To Generate**:
```bash
mvn javadoc:javadoc
```

**View**: `target/site/apidocs/index.html`

**Coverage**: 29+ classes, 300+ methods, 170+ fields

---

### 2. [UML Class Diagrams](uml/class-diagrams/README.md)
**Location**: `docs/uml/class-diagrams/`

Visual representation of system architecture and class structure.

**Available Diagrams**:
- ✅ `01-system-overview.puml` - Overall architecture (DRAFT CREATED)
- ✅ `02-model-hierarchy.puml` - Model layer details (DRAFT CREATED)
- 📝 `03-enum-types.puml` - Enumeration types (TO CREATE)
- 📝 `04-utility-classes.puml` - Utility classes (TO CREATE)
- 📝 `05-exception-hierarchy.puml` - Exception structure (TO CREATE)
- 📝 `06-dto-layer.puml` - Data transfer objects (TO CREATE)

---

### 3. [UML Sequence Diagrams](uml/sequence-diagrams/README.md)
**Location**: `docs/uml/sequence-diagrams/`

Dynamic behavior and workflow interactions.

**Available Diagrams**:
- ✅ `01-student-application-workflow.puml` - Application process (DRAFT CREATED)
- 📝 `02-company-internship-creation.puml` - Internship creation (TO CREATE)
- 📝 `03-application-approval-process.puml` - Approval workflow (TO CREATE)
- ✅ `04-authentication-authorization.puml` - Auth flow (DRAFT CREATED)
- 📝 `05-withdrawal-request-processing.puml` - Withdrawal process (TO CREATE)
- 📝 `06-staff-approval-workflows.puml` - Staff operations (TO CREATE)
- 📝 `07-csv-data-operations.puml` - Data persistence (TO CREATE)
- 📝 `08-slot-management.puml` - Slot allocation (TO CREATE)

---

### 4. [API Documentation](api/README.md)
**Location**: `docs/api/`

REST API endpoint specifications and examples.

**Contents**:
- Base URL: `http://localhost:7070/api`
- Authentication endpoints
- Student endpoints
- Company representative endpoints
- Staff endpoints
- Error codes and handling

---

## 🎯 Documentation Status

### Completed ✅
- [x] Documentation folder structure
- [x] README files for all sections
- [x] Draft PlantUML templates (4 diagrams)
- [x] API documentation outline
- [x] JavaDoc generation setup

### In Progress 🔄
- [ ] Complete all UML class diagrams
- [ ] Complete all UML sequence diagrams
- [ ] Detailed API endpoint documentation
- [ ] Postman collection
- [ ] Swagger/OpenAPI specification

### Not Started 📝
- [ ] User manual
- [ ] Developer guide
- [ ] Deployment guide
- [ ] Testing documentation

---

## 🛠️ Tools and Setup

### For JavaDocs
```bash
# Generate JavaDocs
mvn javadoc:javadoc

# View locally
start target/site/apidocs/index.html
```

### For UML Diagrams (PlantUML)

**Install VS Code Extension**:
1. Open VS Code
2. Extensions (Ctrl+Shift+X)
3. Search "PlantUML"
4. Install

**Install Graphviz** (required):
```bash
# Windows (Chocolatey)
choco install graphviz

# Or download from: https://graphviz.org/download/
```

**Generate Diagrams**:
```bash
# Single diagram
java -jar plantuml.jar diagram.puml

# All diagrams in folder
java -jar plantuml.jar docs/uml/**/*.puml

# In VS Code: Alt+D to preview
```

### For API Documentation
- **Postman**: https://www.postman.com/downloads/
- **Swagger Editor**: https://editor.swagger.io/
- **ReDoc**: https://redocly.github.io/redoc/

---

## 📖 Documentation Guidelines

### JavaDoc Standards
```java
/**
 * Brief description (required).
 * 
 * <p>Detailed description (optional).</p>
 * 
 * @param name description
 * @return description
 * @throws Exception when...
 * @author SC2002 Group 6
 * @since 2025-10-14
 */
```

### PlantUML Standards
- Use clear participant names
- Include activation boxes
- Show error flows (alt/else)
- Add explanatory notes
- Keep diagrams focused

### API Documentation Standards
- Show request/response examples
- Document all error codes
- Include authentication requirements
- Provide cURL examples

---

## 🔄 Maintenance

### When to Update Documentation

**JavaDocs**: Automatically updated during builds
- Run `mvn javadoc:javadoc` after code changes

**UML Class Diagrams**: Update when structure changes
- New classes added
- Relationships modified
- Business rules changed

**UML Sequence Diagrams**: Update when workflows change
- New features added
- Flow modifications
- Error handling updated

**API Documentation**: Update when endpoints change
- New endpoints added
- Request/response formats changed
- Error codes modified

---

## 📊 Documentation Metrics

| Type | Count | Status |
|------|-------|--------|
| JavaDoc Classes | 29+ | ✅ Complete |
| JavaDoc Methods | 300+ | ✅ Complete |
| UML Class Diagrams | 2/6 | 🔄 In Progress |
| UML Sequence Diagrams | 2/10 | 🔄 In Progress |
| API Endpoints | 20+ | 📝 Outlined |
| README Files | 5 | ✅ Complete |

---

## 👥 Team Responsibilities

### Documentation Tasks Assignment

**JavaDoc Completion**:
- Member 1: Model classes
- Member 2: Utility classes
- Member 3: Service layer (when created)
- Member 4: Controller layer (when created)
- Member 5: Exception and DTO classes

**UML Diagrams**:
- Member 1: Class diagrams (3-6)
- Member 2: Sequence diagrams (2-5)
- Member 3: Sequence diagrams (6-8)
- Member 4: API documentation
- Member 5: Review and finalization

---

## 🎓 Learning Resources

### UML
- [UML 2.5 Specification](https://www.omg.org/spec/UML/2.5/)
- [PlantUML Guide](https://plantuml.com/)
- [Visual Paradigm Tutorials](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/)

### JavaDoc
- [Oracle JavaDoc Guide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)
- [JavaDoc Best Practices](https://blog.joda.org/2012/11/javadoc-coding-standards.html)

### API Documentation
- [OpenAPI Specification](https://swagger.io/specification/)
- [REST API Best Practices](https://restfulapi.net/)

---

## 📞 Support

For questions or issues with documentation:
1. Check relevant README files in subdirectories
2. Refer to learning resources above
3. Consult with team members
4. Review existing draft examples

---

**Project**: SC2002 Internship Placement Management System  
**Group**: Group 6  
**Last Updated**: October 22, 2025  
**Status**: 🔄 Documentation in progress

---

## ✅ Next Steps

1. **Generate JavaDocs**: Run `mvn javadoc:javadoc`
2. **Complete UML Diagrams**: Fill in remaining class and sequence diagrams
3. **Finalize API Docs**: Complete endpoint documentation
4. **Create Postman Collection**: Set up API testing
5. **Review and Polish**: Ensure consistency across all documentation
