package enums;

/**
 * Enumeration representing the different user roles in the system.
 * 
 * <p>This enum defines the three types of users who can access the
 * Internship Placement Management System, each with distinct permissions
 * and capabilities.</p>
 * 
 * <p><b>Role Descriptions:</b></p>
 * <ul>
 *   <li><b>STUDENT</b>: Can browse internships, submit applications (max 3), 
 *       accept placements (max 1), and request withdrawals</li>
 *   <li><b>COMPANY_REPRESENTATIVE</b>: Can create internships (max 5), 
 *       review applications, approve/reject applicants, toggle visibility</li>
 *   <li><b>CAREER_CENTER_STAFF</b>: Can authorize representatives, 
 *       approve internship opportunities, process withdrawals, generate reports</li>
 * </ul>
 * 
 * <p><b>User ID Formats:</b></p>
 * <ul>
 *   <li>Students: Start with 'U', followed by 7 digits, end with a letter (e.g., U2345123F)</li>
 *   <li>Company Representatives: Company email address</li>
 *   <li>Career Center Staff: NTU account</li>
 * </ul>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
public enum UserRole {
    
    /**
     * Student role - can browse and apply for internships.
     * Automatically registered by reading from student list file.
     */
    STUDENT("Student"),
    
    /**
     * Company representative role - can post internships and review applications.
     * Must register and be authorized by career center staff before login.
     */
    COMPANY_REPRESENTATIVE("Company Representative"),
    
    /**
     * Career center staff role - can approve accounts, internships, and withdrawals.
     * Automatically registered by reading from staff list file.
     */
    CAREER_CENTER_STAFF("Career Center Staff");

    private final String displayName;

    /**
     * Constructor for UserRole enum.
     * 
     * @param displayName the human-readable name for the role
     */
    UserRole(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the user role.
     * 
     * @return the human-readable name of the role
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts the enum to a string representation.
     * 
     * @return the display name of the role
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Checks if this role requires authorization before login.
     * 
     * @return true if role is COMPANY_REPRESENTATIVE
     */
    public boolean requiresAuthorization() {
        return this == COMPANY_REPRESENTATIVE;
    }

    /**
     * Checks if this role has administrative privileges.
     * 
     * @return true if role is CAREER_CENTER_STAFF
     */
    public boolean isAdministrative() {
        return this == CAREER_CENTER_STAFF;
    }

    /**
     * Checks if this role can apply for internships.
     * 
     * @return true if role is STUDENT
     */
    public boolean canApplyForInternships() {
        return this == STUDENT;
    }

    /**
     * Checks if this role can create internship opportunities.
     * 
     * @return true if role is COMPANY_REPRESENTATIVE
     */
    public boolean canCreateInternships() {
        return this == COMPANY_REPRESENTATIVE;
    }

    /**
     * Parses a string to get the corresponding UserRole.
     * Case-insensitive matching.
     * 
     * @param value the string value to parse
     * @return the corresponding UserRole, or null if not found
     */
    public static UserRole fromString(String value) {
        if (value == null) {
            return null;
        }
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value) || 
                role.displayName.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}
