package model;

import enums.*;

/**
 * Demonstration class showcasing all model classes and their functionality.
 * This class provides example usage of all entity classes in the system.
 * 
 * <p>This demonstration includes:
 * <ul>
 *   <li>Creating and configuring Student, CompanyRepresentative, and CareerCenterStaff</li>
 *   <li>Creating InternshipOpportunity with various configurations</li>
 *   <li>Student application workflow and business rules</li>
 *   <li>Withdrawal request processing</li>
 *   <li>Demonstrating inheritance and polymorphism</li>
 * </ul>
 * 
 * <p><strong>Usage:</strong> Run this class as a Java application to see model demonstrations.</p>
 * 
 * @author SC2002 Group 6 - Member 1
 * @version 1.0
 * @since 2025-01-15
 */
public class ModelDemo {

    /**
     * Main method demonstrating all model functionality.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("SC2002 Internship Placement Management System - Model Demonstration");
        System.out.println("=".repeat(80));
        System.out.println();

        demonstrateUserHierarchy();
        demonstrateStudentFunctionality();
        demonstrateInternshipOpportunity();
        demonstrateApplicationWorkflow();
        demonstrateWithdrawalRequest();
        demonstratePolymorphism();

        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("Model Demonstration Complete");
        System.out.println("=".repeat(80));
    }

    /**
     * Demonstrates the User inheritance hierarchy and role-based functionality.
     */
    private static void demonstrateUserHierarchy() {
        System.out.println("\n[1] USER HIERARCHY DEMONSTRATION");
        System.out.println("-".repeat(80));

        // Create Student
        Student student = new Student(
            "U2345123F",
            "password",
            "John Doe",
            "john.doe@student.ntu.edu.sg",
            3,
            "CS"
        );
        System.out.println("Created Student:");
        System.out.println(student.getDisplayInfo());
        System.out.println();

        // Create Company Representative
        CompanyRepresentative rep = new CompanyRepresentative(
            "jane.smith@techcorp.com",
            "password",
            "Jane Smith",
            "jane.smith@techcorp.com",
            "TechCorp Solutions",
            "Engineering",
            "HR Manager"
        );
        System.out.println("Created Company Representative:");
        System.out.println(rep.getDisplayInfo());
        System.out.println();

        // Create Career Center Staff
        CareerCenterStaff staff = new CareerCenterStaff(
            "staffA001",
            "password",
            "Dr. Sarah Johnson",
            "sarah.johnson@ntu.edu.sg",
            "Career Services"
        );
        System.out.println("Created Career Center Staff:");
        System.out.println(staff.getDisplayInfo());
        System.out.println();
    }

    /**
     * Demonstrates Student-specific functionality and business rules.
     */
    private static void demonstrateStudentFunctionality() {
        System.out.println("\n[2] STUDENT FUNCTIONALITY DEMONSTRATION");
        System.out.println("-".repeat(80));

        // Year 2 Student - Can only apply for BASIC
        Student juniorStudent = new Student(
            "U1234567A",
            "password",
            "Alice Wong",
            "alice.wong@student.ntu.edu.sg",
            2,
            "CE"
        );

        System.out.println("Junior Student (Year 2) Eligibility:");
        System.out.println("- Can apply for BASIC: " + 
            juniorStudent.canApplyForLevel(InternshipLevel.BASIC));
        System.out.println("- Can apply for INTERMEDIATE: " + 
            juniorStudent.canApplyForLevel(InternshipLevel.INTERMEDIATE));
        System.out.println("- Can apply for ADVANCED: " + 
            juniorStudent.canApplyForLevel(InternshipLevel.ADVANCED));
        System.out.println();

        // Year 4 Student - Can apply for all levels
        Student seniorStudent = new Student(
            "U9876543Z",
            "password",
            "Bob Chen",
            "bob.chen@student.ntu.edu.sg",
            4,
            "DSAI"
        );

        System.out.println("Senior Student (Year 4) Eligibility:");
        System.out.println("- Can apply for BASIC: " + 
            seniorStudent.canApplyForLevel(InternshipLevel.BASIC));
        System.out.println("- Can apply for INTERMEDIATE: " + 
            seniorStudent.canApplyForLevel(InternshipLevel.INTERMEDIATE));
        System.out.println("- Can apply for ADVANCED: " + 
            seniorStudent.canApplyForLevel(InternshipLevel.ADVANCED));
        System.out.println();

        // Test maximum applications rule
        System.out.println("Testing Maximum Applications Rule (MAX=" + Student.MAX_APPLICATIONS + "):");
        System.out.println("- Has max applications (0 apps): " + juniorStudent.hasMaxApplications());
        
        // Simulate adding applications (in real system, done through service layer)
        for (int i = 1; i <= 3; i++) {
            System.out.println("- After adding " + i + " application(s): " + 
                (i >= Student.MAX_APPLICATIONS ? "MAX REACHED" : "Can apply more"));
        }
        System.out.println();
    }

