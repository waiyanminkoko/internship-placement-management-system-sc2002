package dto;

import enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Application with student details.
 * Used to return application data to company representatives with student information.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationWithStudentDTO {
    
    /**
     * Unique identifier for the application.
     */
    private String applicationId;

    /**
     * User ID of the student who submitted this application.
     */
    private String studentId;

    /**
     * Name of the student.
     */
    private String studentName;

    /**
     * Student's major.
     */
    private String studentMajor;

    /**
     * Student's year level.
     */
    private int studentYear;

    /**
     * Student's email.
     */
    private String studentEmail;

    /**
     * ID of the internship opportunity.
     */
    private String internshipId;

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
     * Gets the application date (alias for submissionDate).
     * 
     * @return the submission date
     */
    public LocalDateTime getApplicationDate() {
        return submissionDate;
    }

    /**
     * Sets the application date (alias for submissionDate).
     * 
     * @param applicationDate the submission date
     */
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.submissionDate = applicationDate;
    }
}
