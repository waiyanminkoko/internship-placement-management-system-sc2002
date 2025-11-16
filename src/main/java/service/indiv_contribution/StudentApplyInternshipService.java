package service.indiv_contribution;

import enums.InternshipLevel;
import model.Application;
import model.InternshipOpportunity;
import model.Student;
import java.util.UUID;

public class StudentApplyInternshipService {

    // Handles the logic for applying to an internship
    public boolean applyForInternship(Student student, InternshipOpportunity opportunity) {

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

        // Add application ID to student's list of applications
        if (!student.addApplication(applicationId)) {
            System.out.println("Unable to add application to student profile.");
            return false;
        }

        System.out.println("Application submitted successfully for: " + opportunity.getTitle());
        return true;
    }
}