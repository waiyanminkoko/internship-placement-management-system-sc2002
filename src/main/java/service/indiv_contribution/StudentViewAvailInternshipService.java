package service.indiv_contribution;

import model.InternshipOpportunity;
import model.Student;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentViewAvailInternshipService {

    // Returns internships visible to students that match their major and level eligibility
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

    // Returns internships that are currently open for application (approved, visible, within dates, and slots available)
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
}
