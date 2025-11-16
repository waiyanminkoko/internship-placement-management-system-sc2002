package model;

import enums.ApprovalStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class representing a Withdrawal Request submitted by a student.
 * 
 * <p>Students can request to withdraw from an internship application at any stage,
 * including after accepting a placement. All withdrawal requests must be reviewed
 * and approved by career center staff before taking effect.</p>
 * 
 * <p><b>Key Business Rules:</b></p>
 * <ul>
 *   <li>Withdrawal requests can be submitted before or after placement confirmation</li>
 *   <li>All requests must be approved by career center staff</li>
 *   <li>Approved withdrawals release the internship slot</li>
 *   <li>Student must provide a reason for withdrawal</li>
 *   <li>Staff can approve or reject with comments</li>
 *   <li>Once processed (approved/rejected), decision is final</li>
 * </ul>
 * 
 * <p><b>Status Flow:</b></p>
 * <pre>
 * PENDING → APPROVED (by career center staff) → Slot released, application withdrawn
 *        → REJECTED (by career center staff) → Application remains active
 * </pre>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
public class WithdrawalRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the withdrawal request.
     * Generated automatically by the system.
     */
    private String requestId;

    /**
     * User ID of the student submitting the withdrawal request.
     */
    private String studentId;

    /**
     * ID of the application being withdrawn.
     */
    private String applicationId;

    /**
     * ID of the internship opportunity being withdrawn from.
     */
    private String internshipId;

    /**
     * Student's reason for requesting withdrawal.
     * Required field that must be provided by the student.
     */
    private String reason;

    /**
     * Current status of the withdrawal request.
     * Default: PENDING
     */
    private ApprovalStatus status;

    /**
     * Date and time when the withdrawal request was submitted.
     */
    private LocalDateTime requestDate;

    /**
     * User ID of the career center staff who processed this request.
     * Null until request is processed.
     */
    private String processedBy;

    /**
     * Date and time when the request was processed (approved or rejected).
     * Null if still pending.
     */
    private LocalDateTime processedDate;

    /**
     * Comments from the career center staff regarding the decision.
     * Optional field for approval notes or rejection reasons.
     */
    private String staffComments;

    /**
     * Flag indicating if this withdrawal is for a confirmed placement.
     * True if student has already accepted the internship placement.
     */
    private boolean isPlacementConfirmed;

    /**
     * Default constructor required for serialization.
     */
    public WithdrawalRequest() {
        this.status = ApprovalStatus.PENDING;
        this.requestDate = LocalDateTime.now();
        this.isPlacementConfirmed = false;
    }

    /**
     * Parameterized constructor to create a withdrawal request with required fields.
     * 
     * @param requestId unique identifier
     * @param studentId ID of the student submitting the request
     * @param applicationId ID of the application to withdraw
     * @param reason student's reason for withdrawal
     * @param isPlacementConfirmed whether the placement has been confirmed
     */
    public WithdrawalRequest(String requestId, String studentId, String applicationId,
                            String reason, boolean isPlacementConfirmed) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.applicationId = applicationId;
        this.reason = reason;
        this.status = ApprovalStatus.PENDING;
        this.requestDate = LocalDateTime.now();
        this.isPlacementConfirmed = isPlacementConfirmed;
    }

    /**
     * Checks if the request is pending review.
     * 
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return status == ApprovalStatus.PENDING;
    }

    /**
     * Checks if the request has been approved.
     * 
     * @return true if status is APPROVED
     */
    public boolean isApproved() {
        return status == ApprovalStatus.APPROVED;
    }

    /**
     * Checks if the request has been rejected.
     * 
     * @return true if status is REJECTED
     */
    public boolean isRejected() {
        return status == ApprovalStatus.REJECTED;
    }

    /**
     * Checks if the request has been processed (approved or rejected).
     * 
     * @return true if status is not PENDING
     */
    public boolean isProcessed() {
        return status != ApprovalStatus.PENDING;
    }

    /**
     * Approves the withdrawal request (called by career center staff).
     * 
     * @param staffId ID of the staff member approving the request
     * @param comments optional comments about the approval
     */
    public void approve(String staffId, String comments) {
        this.status = ApprovalStatus.APPROVED;
        this.processedBy = staffId;
        this.processedDate = LocalDateTime.now();
        this.staffComments = comments;
    }

    /**
     * Rejects the withdrawal request (called by career center staff).
     * 
     * @param staffId ID of the staff member rejecting the request
     * @param reason reason for rejection
     */
    public void reject(String staffId, String reason) {
        this.status = ApprovalStatus.REJECTED;
        this.processedBy = staffId;
        this.processedDate = LocalDateTime.now();
        this.staffComments = reason;
    }

    /**
     * Checks if this is a post-placement withdrawal request.
     * These may require additional scrutiny or different processing.
     * 
     * @return true if withdrawal is for a confirmed placement
     */
    public boolean isPostPlacement() {
        return isPlacementConfirmed;
    }

    /**
     * Checks if this is a pre-placement withdrawal request.
     * 
     * @return true if withdrawal is before placement confirmation
     */
    public boolean isPrePlacement() {
        return !isPlacementConfirmed;
    }

    /**
     * Gets a human-readable representation of the request type.
     * 
     * @return "Post-Placement Withdrawal" or "Pre-Placement Withdrawal"
     */
    public String getRequestType() {
        return isPlacementConfirmed ? "Post-Placement Withdrawal" : "Pre-Placement Withdrawal";
    }

    /**
     * Gets a human-readable representation of the request status.
     * 
     * @return status display name
     */
    public String getStatusDisplay() {
        return status.getDisplayName();
    }

    /**
     * Gets the withdrawal request ID (alias for getRequestId).
     * 
     * @return the withdrawal request ID
     */
    public String getWithdrawalId() {
        return requestId;
    }

    /**
     * Sets the withdrawal request ID (alias for setRequestId).
     * 
     * @param withdrawalId the withdrawal request ID
     */
    public void setWithdrawalId(String withdrawalId) {
        this.requestId = withdrawalId;
    }

    /**
     * Gets the internship opportunity ID.
     * 
     * @return the internship ID
     */
    public String getInternshipId() {
        return internshipId;
    }

    /**
     * Sets the internship opportunity ID.
     * 
     * @param internshipId the internship ID
     */
    public void setInternshipId(String internshipId) {
        this.internshipId = internshipId;
    }

    /**
     * Sets the request date using LocalDate.
     * 
     * @param date the request date
     */
    public void setRequestDate(java.time.LocalDate date) {
        if (date != null) {
            this.requestDate = date.atStartOfDay();
        }
    }

    /**
     * Compares this withdrawal request to another object for equality.
     * Two requests are equal if they have the same requestId.
     * 
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalRequest that = (WithdrawalRequest) o;
        return Objects.equals(requestId, that.requestId);
    }

    /**
     * Generates a hash code for this withdrawal request based on requestId.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    /**
     * Returns a string representation of the withdrawal request.
     * 
     * @return a string containing request details
     */
    @Override
    public String toString() {
        return String.format(
            "WithdrawalRequest{id='%s', student='%s', application='%s', status=%s, type='%s'}",
            requestId, studentId, applicationId, status, getRequestType()
        );
    }
}
