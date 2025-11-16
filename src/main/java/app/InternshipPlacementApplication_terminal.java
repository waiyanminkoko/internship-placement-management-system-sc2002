package app;

import model.*;
import enums.*;
import util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Terminal-based test application for the Internship Placement Management System.
 * <p> Note: Not used in the final deployment; for testing purposes only in the early stages.
 * 
 * <p>This application provides a comprehensive testing environment for all backend
 * functionalities without requiring a frontend or Spring Boot server. It allows
 * the development team to test business logic, data operations, and system workflows
 * directly through a command-line interface.</p>
 * 
 * <p><b>Test Categories:</b></p>
 * <ul>
 *   <li>User Management (Students, Company Representatives, Staff)</li>
 *   <li>Internship Opportunity Management</li>
 *   <li>Application Workflow Testing</li>
 *   <li>Business Rules Validation</li>
 *   <li>CSV Data Operations</li>
 *   <li>Exception Handling</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-22
 */

// To run: mvn exec:java "-Dexec.mainClass=InternshipPlacementApplication_terminal"
public class InternshipPlacementApplication_terminal {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Test data storage
    private static List<Student> students = new ArrayList<>();
    private static List<CompanyRepresentative> representatives = new ArrayList<>();
    private static List<CareerCenterStaff> staff = new ArrayList<>();
    private static List<InternshipOpportunity> internships = new ArrayList<>();
    private static List<Application> applications = new ArrayList<>();
    private static List<WithdrawalRequest> withdrawalRequests = new ArrayList<>();

    /**
     * Main method to start the terminal-based testing application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        printWelcomeMessage();
        initializeTestData();
        
        while (true) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> testUserManagement();
                    case 2 -> testInternshipManagement();
                    case 3 -> testApplicationWorkflow();
                    case 4 -> testBusinessRules();
                    case 5 -> testCSVOperations();
                    case 6 -> testExceptionHandling();
                    case 7 -> runDemoScenarios();
                    case 8 -> displaySystemStatus();
                    case 9 -> {
                        System.out.println("\n[SUCCESS] Thank you for testing the Internship Placement Management System!");
                        System.out.println("[READY] Ready for frontend integration!");
                        return;
                    }
                    default -> System.out.println("[ERROR] Invalid choice. Please try again.");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (Exception e) {
                System.err.println("[ERROR] Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays the welcome message and system information.
     */
    private static void printWelcomeMessage() {
        System.out.println("");
        System.out.println("           INTERNSHIP PLACEMENT MANAGEMENT SYSTEM              ");
        System.out.println("                    Terminal Test Environment                   ");
        System.out.println("                       SC2002 SCED Group 6                      ");
        System.out.println("");
        System.out.println("[INIT] Backend Logic Testing Environment");
        System.out.println("[DATE] " + LocalDate.now().format(dateFormatter));
        System.out.println("[INFO] Testing all business rules and functionalities before frontend integration");
        System.out.println();
    }

    /**
     * Displays the main menu options.
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    MAIN TESTING MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. User Management Testing");
        System.out.println("2. Internship Management Testing");
        System.out.println("3. Application Workflow Testing");
        System.out.println("4. Business Rules Validation");
        System.out.println("5. CSV Operations Testing");
        System.out.println("6. Exception Handling Testing");
        System.out.println("7. Demo Scenarios");
        System.out.println("8. System Status Overview");
        System.out.println("9. Exit");
        System.out.println("=".repeat(60));
    }

    /**
     * Tests user management functionalities.
     */
    private static void testUserManagement() {
        System.out.println("\n[USERS] USER MANAGEMENT TESTING");
        System.out.println("-".repeat(50));
        System.out.println("1. Create Test Student");
        System.out.println("2. Create Test Company Representative");
        System.out.println("3. Create Test Staff Member");
        System.out.println("4. Display All Users");
        System.out.println("5. Test User Authentication");
        System.out.println("6. Test Authorization Rules");
        System.out.println("7. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> createTestStudent();
            case 2 -> createTestCompanyRep();
            case 3 -> createTestStaff();
            case 4 -> displayAllUsers();
            case 5 -> testUserAuthentication();
            case 6 -> testAuthorizationRules();
        }
    }

