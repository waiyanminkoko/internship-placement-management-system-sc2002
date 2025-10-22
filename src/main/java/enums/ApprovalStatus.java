package enums;

/**
 * Enumeration representing the approval status for various requests in the system.
 * 
 * <p>This enum is used for tracking the approval workflow of:</p>
 * <ul>
 *   <li>Company representative account authorization requests</li>
 *   <li>Internship opportunity approval by career center staff</li>
 *   <li>Student withdrawal requests</li>
 * </ul>
 * 
 * <p><b>Status Flow:</b></p>
 * <pre>
 * PENDING → APPROVED (authorized by career center staff)
 *        → REJECTED (rejected by career center staff)
 * </pre>
 * 
 * <p><b>Business Rules:</b></p>
 * <ul>
 *   <li>All requests start with PENDING status</li>
 *   <li>Career Center Staff has authority to approve or reject</li>
 *   <li>Once APPROVED or REJECTED, the decision is final</li>
 *   <li>Company representatives can only login after account is APPROVED</li>
 *   <li>Internship opportunities only visible to students when APPROVED</li>
 *   <li>Withdrawal requests only processed when APPROVED</li>
 * </ul>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
public enum ApprovalStatus {
    
    /**
     * Request is pending review by career center staff.
     * Default status for all new requests.
     */
    PENDING("Pending"),
    
    /**
     * Request has been approved by career center staff.
     * Action is now authorized and can proceed.
     */
    APPROVED("Approved"),
    
    /**
     * Request has been rejected by career center staff.
     * Action is denied and cannot proceed.
     */
    REJECTED("Rejected");

    private final String displayName;

    /**
     * Constructor for ApprovalStatus enum.
     * 
     * @param displayName the human-readable name for the status
     */
    ApprovalStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the approval status.
     * 
     * @return the human-readable name of the status
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts the enum to a string representation.
     * 
     * @return the display name of the status
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Checks if the status represents a pending (reviewable) state.
     * 
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return this == PENDING;
    }

    /**
     * Checks if the status represents an approved state.
     * 
     * @return true if status is APPROVED
     */
    public boolean isApproved() {
        return this == APPROVED;
    }

    /**
     * Checks if the status represents a rejected state.
     * 
     * @return true if status is REJECTED
     */
    public boolean isRejected() {
        return this == REJECTED;
    }

    /**
     * Checks if the status represents a final (non-changeable) decision.
     * 
     * @return true if status is APPROVED or REJECTED
     */
    public boolean isFinalDecision() {
        return this == APPROVED || this == REJECTED;
    }

    /**
     * Parses a string to get the corresponding ApprovalStatus.
     * Case-insensitive matching.
     * 
     * @param value the string value to parse
     * @return the corresponding ApprovalStatus, or null if not found
     */
    public static ApprovalStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.name().equalsIgnoreCase(value) || 
                status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
