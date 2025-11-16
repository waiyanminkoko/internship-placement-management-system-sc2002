package dto;

import lombok.Data;

/**
 * Data Transfer Object for displaying withdrawal request details with enriched information.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-11-15
 */
@Data
public class WithdrawalDetailsDTO {
    
    private String withdrawalId;
    private String studentId;
    private String studentName;
    private String studentMajor;
    private Integer studentYear;
    private String applicationId;
    private String internshipId;
    private String internshipTitle;
    private String companyName;
    private String reason;
    private String status;
    private String requestDate;
    private String processedBy;
    private String processedDate;
    private String staffComments;
    
    /**
     * Default constructor.
     */
    public WithdrawalDetailsDTO() {
    }
}
