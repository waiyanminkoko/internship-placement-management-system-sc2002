package dto;

/**
 * Data Transfer Object for requesting withdrawal from an application.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public class WithdrawalRequestDTO {
    
    private String applicationId;
    private String reason;

    /**
     * Default constructor.
     */
    public WithdrawalRequestDTO() {
    }

    /**
     * Constructor with application ID and reason.
     * 
     * @param applicationId The ID of the application to withdraw from
     * @param reason The reason for withdrawal
     */
    public WithdrawalRequestDTO(String applicationId, String reason) {
        this.applicationId = applicationId;
        this.reason = reason;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
