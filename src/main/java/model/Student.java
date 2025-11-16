package model;

import enums.InternshipLevel;
import enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a Student user in the system.
 * 
 * <p>Students can browse internship opportunities, submit applications,
 * accept placement offers, and request withdrawals. Access and capabilities
 * are determined by their year of study and major.</p>
 * 
 * <p><b>Key Business Rules:</b></p>
 * <ul>
 *   <li>Maximum of 3 concurrent internship applications</li>
 *   <li>Year 1-2 students can ONLY apply for BASIC level internships</li>
 *   <li>Year 3-4 students can apply for all levels (BASIC, INTERMEDIATE, ADVANCED)</li>
 *   <li>Only 1 internship placement can be accepted</li>
 *   <li>Cannot apply for new internships after accepting a placement</li>
 *   <li>All other applications automatically withdrawn when placement accepted</li>
 * </ul>
 * 
 * <p><b>Registration:</b> Students are automatically registered by reading from
 * the student list CSV file at system initialization.</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Maximum number of concurrent applications allowed per student.
     */
    public static final int MAX_APPLICATIONS = 3;

    /**
     * Year of study (1, 2, 3, or 4).
     * Determines eligibility for internship levels.
     */
    private int yearOfStudy;

    /**
     * Student's major/program of study.
     * Examples: CSC (Computer Science), EEE (Electrical Engineering), MAE (Mechanical Engineering)
     * Used to filter internships by preferred major.
     */
    private String major;

    /**
     * List of application IDs for internships this student has applied for.
     * Maximum size is MAX_APPLICATIONS (3).
     */
    private List<String> applicationIds;

    /**
     * ID of the accepted internship placement, if any.
     * Null if no placement has been accepted yet.
     */
    private String acceptedPlacementId;

    /**
     * Default constructor required for serialization.
     */
    public Student() {
        super();
        this.applicationIds = new ArrayList<>();
    }

    /**
     * Parameterized constructor to create a student with all required fields.
     * 
     * @param userId unique student identifier (format: U2345123F)
     * @param password student's password
     * @param name student's full name
     * @param email student's email address
     * @param yearOfStudy year of study (1-4)
     * @param major student's major/program
     */
    public Student(String userId, String password, String name, String email, 
                   int yearOfStudy, String major) {
        super(userId, password, name, email, UserRole.STUDENT);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applicationIds = new ArrayList<>();
        this.acceptedPlacementId = null;
    }

    /**
     * Checks if the student can apply for internships of a specific level.
     * 
     * <p><b>Eligibility Rules:</b></p>
     * <ul>
     *   <li>Year 1-2: BASIC level only</li>
     *   <li>Year 3-4: All levels (BASIC, INTERMEDIATE, ADVANCED)</li>
     * </ul>
     * 
     * @param level the internship level to check
     * @return true if student is eligible for the level, false otherwise
     */
    public boolean canApplyForLevel(InternshipLevel level) {
        if (level == null) {
            return false;
        }
        
        // Year 1-2 students can only apply for BASIC level
        if (yearOfStudy <= 2) {
            return level == InternshipLevel.BASIC;
        }
        
        // Year 3-4 students can apply for all levels
        return true;
    }

    /**
     * Checks if the student has reached the maximum number of applications.
     * 
     * @return true if student has 3 or more applications, false otherwise
     */
    public boolean hasMaxApplications() {
        return applicationIds != null && applicationIds.size() >= MAX_APPLICATIONS;
    }

    /**
     * Checks if the student has accepted an internship placement.
     * 
     * @return true if student has accepted a placement, false otherwise
     */
    public boolean hasAcceptedPlacement() {
        return acceptedPlacementId != null && !acceptedPlacementId.trim().isEmpty();
    }

    /**
     * Checks if the student has accepted an internship placement (boolean method).
     * 
     * @return true if student has accepted a placement, false otherwise
     */
    public boolean isHasAcceptedPlacement() {
        return hasAcceptedPlacement();
    }

    /**
     * Checks if the student can submit a new application.
     * 
     * <p><b>Conditions:</b></p>
     * <ul>
     *   <li>Must not have reached maximum applications (3)</li>
     *   <li>Must not have already accepted a placement</li>
     * </ul>
     * 
     * @return true if student can apply, false otherwise
     */
    public boolean canSubmitApplication() {
        return !hasMaxApplications() && !hasAcceptedPlacement();
    }

    /**
     * Adds an application ID to the student's list of applications.
     * 
     * @param applicationId the ID of the application to add
     * @return true if application was added successfully, false if maximum reached
     */
    public boolean addApplication(String applicationId) {
        if (hasMaxApplications() || hasAcceptedPlacement()) {
            return false;
        }
        if (applicationIds == null) {
            applicationIds = new ArrayList<>();
        }
        applicationIds.add(applicationId);
        return true;
    }

    /**
     * Removes an application ID from the student's list of applications.
     * 
     * @param applicationId the ID of the application to remove
     * @return true if application was removed successfully, false otherwise
     */
    public boolean removeApplication(String applicationId) {
        if (applicationIds == null) {
            return false;
        }
        return applicationIds.remove(applicationId);
    }

    /**
     * Accepts an internship placement and records the placement ID.
     * 
     * @param placementId the ID of the accepted placement
     */
    public void acceptPlacement(String placementId) {
        this.acceptedPlacementId = placementId;
    }

    /**
     * Sets whether the student has accepted a placement.
     * 
     * @param hasAccepted true if placement is accepted, false otherwise
     */
    public void setHasAcceptedPlacement(boolean hasAccepted) {
        if (!hasAccepted) {
            this.acceptedPlacementId = null;
        }
    }

    /**
     * Gets the year of study (alias for getYearOfStudy).
     * 
     * @return the year of study
     */
    public int getYear() {
        return yearOfStudy;
    }

    /**
     * Gets the count of active applications (alias for getApplicationIds().size()).
     * 
     * @return the number of active applications
     */
    public int getActiveApplicationsCount() {
        return applicationIds != null ? applicationIds.size() : 0;
    }

    /**
     * Checks if the student can apply for more applications.
     * Same as canSubmitApplication() - provides more readable method name.
     * 
     * @return true if student can apply for more internships, false otherwise
     */
    public boolean canApplyMoreApplications() {
        return canSubmitApplication();
    }

    /**
     * Sets the year of study (alias for setYearOfStudy).
     * 
     * @param year the year of study
     */
    public void setYear(Integer year) {
        if (year != null) {
            this.yearOfStudy = year;
        }
    }

    /**
     * Gets the number of active applications.
     * 
     * @return the count of applications
     */
    public int getApplicationCount() {
        return applicationIds != null ? applicationIds.size() : 0;
    }

    /**
     * Checks if the student is a junior (Year 1-2).
     * 
     * @return true if year of study is 1 or 2
     */
    public boolean isJunior() {
        return yearOfStudy <= 2;
    }

    /**
     * Checks if the student is a senior (Year 3-4).
     * 
     * @return true if year of study is 3 or 4
     */
    public boolean isSenior() {
        return yearOfStudy >= 3;
    }

    /**
     * Gets a display-friendly representation of the student.
     * Overrides the abstract method from User class.
     * 
     * @return formatted string with student details
     */
    @Override
    public String getDisplayInfo() {
        return String.format(
            "Student: %s (%s)\nYear: %d | Major: %s\nApplications: %d/%d | Accepted Placement: %s",
            getName(), getUserId(), yearOfStudy, major, 
            getApplicationCount(), MAX_APPLICATIONS,
            hasAcceptedPlacement() ? "Yes" : "No"
        );
    }

    /**
     * Returns a string representation of the student.
     * 
     * @return a string containing student details
     */
    @Override
    public String toString() {
        return String.format(
            "Student{userId='%s', name='%s', year=%d, major='%s', applications=%d, hasPlacement=%s}",
            getUserId(), getName(), yearOfStudy, major, 
            getApplicationCount(), hasAcceptedPlacement()
        );
    }
}
