package enums;

/**
 * Enumeration representing the different levels of internship opportunities.
 * 
 * <p>This enum defines the three tiers of internships available in the system,
 * which determines student eligibility based on their year of study:</p>
 * <ul>
 *   <li><b>BASIC</b>: Entry-level internships suitable for Year 1-4 students</li>
 *   <li><b>INTERMEDIATE</b>: Mid-level internships for Year 3-4 students only</li>
 *   <li><b>ADVANCED</b>: Advanced internships for Year 3-4 students only</li>
 * </ul>
 * 
 * <p><b>Business Rules:</b></p>
 * <ul>
 *   <li>Year 1 and 2 students can ONLY apply for BASIC level internships</li>
 *   <li>Year 3 and above students can apply for all levels (BASIC, INTERMEDIATE, ADVANCED)</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
public enum InternshipLevel {
    
    /**
     * Basic level internships suitable for all students (Year 1-4).
     * Entry-level positions with foundational responsibilities.
     */
    BASIC("Basic"),
    
    /**
     * Intermediate level internships for Year 3-4 students.
     * Requires more experience and technical proficiency.
     */
    INTERMEDIATE("Intermediate"),
    
    /**
     * Advanced level internships for Year 3-4 students.
     * High-level positions with significant responsibilities.
     */
    ADVANCED("Advanced");

    private final String displayName;

    /**
     * Constructor for InternshipLevel enum.
     * 
     * @param displayName the human-readable name for the level
     */
    InternshipLevel(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the internship level.
     * 
     * @return the human-readable name of the level
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts the enum to a string representation.
     * 
     * @return the display name of the level
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Parses a string to get the corresponding InternshipLevel.
     * Case-insensitive matching.
     * 
     * @param value the string value to parse
     * @return the corresponding InternshipLevel, or null if not found
     */
    public static InternshipLevel fromString(String value) {
        if (value == null) {
            return null;
        }
        for (InternshipLevel level : InternshipLevel.values()) {
            if (level.name().equalsIgnoreCase(value) || 
                level.displayName.equalsIgnoreCase(value)) {
                return level;
            }
        }
        return null;
    }
}
