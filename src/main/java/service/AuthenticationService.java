package service;

import model.User;

/**
 * Service interface for authentication operations.
 * Handles user login, logout, and password management.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface AuthenticationService {
    
    /**
     * Authenticates a user with their credentials.
     * 
     * @param userId The user's ID (student ID, staff ID, or email for representatives)
     * @param password The user's password
     * @return The authenticated User object
     * @throws exception.UnauthorizedException if credentials are invalid
     * @throws exception.ResourceNotFoundException if user not found
     */
    User login(String userId, String password);
    
    /**
     * Logs out the current user and terminates their session.
     * 
     * @param userId The ID of the user to logout
     */
    void logout(String userId);
    
    /**
     * Changes a user's password.
     * 
     * @param user The user whose password is being changed
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @throws exception.UnauthorizedException if old password is incorrect
     * @throws exception.InvalidInputException if new password is invalid
     */
    void changePassword(User user, String oldPassword, String newPassword);
    
    /**
     * Validates if a user is currently authenticated.
     * 
     * @param userId The user ID to check
     * @return true if user is authenticated, false otherwise
     */
    boolean isAuthenticated(String userId);
    
    /**
     * Registers a new company representative (pending approval).
     * 
     * @param representativeId The representative ID
     * @param name The representative's name
     * @param email The representative's email
     * @param password The representative's password
     * @param companyName The company name
     * @param industry The company industry
     * @param position The representative's position/title
     * @throws exception.InvalidInputException if input is invalid
     */
    void registerRepresentative(String representativeId, String name, String email, 
                               String password, String companyName, String industry, String position);
}
