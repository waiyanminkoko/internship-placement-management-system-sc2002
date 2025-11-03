package dto;

import lombok.Data;

@Data
public class ApprovalDecisionRequest {
    private String id; // email for company rep, internship ID, or withdrawal ID
    private boolean approve; // true approve, false reject
    private String reason; // optional rejection reason
}
