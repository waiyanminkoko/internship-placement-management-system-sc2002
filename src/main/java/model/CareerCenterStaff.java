package model;

import enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity class representing a Career Center Staff user in the system.
 * 
 * <p>Career center staff have administrative privileges to manage the entire
 * internship placement system. They act as gatekeepers for quality control
 * and ensure compliance with university policies.</p>
 * 
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Authorize or reject company representative account registrations</li>
 *   <li>Approve or reject internship opportunities posted by companies</li>
 *   <li>Process student withdrawal requests (before and after placement)</li>
 *   <li>Generate comprehensive reports on internship activities</li>
 *   <li>Monitor system compliance and data integrity</li>
 * </ul>
 * 
 * <p><b>Registration:</b> Career center staff are automatically registered by
 * reading from the staff list CSV file at system initialization.</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CareerCenterStaff extends User {
    
    private static final long serialVersionUID = 1L;

    /**
     * Department within the career center.
     * Example: Career Services, Student Affairs, Internship Coordination
     */
    private String department;

    /**
     * List of company representative IDs approved by this staff member.
     */
    private java.util.List<String> approvedRepresentativeIds = new java.util.ArrayList<>();

    /**
     * List of internship opportunity IDs approved by this staff member.
     */
    private java.util.List<String> approvedInternshipIds = new java.util.ArrayList<>();

    /**
     * Default constructor required for serialization.
     */
    public CareerCenterStaff() {
        super();
    }

    /**
     * Parameterized constructor to create a career center staff with all required fields.
     * 
     * @param userId unique identifier (NTU account)
     * @param password staff's password
     * @param name staff's full name
     * @param email staff's email address
     * @param department staff's department
     */
    public CareerCenterStaff(String userId, String password, String name, 
                            String email, String department) {
        super(userId, password, name, email, UserRole.CAREER_CENTER_STAFF);
        this.department = department;
    }

    /**
     * Checks if this staff member has administrative privileges.
     * All career center staff have admin access.
     * 
     * @return always true for staff members
     */
    public boolean hasAdminPrivileges() {
        return true;
    }

    /**
     * Checks if this staff member can authorize company representatives.
     * All career center staff can authorize representatives.
     * 
     * @return always true for staff members
     */
    public boolean canAuthorizeRepresentatives() {
        return true;
    }

    /**
     * Checks if this staff member can approve internship opportunities.
     * All career center staff can approve internships.
     * 
     * @return always true for staff members
     */
    public boolean canApproveInternships() {
        return true;
    }

    /**
     * Checks if this staff member can process withdrawal requests.
     * All career center staff can process withdrawals.
     * 
     * @return always true for staff members
     */
    public boolean canProcessWithdrawals() {
        return true;
    }

    /**
     * Checks if this staff member can generate reports.
     * All career center staff can generate reports.
     * 
     * @return always true for staff members
     */
    public boolean canGenerateReports() {
        return true;
    }

    /**
     * Authorizes a company representative by adding their ID to the approved list.
     * 
     * @param representativeId the ID of the representative to authorize
     */
    public void authorizeRepresentative(String representativeId) {
        if (approvedRepresentativeIds == null) {
            approvedRepresentativeIds = new java.util.ArrayList<>();
        }
        if (!approvedRepresentativeIds.contains(representativeId)) {
            approvedRepresentativeIds.add(representativeId);
        }
    }

    /**
     * Approves an internship by adding its ID to the approved list.
     * 
     * @param internshipId the ID of the internship to approve
     */
    public void approveInternship(String internshipId) {
        if (approvedInternshipIds == null) {
            approvedInternshipIds = new java.util.ArrayList<>();
        }
        if (!approvedInternshipIds.contains(internshipId)) {
            approvedInternshipIds.add(internshipId);
        }
    }

    /**
     * Gets a display-friendly representation of the career center staff.
     * Overrides the abstract method from User class.
     * 
     * @return formatted string with staff details
     */
    @Override
    public String getDisplayInfo() {
        return String.format(
            "Career Center Staff: %s (%s)\nDepartment: %s\nPrivileges: Full Administrative Access",
            getName(), getUserId(), department
        );
    }

    /**
     * Returns a string representation of the career center staff.
     * 
     * @return a string containing staff details
     */
    @Override
    public String toString() {
        return String.format(
            "CareerCenterStaff{userId='%s', name='%s', department='%s'}",
            getUserId(), getName(), department
        );
    }
}
