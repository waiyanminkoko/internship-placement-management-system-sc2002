# SC2002 Object-Oriented Design & Programming Assignment
## Building an OO Application - 2025/2026 Semester 1

---

## 1. OBJECTIVES

- Apply Object-Oriented (OO) concepts learned in the course
- Model, design and develop an OO application
- Gain familiarity with using Java as an object-oriented programming language
- Work collaboratively as a group to achieve a common goal

---

## 4. THE ASSIGNMENT

### Project: Internship Placement Management System

### System Overview

- **Centralized hub** for Students, Company Representatives, and Career Center Staff
- All users must login using their account

#### User ID Formats:
- **Students:** Start with `U`, followed by 7 digits, end with a letter (e.g., `U2345123F`)
- **Company Representatives:** Company email address
- **Career Center Staff:** NTU account

#### Default Credentials:
- **Default Password:** `password` (for all users)
- Users can change their password in the system

#### Additional User Information:
- **Student:** Year of Study, Major
- **Company Representatives:** Company Name, Department, Position
- **Career Center Staff:** Staff Department

#### Initialization:
- User list can be initiated through a file uploaded at system initialization

---

## USER CAPABILITIES

### 1. ALL USERS

**Base Attributes:**
- User ID
- Name
- Password

**Base Features:**
- Login
- Logout
- Change password

---

### 2. STUDENT

**Registration:**
- Automatic registration by reading from student list file

**View Internship Opportunities:**
Can only view internships based on:
- **Student Profile:**
  - Year of study (Year 1 to 4)
  - Major (CSC, EEE, MAE, etc.)
- **Visibility:** Must be toggled "on"

**Application Rules:**
- Maximum of **3 internship applications** at once
- **Year 1 & 2 students:** Can ONLY apply for Basic-level internships
- **Year 3 & above students:** Can apply for any level (Basic, Intermediate, Advanced)

**View Applications:**
- Can view internships they applied for, even after visibility is turned off
- View application status: "Pending", "Successful", or "Unsuccessful"
- Status is "Pending" by default, updated by Company Representative

**Placement Acceptance:**
- If status is "Successful", student can accept the internship placement
- **Only 1 internship placement** can be accepted
- All other applications automatically withdrawn once placement accepted

**Withdrawal Requests:**
- Can request withdrawal for internship application before/after placement confirmation
- Subject to approval from Career Center Staff

---

### 3. COMPANY REPRESENTATIVES

**Registration:**
- Company Representative list is empty at beginning
- Must register as representative of a specific company
- Can only login once approved by Career Center Staff

**Create Internship Opportunities:**
- Can create up to **5 internship opportunities**

**Internship Details Include:**
- Internship Title
- Description
- Internship Level (Basic, Intermediate, Advanced)
- Preferred Majors (1 preferred major)
- Application opening date
- Application closing date
- Status ("Pending", "Approved", "Rejected", "Filled")
- Company Name
- Company Representative in charge (automatically assigned)
- Number of slots (max of 10)

**Approval Process:**
- Internship opportunities must be approved by Career Center Staff
- Once status is "Approved", students may apply
- If "Filled" or after Closing Date, students cannot apply anymore

**Application Management:**
- View application details and student details for their internship opportunities
- May Approve or Reject internship applications
- Once approved, student application status becomes "Successful"
- Student can then accept placement confirmation
- Internship status becomes "Filled" only when all available slots are confirmed by students

**Visibility Control:**
- Can toggle internship opportunity visibility to "on" or "off"
- Reflected in internship list visible to students

---

### 4. CAREER CENTER STAFF

**Registration:**
- Automatic registration by reading from staff list file

**Account Authorization:**
- Authorize or reject Company Representative account creation

**Internship Approval:**
- Approve or reject internship opportunities submitted by Company Representatives
- Once approved, status changes to "Approved" and becomes visible to eligible students

**Withdrawal Management:**
- Approve or reject student withdrawal requests (before and after placement confirmation)

**Report Generation:**
- Generate comprehensive reports regarding internship opportunities
- **Filters available:**
  - Status
  - Preferred Majors
  - Internship Level
  - Other criteria

