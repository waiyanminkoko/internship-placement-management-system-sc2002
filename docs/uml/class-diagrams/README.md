# UML Class Diagrams

## 📊 Overview

This directory contains UML Class Diagrams representing the static structure of the Internship Placement Management System.

## 📂 Diagram Files

### Main Diagrams

1. **`02-model-hierarchy.puml`** ✅
   - User class hierarchy and inheritance structure
   - Model relationships (Student, CompanyRepresentative, CareerCenterStaff)
   - Core domain model classes
   - Key attributes and relationships
   - Multiplicity specifications

2. **`03-backend-oop-class-diagram.puml`** ✅ **[MOST COMPREHENSIVE]**
   - **ALL classes with complete attributes and methods**
   - **ALL relationships** (inheritance, associations, dependencies)
   - **OOP principles annotations**:
     - Abstraction (abstract User class)
     - Encapsulation (private fields, public methods)
     - Inheritance (User hierarchy)
     - Polymorphism (overridden methods)
     - Interfaces (Repository and Service interfaces)
     - Generics (Repository<T, ID>, ApiResponse<T>)
   - **Design Patterns**:
     - Repository Pattern
     - Service Layer Pattern
     - MVC Pattern
     - DTO Pattern
   - **All layers**: Model, Repository, Service, Controller, Utility, Exception, DTO, Enums
   - **Business rules highlighted** with notes
   - **Multiplicity and visibility** clearly shown

3. **`04-backend-simplified-abstract.puml`** ✅
   - Simplified high-level view of backend architecture
   - Abstract representation of major components
   - Focus on overall system structure
   - Key architectural patterns
   - Reduced detail for better overview

## 🎨 Diagram Format

Diagrams can be created using:
- **PlantUML** (`.puml` files) - Recommended for version control
- **Draw.io/diagrams.net** (`.drawio` files)
- **Lucidchart** (`.lucidchart` files)
- **Visual Paradigm** (`.vpp` files)
- **StarUML** (`.mdj` files)

### Recommended: PlantUML

PlantUML offers:
- Text-based format (easy version control)
- Automatic layout
- Integration with IDEs
- Export to PNG, SVG, PDF

## 📋 Diagram Content Guidelines

Each class diagram should include:

### 1. **Classes**
```
┌─────────────────────────┐
│     ClassName           │
├─────────────────────────┤
│ - privateField: Type    │
│ # protectedField: Type  │
│ + publicField: Type     │
├─────────────────────────┤
│ + publicMethod(): Type  │
│ - privateMethod(): Type │
└─────────────────────────┘
```

### 2. **Relationships**
- **Inheritance** (extends): ────▷
- **Implementation** (implements): ┈┈┈▷
- **Association**: ────
- **Aggregation**: ────◇
- **Composition**: ────◆
- **Dependency**: ┈┈┈>

### 3. **Multiplicity**
- `1` - Exactly one
- `0..1` - Zero or one
- `*` - Zero or more
- `1..*` - One or more
- `n..m` - Specific range

### 4. **Visibility**
- `+` Public
- `-` Private
- `#` Protected
- `~` Package

## 📝 PlantUML Template

Create diagrams using this template:

```plantuml
@startuml diagram-name
' Title and metadata
title Internship Placement Management System
subtitle Component Name
footer SC2002 Group 6 - Page %page% of %lastpage%

' Styling
skinparam classAttributeIconSize 0
skinparam monochrome false
skinparam shadowing false

' Packages
package model {
    abstract class User {
        - userId: String
        - password: String
        - name: String
        - email: String
        - role: UserRole
        + authenticate(): boolean
        + {abstract} getDisplayInfo(): String
    }
    
    class Student extends User {
        - yearOfStudy: int
        - major: String
        - applicationIds: List<String>
        - acceptedPlacementId: String
        + canApplyForLevel(level: InternshipLevel): boolean
        + hasMaxApplications(): boolean
    }
}

package enums {
    enum UserRole {
        STUDENT
        COMPANY_REPRESENTATIVE
        CAREER_CENTER_STAFF
    }
}

' Relationships
User --> UserRole : has

@enduml
```