    /**
     * Creates a test student with user input.
     */
    private static void createTestStudent() {
        System.out.println("\n[STUDENT] Creating Test Student");
        System.out.println("-".repeat(30));
        
        try {
            String userId = getStringInput("Student ID (e.g., U2345123F): ");
            String name = getStringInput("Full Name: ");
            String email = getStringInput("Email: ");
            int year = getIntInput("Year of Study (1-4): ");
            String major = getStringInput("Major (e.g., CSC, EEE, MAE): ");
            
            Student student = new Student(userId, "password", name, email, year, major);
            students.add(student);
            
            System.out.println("[SUCCESS] Student created successfully!");
            System.out.println(student.getDisplayInfo());
            
            // Test business rules
            System.out.println("\n[TEST] Testing Business Rules:");
            System.out.println("Can apply for BASIC: " + student.canApplyForLevel(InternshipLevel.BASIC));
            System.out.println("Can apply for INTERMEDIATE: " + student.canApplyForLevel(InternshipLevel.INTERMEDIATE));
            System.out.println("Can apply for ADVANCED: " + student.canApplyForLevel(InternshipLevel.ADVANCED));
            System.out.println("Can submit application: " + student.canSubmitApplication());
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error creating student: " + e.getMessage());
        }
    }

    /**
     * Creates a test company representative.
     */
    private static void createTestCompanyRep() {
        System.out.println("\n[COMPANY] Creating Test Company Representative");
        System.out.println("-".repeat(40));
        
        try {
            String userId = getStringInput("Email (as User ID): ");
            String name = getStringInput("Full Name: ");
            String email = userId; // Use same as userId
            String company = getStringInput("Company Name: ");
            String department = getStringInput("Department: ");
            String position = getStringInput("Position: ");
            
            CompanyRepresentative rep = new CompanyRepresentative(userId, "password", name, email, company, department, position);
            representatives.add(rep);
            
            System.out.println("[SUCCESS] Company Representative created successfully!");
            System.out.println(rep.getDisplayInfo());
            
            // Test business rules
            System.out.println("\n[TEST] Testing Business Rules:");
            System.out.println("Can login: " + rep.canLogin());
            System.out.println("Can create internship: " + rep.canCreateInternship());
            
            // Authorize the representative
            rep.authorize();
            System.out.println("[SUCCESS] Representative authorized!");
            System.out.println("Can login now: " + rep.canLogin());
            System.out.println("Can create internship now: " + rep.canCreateInternship());
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error creating company representative: " + e.getMessage());
        }
    }

    /**
     * Creates a test staff member.
     */
    private static void createTestStaff() {
        System.out.println("\n[STAFF] Creating Test Staff Member");
        System.out.println("-".repeat(35));
        
        try {
            String userId = getStringInput("Staff ID (e.g., staff001): ");
            String name = getStringInput("Full Name: ");
            String email = getStringInput("Email: ");
            String department = getStringInput("Department: ");
            
            CareerCenterStaff staffMember = new CareerCenterStaff(userId, "password", name, email, department);
            staff.add(staffMember);
            
            System.out.println("[SUCCESS] Staff member created successfully!");
            System.out.println(staffMember.getDisplayInfo());
            
            // Test business rules
            System.out.println("\n[TEST] Testing Privileges:");
            System.out.println("Has admin privileges: " + staffMember.hasAdminPrivileges());
            System.out.println("Can authorize representatives: " + staffMember.canAuthorizeRepresentatives());
            System.out.println("Can approve internships: " + staffMember.canApproveInternships());
            System.out.println("Can process withdrawals: " + staffMember.canProcessWithdrawals());
            System.out.println("Can generate reports: " + staffMember.canGenerateReports());
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error creating staff member: " + e.getMessage());
        }
    }

    /**
     * Tests internship management functionalities.
     */
    private static void testInternshipManagement() {
        System.out.println("\n INTERNSHIP MANAGEMENT TESTING");
        System.out.println("-".repeat(50));
        System.out.println("1. Create Test Internship");
        System.out.println("2. Approve/Reject Internships");
        System.out.println("3. Test Slot Management");
        System.out.println("4. Test Visibility Controls");
        System.out.println("5. Display All Internships");
        System.out.println("6. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> createTestInternship();
            case 2 -> testInternshipApproval();
            case 3 -> testSlotManagement();
            case 4 -> testVisibilityControls();
            case 5 -> displayAllInternships();
        }
    }

