package dto;

import enums.UserRole;

/**
 * Data Transfer Object for login responses.
 * Contains user information returned after successful authentication.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public class LoginResponse {
    /**
     * The user's unique identifier.
     */
    private String userId;
    
    /**
     * The user's full name.
     */
    private String name;
    
    /**
     * The user's role in the system.
     */
    private UserRole role;
    
    /**
     * The user's email address (if applicable).
     */
    private String email;
    
    /**
     * Authentication token for subsequent requests.
     */
    private String token;
    
    /**
     * The student's major (only for students).
     */
    private String major;
    
    /**
     * The student's year of study (only for students).
     */
    private Integer year;
    
    /**
     * The company name (only for company representatives).
     */
    private String companyName;
    
    /**
     * The department (for staff) or industry (for company representatives).
     */
    private String department;
    
    /**
     * The industry sector (only for company representatives).
     */
    private String industry;
    
    /**
     * The position/title (only for company representatives).
     */
    private String position;
    
    /**
     * Whether the student has accepted a placement (only for students).
     */
    private Boolean hasAcceptedPlacement;

    /**
     * Default constructor.
     */
    public LoginResponse() {
    }

    /**
     * Parameterized constructor.
     * 
     * @param userId The user ID
     * @param name The user's name
     * @param role The user's role
     * @param email The user's email
     */
    public LoginResponse(String userId, String name, UserRole role, String email) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getHasAcceptedPlacement() {
        return hasAcceptedPlacement;
    }

    public void setHasAcceptedPlacement(Boolean hasAcceptedPlacement) {
        this.hasAcceptedPlacement = hasAcceptedPlacement;
    }
}
