# UML Sequence Diagrams

## 🔄 Overview

This directory contains UML Sequence Diagrams representing the dynamic behavior and interactions within the Internship Placement Management System.

## 📂 Diagram Files

### Main Sequence Diagrams

1. **`01-student-application-workflow.puml`** ✅
   - Student browses internships
   - Student submits application
   - Application status tracking
   - Placement acceptance flow
   - **Status**: Implemented

2. **`02-company-rep-application-review.puml`** ✅
   - Company rep views applications for their internship
   - Company rep reviews application details
   - Application approval process with validations
   - Application rejection process
   - Student notifications
   - Slot management and availability checks
   - Ownership verification
   - Business rule enforcement
   - **Status**: Implemented

3. **`02-company-rep-approve-application-simplified.puml`** ✅
   - Simplified view of application approval process
   - Focuses on core approval workflow
   - Streamlined interaction flow
   - Key validation steps
   - **Status**: Implemented

4. **`03-company-rep-registration-approval.puml`** ✅
   - Company representative self-registration
   - Registration data validation
   - Staff views pending representatives
   - Staff approval/rejection process
   - Representative login authentication
   - **Status**: Implemented

5. **`04-authentication-authorization.puml`** ✅
   - User login flow
   - Password validation
   - Role-based access control
   - Session management
   - **Status**: Implemented

## 🎨 Diagram Format

Diagrams can be created using:
- **PlantUML** (`.puml` files) - Recommended
- **Sequence Diagram.org** (`.sd` files)
- **Draw.io** (`.drawio` files)
- **Lucidchart** (`.lucidchart` files)

### Recommended: PlantUML

Benefits:
- Text-based format
- Easy version control
- Automatic layout
- Clean, professional output
- Integration with documentation

## 📋 Sequence Diagram Guidelines

Each sequence diagram should include:

### 1. **Participants/Actors**
```plantuml
actor Student
participant "StudentController" as SC
participant "StudentService" as SS
participant "InternshipRepository" as IR
database "CSV Files" as CSV
```

### 2. **Messages**
- **Synchronous call**: →
- **Return**: ←--
- **Asynchronous call**: ->>
- **Create**: **create**
- **Destroy**: **destroy**

### 3. **Activation Boxes**
Shows when an object is active:
```plantuml
activate StudentService
deactivate StudentService
```

### 4. **Alt/Opt/Loop Fragments**
```plantuml
alt successful case
    ...
else failure case
    ...
end

opt optional behavior
    ...
end

loop for each item
    ...
end
```

## 📝 PlantUML Template

Create sequence diagrams using this template:

```plantuml
@startuml sequence-name
' Title and metadata
title Internship Placement Management System
subtitle Workflow Name
footer SC2002 Group 6 - Page %page%

' Participants
actor Student as student
participant "StudentController" as controller
participant "StudentService" as service
participant "ApplicationRepository" as repo
participant "InternshipRepository" as intRepo
database "CSV Storage" as csv

' Sequence
student -> controller: submitApplication(internshipId)
activate controller

controller -> service: createApplication(studentId, internshipId)
activate service

' Validation
service -> service: validateStudent()
service -> intRepo: getInternship(internshipId)
activate intRepo
intRepo -> csv: read internship
intRepo --> service: internship
deactivate intRepo

alt Internship is accepting applications
    service -> service: checkStudentEligibility()
    
    alt Student is eligible
        service -> repo: saveApplication(application)
        activate repo
        repo -> csv: write application
        repo --> service: success
        deactivate repo
        
        service --> controller: ApplicationDTO
        controller --> student: 200 OK (Application created)
    else Student not eligible
        service --> controller: BusinessRuleException
        controller --> student: 400 Bad Request (Not eligible)
    end
else Internship not accepting
    service --> controller: BusinessRuleException
    controller --> student: 400 Bad Request (Closed)
end

deactivate service
deactivate controller

@enduml
```

## 🔨 Key Workflows to Document

### 1. Student Application Workflow
**Participants**: Student, Controller, Service, Repository, CSV
**Flow**:
1. Student browses available internships
2. Student selects internship
3. System validates eligibility
4. Application submitted
5. Confirmation sent

**Business Rules**:
- Max 3 applications
- Year-level restrictions
- No applications after placement accepted

### 2. Company Internship Creation
**Participants**: Company Rep, Controller, Service, Repository, CSV
**Flow**:
1. Company rep creates internship
2. System validates authorization
3. Internship saved as PENDING
4. Staff notified for approval
5. Confirmation sent

**Business Rules**:
- Max 5 internships per representative
- Must be authorized
- Requires staff approval

### 3. Staff Approval Process
**Participants**: Staff, Controller, Service, Repository, CSV
**Flow**:
1. Staff views pending items
2. Staff reviews details
3. Staff approves/rejects
4. Status updated
5. Notifications sent

**Business Rules**:
- Only staff can approve
- Approved internships become visible
- Rejected items cannot be edited

