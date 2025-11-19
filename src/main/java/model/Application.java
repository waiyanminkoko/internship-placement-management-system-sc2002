package model;

import enums.ApplicationStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class representing an Application for an internship by a student.
 * 
 * <p>Applications track the lifecycle of a student's request to join an internship
 * opportunity, from initial submission through approval or rejection by the
 * company representative, and potential placement acceptance by the student.</p>
 * 
 * <p><b>Key Business Rules:</b></p>
 * <ul>
 *   <li>All applications start with PENDING status</li>
 *   <li>Company representatives can approve (→ SUCCESSFUL) or reject (→ REJECTED)</li>
 *   <li>Students can only accept placement for SUCCESSFUL applications</li>
 *   <li>Maximum 3 applications per student at any time</li>
 *   <li>Accepting one placement automatically withdraws all other applications</li>
 *   <li>Applications can be withdrawn by student (requires staff approval)</li>
 * </ul>
 * 
 * <p><b>Status Flow:</b></p>
 * <pre>
 * PENDING → SUCCESSFUL (approved by company) → Placement Accepted
 *        → REJECTED (rejected by company)
 *        → WITHDRAWN (student request or automatic)
 * </pre>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
public class Application implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the application.
     * Generated automatically by the system.
     */
    private String applicationId;

    /**
     * User ID of the student who submitted this application.
     */
    private String studentId;

    /**
     * ID of the internship opportunity being applied for.
     */
    private String opportunityId;

    /**
     * Current status of the application.
     * Default: PENDING
     */
    private ApplicationStatus status;

    /**
     * Date and time when the application was submitted.
     */
    private LocalDateTime submissionDate;

    /**
     * Date and time when the application status was last updated.
     * Null if never updated after submission.
     */
    private LocalDateTime statusUpdateDate;

    /**
     * Flag indicating if the student has accepted the internship placement.
     * Only applicable when status is SUCCESSFUL.
     */
    private boolean placementAccepted;

    /**
     * Date and time when the placement was accepted by the student.
     * Null if placement not yet accepted.
     */
    private LocalDateTime placementAcceptanceDate;

    /**
     * Comments or feedback from the company representative.
     * Optional field for rejection reasons or approval notes.
     */
    private String representativeComments;

    /**
     * Default constructor required for serialization.
     */
    public Application() {
        this.status = ApplicationStatus.PENDING;
        this.submissionDate = LocalDateTime.now();
        this.placementAccepted = false;
    }

    /**
     * Parameterized constructor to create an application with required fields.
     * 
     * @param applicationId unique identifier
     * @param studentId ID of the applying student
     * @param opportunityId ID of the internship opportunity
     */
    public Application(String applicationId, String studentId, String opportunityId) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.opportunityId = opportunityId;
        this.status = ApplicationStatus.PENDING;
        this.submissionDate = LocalDateTime.now();
        this.placementAccepted = false;
    }

    /**
     * Checks if the application is pending review.
     * 
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }

    /**
     * Checks if the application has been approved.
     * 
     * @return true if status is SUCCESSFUL
     */
    public boolean isSuccessful() {
        return status == ApplicationStatus.SUCCESSFUL;
    }

    /**
     * Checks if the application was rejected.
     * 
     * @return true if status is REJECTED
     */
    public boolean isRejected() {
        return status == ApplicationStatus.REJECTED;
    }

    /**
     * Checks if the application has been withdrawn.
     * 
     * @return true if status is WITHDRAWN
     */
    public boolean isWithdrawn() {
        return status == ApplicationStatus.WITHDRAWN;
    }

    /**
     * Checks if the student can accept the internship placement.
     * 
     * @return true if status is SUCCESSFUL and placement not yet accepted
     */
    public boolean canAcceptPlacement() {
        return status == ApplicationStatus.SUCCESSFUL && !placementAccepted;
    }

    /**
     * Approves the application (called by company representative).
     * 
     * @param comments optional comments from the representative
     */
    public void approve(String comments) {
        this.status = ApplicationStatus.SUCCESSFUL;
        this.statusUpdateDate = LocalDateTime.now();
        this.representativeComments = comments;
    }

    /**
     * Rejects the application (called by company representative).
     * 
     * @param reason reason for rejection
     */
    public void reject(String reason) {
        this.status = ApplicationStatus.REJECTED;
        this.statusUpdateDate = LocalDateTime.now();
        this.representativeComments = reason;
    }

    /**
     * Withdraws the application (student request or automatic).
     * 
     * @param reason reason for withdrawal
     */
    public void withdraw(String reason) {
        this.status = ApplicationStatus.WITHDRAWN;
        this.statusUpdateDate = LocalDateTime.now();
        if (reason != null && !reason.trim().isEmpty()) {
            this.representativeComments = reason;
        }
    }

    /**
     * Accepts the internship placement (called by student).
     * Can only be done for SUCCESSFUL applications.
     * 
     * @return true if placement was accepted successfully, false otherwise
     */
    public boolean acceptPlacement() {
        if (status != ApplicationStatus.SUCCESSFUL || placementAccepted) {
            return false;
        }
        this.placementAccepted = true;
        this.placementAcceptanceDate = LocalDateTime.now();
        return true;
    }

    /**
     * Checks if the application status can be changed.
     * Only PENDING applications can have their status changed.
     * 
     * @return true if status is PENDING
     */
    public boolean canChangeStatus() {
        return status == ApplicationStatus.PENDING;
    }

    /**
     * Checks if the application is currently active (not withdrawn).
     * 
     * @return true if status is not WITHDRAWN
     */
    public boolean isActive() {
        return status != ApplicationStatus.WITHDRAWN;
    }

    /**
     * Determines if this application can be withdrawn by the student.
     * 
     * Withdrawal is allowed for:
     * 1. PENDING applications (not yet reviewed by company)
     * 2. SUCCESSFUL applications that have NOT been accepted yet
     * 
     * Once a placement is accepted, withdrawal through the system is NOT allowed.
     * Students must contact Career Center Staff directly for accepted placements.
     * 
     * @return true if can be withdrawn through the system
     */
    public boolean canBeWithdrawn() {
        return status == ApplicationStatus.PENDING || 
               (status == ApplicationStatus.SUCCESSFUL && !placementAccepted);
    }

    /**
     * Sets the internship opportunity ID.
     * 
     * @param internshipId the internship ID
     */
    public void setInternshipId(String internshipId) {
        this.opportunityId = internshipId;
    }

    /**
     * Sets the application date using LocalDate.
     * 
     * @param date the application date
     */
    public void setApplicationDate(java.time.LocalDate date) {
        if (date != null) {
            this.submissionDate = date.atStartOfDay();
        }
    }

    /**
     * Gets a human-readable representation of the application status.
     * 
     * @return status display name
     */
    public String getStatusDisplay() {
        if (status == ApplicationStatus.SUCCESSFUL && placementAccepted) {
            return "Placement Accepted";
        }
        return status.getDisplayName();
    }

    /**
     * Gets the internship ID (alias for getOpportunityId).
     * 
     * @return the internship opportunity ID
     */
    public String getInternshipId() {
        return opportunityId;
    }

    /**
     * Compares this application to another object for equality.
     * Two applications are equal if they have the same applicationId.
     * 
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(applicationId, that.applicationId);
    }

    /**
     * Generates a hash code for this application based on applicationId.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(applicationId);
    }

    /**
     * Returns a string representation of the application.
     * 
     * @return a string containing application details
     */
    @Override
    public String toString() {
        return String.format(
            "Application{id='%s', student='%s', opportunity='%s', status=%s, placementAccepted=%s}",
            applicationId, studentId, opportunityId, status, placementAccepted
        );
    }
}