    /**
     * Creates a test internship opportunity.
     */
    private static void createTestInternship() {
        System.out.println("\n Creating Test Internship");
        System.out.println("-".repeat(35));
        
        if (representatives.isEmpty()) {
            System.out.println(" No company representatives available. Create one first!");
            return;
        }
        
        try {
            String opportunityId = IdGenerator.generateInternshipId();
            String title = getStringInput("Internship Title: ");
            String description = getStringInput("Description: ");
            
            System.out.println("Levels: 1-BASIC, 2-INTERMEDIATE, 3-ADVANCED");
            int levelChoice = getIntInput("Select Level (1-3): ");
            InternshipLevel level = switch (levelChoice) {
                case 1 -> InternshipLevel.BASIC;
                case 2 -> InternshipLevel.INTERMEDIATE;
                case 3 -> InternshipLevel.ADVANCED;
                default -> InternshipLevel.BASIC;
            };
            
            String preferredMajor = getStringInput("Preferred Major (or 'Any'): ");
            LocalDate openingDate = getDateInput("Opening Date (yyyy-MM-dd): ");
            LocalDate closingDate = getDateInput("Closing Date (yyyy-MM-dd): ");
            LocalDate startDate = getDateInput("Start Date (yyyy-MM-dd): ");
            LocalDate endDate = getDateInput("End Date (yyyy-MM-dd): ");
            int totalSlots = getIntInput("Total Slots (1-10): ");
            
            // Use first representative for testing
            CompanyRepresentative rep = representatives.get(0);
            
            InternshipOpportunity internship = new InternshipOpportunity(
                opportunityId, title, description, level, preferredMajor,
                openingDate, closingDate, startDate, endDate, rep.getCompanyName(), rep.getUserId(), totalSlots
            );
            
            internships.add(internship);
            rep.addInternship(opportunityId);
            
            System.out.println(" Internship created successfully!");
            System.out.println(internship.toString());
            
            // Test business rules
            System.out.println("\n Testing Business Rules:");
            System.out.println("Is accepting applications: " + internship.isAcceptingApplications());
            System.out.println("Available slots: " + internship.getAvailableSlots());
            System.out.println("Can be edited: " + internship.canBeEdited());
            
        } catch (Exception e) {
            System.err.println(" Error creating internship: " + e.getMessage());
        }
    }

    /**
     * Tests application workflow functionalities.
     */
    private static void testApplicationWorkflow() {
        System.out.println("\n APPLICATION WORKFLOW TESTING");
        System.out.println("-".repeat(50));
        System.out.println("1. Create Test Application");
        System.out.println("2. Process Applications");
        System.out.println("3. Test Placement Acceptance");
        System.out.println("4. Test Withdrawal Requests");
        System.out.println("5. Display All Applications");
        System.out.println("6. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> createTestApplication();
            case 2 -> processApplications();
            case 3 -> testPlacementAcceptance();
            case 4 -> testWithdrawalRequests();
            case 5 -> displayAllApplications();
        }
    }

    /**
     * Tests business rules validation.
     */
    private static void testBusinessRules() {
        System.out.println("\n BUSINESS RULES VALIDATION");
        System.out.println("-".repeat(50));
        System.out.println("1. Test Student Application Limits");
        System.out.println("2. Test Year-Level Restrictions");
        System.out.println("3. Test Company Representative Limits");
        System.out.println("4. Test Internship Slot Management");
        System.out.println("5. Test Authorization Rules");
        System.out.println("6. Run All Business Rule Tests");
        System.out.println("7. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> testStudentApplicationLimits();
            case 2 -> testYearLevelRestrictions();
            case 3 -> testRepresentativeLimits();
            case 4 -> testInternshipSlotRules();
            case 5 -> testAuthorizationRules();
            case 6 -> runAllBusinessRuleTests();
        }
    }

    /**
     * Tests CSV operations.
     */
    private static void testCSVOperations() {
        System.out.println("\n CSV OPERATIONS TESTING");
        System.out.println("-".repeat(50));
        System.out.println("1. Test CSV Reading");
        System.out.println("2. Test CSV Writing");
        System.out.println("3. Test Data Validation");
        System.out.println("4. Test File Operations");
        System.out.println("5. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> testCSVReading();
            case 2 -> testCSVWriting();
            case 3 -> testDataValidation();
            case 4 -> testFileOperations();
        }
    }

    /**
     * Tests exception handling.
     */
    private static void testExceptionHandling() {
        System.out.println("\n EXCEPTION HANDLING TESTING");
        System.out.println("-".repeat(50));
        System.out.println("1. Test Invalid Input Exceptions");
        System.out.println("2. Test Business Rule Exceptions");
        System.out.println("3. Test Resource Not Found");
        System.out.println("4. Test Unauthorized Access");
        System.out.println("5. Test Data Persistence Errors");
        System.out.println("6. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> testInvalidInputExceptions();
            case 2 -> testBusinessRuleExceptions();
            case 3 -> testResourceNotFound();
            case 4 -> testUnauthorizedAccess();
            case 5 -> testDataPersistenceErrors();
        }
    }