---

## MISCELLANEOUS FEATURES

**Filtering:**
- All users can filter internship opportunities by:
  - Status
  - Preferred Majors
  - Internship Level
  - Closing Date
  - etc.
- **Default order:** Alphabetical
- User filter settings are saved when switching menu pages

---

## TECHNICAL REQUIREMENTS

**Interface:**
- Command Line Interface (CLI) application
- GUI implementation is optional (no bonus marks)
- Focus is on Object-Oriented Design and Programming (OODP)

**Data Management:**
- Sample data file for user list provided in Excel format
- Options:
  - Use directly
  - Copy content to text file
  - Create own data files
- **NOT ALLOWED:**
  - Database applications (MySQL, MS Access, etc.)
  - JSON or XML formats

---

## 5. THE REPORT

The report must include:

### a) UML Class Diagram (exported as image)
- Show class relationships and notation clearly
- Include notes to explain if necessary
- **Annotate diagram** to highlight specific OO principles (encapsulation, polymorphism, etc.)

### b) UML Sequence Diagram (exported as image)
- Show flow of Company Representative's Role in Internship Management and Application Process
- Example: student application review processes
- Must show all participating objects with sufficient detailed flow and relevant interaction fragments

### c) Additional Features
- Clearly highlight any additional features/functionalities implemented

### d) Design Considerations
Based on lecture concepts, write-up on:
- Design considerations and use of OO concepts
- Extensibility and maintainability of design
- Trade-offs made in design
- Reflection on design patterns used and their contribution to system design
- Alternative patterns considered and reasons for final choices

### e) Reflection
- Difficulties encountered and solutions
- Knowledge learned from the course
- Further improvement suggestions
- Strong demonstration of learning points and insights of good design and implementation practices

### f) GitHub Repository
- Include link to GitHub repository with all relevant files and code

### g) Declaration of Original Work
- Duly signed Declaration of Original Work form (Appendix B)

### h) Work Breakdown Structure (WBS) - Optional
- By default, all group members receive same mark
- If individual contributions differ and group wishes to assign marks accordingly, complete WBS.xls
- All members must agree on WBS contents
- Email completed WBS.xls to Dr. Li Fang and TA with all group members copied

---

## 6. DEMONSTRATION & PRESENTATION

**Deadline:** Week 14 Friday, 11:59pm

**Requirements:**
- Present to TA demonstrating ALL required functionalities
- Plan demonstration in story-boarding flow
- Introduce members and group number at start
- All group members must take turn to present
- Presenter must show face while presenting

**Content:**
- Explain essential and relevant information about the application
- Run-through and elaborate on essential parts of implementation/coding

**Technical Requirements:**
- **Duration:** Maximum 15 minutes total
- **Font size:** Large enough to be readable and viewable
- **Demo:** Real-time (NOT pre-run display)
- **Format:** Online meeting or physical presentation

**Testing:**
- Create own test cases and data to test application thoroughly

---

## 7. THE DELIVERABLE

**Deadline:** One day before scheduled oral presentation with TA

**Submission must include:**
- The report (separate diagram file if diagram is unclear in report)
- All implementation codes and Java documentation (Javadoc)
- Other relevant files (data files, setup instructions, etc.)

---

## 8. ASSESSMENT WEIGHTAGE

### UML Class Diagram [25 Marks]
- Show Entity classes, essential Control and Boundary classes, enumeration types
- Clarity, Correctness, and Completeness of details and relationships

### UML Sequence Diagram [20 Marks]
- Show sequence diagram mentioned in 5(b)
- Clarity, Correctness, and Completeness of flow and object interactions (Boundary-Control-Entities)

### Design Consideration [15 Marks]
- Usage of OO concepts and principles - correctness and appropriateness
- Explanation of design choices and fit with project requirements
- Coupling and cohesion of classes

### Implementation Code [20 Marks]
- Diagram to Code correctness
- Readability
- Java naming convention
- Exception handling
- Completeness of Java Doc
- Overall quality
- Creativity of additional features/functionality
- **Java API HTML documentation** of ALL defined classes using Javadoc must be submitted

