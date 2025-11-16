package dto;

import lombok.Data;

/**
 * Data Transfer Object for report filtering criteria.
 * Used by Career Center Staff to generate filtered reports.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-11-10
 */
@Data
public class ReportFilter {
    
    /**
     * Filter by student major.
     */
    private String major;
    
    /**
     * Filter by student year of study.
     */
    private Integer year;
    
    /**
     * Filter by company name.
     */
    private String companyName;
    
    /**
     * Filter by internship start date (opening date).
     * Format: yyyy-MM-dd
     */
    private String startDate;
    
    /**
     * Filter by internship end date (closing date).
     * Format: yyyy-MM-dd
     */
    private String endDate;
    
    /**
     * Default constructor.
     */
    public ReportFilter() {
    }
    
    /**
     * Parameterized constructor.
     * 
     * @param major student major filter
     * @param year student year filter
     * @param companyName company name filter
     * @param startDate start date filter
     * @param endDate end date filter
     */
    public ReportFilter(String major, Integer year, String companyName, String startDate, String endDate) {
        this.major = major;
        this.year = year;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
