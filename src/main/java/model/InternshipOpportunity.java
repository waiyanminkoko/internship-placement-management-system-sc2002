package model;

import enums.InternshipLevel;
import enums.InternshipStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity class representing an Internship Opportunity in the system.
 * 
 * <p>Internship opportunities are created by company representatives and must be
 * approved by career center staff before students can apply. Each opportunity
 * has specific requirements and a limited number of available slots.</p>
 * 
 * <p><b>Key Business Rules:</b></p>
 * <ul>
 *   <li>Must be approved by career center staff before becoming visible to students</li>
 *   <li>Maximum of 10 slots per internship opportunity</li>
 *   <li>Status becomes FILLED when all slots are confirmed by students</li>
 *   <li>Students cannot apply after closing date or when status is FILLED</li>
 *   <li>Visibility can be toggled by company representative</li>
 *   <li>Only visible to students matching preferred major or "Any"</li>
 *   <li>Level determines student eligibility (Year 1-2 for BASIC only)</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
public class InternshipOpportunity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Maximum number of slots per internship opportunity.
     */
    public static final int MAX_SLOTS = 10;

    /**
     * Unique identifier for the internship opportunity.
     * Generated automatically by the system.
     */
    private String opportunityId;

    /**
     * Title of the internship position.
     * Example: "Software Engineering Intern", "Data Analyst Intern"
     */
    private String title;

    /**
     * Detailed description of the internship role and responsibilities.
     */
    private String description;

    /**
     * Level of the internship (BASIC, INTERMEDIATE, ADVANCED).
     * Determines student eligibility based on year of study.
     */
    private InternshipLevel level;

    /**
     * Preferred major for applicants.
     * Can be specific (e.g., "CSC", "EEE") or "Any" for all majors.
     */
    private String preferredMajor;

    /**
     * Date when applications open for this internship.
     */
    private LocalDate openingDate;

    /**
     * Date when applications close for this internship.
     * Applications cannot be submitted after this date.
     */
    private LocalDate closingDate;

    /**
     * Date when the internship period starts.
     */
    private LocalDate startDate;

    /**
     * Date when the internship period ends.
     */
    private LocalDate endDate;

    /**
     * Current status of the internship opportunity.
     * Flow: PENDING → APPROVED → FILLED (or REJECTED)
     */
    private InternshipStatus status;

    /**
     * Name of the company offering the internship.
     */
    private String companyName;

    /**
     * User ID of the company representative who created this opportunity.
     */
    private String representativeId;

    /**
     * Email of the company representative who created this opportunity.
     */
    private String createdBy;

    /**
     * Email of the company representative for this opportunity.
     */
    private String companyRepEmail;

    /**
     * Total number of available slots for this internship.
     * Maximum value is MAX_SLOTS (10).
     */
    private int totalSlots;

    /**
     * Number of slots that have been confirmed by students.
     * Internship becomes FILLED when filledSlots equals totalSlots.
     */
    private int filledSlots;

    /**
     * Visibility flag indicating if students can see this opportunity.
     * Can be toggled by company representative.
     * Only affects APPROVED internships (PENDING/REJECTED never visible).
     */
    private boolean visible;

    /**
     * Transient field: Count of pending applications for this opportunity.
     * This is calculated at runtime and not persisted to CSV.
     * Used for UI display purposes only.
     */
    private transient int pendingApplicationCount;

    /**
     * Default constructor required for serialization.
     */
    public InternshipOpportunity() {
        this.status = InternshipStatus.PENDING;
        this.filledSlots = 0;
        this.visible = true;
        this.pendingApplicationCount = 0;
    }

    /**
     * Parameterized constructor to create an internship opportunity with required fields.
     * 
     * @param opportunityId unique identifier
     * @param title internship title
     * @param description internship description
     * @param level internship level
     * @param preferredMajor preferred major or "Any"
     * @param openingDate application opening date
     * @param closingDate application closing date
     * @param startDate internship start date
     * @param endDate internship end date
     * @param companyName company offering the internship
     * @param representativeId ID of the representative creating the opportunity
     * @param totalSlots number of available positions
     */
    public InternshipOpportunity(String opportunityId, String title, String description,
                                InternshipLevel level, String preferredMajor,
                                LocalDate openingDate, LocalDate closingDate,
                                LocalDate startDate, LocalDate endDate,
                                String companyName, String representativeId, int totalSlots) {
        this.opportunityId = opportunityId;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companyName = companyName;
        this.representativeId = representativeId;
        this.createdBy = representativeId;
        this.companyRepEmail = representativeId;
        this.totalSlots = Math.min(totalSlots, MAX_SLOTS);
        this.status = InternshipStatus.PENDING;
        this.filledSlots = 0;
        this.visible = true;
    }

    /**
     * Checks if the internship is currently accepting applications.
     * 
     * <p><b>Conditions:</b></p>
     * <ul>
     *   <li>Status must be APPROVED (not PENDING, REJECTED, or FILLED)</li>
     *   <li>Current date must be between opening and closing dates</li>
     *   <li>Must have available slots (filledSlots less than totalSlots)</li>
     *   <li>Must be visible</li>
     * </ul>
     * 
     * @return true if accepting applications, false otherwise
     */
    public boolean isAcceptingApplications() {
        if (status != InternshipStatus.APPROVED || !visible) {
            return false;
        }
        LocalDate today = LocalDate.now();
        if (today.isBefore(openingDate) || today.isAfter(closingDate)) {
            return false;
        }
        return filledSlots < totalSlots;
    }

    /**
     * Checks if the internship is visible to students.
     * 
     * @return true if status is APPROVED or FILLED and visibility is on
     */
    public boolean isVisibleToStudents() {
        return visible && (status == InternshipStatus.APPROVED || status == InternshipStatus.FILLED);
    }

    /**
     * Checks if the internship has available slots.
     * 
     * @return true if filled slots is less than total slots
     */
    public boolean hasAvailableSlots() {
        return filledSlots < totalSlots;
    }

    /**
     * Confirms one placement, incrementing the filled slots count.
     * If all slots are filled, status is updated to FILLED.
     * 
     * @return true if slot was confirmed successfully, false if no slots available
     */
    public boolean confirmPlacement() {
        if (filledSlots >= totalSlots) {
            return false;
        }
        filledSlots++;
        if (filledSlots >= totalSlots) {
            status = InternshipStatus.FILLED;
            visible = false; // Automatically hide when filled
        }
        return true;
    }

    /**
     * Releases one placement slot (e.g., due to withdrawal).
     * Updates status from FILLED to APPROVED if applicable.
     * 
     * @return true if slot was released successfully, false if no filled slots
     */
    public boolean releasePlacement() {
        if (filledSlots <= 0) {
            return false;
        }
        filledSlots--;
        if (status == InternshipStatus.FILLED && filledSlots < totalSlots) {
            status = InternshipStatus.APPROVED;
            visible = true; // Make visible again when slots become available
        }
        return true;
    }

    /**
     * Gets the number of available slots remaining.
     * 
     * @return number of unfilled slots
     */
    public int getAvailableSlots() {
        return totalSlots - filledSlots;
    }

    /**
     * Checks if the internship can accept applications.
     * Same as isAcceptingApplications() - provides more readable method name.
     * 
     * @return true if accepting applications, false otherwise
     */
    public boolean canAcceptApplications() {
        return isAcceptingApplications();
    }

    /**
     * Checks if the internship is completely filled.
     * 
     * @return true if all slots are filled, false otherwise
     */
    public boolean isFilled() {
        return filledSlots >= totalSlots;
    }

    /**
     * Checks if the internship can be edited by the representative.
     * Only PENDING internships can be edited (before staff approval).
     * 
     * @return true if status is PENDING
     */
    public boolean canBeEdited() {
        return status == InternshipStatus.PENDING;
    }

    /**
     * Checks if this opportunity matches the student's major.
     * All students can see all internships, but preferred major indicates better fit.
     * 
     * @param studentMajor the student's major
     * @return always true - all students can see all internships regardless of major
     */
    public boolean matchesMajor(String studentMajor) {
        // Allow all students to see all internships regardless of major preference
        return true;
    }

    /**
     * Checks if the internship is eligible for a specific student.
     * 
     * @param student the student to check
     * @return true if student is eligible
     */
    public boolean isEligibleForStudent(model.Student student) {
        if (student == null) {
            return false;
        }
        return matchesMajor(student.getMajor()) && student.canApplyForLevel(level);
    }

    /**
     * Checks if the application period is currently open.
     * 
     * @return true if applications are being accepted
     */
    public boolean isApplicationOpen() {
        if (status != InternshipStatus.APPROVED) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !today.isBefore(openingDate) && !today.isAfter(closingDate);
    }

    /**
     * Increments the filled slots count.
     * 
     * @return true if increment was successful
     */
    public boolean incrementFilledSlots() {
        if (filledSlots >= totalSlots) {
            return false;
        }
        filledSlots++;
        if (filledSlots >= totalSlots) {
            status = InternshipStatus.FILLED;
            visible = false; // Automatically hide when filled
        }
        return true;
    }

    /**
     * Approves the internship opportunity (called by career center staff).
     */
    public void approve() {
        this.status = InternshipStatus.APPROVED;
    }

    /**
     * Rejects the internship opportunity (called by career center staff).
     */
    public void reject() {
        this.status = InternshipStatus.REJECTED;
    }

    /**
     * Gets the visibility status of the internship.
     * 
     * @return true if visible, false if hidden
     */
    public boolean isVisible() {
        return this.visible;
    }
    
    /**
     * Gets the visibility status of the internship (alias for JSON serialization).
     * 
     * @return true if visible, false if hidden
     */
    public boolean getVisibility() {
        return this.visible;
    }

    /**
     * Toggles the visibility of the internship.
     */
    public void toggleVisibility() {
        this.visible = !this.visible;
    }

    /**
     * Sets the visibility of the internship.
     * 
     * @param visible the visibility flag
     */
    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the email of the representative who created this opportunity.
     * 
     * @return the creator's email
     */
    public String getCreatedBy() {
        return createdBy != null ? createdBy : representativeId;
    }

    /**
     * Sets the email of the representative who created this opportunity.
     * 
     * @param createdBy the creator's email
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the company representative's email.
     * 
     * @return the company rep email
     */
    public String getCompanyRepEmail() {
        return companyRepEmail != null ? companyRepEmail : representativeId;
    }

    /**
     * Sets the company representative's email.
     * 
     * @param companyRepEmail the company rep email
     */
    public void setCompanyRepEmail(String companyRepEmail) {
        this.companyRepEmail = companyRepEmail;
    }

    /**
     * Gets the total number of slots (alias for getTotalSlots).
     * 
     * @return the total number of slots
     */
    public int getSlots() {
        return totalSlots;
    }

    /**
     * Sets the total number of slots (alias for setTotalSlots).
     * 
     * @param slots the number of slots
     */
    public void setSlots(int slots) {
        this.totalSlots = Math.min(slots, MAX_SLOTS);
    }

    /**
     * Gets the internship ID (alias for getOpportunityId).
     * 
     * @return the internship ID
     */
    public String getInternshipId() {
        return opportunityId;
    }

    /**
     * Sets the internship ID (alias for setOpportunityId).
     * Used for CSV persistence.
     * 
     * @param internshipId the internship ID to set
     */
    public void setInternshipId(String internshipId) {
        this.opportunityId = internshipId;
    }

    /**
     * Compares this internship to another object for equality.
     * Two internships are equal if they have the same opportunityId.
     * 
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternshipOpportunity that = (InternshipOpportunity) o;
        return Objects.equals(opportunityId, that.opportunityId);
    }

    /**
     * Generates a hash code for this internship based on opportunityId.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(opportunityId);
    }

    /**
     * Returns a string representation of the internship opportunity.
     * 
     * @return a string containing internship details
     */
    @Override
    public String toString() {
        return String.format(
            "InternshipOpportunity{id='%s', title='%s', company='%s', level=%s, status=%s, slots=%d/%d}",
            opportunityId, title, companyName, level, status, filledSlots, totalSlots
        );
    }
}
