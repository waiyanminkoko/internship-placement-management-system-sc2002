package dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ReportFilterRequest {
    private Set<String> statuses; // e.g. PENDING, APPROVED
    private Set<String> preferredMajors; // e.g. CSC, CEE
    private Set<String> levels; // BASIC, INTERMEDIATE, ADVANCED
    private LocalDate createdFrom;
    private LocalDate createdTo;
    private String createdBy; // company rep email (optional)
    private Boolean visible; // null = don't filter
}
