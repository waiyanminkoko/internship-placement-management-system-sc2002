package dto;

/**
 * Data Transfer Object for login requests.
 * Contains user credentials for authentication.
 * 
 * <p><strong>Note:</strong> Validation annotations removed to avoid external dependencies.
 * Validation will be performed in the service layer using ValidationUtil.</p>
 * 
 * @author SC2002 Group 6 - Member 4
 * @version 1.0
 * @since 2025-01-15
 */
public class LoginRequest {

    private String userId;

    private String password;

    public LoginRequest() {}

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