    /**
     * Demonstrates InternshipOpportunity creation and management.
     * 
     * <p><strong>InternshipOpportunity Model Functionality:</strong></p>
     * <ul>
     *   <li><strong>Constructor:</strong> Creates opportunity with ID, title, description, level, major,
     *       opening/closing dates, status, company, representative, total slots, and visibility</li>
     *   <li><strong>getDisplayInfo():</strong> Returns formatted string with all opportunity details</li>
     *   <li><strong>getStatus():</strong> Returns current InternshipStatus (PENDING, APPROVED, REJECTED, CLOSED)</li>
     *   <li><strong>isVisible():</strong> Returns boolean indicating if opportunity is visible to students</li>
     *   <li><strong>getAvailableSlots():</strong> Returns number of unfilled slots (total - filled)</li>
     *   <li><strong>getSlots():</strong> Returns total number of internship slots</li>
     *   <li><strong>isAcceptingApplications():</strong> Checks if status is APPROVED and closing date not passed</li>
     *   <li><strong>getDaysUntilClosing():</strong> Calculates days between today and closing date</li>
     *   <li><strong>hasAvailableSlots():</strong> Returns true if filled slots less than total slots</li>
     *   <li><strong>incrementFilledSlots():</strong> Increases filled slots by 1 (used when student accepts placement)</li>
     *   <li><strong>getFilledSlots():</strong> Returns number of slots currently filled</li>
     * </ul>
     * 
     * <p><strong>Example Usage in Service Layer:</strong></p>
     * <pre>{@code
     * // Service layer would check: internship.isAcceptingApplications() && internship.hasAvailableSlots()
     * // When student accepts placement: internship.incrementFilledSlots()
     * }</pre>
     */
    private static void demonstrateInternshipOpportunity() {
        System.out.println("\n[3] INTERNSHIP OPPORTUNITY DEMONSTRATION");
        System.out.println("-".repeat(80));
        System.out.println("InternshipOpportunity Model Capabilities:");
        System.out.println("- Creates opportunity with complete details (ID, title, level, major, dates, slots)");
        System.out.println("- getDisplayInfo() method provides formatted display of all opportunity details");
        System.out.println("- Status management: PENDING → APPROVED → CLOSED workflow");
        System.out.println("- Slot management: tracks filled vs available slots");
        System.out.println("- Date validation: opening date, closing date, automatic expiration");
        System.out.println("- Visibility control: can hide/show opportunities to students");
        System.out.println("- Business rules: isAcceptingApplications() checks status and closing date");
        System.out.println("- getDaysUntilClosing() calculates time remaining to apply");
        System.out.println("- incrementFilledSlots() updates when student accepts placement");
        System.out.println("- hasAvailableSlots() prevents over-enrollment");
        System.out.println();
    }

    /**
     * Demonstrates Application workflow and status transitions.
     * 
     * <p><strong>Application Model Functionality:</strong></p>
     * <ul>
     *   <li><strong>Constructor:</strong> Creates application with ID, studentId, internshipId,
     *       initial status, submission timestamp, and placement accepted flag</li>
     *   <li><strong>getDisplayInfo():</strong> Returns formatted string showing application details</li>
     *   <li><strong>getStatus():</strong> Returns ApplicationStatus (PENDING, SUCCESSFUL, REJECTED, WITHDRAWN)</li>
     *   <li><strong>isPending():</strong> Returns true if status is PENDING</li>
     *   <li><strong>isApproved():</strong> Returns true if status is SUCCESSFUL</li>
     *   <li><strong>setStatus():</strong> Updates application status (enforces workflow rules)</li>
     *   <li><strong>isPlacementAccepted():</strong> Returns true if student accepted the placement</li>
     *   <li><strong>setPlacementAccepted():</strong> Marks placement as accepted by student</li>
     * </ul>
     * 
     * <p><strong>Application Status Workflow:</strong></p>
     * <pre>{@code
     * PENDING → SUCCESSFUL (company approves) → Student accepts placement (flag = true)
     * PENDING → REJECTED (company rejects)
     * PENDING/SUCCESSFUL → WITHDRAWN (student withdraws via WithdrawalRequest)
     * }</pre>
     */
    private static void demonstrateApplicationWorkflow() {
        System.out.println("\n[4] APPLICATION WORKFLOW DEMONSTRATION");
        System.out.println("-".repeat(80));
        System.out.println("Application Model Capabilities:");
        System.out.println("- Links student to internship opportunity (studentId + internshipId)");
        System.out.println("- Status workflow: PENDING → SUCCESSFUL → Placement Accepted");
        System.out.println("- Tracks submission timestamp (LocalDateTime)");
        System.out.println("- isPending() checks if awaiting company decision");
        System.out.println("- isApproved() checks if status is SUCCESSFUL");
        System.out.println("- Placement acceptance flag: student must explicitly accept after approval");
        System.out.println("- getDisplayInfo() shows complete application history");
        System.out.println("- Status transitions enforce business rules (cannot go from REJECTED to SUCCESSFUL)");
        System.out.println();
        System.out.println("Typical Workflow:");
        System.out.println("1. Student submits → Status = PENDING");
        System.out.println("2. Company reviews → Status = SUCCESSFUL or REJECTED");
        System.out.println("3. Student accepts placement → placementAccepted = true");
        System.out.println("4. Alternative: Student withdraws → Status = WITHDRAWN (requires staff approval)");
        System.out.println();
    }