    /**
     * Runs demo scenarios to showcase the system.
     */
    private static void runDemoScenarios() {
        System.out.println("\n DEMO SCENARIOS");
        System.out.println("-".repeat(50));
        System.out.println("1. Complete Student Application Journey");
        System.out.println("2. Company Representative Workflow");
        System.out.println("3. Staff Approval Process");
        System.out.println("4. End-to-End System Demo");
        System.out.println("5. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> demoStudentJourney();
            case 2 -> demoCompanyWorkflow();
            case 3 -> demoStaffProcess();
            case 4 -> demoEndToEndSystem();
        }
    }

    // Helper methods for testing specific functionalities

    private static void createTestApplication() {
        if (students.isEmpty() || internships.isEmpty()) {
            System.out.println(" Need at least one student and one internship to create an application!");
            return;
        }
        
        try {
            String applicationId = IdGenerator.generateApplicationId();
            Student student = students.get(0);
            InternshipOpportunity internship = internships.get(0);
            
            Application application = new Application(
                applicationId, student.getUserId(), internship.getOpportunityId()
            );
            
            applications.add(application);
            student.addApplication(applicationId);
            
            System.out.println(" Application created successfully!");
            System.out.println(application.toString());
            System.out.println("\n Application Status:");
            System.out.println("Is pending: " + application.isPending());
            System.out.println("Can accept placement: " + application.canAcceptPlacement());
            System.out.println("Status display: " + application.getStatusDisplay());
            
        } catch (Exception e) {
            System.err.println(" Error creating application: " + e.getMessage());
        }
    }

    private static void testStudentApplicationLimits() {
        System.out.println("\n Testing Student Application Limits");
        
        if (students.isEmpty()) {
            System.out.println(" No students available for testing!");
            return;
        }
        
        Student student = students.get(0);
        System.out.println("Student: " + student.getName());
        System.out.println("Current applications: " + student.getApplicationCount());
        System.out.println("Max applications allowed: " + Student.MAX_APPLICATIONS);
        System.out.println("Has max applications: " + student.hasMaxApplications());
        System.out.println("Can submit application: " + student.canSubmitApplication());
        
        // Test adding applications up to limit
        int originalCount = student.getApplicationCount();
        for (int i = originalCount; i < Student.MAX_APPLICATIONS; i++) {
            String appId = IdGenerator.generateApplicationId();
            boolean added = student.addApplication(appId);
            System.out.println("Adding application " + (i + 1) + ": " + (added ? " Success" : " Failed"));
        }
        
        // Try to add one more (should fail)
        String extraAppId = IdGenerator.generateApplicationId();
        boolean extraAdded = student.addApplication(extraAppId);
        System.out.println("Adding extra application: " + (extraAdded ? " Should have failed!" : " Correctly rejected"));
    }

    private static void testYearLevelRestrictions() {
        System.out.println("\n Testing Year-Level Restrictions");
        
        // Test different year levels
        String[] years = {"1", "2", "3", "4"};
        for (String year : years) {
            Student testStudent = new Student("TEST" + year, "password", "Test Student Year " + year, 
                                            "test" + year + "@email.com", Integer.parseInt(year), "CSC");
            
            System.out.println("\nYear " + year + " Student:");
            System.out.println("Can apply for BASIC: " + testStudent.canApplyForLevel(InternshipLevel.BASIC));
            System.out.println("Can apply for INTERMEDIATE: " + testStudent.canApplyForLevel(InternshipLevel.INTERMEDIATE));
            System.out.println("Can apply for ADVANCED: " + testStudent.canApplyForLevel(InternshipLevel.ADVANCED));
        }
    }

    private static void displayAllUsers() {
        System.out.println("\n ALL USERS");
        System.out.println("-".repeat(50));
        
        System.out.println(" STUDENTS (" + students.size() + "):");
        for (Student student : students) {
            System.out.println("  " + student.toString());
        }
        
        System.out.println("\n COMPANY REPRESENTATIVES (" + representatives.size() + "):");
        for (CompanyRepresentative rep : representatives) {
            System.out.println("  " + rep.toString());
        }
        
        System.out.println("\n STAFF (" + staff.size() + "):");
        for (CareerCenterStaff staffMember : staff) {
            System.out.println("  " + staffMember.toString());
        }
    }

