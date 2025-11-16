package service.indiv_contribution;

import enums.ApplicationStatus;
import model.Application;
import model.InternshipOpportunity;
import model.Student;

import java.util.List;
import java.util.Objects;

/**
 * Handles the "Accept Internship" use case for Students.
 *
 * Business Rules (Assignment §10, §18):
 * - A student can accept ONLY ONE internship placement.
 * - Acceptance only allowed for SUCCESSFUL applications.
 * - When accepted, all other applications are automatically withdrawn.
 * - The corresponding internship increments its filled slots count.
 * - Internship becomes FILLED once all slots are confirmed.
 */
public class StudentAcceptInternshipService {

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
}