    /**
     * Demonstrates Withdrawal Request creation and processing.
     * 
     * <p><strong>WithdrawalRequest Model Functionality:</strong></p>
     * <ul>
     *   <li><strong>Constructor:</strong> Creates withdrawal request with ID, studentId, applicationId,
     *       reason, initial status (PENDING), processedBy (null initially), processedDate (null initially)</li>
     *   <li><strong>getDisplayInfo():</strong> Returns formatted string with all request details</li>
     *   <li><strong>getStatus():</strong> Returns ApprovalStatus (PENDING, APPROVED, REJECTED)</li>
     *   <li><strong>isPending():</strong> Returns true if status is PENDING</li>
     *   <li><strong>isApproved():</strong> Returns true if status is APPROVED</li>
     *   <li><strong>isRejected():</strong> Returns true if status is REJECTED</li>
     *   <li><strong>setStatus():</strong> Updates approval status (only staff can change)</li>
     *   <li><strong>setProcessedBy():</strong> Sets staff ID who processed the request</li>
     *   <li><strong>setProcessedDate():</strong> Sets timestamp when request was processed</li>
     *   <li><strong>getProcessedBy():</strong> Returns ID of staff who processed request</li>
     *   <li><strong>getProcessedDate():</strong> Returns timestamp of processing</li>
     * </ul>
     * 
     * <p><strong>Withdrawal Request Workflow:</strong></p>
     * <pre>{@code
     * 1. Student submits withdrawal request with reason → Status = PENDING
     * 2. Staff reviews request
     * 3. Staff approves → Status = APPROVED, Application.status = WITHDRAWN
     * 4. Staff rejects → Status = REJECTED, Application remains unchanged
     * }</pre>
     */
    private static void demonstrateWithdrawalRequest() {
        System.out.println("\n[5] WITHDRAWAL REQUEST DEMONSTRATION");
        System.out.println("-".repeat(80));
        System.out.println("WithdrawalRequest Model Capabilities:");
        System.out.println("- Student initiates withdrawal from accepted placement");
        System.out.println("- Requires reason (mandatory field)");
        System.out.println("- Links to specific Application (applicationId)");
        System.out.println("- Status: PENDING → APPROVED or REJECTED");
        System.out.println("- isPending() checks if awaiting staff decision");
        System.out.println("- isApproved() checks if request was approved");
        System.out.println("- isRejected() checks if request was rejected");
        System.out.println("- Tracks who processed (staffId) and when (timestamp)");
        System.out.println("- getDisplayInfo() shows complete request history");
        System.out.println();
        System.out.println("Business Rules:");
        System.out.println("- Only students with accepted placements can request withdrawal");
        System.out.println("- Staff must review and approve/reject each request");
        System.out.println("- If approved, application status changes to WITHDRAWN");
        System.out.println("- If approved, internship slot becomes available again");
        System.out.println("- Reason is required for audit trail");
        System.out.println();
    }

    /**
     * Demonstrates polymorphism using the User hierarchy.
     */
    private static void demonstratePolymorphism() {
        System.out.println("\n[6] POLYMORPHISM DEMONSTRATION");
        System.out.println("-".repeat(80));

        // Create array of different user types
        User[] users = new User[]{
            new Student("U1111111A", "pass", "Student A", "student@ntu.edu.sg", 3, "CS"),
            new CompanyRepresentative("rep@company.com", "pass", "Rep B", "rep@company.com", 
                "Company", "Dept", "Manager"),
            new CareerCenterStaff("staff001", "pass", "Staff C", "staff@ntu.edu.sg", "Services")
        };

        System.out.println("Demonstrating polymorphic behavior with getDisplayInfo():");
        System.out.println();

        for (int i = 0; i < users.length; i++) {
            System.out.println("User " + (i + 1) + " (" + users[i].getRole() + "):");
            System.out.println(users[i].getDisplayInfo());
            System.out.println();
        }

        System.out.println("All users share common User interface but provide specialized implementations.");
        System.out.println();
    }
}