    private static void displayAllInternships() {
        System.out.println("\n ALL INTERNSHIPS (" + internships.size() + ")");
        System.out.println("-".repeat(50));
        
        for (InternshipOpportunity internship : internships) {
            System.out.println(internship.toString());
            System.out.println("  Status: " + internship.getStatus());
            System.out.println("  Slots: " + internship.getFilledSlots() + "/" + internship.getTotalSlots());
            System.out.println("  Accepting applications: " + internship.isAcceptingApplications());
            System.out.println();
        }
    }

    private static void displayAllApplications() {
        System.out.println("\n ALL APPLICATIONS (" + applications.size() + ")");
        System.out.println("-".repeat(50));
        
        for (Application application : applications) {
            System.out.println(application.toString());
        }
    }

    private static void displaySystemStatus() {
        System.out.println("\n SYSTEM STATUS OVERVIEW");
        System.out.println("=".repeat(60));
        System.out.println(" Total Users: " + (students.size() + representatives.size() + staff.size()));
        System.out.println("    Students: " + students.size());
        System.out.println("    Company Representatives: " + representatives.size());
        System.out.println("    Staff: " + staff.size());
        System.out.println();
        System.out.println(" Internships: " + internships.size());
        System.out.println(" Applications: " + applications.size());
        System.out.println(" Withdrawal Requests: " + withdrawalRequests.size());
        System.out.println();
        System.out.println(" System Status: All backend components operational");
        System.out.println(" Ready for frontend integration!");
    }

    // Additional helper methods for other test cases...
    
    private static void initializeTestData() {
        System.out.println(" Initializing test data...");
        
        // Add some default test data
        students.add(new Student("U2345123F", "password", "John Tan", "john.tan@email.com", 3, "CSC"));
        students.add(new Student("U3456789G", "password", "Mary Lee", "mary.lee@email.com", 2, "EEE"));
        
        CompanyRepresentative rep = new CompanyRepresentative("alice@techcorp.com", "password", 
                                                            "Alice Tan", "alice@techcorp.com", 
                                                            "TechCorp", "HR", "HR Manager");
        rep.authorize();
        representatives.add(rep);
        
        staff.add(new CareerCenterStaff("staff001", "password", "Dr. Emily Ng", 
                                      "emily.ng@ntu.edu.sg", "Career Services"));
        
        System.out.println(" Test data initialized!");
    }

    // Utility methods for input handling
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid number.");
            }
        }
    }
    
    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                String input = getStringInput(prompt);
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println(" Please enter date in format: yyyy-MM-dd");
            }
        }
    }

    // Placeholder methods for other test functionalities
    private static void testUserAuthentication() { System.out.println(" User authentication test - TODO"); }
    private static void testAuthorizationRules() { System.out.println(" Authorization rules test - TODO"); }
    private static void testInternshipApproval() { System.out.println(" Internship approval test - TODO"); }
    private static void testSlotManagement() { System.out.println(" Slot management test - TODO"); }
    private static void testVisibilityControls() { System.out.println(" Visibility controls test - TODO"); }
    private static void processApplications() { System.out.println(" Process applications test - TODO"); }
    private static void testPlacementAcceptance() { System.out.println(" Placement acceptance test - TODO"); }
    private static void testWithdrawalRequests() { System.out.println(" Withdrawal requests test - TODO"); }
    private static void testRepresentativeLimits() { System.out.println(" Representative limits test - TODO"); }
    private static void testInternshipSlotRules() { System.out.println(" Internship slot rules test - TODO"); }
    private static void runAllBusinessRuleTests() { System.out.println(" All business rules test - TODO"); }
    private static void testCSVReading() { System.out.println(" CSV reading test - TODO"); }
    private static void testCSVWriting() { System.out.println(" CSV writing test - TODO"); }
    private static void testDataValidation() { System.out.println(" Data validation test - TODO"); }
    private static void testFileOperations() { System.out.println(" File operations test - TODO"); }
    private static void testInvalidInputExceptions() { System.out.println(" Invalid input exceptions test - TODO"); }
    private static void testBusinessRuleExceptions() { System.out.println(" Business rule exceptions test - TODO"); }
    private static void testResourceNotFound() { System.out.println(" Resource not found test - TODO"); }
    private static void testUnauthorizedAccess() { System.out.println(" Unauthorized access test - TODO"); }
    private static void testDataPersistenceErrors() { System.out.println(" Data persistence errors test - TODO"); }
    private static void demoStudentJourney() { System.out.println(" Student journey demo - TODO"); }
    private static void demoCompanyWorkflow() { System.out.println(" Company workflow demo - TODO"); }
    private static void demoStaffProcess() { System.out.println(" Staff process demo - TODO"); }
    private static void demoEndToEndSystem() { System.out.println(" End-to-end system demo - TODO"); }
}
