package repository;

import model.CareerCenterStaff;
import java.util.Optional;

/**
 * Repository interface for CareerCenterStaff entity operations.
 * Extends the generic Repository interface with staff-specific queries.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface CareerCenterStaffRepository extends Repository<CareerCenterStaff, String> {
    /**
     * Finds a staff member by their user ID.
     * 
     * @param userId The user ID (staff ID) to search for
     * @return Optional containing the staff member if found, empty otherwise
     */
    Optional<CareerCenterStaff> findByUserId(String userId);
    
    /**
     * Finds a staff member by their email address.
     * 
     * @param email The email address to search for
     * @return Optional containing the staff member if found, empty otherwise
     */
    Optional<CareerCenterStaff> findByEmail(String email);
}
