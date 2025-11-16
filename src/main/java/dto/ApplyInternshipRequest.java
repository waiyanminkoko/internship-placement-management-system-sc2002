package dto;

import lombok.Data;

/**
 * DTO for internship application request.
 * Used when students apply for internship opportunities.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Data
public class ApplyInternshipRequest {
    
    /**
     * The ID of the internship opportunity to apply for.
     */
    private String internshipId;
    
    /**
     * Default constructor.
     */
    public ApplyInternshipRequest() {
    }
    
    /**
     * Constructor with internship ID.
     * 
     * @param internshipId the internship opportunity ID
     */
    public ApplyInternshipRequest(String internshipId) {
        this.internshipId = internshipId;
    }
}