### Demonstration and Report [20 Marks]
- Coverage of application essentials and functionalities
- User friendliness
- Demo flow
- Innovation
- Report structure and reflection
- Highlight clearly any additional features implemented

---

## 9. SUBMISSION

**Group submission:** One submission per group

**Upload to:** Individual SC/CE/CZ2002 LAB site in NTULearn
- Link provided on left panel "Assignment Submission"

**File naming convention:** `<lab_grp>-grp<assignment_grp#>.<ext>`
- Example: `FEP2-grp3.pdf`
- Extensions: pdf, doc, zip

**Deadline:** One day before scheduled oral presentation with TA

**Late Penalty:** 
- **3 marks deducted** per calendar day for late submission
- Lateness based on date captured in NTULearn or subsequent resubmissions (whichever is later)

---

## APPENDIX A: SAMPLE TEST CASES

| No. | Test Cases | Expected Behavior | Failure Indicators |
|-----|------------|-------------------|-------------------|
| 1 | Valid User Login | User should be able to access their dashboard based on their roles | User cannot log in or receive incorrect error messages |
| 2 | Invalid ID | User receives notification about incorrect ID | User is allowed to log in with invalid ID or fail to provide meaningful error message |
| 3 | Incorrect Password | System should deny access and alert user to incorrect password | User logs in successfully with wrong password or fail to provide meaningful error message |
| 4 | Password Change Functionality | System updates password, prompt re-login and allows login with new credentials | System does not update password or denies access with new password |
| 5 | Company Representative Account Creation | New Company Representative should only be able to log in after approval by Career Center Staff | Company Representative can log in without authorization |
| 6 | Internship Opportunity Visibility Based on User Profile and Toggle | Internship opportunities visible to students based on year of study, major, internship level eligibility, and visibility setting | Students see internship opportunities not relevant to their profile or when visibility is off |
| 7 | Internship Application Eligibility | Students can only apply for internship opportunities relevant to their profile and when visibility is on | Students can apply for internship opportunities not relevant to their profile or when visibility is off |
| 8 | Viewing Application Status after Visibility Toggle Off | Students continue to have access to their application details regardless of visibility | Application details become inaccessible once visibility is off |
| 10 | Single Internship Placement Acceptance per Student | System allows accepting one internship placement and automatically withdraws all other applications | Student can accept more than one placement, or other applications remain active |
| 13 | Company Representative Internship Opportunity Creation | System allows Company Representatives to create internship opportunities only when they meet system requirements | System allows creation with invalid data or exceeds maximum allowed opportunities |
| 14 | Internship Opportunity Approval Status | Company Representatives can view pending, approved, or rejected status updates | Status updates not visible, incorrect, or not properly saved |
| 15 | Internship Detail Access for Company Representative | Company Representatives can always access full details of opportunities they created, regardless of visibility | Opportunity details become inaccessible when visibility is toggled off |
| 16 | Restriction on Editing Approved Opportunities | Edit functionality restricted once opportunities are approved by Career Center Staff | Company Representatives able to make changes after approval |
| 18 | Student Application Management and Placement Confirmation | Company Representatives retrieve correct applications, update slot availability accurately, confirm placement details | Incorrect application retrieval, slot counts not updating, or failure to reflect placement confirmation |
| 19 | Internship Placement Confirmation Status Update | Placement confirmation status updated to reflect actual confirmation condition | System fails to update or incorrectly records status |
| 20 | Create, Edit, and Delete Internship Opportunity Listings | Company Representatives should be able to add, modify (before approval), and remove opportunities | Inability to create, edit, or delete opportunities or errors during operations |
| 21 | Career Center Staff Internship Opportunity Approval | Career Center Staff can review and approve/reject opportunities | Cannot access submitted opportunities, approval/rejection fails, or approved opportunities don't become visible |
| 22 | Toggle Internship Opportunity Visibility | Changes in visibility reflected accurately in internship opportunity list | Visibility settings do not update or affect listing as expected |
| 23 | Career Center Staff Internship Opportunity Management | Withdrawal approvals/rejections processed correctly with system updates | Incorrect or failed processing of withdrawal requests or slot counts not updating |
| 24 | Generate and Filter Internship Opportunities | Accurate report generation with filters by status, major, company, level, etc. | Reports are inaccurate, incomplete, or filtering doesn't work as expected |