### 4. Placement Acceptance
**Participants**: Student, Controller, Service, Repository, CSV
**Flow**:
1. Student receives approval
2. Student accepts placement
3. Other applications withdrawn
4. Slot confirmed
5. Status updated

**Business Rules**:
- Only for approved applications
- One placement per student
- Auto-withdraw other applications
- Cannot undo acceptance

### 5. Withdrawal Request
**Participants**: Student, Staff, Controllers, Services, Repositories, CSV
**Flow**:
1. Student requests withdrawal
2. Withdrawal created
3. Staff reviews
4. Staff approves/rejects
5. If approved, slot released

**Business Rules**:
- Requires staff approval
- Slot released if approved
- Different rules before/after placement

## 📊 Diagram Checklist

### For Each Sequence Diagram
- [ ] Clear title and subtitle
- [ ] All participants identified
- [ ] Activation boxes used properly
- [ ] Return messages included
- [ ] Alternative flows shown (alt/else)
- [ ] Error handling included
- [ ] Business rules enforced
- [ ] Timing/order clear
- [ ] Notes for clarification
- [ ] Footer with project info

### Business Rules Validation
- [ ] Max application limits checked
- [ ] Year-level restrictions validated
- [ ] Authorization verified
- [ ] Slot availability confirmed
- [ ] Status transitions correct

### Error Scenarios
- [ ] Invalid input handling
- [ ] Business rule violations
- [ ] Resource not found
- [ ] Unauthorized access
- [ ] Concurrent modification

## 📊 Current Implementation Status

All 5 sequence diagrams are fully implemented and documented:

| Diagram | File | Status |
|---------|------|--------|
| Student Application Workflow | `01-student-application-workflow.puml` | ✅ Complete |
| Company Rep Application Review | `02-company-rep-application-review.puml` | ✅ Complete |
| Simplified Application Approval | `02-company-rep-approve-application-simplified.puml` | ✅ Complete |
| Company Rep Registration | `03-company-rep-registration-approval.puml` | ✅ Complete |
| Authentication & Authorization | `04-authentication-authorization.puml` | ✅ Complete |

**Total Diagrams**: 5  
**Completion Rate**: 100%

## 🎯 Special Notations

### Notes and Comments
```plantuml
note left of Student
    Student must be
    logged in
end note

note right of Service
    Business rule:
    Max 3 applications
end note

note over Student, Service
    This is a critical workflow
end note
```

### Reference to Other Diagrams
```plantuml
ref over Student, Controller : Authentication Flow (see diagram 04)
```

### Timing Constraints
```plantuml
Student -> Controller: request
& Controller -> Service: process
note right: Parallel processing
```

## 🔄 Interaction Patterns

### Pattern 1: CRUD Operations
```
Actor → Controller → Service → Repository → CSV
CSV → Repository → Service → Controller → Actor
```

### Pattern 2: Validation Flow
```
Actor → Controller → Service
Service → Service: validate()
alt valid → proceed
else invalid → return error
```

### Pattern 3: Multi-Step Workflow
```
Actor → Controller → Service A → Service B → Repository
Repository → Service B → Service A → Controller → Actor
```

## 📚 Reference Resources

- [UML Sequence Diagram Tutorial](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-sequence-diagram/)
- [PlantUML Sequence Diagram](https://plantuml.com/sequence-diagram)
- [Interaction Overview Diagrams](https://www.uml-diagrams.org/interaction-overview-diagrams.html)
- [Best Practices for Sequence Diagrams](https://www.ibm.com/docs/en/rational-soft-arch/9.7.0?topic=diagrams-sequence)

## 🛠️ Generating Diagrams

### Using PlantUML

```bash
# Generate single diagram
java -jar plantuml.jar sequence-diagram.puml

# Generate all sequence diagrams
java -jar plantuml.jar *.puml

# Generate with specific format
java -jar plantuml.jar -tsvg sequence-diagram.puml

# Watch mode (auto-regenerate on save)
java -jar plantuml.jar -watch sequence-diagram.puml
```

### Using VS Code

1. Install PlantUML extension
2. Open `.puml` file
3. Press `Alt+D` to preview
4. Right-click → "Export Current Diagram"

## 🔄 Updating Diagrams

Update sequence diagrams when:
- New features added
- Workflows change
- Business rules modified
- Error handling updated
- Integration points change

## 💡 Tips for Creating Diagrams

1. **Start Simple**: Begin with happy path
2. **Add Complexity**: Then add error cases
3. **Group Related Flows**: Keep related interactions together
4. **Use Fragments**: Alt/opt/loop for clarity
5. **Include Notes**: Explain business rules
6. **Show Timing**: Use activation boxes
7. **Keep Readable**: Don't overcrowd
8. **Version Control**: Use text-based formats

---

**Notation**: UML 2.5  
**Last Updated**: November 16, 2025  
**Status**: ✅ Complete and Operational
