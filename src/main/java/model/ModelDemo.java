package model;

import enums.*;
import java.time.LocalDate;

/**
 * Demonstration class showcasing the model classes and their business logic.
 * This class contains example usage of all model classes.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public class ModelDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Internship Placement System - Model Demo ===\n");
        
        // Demo 1: Student Business Rules
        demonstrateStudentRules();
        
        // Demo 2: Company Representative Business Rules
        demonstrateCompanyRepRules();
        
        // Demo 3: Career Center Staff Operations
        demonstrateStaffOperations();
        
        // Demo 4: Internship Opportunity Business Rules
        demonstrateInternshipRules();
        
        // Demo 5: Application Lifecycle
        demonstrateApplicationLifecycle();
        
        // Demo 6: Withdrawal Process
        demonstrateWithdrawalProcess();
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    /**
     * Demonstrates Student business rules and validations.
     */
    private static void demonstrateStudentRules() {
        System.out.println("--- Student Business Rules ---");
        
        // Create a Year 1 student
        Student juniorStudent = new Student(
            "U2345123A", "John Doe", "password", 
            "Computer Science", 1, "john@e.ntu.edu.sg"
        );
        
        // Create a Year 3 student
        Student seniorStudent = new Student(
            "U2345124B", "Jane Smith", "password",
            "Data Science and AI", 3, "jane@e.ntu.edu.sg"
        );
        
        // Test year-based level restrictions
        System.out.println("Junior Student (Year 1):");
        System.out.println("  Can apply for BASIC: " + 
            juniorStudent.canApplyForLevel(InternshipLevel.BASIC));
        System.out.println("  Can apply for ADVANCED: " + 
            juniorStudent.canApplyForLevel(InternshipLevel.ADVANCED));
        
        System.out.println("\nSenior Student (Year 3):");
        System.out.println("  Can apply for BASIC: " + 
            seniorStudent.canApplyForLevel(InternshipLevel.BASIC));
        System.out.println("  Can apply for ADVANCED: " + 
            seniorStudent.canApplyForLevel(InternshipLevel.ADVANCED));
        
        // Test application limits
        System.out.println("\nApplication Limits:");
        System.out.println("  Initial applications: " + seniorStudent.getActiveApplicationsCount());
        System.out.println("  Can apply more: " + seniorStudent.canApplyMoreApplications());
        
        seniorStudent.addApplication("APP-20251007-00001");
        seniorStudent.addApplication("APP-20251007-00002");
        seniorStudent.addApplication("APP-20251007-00003");
        
        System.out.println("  After 3 applications: " + seniorStudent.getActiveApplicationsCount());
        System.out.println("  Can apply more: " + seniorStudent.canApplyMoreApplications());
        
        // Test placement acceptance
        System.out.println("\nPlacement Acceptance:");
        seniorStudent.acceptPlacement("APP-20251007-00001");
        System.out.println("  Has accepted placement: " + seniorStudent.isHasAcceptedPlacement());
        System.out.println("  Can apply more: " + seniorStudent.canApplyMoreApplications());
        
        System.out.println();
    }
    
    /**
     * Demonstrates Company Representative business rules.
     */
    private static void demonstrateCompanyRepRules() {
        System.out.println("--- Company Representative Business Rules ---");
        
        CompanyRepresentative rep = new CompanyRepresentative(
            "hr@techcorp.com", "Sarah Johnson", "password",
            "TechCorp Inc", "HR Department", "HR Manager",
            ApprovalStatus.APPROVED
        );
        
        System.out.println("Representative: " + rep.getName());
        System.out.println("  Company: " + rep.getCompanyName());
        System.out.println("  Is Approved: " + rep.isApproved());
        System.out.println("  Can Create More Opportunities: " + rep.canCreateMoreOpportunities());
        
        // Add internship opportunities
        System.out.println("\nAdding Internship Opportunities:");
        for (int i = 1; i <= 5; i++) {
            rep.addOpportunity("INT-20251007-0000" + i);
            System.out.println("  Opportunity " + i + " added. Total: " + rep.getOpportunityCount());
        }
        
        System.out.println("  Can create more: " + rep.canCreateMoreOpportunities());
        
        try {
            rep.addOpportunity("INT-20251007-00006");
        } catch (IllegalStateException e) {
            System.out.println("  Error adding 6th opportunity: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Demonstrates Career Center Staff operations.
     */
    private static void demonstrateStaffOperations() {
        System.out.println("--- Career Center Staff Operations ---");
        
        CareerCenterStaff staff = new CareerCenterStaff(
            "STAFF001", "password", "Dr. Alice Wong",
            "alice.wong@ntu.edu.sg", "Career Services"
        );
        
        System.out.println("Staff: " + staff.getName());
        System.out.println("  Department: " + staff.getDepartment());
        
        // Authorize representatives
        System.out.println("\nAuthorizing Representatives:");
        staff.authorizeRepresentative("hr@techcorp.com");
        staff.authorizeRepresentative("hr@innovate.com");
        System.out.println("  Approved Representatives: " + 
            staff.getApprovedRepresentativeIds().size());
        
        // Approve internships
        System.out.println("\nApproving Internships:");
        staff.approveInternship("INT-20251007-00001");
        staff.approveInternship("INT-20251007-00002");
        System.out.println("  Approved Internships: " + 
            staff.getApprovedInternshipIds().size());
        
        System.out.println();
    }
    
    /**
     * Demonstrates Internship Opportunity business rules.
     */
    private static void demonstrateInternshipRules() {
        System.out.println("--- Internship Opportunity Business Rules ---");
        
        InternshipOpportunity internship = new InternshipOpportunity(
            "INT-20251007-00001",
            "Software Developer Intern",
            "Develop web applications using Java and React",
            InternshipLevel.INTERMEDIATE,
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            LocalDate.now().plusMonths(2),
            LocalDate.now().plusMonths(8),
            "TechCorp Inc",
            "hr@techcorp.com",
            5
        );
        // Set status to APPROVED manually after creation
        internship.setStatus(InternshipStatus.APPROVED);
        
        System.out.println("Internship: " + internship.getTitle());
        System.out.println("  Status: " + internship.getStatus().getDisplayName());
        System.out.println("  Level: " + internship.getLevel().getDisplayName());
        System.out.println("  Available Slots: " + internship.getAvailableSlots());
        System.out.println("  Is Visible: " + internship.isVisible());
        System.out.println("  Can Accept Applications: " + internship.canAcceptApplications());
        System.out.println("  Is Application Open: " + internship.isApplicationOpen());
        
        // Test student eligibility
        Student csStudent = new Student("U2345125C", "password", "Bob Lee",
            "bob@e.ntu.edu.sg", 3, "Computer Science");
        Student eeeStudent = new Student("U2345126D", "password", "Charlie Tan",
            "charlie@e.ntu.edu.sg", 3, "Electrical Engineering");
        
        System.out.println("\nStudent Eligibility:");
        System.out.println("  CS Student eligible: " + internship.isEligibleForStudent(csStudent));
        System.out.println("  EEE Student eligible: " + internship.isEligibleForStudent(eeeStudent));
        
        // Fill slots
        System.out.println("\nFilling Slots:");
        for (int i = 0; i < 5; i++) {
            internship.incrementFilledSlots();
            System.out.println("  Filled: " + internship.getFilledSlots() + "/" + 
                internship.getSlots() + " - Status: " + internship.getStatus().getDisplayName());
        }
        
        System.out.println("  Is Filled: " + internship.isFilled());
        System.out.println("  Can Accept Applications: " + internship.canAcceptApplications());
        
        System.out.println();
    }
    
    /**
     * Demonstrates Application lifecycle.
     */
    private static void demonstrateApplicationLifecycle() {
        System.out.println("--- Application Lifecycle ---");
        
        Application app = new Application(
            "APP-20251007-00001",
            "U2345123A",
            "INT-20251007-00001"
        );
        // Application starts with PENDING status by default
        
        System.out.println("Initial Application:");
        System.out.println("  Status: " + app.getStatus().getDisplayName());
        System.out.println("  Is Pending: " + app.isPending());
        System.out.println("  Is Active: " + app.isActive());
        System.out.println("  Can Be Withdrawn: " + app.canBeWithdrawn());
        
        // Representative approves
        System.out.println("\nRepresentative Approves:");
        app.approve("hr@techcorp.com");
        System.out.println("  Status: " + app.getStatus().getDisplayName());
        System.out.println("  Is Successful: " + app.isSuccessful());
        
        // Student accepts placement
        System.out.println("\nStudent Accepts Placement:");
        boolean accepted = app.acceptPlacement();
        System.out.println("  Placement Accepted: " + accepted);
        System.out.println("  Is Placement Accepted: " + app.isPlacementAccepted());
        
        System.out.println();
    }
    
    /**
     * Demonstrates Withdrawal process.
     */
    private static void demonstrateWithdrawalProcess() {
        System.out.println("--- Withdrawal Process ---");
        
        WithdrawalRequest withdrawal = new WithdrawalRequest(
            "WDR-20251007-00001",
            "U2345123A",
            "APP-20251007-00001",
            "Personal reasons - family emergency",
            false
        );
        // Set internship ID after creation
        withdrawal.setInternshipId("INT-20251007-00001");
        
        System.out.println("Withdrawal Request:");
        System.out.println("  Student ID: " + withdrawal.getStudentId());
        System.out.println("  Reason: " + withdrawal.getReason());
        System.out.println("  Status: " + withdrawal.getStatus().getDisplayName());
        System.out.println("  Is Pending: " + withdrawal.isPending());
        
        // Staff approves withdrawal
        System.out.println("\nStaff Approves Withdrawal:");
        withdrawal.approve("STAFF001", "Approved due to valid personal reasons");
        System.out.println("  Status: " + withdrawal.getStatus().getDisplayName());
        System.out.println("  Processed By: " + withdrawal.getProcessedBy());
        System.out.println("  Is Approved: " + withdrawal.isApproved());
        
        System.out.println();
    }
}