---

## APPENDIX C: REPORT REQUIREMENTS

### 1. Format

**Font and Spacing:**
- Main content: Times New Roman, 12pt, 1.5 line spacing
- Code segments: May use other fonts (e.g., Courier New)

**Report Structure:**

1. **Cover Page:** Declaration of Original Work (Appendix B)

2. **Design Considerations**
   - Approach taken, Principles used, Assumptions made
   - Optional: Show important code segments and necessary illustrations

3. **Detailed UML Class Diagram**
   - Further notes, if needed

4. **Detailed UML Sequence Diagram** of stated function
   - Further notes, if needed

5. **Testing**
   - Test Cases and Results

6. **Reflection**
   - Difficulties encountered and solutions
   - Knowledge learned from course
   - Further improvement suggestions

### 2. Length

- **Maximum 12 pages** from cover to cover (including diagrams/testing results/references/appendix)
- Encouraged to present work in fewer pages if possible
- **DO NOT include source code** in report - store in separate folder
- Ensure diagrams are readable and clear
- Save diagrams as image files and include in folder if needed

---

## APPENDIX D: CREATING JAVADOC

### Official Documentation
**Oracle Javadoc Guide:** [Official Documentation Link]

### For Eclipse Users
**YouTube Tutorial:** Using Javadoc in Eclipse

### Example Output
Generated documentation can be viewed by opening `index.html` file in output folder

### Using Javadoc from Command Prompt

**Steps:**

1. **Locate JDK installation path**
   - Windows typically: `C:\Program Files\Java\jdk<version>\`

2. **Open Command Prompt**

3. **Navigate to src directory**
   - Use `cd` command

4. **Run Javadoc command:**

```bash
<path-to-jdk>\bin\javadoc -d ./html -author -private -noqualifier all -version <package1> <package2> ...
```

**Example:**
```bash
C:\subject\2024sem2\sc2002\src> "C:\Program Files\Java\jdk1.8.0_05\bin\javadoc" ^
-d ./html -author -private -noqualifier all -version edu.ntu.ccds.sc2002
```

### Command Components Explanation

| Statement | Purpose |
|-----------|---------|
| `C:\subject\2024sem2\sc2002\src>` | Your source root directory |
| `"C:\Program Files\Java\jdk1.8.0_05\bin\javadoc"` | Path to javadoc.exe (use double quotes if path contains spaces) |
| `-d ./html` | Output directory for generated HTML docs (creates html folder in current directory) |
| `-author` | Includes @author tag (if used in source) |
| `-private` | Includes documentation for private fields and methods |
| `-noqualifier all` | Omits full package qualifiers (e.g., shows String instead of java.lang.String) |
| `-version` | Includes @version tag (if used in source) |
| `edu.ntu.ccds.sc2002` | Your Java package(s) to document |

---

## SUMMARY OF KEY POINTS

### Critical Requirements:
- ✅ CLI application (GUI optional, no bonus)
- ✅ No databases (MySQL, MS Access)
- ✅ No JSON or XML
- ✅ Java with OOP principles
- ✅ Complete Javadoc documentation
- ✅ UML Class and Sequence diagrams
- ✅ Maximum 15-minute demo video
- ✅ Maximum 12-page report
- ✅ Comprehensive testing

### User Types & Core Functions:
1. **Students:** View, apply (max 3), accept placement (max 1), request withdrawal
2. **Company Reps:** Create opportunities (max 5), manage applications, toggle visibility
3. **Career Staff:** Approve accounts/opportunities, approve withdrawals, generate reports

### Business Rules:
- Year 1-2: Basic level only
- Year 3-4: All levels
- Max 3 concurrent applications per student
- Max 1 accepted placement per student
- Max 5 opportunities per company rep
- Max 10 slots per opportunity
- Filter settings persist across menu pages