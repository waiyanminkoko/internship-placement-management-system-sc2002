package service;

import enums.ApplicationStatus;
import enums.InternshipLevel;
import model.Application;
import model.InternshipOpportunity;
import model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Consolidated service class for all Student-related functionality.
 * 
 * This service handles:
 * - Viewing available internships
 * - Viewing applied internships
 * - Applying for internships
 * - Accepting internship placements
 * 
 * Business Rules (Assignment Requirements):
 * - A student can have a maximum of 3 active applications.
 * - A student can accept ONLY ONE internship placement.
 * - Acceptance only allowed for SUCCESSFUL applications.
 * - When accepted, all other applications are automatically withdrawn.
 * - The corresponding internship increments its filled slots count.
 * - Internship becomes FILLED once all slots are confirmed.
 */
public class StudentService {

    // =====================================================================
    // VIEW AVAILABLE INTERNSHIPS
    // =====================================================================

    /**
     * Returns internships visible to students that match their major and level eligibility.
     * 
     * @param student the logged-in student
     * @param all list of all internship opportunities
     * @return list of eligible internships
     */
    public List<InternshipOpportunity> getEligibleInternships(Student student, List<InternshipOpportunity> all) {
        if (all == null) return List.of();

        List<InternshipOpportunity> result = new ArrayList<>();
        for (InternshipOpportunity opp : all) {
            boolean visible = opp.isVisibleToStudents();           // approved or filled and visible
            boolean majorOk = opp.matchesMajor(student.getMajor()); // "Any" or exact match
            boolean levelOk = student.canApplyForLevel(opp.getLevel());

            if (visible && majorOk && levelOk) {
                result.add(opp);
            }
        }

        // Sort alphabetically by title
        result.sort(Comparator.comparing(InternshipOpportunity::getTitle, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    /**
     * Returns internships that are currently open for application 
     * (approved, visible, within dates, and slots available).
     * 
     * @param student the logged-in student
     * @param all list of all internship opportunities
     * @return list of open internships
     */
    public List<InternshipOpportunity> getOpenInternshipsForApplication(Student student, List<InternshipOpportunity> all) {
        if (all == null) return List.of();

        List<InternshipOpportunity> result = new ArrayList<>();
        for (InternshipOpportunity opp : all) {
            boolean eligible = opp.isVisibleToStudents()
                    && opp.matchesMajor(student.getMajor())
                    && student.canApplyForLevel(opp.getLevel());

            if (eligible && opp.isAcceptingApplications()) {
                result.add(opp);
            }
        }

        result.sort(Comparator.comparing(InternshipOpportunity::getTitle, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    // =====================================================================
    // VIEW APPLIED INTERNSHIPS
    // =====================================================================

    /**
     * Return all applications that belong to a specific student (sorted by submission date).
     * 
     * @param student the logged-in student
     * @param allApplications list of all applications
     * @return list of student's applications
     */
    public List<Application> getApplicationsForStudent(Student student, List<Application> allApplications) {
        if (allApplications == null) return List.of();

        List<Application> result = new ArrayList<>();
        for (Application app : allApplications) {
            if (student.getUserId().equals(app.getStudentId())) {
                result.add(app);
            }
        }

        // Sort newest first
        result.sort(Comparator.comparing(Application::getSubmissionDate).reversed());
        return result;
    }

    /**
     * Return only applications that match a specific status (e.g. Pending, Successful).
     * 
     * @param student the logged-in student
     * @param allApplications list of all applications
     * @param status the status to filter by
     * @return list of applications with the specified status
     */
    public List<Application> getApplicationsByStatus(
            Student student, List<Application> allApplications, ApplicationStatus status) {
        if (allApplications == null || status == null) return List.of();

        List<Application> result = new ArrayList<>();
        for (Application app : allApplications) {
            if (student.getUserId().equals(app.getStudentId()) && app.getStatus() == status) {
                result.add(app);
            }
        }

        result.sort(Comparator.comparing(Application::getSubmissionDate).reversed());
        return result;
    }

    /**
     * Format the application results neatly for displaying in a console app.
     * 
     * @param applications list of applications to format
     * @param opportunities list of all internship opportunities
     * @return list of formatted strings
     */
    public List<String> formatApplicationSummaries(
            List<Application> applications, List<InternshipOpportunity> opportunities) {
        List<String> formatted = new ArrayList<>();

        for (Application app : applications) {
            InternshipOpportunity opp = findOpportunity(opportunities, app.getOpportunityId());
            String title = (opp != null) ? opp.getTitle() : "(Unknown Title)";
            String company = (opp != null) ? opp.getCompanyName() : "(Unknown Company)";
            String status = app.getStatusDisplay();
            formatted.add(String.format("%s | %s | %s", title, company, status));
        }

        return formatted;
    }

    // =====================================================================
    // APPLY FOR INTERNSHIP
    // =====================================================================

    /**
     * Handles the logic for applying to an internship.
     * 
     * @param student the logged-in student
     * @param opportunity the internship opportunity to apply for
     * @param allApplications list of all applications (to which the new application will be added)
     * @return true if application is successful, false otherwise
     */
    public boolean applyForInternship(Student student, InternshipOpportunity opportunity, List<Application> allApplications) {

        // Check if the student can apply (max 3 applications, no accepted placement)
        if (!student.canSubmitApplication()) {
            System.out.println("You already have 3 active applications or have accepted a placement.");
            return false;
        }

        // Check eligibility based on internship level and student's year
        InternshipLevel level = opportunity.getLevel();
        if (!student.canApplyForLevel(level)) {
            System.out.println("You are not eligible for this internship level.");
            return false;
        }

        // Check if internship is open for applications (approved, visible, within dates, not filled)
        if (!opportunity.isAcceptingApplications()) {
            System.out.println("This internship is not currently accepting applications.");
            return false;
        }

        // Check if student's major matches internship's preferred major
        if (!opportunity.matchesMajor(student.getMajor())) {
            System.out.println("Your major does not match the preferred major for this internship.");
            return false;
        }

        // Check if there are available slots
        if (opportunity.getAvailableSlots() <= 0) {
            System.out.println("No available slots for this internship.");
            return false;
        }

        // Create a new application (defaults to PENDING)
        String applicationId = UUID.randomUUID().toString();
        Application application = new Application(
                applicationId,
                student.getUserId(),
                opportunity.getOpportunityId()
        );

        // Add application to the global list
        if (allApplications != null) {
            allApplications.add(application);
        }

        // Add application ID to student's list of applications
        if (!student.addApplication(applicationId)) {
            System.out.println("Unable to add application to student profile.");
            return false;
        }

        System.out.println("Application submitted successfully for: " + opportunity.getTitle());
        return true;
    }

    // =====================================================================
    // ACCEPT INTERNSHIP PLACEMENT
    // =====================================================================

    /**
     * Attempts to accept a SUCCESSFUL internship placement for a given student.
     *
     * @param student the logged-in student
     * @param applicationId the ID of the application being accepted
     * @param allApplications list of all applications in the system
     * @param opportunities list of all internship opportunities in the system
     * @return message to display to the student
     */
    public String acceptInternship(
            Student student,
            String applicationId,
            List<Application> allApplications,
            List<InternshipOpportunity> opportunities
    ) {
        // 1. Validate
        if (student == null) return "No student logged in.";
        if (applicationId == null || applicationId.isBlank()) return "Invalid application selected.";

        // 2. Check if already accepted
        if (student.hasAcceptedPlacement()) {
            return "You have already accepted an internship placement.";
        }

        // 3. Locate target application
        Application target = allApplications.stream()
                .filter(a -> Objects.equals(a.getApplicationId(), applicationId))
                .findFirst()
                .orElse(null);

        if (target == null)
            return "Application not found.";
        if (!student.getUserId().equals(target.getStudentId()))
            return "You can only accept your own applications.";
        if (!target.canAcceptPlacement())
            return "Only SUCCESSFUL applications can be accepted.";

        // 4. Mark placement as accepted in application
        boolean accepted = target.acceptPlacement();
        if (!accepted) return "This placement cannot be accepted again.";

        // 5. Link accepted placement to student
        student.acceptPlacement(target.getOpportunityId());

        // 6. Withdraw all other applications
        for (Application app : allApplications) {
            if (app.getStudentId().equals(student.getUserId())
                    && !app.getApplicationId().equals(target.getApplicationId())) {
                app.withdraw("Automatically withdrawn after accepting another placement.");
            }
        }

        // 7. Update the internship opportunity slot
        InternshipOpportunity opp = opportunities.stream()
                .filter(o -> Objects.equals(o.getOpportunityId(), target.getOpportunityId()))
                .findFirst()
                .orElse(null);

        if (opp != null) {
            boolean slotConfirmed = opp.confirmPlacement();
            if (!slotConfirmed) {
                return "The internship has no available slots left.";
            }
        }

        // 8. Return success message
        return String.format(
                "Internship '%s' from %s accepted successfully. All other applications withdrawn.",
                opp != null ? opp.getTitle() : "(Unknown Title)",
                opp != null ? opp.getCompanyName() : "(Unknown Company)"
        );
    }

    // =====================================================================
    // HELPER METHODS
    // =====================================================================

    /**
     * Helper function to find internship by its ID.
     * 
     * @param opportunities list of all internship opportunities
     * @param id the opportunity ID to search for
     * @return the matching internship opportunity or null
     */
    private InternshipOpportunity findOpportunity(List<InternshipOpportunity> opportunities, String id) {
        if (opportunities == null) return null;
        for (InternshipOpportunity opp : opportunities) {
            if (opp.getOpportunityId().equals(id)) {
                return opp;
            }
        }
        return null;
    }
    
    // =====================================================================
    // CONTROLLER INTEGRATION METHODS (Spring Boot REST API)
    // =====================================================================
    
    /**
     * View available opportunities for a student (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student viewing opportunities
     * @return list of available internship opportunities
     */
    public List<InternshipOpportunity> viewAvailableOpportunities(Student student) {
        // This would typically inject repositories, but for now return empty list
        // Implementation will be added when repositories are integrated
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * View applications for a student (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student viewing their applications
     * @return list of student's applications
     */
    public List<Application> viewApplications(Student student) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * Apply for an internship opportunity (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student applying
     * @param opportunityId the internship opportunity ID
     * @return the created application
     */
    public Application applyForOpportunity(Student student, String opportunityId) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * Accept a placement offer (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student accepting the placement
     * @param applicationId the application ID to accept
     */
    public void acceptPlacement(Student student, String applicationId) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * Request withdrawal from an application (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student requesting withdrawal
     * @param applicationId the application ID
     * @param reason the withdrawal reason
     * @return the created withdrawal request
     */
    public model.WithdrawalRequest requestWithdrawal(Student student, String applicationId, String reason) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * Get count of active applications for a student (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student
     * @return count of active applications
     */
    public long getActiveApplicationCount(Student student) {
        return student.getActiveApplicationsCount();
    }
    
    /**
     * Get all withdrawal requests for a student (REST API version).
     * This method is used by the REST controller.
     * 
     * @param student the student
     * @return list of withdrawal requests
     */
    public List<model.WithdrawalRequest> getWithdrawalRequests(Student student) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * Update a pending withdrawal request (REST API version).
     * Only pending requests can be updated.
     * This method is used by the REST controller.
     * 
     * @param student the student who owns the request
     * @param withdrawalId the withdrawal request ID
     * @param newReason the updated reason
     * @return the updated withdrawal request
     */
    public model.WithdrawalRequest updateWithdrawalRequest(Student student, String withdrawalId, String newReason) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
    
    /**
     * Cancels a pending withdrawal request.
     * Allows the student to withdraw their withdrawal request so they can accept the placement.
     * Updates status to CANCELLED instead of deleting the record.
     * 
     * @param student the student who owns the request
     * @param withdrawalId the withdrawal request ID
     * @param cancellationReason the reason for cancelling the withdrawal request
     */
    public void cancelWithdrawalRequest(Student student, String withdrawalId, String cancellationReason) {
        throw new UnsupportedOperationException("This method requires repository integration. Use StudentServiceImpl instead.");
    }
}
