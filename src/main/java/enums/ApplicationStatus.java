package enums;

/**
 * Enumeration representing the status of a student's application.
 * 
 * <p>This enum tracks the lifecycle of a student's application for an internship,
 * from initial submission through final outcome.</p>
 * 
 * <p><b>Status Flow:</b></p>
 * <pre>
 * PENDING → SUCCESSFUL (approved by company representative)
 *        → UNSUCCESSFUL (rejected by company representative)
 *        → WITHDRAWN (student or staff initiated)
 * </pre>
 * 
 * <p><b>Business Rules:</b></p>
 * <ul>
 *   <li>All applications start with PENDING status</li>
 *   <li>Company Representatives can approve (→ SUCCESSFUL) or reject (→ UNSUCCESSFUL)</li>
 *   <li>Students can request withdrawal, which requires staff approval</li>
 *   <li>If student accepts another placement, all other applications become WITHDRAWN</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
public enum ApplicationStatus {
    
    /**
     * Application has been submitted and is awaiting review.
     * Default status for all new applications.
     */
    PENDING("Pending"),
    
    /**
     * Application has been approved by the company representative.
     * Student can now accept the internship placement.
     */
    SUCCESSFUL("Successful"),
    
    /**
     * Application has been rejected by the company representative.
     * Student cannot proceed with this internship.
     */
    UNSUCCESSFUL("Unsuccessful"),
    
    /**
     * Application has been withdrawn (by student request or automatic withdrawal).
     * Occurs when student accepts another placement or withdrawal is approved by staff.
     */
    WITHDRAWN("Withdrawn");

    private final String displayName;

    /**
     * Constructor for ApplicationStatus enum.
     * 
     * @param displayName the human-readable name for the status
     */
    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the application status.
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
     * Checks if the status represents a final (non-changeable) state.
     * 
     * @return true if status is SUCCESSFUL, UNSUCCESSFUL, or WITHDRAWN
     */
    public boolean isFinalStatus() {
        return this == SUCCESSFUL || this == UNSUCCESSFUL || this == WITHDRAWN;
    }

    /**
     * Checks if the status allows for placement acceptance.
     * 
     * @return true if status is SUCCESSFUL
     */
    public boolean canAcceptPlacement() {
        return this == SUCCESSFUL;
    }

    /**
     * Parses a string to get the corresponding ApplicationStatus.
     * Case-insensitive matching.
     * 
     * @param value the string value to parse
     * @return the corresponding ApplicationStatus, or null if not found
     */
    public static ApplicationStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (ApplicationStatus status : ApplicationStatus.values()) {
            if (status.name().equalsIgnoreCase(value) || 
                status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
