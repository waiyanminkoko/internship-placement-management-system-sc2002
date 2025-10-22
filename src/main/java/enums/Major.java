package enums;

/**
 * Enumeration representing academic majors in the university system.
 * Used to match students with appropriate internship opportunities based on their field of study.
 * 
 * <p>The system supports major-based filtering where internships can be targeted to specific
 * academic programs or made available to all majors using the ANY designation.</p>
 * 
 * <p><strong>Supported Majors:</strong></p>
 * <ul>
 *   <li>COMPUTER_SCIENCE - Computer Science program</li>
 *   <li>COMPUTER_ENGINEERING - Computer Engineering program</li>
 *   <li>DATA_SCIENCE_AI - Data Science and Artificial Intelligence program</li>
 *   <li>BUSINESS_ANALYTICS - Business Analytics program</li>
 *   <li>ANY - Open to all majors</li>
 * </ul>
 * 
 * @author SC2002 Group 6
 * @version 1.0
 * @since 2025-01-15
 */
public enum Major {
    /**
     * Computer Science major - for software development and computing focused internships.
     */
    COMPUTER_SCIENCE("Computer Science", "CS"),
    
    /**
     * Computer Engineering major - for hardware/software integration internships.
     */
    COMPUTER_ENGINEERING("Computer Engineering", "CE"),
    
    /**
     * Data Science and Artificial Intelligence major - for data analytics and AI internships.
     */
    DATA_SCIENCE_AI("Data Science & AI", "DSAI"),
    
    /**
     * Business Analytics major - for business intelligence and analytics internships.
     */
    BUSINESS_ANALYTICS("Business Analytics", "BA"),
    
    /**
     * ANY major - internship is open to students from all academic programs.
     */
    ANY("Any Major", "ANY");

    /**
     * Full display name of the major.
     */
    private final String displayName;
    
    /**
     * Short code/abbreviation for the major.
     */
    private final String code;

    /**
     * Constructs a Major enum with display name and code.
     * 
     * @param displayName the full name of the major for display purposes
     * @param code the short code/abbreviation for the major
     */
    Major(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    /**
     * Gets the full display name of the major.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the short code/abbreviation of the major.
     * 
     * @return the major code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets a formatted string representation of the major.
     * 
     * @return formatted string with code and display name
     */
    @Override
    public String toString() {
        return code + " - " + displayName;
    }

    /**
     * Parses a string code into a Major enum value.
     * Case-insensitive matching against both code and display name.
     * 
     * @param code the code string to parse (e.g., "CS", "DSAI", "ANY")
     * @return the matching Major enum value, or ANY if no match found
     */
    public static Major fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return ANY;
        }
        
        String normalized = code.trim().toUpperCase();
        
        for (Major major : values()) {
            if (major.code.equalsIgnoreCase(normalized) || 
                major.displayName.equalsIgnoreCase(code.trim()) ||
                major.name().equalsIgnoreCase(normalized)) {
                return major;
            }
        }
        
        return ANY;
    }

    /**
     * Checks if this major matches the given major or if either is ANY.
     * Used for internship eligibility checking.
     * 
     * @param other the major to check against
     * @return true if majors match or if either is ANY
     */
    public boolean matches(Major other) {
        if (this == ANY || other == ANY) {
            return true;
        }
        return this == other;
    }
}
