package repository;

import model.User;
import java.io.IOException;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides authentication-related operations across all user types.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface UserRepository {
    
    /**
     * Finds a user by their ID.
     * Searches across all user types (students, staff, representatives).
     * 
     * @param userId The user ID to search for
     * @return Optional containing the user if found, empty otherwise
     * @throws IOException if there's an error reading the CSV files
     */
    Optional<User> findById(String userId) throws IOException;
    
    /**
     * Validates user credentials for login.
     * 
     * @param userId The user ID
     * @param password The password to validate
     * @return Optional containing the user if credentials are valid, empty otherwise
     * @throws IOException if there's an error reading the CSV files
     */
    Optional<User> authenticate(String userId, String password) throws IOException;
    
    /**
     * Updates a user's password.
     * 
     * @param userId The user ID
     * @param newPassword The new password
     * @return true if password was updated successfully, false otherwise
     * @throws IOException if there's an error writing to the CSV file
     */
    boolean updatePassword(String userId, String newPassword) throws IOException;
}
