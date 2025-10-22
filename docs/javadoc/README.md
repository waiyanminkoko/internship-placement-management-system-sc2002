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

The generated JavaDoc documentation includes:

### 1. **Model Classes** (`model/`)
- `User` (abstract base class)
- `Student`
- `CompanyRepresentative`
- `CareerCenterStaff`
- `InternshipOpportunity`
- `Application`
- `WithdrawalRequest`

### 2. **Enum Types** (`enums/`)
- `UserRole`
- `ApplicationStatus`
- `ApprovalStatus`
- `InternshipLevel`
- `InternshipStatus`
- `Major`

### 3. **Utility Classes** (`util/`)
- `CSVUtil`
- `CSVReader`
- `CSVWriter`
- `IdGenerator`
- `ValidationUtil`
- `DateUtil`

### 4. **Exception Classes** (`exception/`)
- `BusinessRuleException`
- `CsvParseException`
- `DataPersistenceException`
- `InvalidInputException`
- `ResourceNotFoundException`
- `UnauthorizedException`
- `GlobalExceptionHandler`

### 5. **DTO Classes** (`dto/`)
- `ApiResponse`
- `LoginRequest`

### 6. **Configuration** (`config/`)
- `WebConfig`

### 7. **Main Application**
- `InternshipPlacementApplication`
- `InternshipPlacementApplication_terminal`

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

| Package | Classes | Methods | Fields |
|---------|---------|---------|--------|
| model | 7 | ~150+ | ~80+ |
| enums | 6 | ~30+ | ~40+ |
| util | 6 | ~80+ | ~30+ |
| exception | 7 | ~20+ | ~10+ |
| dto | 2 | ~15+ | ~8+ |
| config | 1 | ~5+ | ~2+ |
| **Total** | **29+** | **300+** | **170+** |

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

### 3. Business Rule Documentation
Important business rules are highlighted:
- Student application limits (max 3)
- Year-level restrictions (Year 1-2 = BASIC only)
- Company representative limits (max 5 internships)
- Internship slot limits (max 10)
- Authorization requirements

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

## ✅ Documentation Checklist

- [ ] All public classes documented
- [ ] All public methods documented
- [ ] All public fields documented
- [ ] Business rules highlighted
- [ ] Examples provided where helpful
- [ ] Cross-references added
- [ ] @param tags complete
- [ ] @return tags complete
- [ ] @throws tags complete
- [ ] @since tags present
- [ ] @author tags present

---

**Generated By**: Maven JavaDoc Plugin  
**Java Version**: 21  
**Last Updated**: October 22, 2025