## 🔨 Tools and Setup

### Install PlantUML

#### Option 1: VS Code Extension
```
Extension: PlantUML
Install: Ctrl+Shift+X → Search "PlantUML" → Install
```

#### Option 2: Command Line
```bash
# Install Graphviz (required for PlantUML)
# Windows (using Chocolatey)
choco install graphviz

# macOS
brew install graphviz

# Install PlantUML
# Download from: https://plantuml.com/download
```

#### Option 3: Online
- Visit: https://www.plantuml.com/plantuml/uml/

### Generate Diagrams

```bash
# Generate PNG
java -jar plantuml.jar diagram.puml

# Generate SVG
java -jar plantuml.jar -tsvg diagram.puml

# Generate all diagrams in directory
java -jar plantuml.jar *.puml
```

## 📊 Current Implementation Status

All 3 class diagrams are fully implemented and documented:

| Diagram | File | Focus | Status |
|---------|------|-------|--------|
| Model Hierarchy | `02-model-hierarchy.puml` | User hierarchy and domain models | ✅ Complete |
| Backend OOP Structure | `03-backend-oop-class-diagram.puml` | Complete system with OOP principles | ✅ Complete |
| Simplified Abstract | `04-backend-simplified-abstract.puml` | High-level architectural overview | ✅ Complete |

**Total Diagrams**: 3  
**Completion Rate**: 100%

## 📊 Diagram Checklist

### Model Hierarchy Diagram (`02-model-hierarchy.puml`)
- [x] User inheritance tree
- [x] All model classes
- [x] Key attributes shown
- [x] Relationships indicated
- [x] Multiplicity specified

### Backend OOP Class Diagram (`03-backend-oop-class-diagram.puml`)
- [x] All packages and layers
- [x] Complete class attributes and methods
- [x] All relationships and dependencies
- [x] OOP principles demonstrated
- [x] Design patterns highlighted
- [x] Business rules annotated

### Simplified Abstract Diagram (`04-backend-simplified-abstract.puml`)
- [x] High-level system architecture
- [x] Major components shown
- [x] Key relationships identified
- [x] Abstract view for overview
- [x] Reduced complexity for clarity

## 🎯 Key Classes to Include

### Core Model Classes
- `User` (abstract)
- `Student`
- `CompanyRepresentative`
- `CareerCenterStaff`
- `InternshipOpportunity`
- `Application`
- `WithdrawalRequest`

### Key Relationships to Show
1. User → Student/CompanyRepresentative/CareerCenterStaff (inheritance)
2. Student → Application (1 to many)
3. InternshipOpportunity → Application (1 to many)
4. CompanyRepresentative → InternshipOpportunity (1 to many)
5. Student → WithdrawalRequest (1 to many)

### Important Enums
- `UserRole`
- `ApplicationStatus`
- `ApprovalStatus`
- `InternshipLevel`
- `InternshipStatus`
- `Major`

## 📐 Design Principles

### SOLID Principles Visualization
- **S**ingle Responsibility: Each class has one clear purpose
- **O**pen/Closed: Use abstract classes and interfaces
- **L**iskov Substitution: User hierarchy is substitutable
- **I**nterface Segregation: Focused interfaces
- **D**ependency Inversion: Depend on abstractions

### Design Patterns
Highlight any design patterns used:
- Factory pattern (ID generation)
- Strategy pattern (CSV operations)
- Template method (User class hierarchy)
- Singleton (utility classes)

## 🔄 Updating Diagrams

When to update class diagrams:
- New classes added
- Class structure changed
- Relationships modified
- Business rules updated
- Inheritance hierarchy changed

## 📚 Reference Resources

- [UML 2.5 Specification](https://www.omg.org/spec/UML/2.5/)
- [PlantUML Documentation](https://plantuml.com/)
- [UML Class Diagram Tutorial](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/uml-class-diagram-tutorial/)
- [Martin Fowler's UML Distilled](https://martinfowler.com/books/uml.html)

---

**Notation**: UML 2.5  
**Last Updated**: November 16, 2025  
**Status**: ✅ Complete and Up-to-Date
