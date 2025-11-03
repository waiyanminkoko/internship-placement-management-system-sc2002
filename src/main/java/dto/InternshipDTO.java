package dto;

import lombok.Data;

@Data
public class InternshipDTO {
    private String id;
    private String title;
    private String company;
    private String preferredMajors; // CSV string (normalize in service if needed)
    private String level;
    private String status;
    private boolean visible;
    private int slots;
    private String createdAt;
    private String createdBy;
}
