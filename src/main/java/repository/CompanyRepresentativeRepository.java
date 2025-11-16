package repository;

import model.CompanyRepresentative;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CompanyRepresentative entity operations.
 * Extends the generic Repository interface with company representative-specific queries.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface CompanyRepresentativeRepository extends Repository<CompanyRepresentative, String> {
    /**
     * Finds company representatives by approval status.
     * 
     * @param approved true to find approved representatives, false for pending/rejected
     * @return List of representatives with the specified approval status
     */
    List<CompanyRepresentative> findByApprovalStatus(boolean approved);
    
    /**
     * Finds a company representative by their user ID (email).
     * 
     * @param userId The user ID (email) to search for
     * @return Optional containing the representative if found, empty otherwise
     */
    Optional<CompanyRepresentative> findByUserId(String userId);
    
    /**
     * Finds company representatives by company name.
     * 
     * @param companyName The company name to search for
     * @return List of representatives from the specified company
     */
    List<CompanyRepresentative> findByCompanyName(String companyName);
}
