package model;

import enums.ApprovalStatus;
import enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a Company Representative user in the system.
 * 
 * <p>Company representatives can create internship opportunities, manage applications,
 * approve/reject applicants, and control the visibility of their postings.</p>
 * 
 * <p><b>Key Business Rules:</b></p>
 * <ul>
 *   <li>Maximum of 5 internship opportunities can be created per representative</li>
 *   <li>Must register and be authorized by Career Center Staff before login</li>
 *   <li>Can only edit internship opportunities before staff approval</li>
 *   <li>Can approve/reject applications for their own internships only</li>
 *   <li>Can toggle visibility of their internships</li>
 * </ul>
 * 
 * <p><b>Registration:</b> Company representatives must self-register through
 * the system. Account is inactive until authorized by career center staff.</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyRepresentative extends User {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Maximum number of internship opportunities per representative.
     */
    public static final int MAX_INTERNSHIPS = 5;

    /**
     * Name of the company the representative works for.
     */
    private String companyName;

    /**
     * Industry sector of the company.
     * Example: Technology, Finance, Healthcare, Manufacturing
     */
    private String industry;

    /**
     * Job position/title of the representative.
     * Example: HR Manager, Recruitment Specialist
     */
    private String position;

    /**
     * Authorization status indicating if the representative can login.
     * Must be set to true by Career Center Staff before account is active.
     */
    private boolean isAuthorized;

    /**
     * Approval status of the representative (PENDING, APPROVED, REJECTED).
     */
    private ApprovalStatus status;

    /**
     * User ID of the staff member who approved/rejected this representative.
     */
    private String approvedByStaffId;

    /**
     * List of internship opportunity IDs created by this representative.
     * Maximum size is MAX_INTERNSHIPS (5).
     */
    private List<String> internshipIds;

    /**
     * Registration date and time when this representative account was created.
     */
    private java.time.LocalDateTime registrationDate;

    /**
     * Default constructor required for serialization.
     */
    public CompanyRepresentative() {
        super();
        this.isAuthorized = false;
        this.status = ApprovalStatus.PENDING;
        this.internshipIds = new ArrayList<>();
        this.registrationDate = java.time.LocalDateTime.now();
    }

    /**
     * Parameterized constructor to create a company representative with all required fields.
     * 
     * @param userId unique identifier (company email address)
     * @param password representative's password
     * @param name representative's full name
     * @param email representative's email address
     * @param companyName name of the company
     * @param industry industry sector of the company
     * @param position job position/title
     */
    public CompanyRepresentative(String userId, String password, String name, String email,
                                String companyName, String industry, String position) {
        super(userId, password, name, email, UserRole.COMPANY_REPRESENTATIVE);
        this.companyName = companyName;
        this.industry = industry;
        this.position = position;
        this.isAuthorized = false;
        this.status = ApprovalStatus.PENDING;
        this.internshipIds = new ArrayList<>();
    }

    /**
     * Alternative constructor with approval status.
     * 
     * @param userId unique identifier
     * @param name representative's name
     * @param password representative's password
     * @param companyName name of the company
     * @param industry industry sector of the company
     * @param position job position/title
     * @param status approval status
     */
    public CompanyRepresentative(String userId, String name, String password,
                                String companyName, String industry, String position, ApprovalStatus status) {
        super(userId, password, name, userId, UserRole.COMPANY_REPRESENTATIVE);
        this.companyName = companyName;
        this.industry = industry;
        this.position = position;
        this.status = status;
        this.isAuthorized = (status == ApprovalStatus.APPROVED);
        this.internshipIds = new ArrayList<>();
    }

    /**
     * Checks if the representative has reached the maximum number of internships.
     * 
     * @return true if representative has 5 or more internships, false otherwise
     */
    public boolean hasMaxInternships() {
        return internshipIds != null && internshipIds.size() >= MAX_INTERNSHIPS;
    }

    /**
     * Checks if the representative can create a new internship opportunity.
     * 
     * <p><b>Conditions:</b></p>
     * <ul>
     *   <li>Must be authorized by staff</li>
     *   <li>Must not have reached maximum internships (5)</li>
     * </ul>
     * 
     * @return true if representative can create internship, false otherwise
     */
    public boolean canCreateInternship() {
        return isAuthorized && !hasMaxInternships();
    }

    /**
     * Checks if the representative can login to the system.
     * 
     * @return true if authorized by career center staff, false otherwise
     */
    public boolean canLogin() {
        return isAuthorized;
    }

    /**
     * Checks if the representative is approved.
     * 
     * @return true if status is APPROVED, false otherwise
     */
    public boolean isApproved() {
        return status == ApprovalStatus.APPROVED;
    }

    /**
     * Adds an internship ID to the representative's list of created internships.
     * 
     * @param internshipId the ID of the internship to add
     * @return true if internship was added successfully, false if maximum reached
     */
    public boolean addInternship(String internshipId) {
        if (hasMaxInternships()) {
            return false;
        }
        if (internshipIds == null) {
            internshipIds = new ArrayList<>();
        }
        internshipIds.add(internshipId);
        return true;
    }

    /**
     * Removes an internship ID from the representative's list.
     * 
     * @param internshipId the ID of the internship to remove
     * @return true if internship was removed successfully, false otherwise
     */
    public boolean removeInternship(String internshipId) {
        if (internshipIds == null) {
            return false;
        }
        return internshipIds.remove(internshipId);
    }

    /**
     * Authorizes this representative, allowing them to login.
     * Should only be called by Career Center Staff.
     */
    public void authorize() {
        this.isAuthorized = true;
        this.status = ApprovalStatus.APPROVED;
    }

    /**
     * Revokes authorization for this representative, preventing login.
     * Should only be called by Career Center Staff.
     */
    public void revokeAuthorization() {
        this.isAuthorized = false;
        this.status = ApprovalStatus.REJECTED;
    }

    /**
     * Sets the approval status of the representative.
     * 
     * @param status the new approval status
     */
    public void setStatus(ApprovalStatus status) {
        this.status = status;
        this.isAuthorized = (status == ApprovalStatus.APPROVED);
    }

    /**
     * Sets the staff member who approved/rejected this representative.
     * 
     * @param staffId the staff member's user ID
     */
    public void setApprovedByStaffId(String staffId) {
        this.approvedByStaffId = staffId;
    }

    /**
     * Sets the internship opportunity IDs (alias for setInternshipIds).
     * Used for CSV persistence.
     * 
     * @param ids the list of internship opportunity IDs
     */
    public void setInternshipOpportunityIds(List<String> ids) {
        this.internshipIds = ids;
    }

    /**
     * Gets the internship opportunity IDs (alias for getInternshipIds).
     * Used for CSV persistence.
     * 
     * @return the list of internship opportunity IDs
     */
    public List<String> getInternshipOpportunityIds() {
        return this.internshipIds;
    }

    /**
     * Gets the number of internships created by this representative.
     * 
     * @return the count of internships
     */
    public int getInternshipCount() {
        return internshipIds != null ? internshipIds.size() : 0;
    }

    /**
     * Checks if the representative can create more opportunities.
     * Same as canCreateInternship() - provides more readable method name.
     * 
     * @return true if representative can create more opportunities, false otherwise
     */
    public boolean canCreateMoreOpportunities() {
        return canCreateInternship();
    }

    /**
     * Adds an opportunity ID (alias for addInternship).
     * 
     * @param opportunityId the ID of the opportunity to add
     * @return true if opportunity was added successfully, false if maximum reached
     */
    public boolean addOpportunity(String opportunityId) {
        boolean added = addInternship(opportunityId);
        if (!added && hasMaxInternships()) {
            throw new IllegalStateException("Cannot create more than " + MAX_INTERNSHIPS + " internship opportunities");
        }
        return added;
    }

    /**
     * Gets the count of opportunities (alias for getInternshipCount).
     * 
     * @return the number of internship opportunities
     */
    public int getOpportunityCount() {
        return getInternshipCount();
    }

    /**
     * Gets a display-friendly representation of the company representative.
     * Overrides the abstract method from User class.
     * 
     * @return formatted string with representative details
     */
    @Override
    public String getDisplayInfo() {
        return String.format(
            "Company Representative: %s (%s)\nCompany: %s | Industry: %s\nPosition: %s\nAuthorized: %s | Internships: %d/%d",
            getName(), getUserId(), companyName, industry, position,
            isAuthorized ? "Yes" : "No", getInternshipCount(), MAX_INTERNSHIPS
        );
    }

    /**
     * Returns a string representation of the company representative.
     * 
     * @return a string containing representative details
     */
    @Override
    public String toString() {
        return String.format(
            "CompanyRepresentative{userId='%s', name='%s', company='%s', authorized=%s, internships=%d}",
            getUserId(), getName(), companyName, isAuthorized, getInternshipCount()
        );
    }
}
