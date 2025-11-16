package dto;

import enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Application with enriched internship details.
 * Used to return application data to the frontend with associated internship information.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyInternshipDTO {
    
    /**
     * Unique identifier for the application.
     */
    private String applicationId;

    /**
     * User ID of the student who submitted this application.
     */
    private String studentId;

    /**
     * ID of the internship opportunity.
     */
    private String internshipId;

    /**
     * Title of the internship opportunity.
     */
    private String internshipTitle;

    /**
     * Name of the company offering the internship.
     */
    private String companyName;

    /**
     * Current status of the application.
     */
    private ApplicationStatus status;

    /**
     * Date and time when the application was submitted.
     */
    private LocalDateTime submissionDate;

    /**
     * Date and time when the application status was last updated.
     */
    private LocalDateTime statusUpdateDate;

    /**
     * Flag indicating if the student has accepted the internship placement.
     */
    private boolean placementAccepted;

    /**
     * Date and time when the placement was accepted by the student.
     */
    private LocalDateTime placementAcceptanceDate;

    /**
     * Comments or feedback from the company representative.
     */
    private String representativeComments;
    
    /**
     * Comments or feedback from the staff.
     */
    private String staffComments;

    /**
     * Gets the applied date (alias for submissionDate).
     * 
     * @return the submission date
     */
    public LocalDateTime getAppliedDate() {
        return submissionDate;
    }

    /**
     * Sets the applied date (alias for submissionDate).
     * 
     * @param appliedDate the submission date
     */
    public void setAppliedDate(LocalDateTime appliedDate) {
        this.submissionDate = appliedDate;
    }
}
