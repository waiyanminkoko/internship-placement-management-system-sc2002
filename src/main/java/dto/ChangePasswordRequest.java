package dto;

/**
 * Data Transfer Object for password change requests.
 * Contains the old and new passwords for password updates.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public class ChangePasswordRequest {
    /**
     * The current password for verification.
     */
    private String oldPassword;
    
    /**
     * The new password to set.
     */
    private String newPassword;

    /**
     * Default constructor.
     */
    public ChangePasswordRequest() {
    }

    /**
     * Parameterized constructor.
     * 
     * @param oldPassword The current password
     * @param newPassword The new password
     */
    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
