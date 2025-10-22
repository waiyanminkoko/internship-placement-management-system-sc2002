package model;

import enums.UserRole;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class representing a user in the Internship Placement Management System.
 * 
 * <p>This class implements the inheritance hierarchy for three types of users:</p>
 * <ul>
 *   <li>{@link Student} - can browse and apply for internships</li>
 *   <li>{@link CompanyRepresentative} - can post internships and review applications</li>
 *   <li>{@link CareerCenterStaff} - can approve accounts, internships, and process withdrawals</li>
 * </ul>
 * 
 * <p><b>Common Attributes:</b></p>
 * <ul>
 *   <li>User ID (unique identifier based on role)</li>
 *   <li>Password (default: "password", can be changed)</li>
 *   <li>Name and Email</li>
 *   <li>User Role</li>
 * </ul>
 * 
 * <p><b>OOP Principles Demonstrated:</b></p>
 * <ul>
 *   <li><b>Abstraction</b>: Abstract class with abstract method getDisplayInfo()</li>
 *   <li><b>Encapsulation</b>: Private fields with getters/setters</li>
 *   <li><b>Inheritance</b>: Base class for Student, CompanyRepresentative, CareerCenterStaff</li>
 *   <li><b>Polymorphism</b>: Subclasses override getDisplayInfo() with specific implementations</li>
 * </ul>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Data
public abstract class User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Unique user identifier.
     * Format varies by role:
     * - Students: U followed by 7 digits and a letter (e.g., U2345123F)
     * - Company Representatives: Company email address
     * - Career Center Staff: NTU account
     */
    private String userId;

    /**
     * User's password for authentication.
     * Default password is "password" for all new users.
     * Should be changed on first login for security.
     */
    private String password;

    /**
     * User's full name.
     */
    private String name;

    /**
     * User's email address for communication.
     */
    private String email;

    /**
     * Role of the user in the system.
     * Determines permissions and available functionalities.
     */
    private UserRole role;

    /**
     * Default constructor required for serialization.
     */
    protected User() {
    }

    /**
     * Parameterized constructor to create a user with all required fields.
     * 
     * @param userId unique identifier for the user
     * @param password user's password
     * @param name user's full name
     * @param email user's email address
     * @param role user's role in the system
     */
    protected User(String userId, String password, String name, String email, UserRole role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Abstract method to get a display-friendly representation of the user.
     * Each subclass must implement this method to provide role-specific information.
     * 
     * <p><b>Polymorphism:</b> This method demonstrates polymorphism as each subclass
     * provides its own implementation showing relevant user details.</p>
     * 
     * @return a formatted string with user details specific to their role
     */
    public abstract String getDisplayInfo();

    /**
     * Changes the user's password after validating the old password.
     * 
     * <p><b>Business Rules:</b></p>
     * <ul>
     *   <li>Old password must match current password</li>
     *   <li>New password must be different from old password</li>
     *   <li>New password must not be null or empty</li>
     * </ul>
     * 
     * @param oldPassword the current password for verification
     * @param newPassword the new password to set
     * @return true if password was successfully changed, false otherwise
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (oldPassword == null || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        if (!this.password.equals(oldPassword)) {
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            return false;
        }
        this.password = newPassword;
        return true;
    }

    /**
     * Validates if the provided password matches the user's password.
     * 
     * @param password the password to validate
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(String password) {
        return this.password != null && this.password.equals(password);
    }

    /**
     * Checks if the user is using the default password.
     * Users should be prompted to change default password for security.
     * 
     * @return true if user is still using the default password "password"
     */
    public boolean hasDefaultPassword() {
        return "password".equals(this.password);
    }

    /**
     * Gets the role display name for UI purposes.
     * 
     * @return the human-readable role name
     */
    public String getRoleDisplayName() {
        return role != null ? role.getDisplayName() : "Unknown";
    }

    /**
     * Compares this user to another object for equality.
     * Two users are equal if they have the same userId.
     * 
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    /**
     * Generates a hash code for this user based on userId.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    /**
     * Returns a string representation of the user.
     * 
     * @return a string containing userId, name, and role
     */
    @Override
    public String toString() {
        return String.format("User{userId='%s', name='%s', role=%s}", 
                           userId, name, role);
    }
}
