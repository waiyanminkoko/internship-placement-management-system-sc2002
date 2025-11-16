package enums;

/**
 * Enumeration representing the status of an internship opportunity.
 * 
 * <p>This enum tracks the approval workflow and availability status of internship
 * opportunities created by company representatives.</p>
 * 
 * <p><b>Status Flow:</b></p>
 * <pre>
 * PENDING → APPROVED (approved by career center staff)
 *        → REJECTED (rejected by career center staff)
 * APPROVED → FILLED (all slots confirmed by students)
 * </pre>
 * 
 * <p><b>Business Rules:</b></p>
 * <ul>
 *   <li>All new internship opportunities start with PENDING status</li>
 *   <li>Career Center Staff must approve before students can apply</li>
 *   <li>Only APPROVED opportunities are visible to eligible students</li>
 *   <li>Status becomes FILLED when all available slots are confirmed</li>
 *   <li>Students cannot apply to FILLED or REJECTED opportunities</li>
 *   <li>Applications cannot be submitted after closing date even if APPROVED</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
public enum InternshipStatus {
    
    /**
     * Internship opportunity has been created and is awaiting staff approval.
     * Not visible to students. Default status for new opportunities.
     */
    PENDING("Pending"),
    
    /**
     * Internship opportunity has been approved by career center staff.
     * Visible to eligible students and open for applications.
     */
    APPROVED("Approved"),
    
    /**
     * Internship opportunity has been rejected by career center staff.
     * Not visible to students and cannot accept applications.
     */
    REJECTED("Rejected"),
    
    /**
     * All available internship slots have been confirmed by students.
     * No longer accepting applications. Visible for reference only.
     */
    FILLED("Filled");

    private final String displayName;

    /**
     * Constructor for InternshipStatus enum.
     * 
     * @param displayName the human-readable name for the status
     */
    InternshipStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the internship status.
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
     * Checks if the opportunity accepts new applications.
     * 
     * @return true if status is APPROVED (not FILLED, PENDING, or REJECTED)
     */
    public boolean acceptsApplications() {
        return this == APPROVED;
    }

    /**
     * Checks if the opportunity is visible to students.
     * 
     * @return true if status is APPROVED or FILLED
     */
    public boolean isVisibleToStudents() {
        return this == APPROVED || this == FILLED;
    }

    /**
     * Checks if the status can be edited by company representatives.
     * 
     * @return true if status is PENDING (before staff approval)
     */
    public boolean canBeEdited() {
        return this == PENDING;
    }

    /**
     * Parses a string to get the corresponding InternshipStatus.
     * Case-insensitive matching.
     * 
     * @param value the string value to parse
     * @return the corresponding InternshipStatus, or null if not found
     */
    public static InternshipStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (InternshipStatus status : InternshipStatus.values()) {
            if (status.name().equalsIgnoreCase(value) || 
                status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
