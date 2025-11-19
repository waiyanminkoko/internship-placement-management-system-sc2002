package service.indiv_contribution;

import enums.ApplicationStatus;
import model.Application;
import model.InternshipOpportunity;
import model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Handles "View Applied Internships" for Student users.
 * 
 * Allows a student to view all their internship applications and
 * filter them by status (Pending, Successful, Rejected, Withdrawn).
 */
public class StudentViewAppliedInternshipService {

    // Return all applications that belong to a specific student (sorted by submission date)
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

    // Return only applications that match a specific status (e.g. Pending, Successful)
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

    // Optional: Format the results neatly for displaying in a console app
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

    // Helper function to find internship by its ID
    private InternshipOpportunity findOpportunity(List<InternshipOpportunity> opportunities, String id) {
        if (opportunities == null) return null;
        for (InternshipOpportunity opp : opportunities) {
            if (opp.getOpportunityId().equals(id)) {
                return opp;
            }
        }
        return null;
    }
}
