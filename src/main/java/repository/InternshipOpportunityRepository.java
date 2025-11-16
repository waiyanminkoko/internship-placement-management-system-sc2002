package repository;

import enums.InternshipLevel;
import enums.InternshipStatus;
import model.InternshipOpportunity;
import java.util.List;

/**
 * Repository interface for InternshipOpportunity entity operations.
 * Extends the generic Repository interface with internship-specific queries.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface InternshipOpportunityRepository extends Repository<InternshipOpportunity, String> {
    /**
     * Finds internship opportunities by status.
     * 
     * @param status The internship status to search for
     * @return List of internships with the specified status
     */
    List<InternshipOpportunity> findByStatus(InternshipStatus status);
    
    /**
     * Finds internship opportunities created by a specific representative.
     * 
     * @param repId The representative ID (email) to search for
     * @return List of internships created by the representative
     */
    List<InternshipOpportunity> findByRepresentativeId(String repId);
    
    /**
     * Finds visible internship opportunities by major and level.
     * Only returns approved and visible internships.
     * 
     * @param major The preferred major (null or "ANY" for all majors)
     * @param level The internship level (null for all levels)
     * @return List of matching visible internships
     */
    List<InternshipOpportunity> findVisibleByMajorAndLevel(String major, InternshipLevel level);
    
    /**
     * Finds all visible and approved internship opportunities.
     * 
     * @return List of all visible internships
     */
    List<InternshipOpportunity> findAllVisible();
    
    /**
     * Finds internships by company name.
     * 
     * @param companyName The company name to search for
     * @return List of internships from the specified company
     */
    List<InternshipOpportunity> findByCompanyName(String companyName);
}
