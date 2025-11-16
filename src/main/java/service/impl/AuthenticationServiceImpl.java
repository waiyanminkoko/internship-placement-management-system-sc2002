package service.impl;

import enums.UserRole;
import exception.BusinessRuleException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import exception.UnauthorizedException;
import model.CareerCenterStaff;
import model.CompanyRepresentative;
import model.Student;
import model.User;
import repository.CareerCenterStaffRepository;
import repository.CompanyRepresentativeRepository;
import repository.StudentRepository;
import service.AuthenticationService;
import util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of AuthenticationService.
 * Handles user authentication, session management, and password changes.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CareerCenterStaffRepository staffRepository;
    
    @Autowired
    private CompanyRepresentativeRepository representativeRepository;
    
    // Simple in-memory session management
    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();
    
    @Override
    public User login(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new InvalidInputException("User ID cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        }
        
        User user = findUser(userId);
        
        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        
        // Verify password
        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid credentials");
        }
        
        // For company representatives, check approval status
        if (user.getRole() == UserRole.COMPANY_REPRESENTATIVE) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            if (rep.getStatus() == enums.ApprovalStatus.REJECTED) {
                throw new UnauthorizedException("Company representative account has been rejected. Please contact the respective school's Career Support Staff.");
            }
            if (!rep.isApproved()) {
                throw new UnauthorizedException("Company representative account is not yet approved. Please contact the respective school's Career Support Staff.");
            }
        }
        
        // Store session
        activeSessions.put(userId, user);
        
        return user;
    }
    
    @Override
    public void logout(String userId) {
        if (userId != null) {
            activeSessions.remove(userId);
        }
    }
    
    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) {
            throw new InvalidInputException("User cannot be null");
        }
        
        // Verify old password
        if (!user.getPassword().equals(oldPassword)) {
            throw new UnauthorizedException("Current password is incorrect");
        }
        
        // Validate new password
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new InvalidInputException("New password must be at least 6 characters long");
        }
        
        // Update password
        user.setPassword(newPassword);
        
        // Save based on user role
        switch (user.getRole()) {
            case STUDENT:
                studentRepository.save((Student) user);
                break;
            case CAREER_CENTER_STAFF:
                staffRepository.save((CareerCenterStaff) user);
                break;
            case COMPANY_REPRESENTATIVE:
                representativeRepository.save((CompanyRepresentative) user);
                break;
            default:
                throw new IllegalStateException("Unknown user role: " + user.getRole());
        }
    }
    
    @Override
    public boolean isAuthenticated(String userId) {
        return activeSessions.containsKey(userId);
    }
    
    /**
     * Finds a user across all user repositories.
     * 
     * @param userId The user ID to search for
     * @return The User object if found, null otherwise
     */
    private User findUser(String userId) {
        // Try student repository
        Optional<Student> student = studentRepository.findById(userId);
        if (student.isPresent()) {
            return student.get();
        }
        
        // Try staff repository
        Optional<CareerCenterStaff> staff = staffRepository.findById(userId);
        if (staff.isPresent()) {
            return staff.get();
        }
        
        // Try representative repository (uses email as ID)
        Optional<CompanyRepresentative> rep = representativeRepository.findById(userId);
        if (rep.isPresent()) {
            return rep.get();
        }
        
        return null;
    }
    
    /**
     * Gets an active session for a user.
     * 
     * @param userId The user ID
     * @return The User object if session exists
     * @throws UnauthorizedException if no active session
     */
    public User getActiveSession(String userId) {
        User user = activeSessions.get(userId);
        if (user == null) {
            throw new UnauthorizedException("No active session for user: " + userId);
        }
        return user;
    }
    
    /**
     * Clears all active sessions (useful for testing).
     */
    public void clearAllSessions() {
        activeSessions.clear();
    }
    
    @Override
    public void registerRepresentative(String representativeId, String name, String email, 
                                      String password, String companyName, String industry, String position) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new InvalidInputException("Invalid email format");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new InvalidInputException("Password must be at least 6 characters long");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new InvalidInputException("Company name cannot be empty");
        }
        if (industry == null || industry.trim().isEmpty()) {
            throw new InvalidInputException("Industry cannot be empty");
        }
        
        // Check if representative already exists
        Optional<CompanyRepresentative> existing = representativeRepository.findById(email);
        if (existing.isPresent()) {
            throw new BusinessRuleException("A representative with this email already exists");
        }
        
        // Create new representative with pending status
        CompanyRepresentative representative = new CompanyRepresentative(
            email,  // email is used as user ID
            name,
            password,
            companyName,
            industry,  // department field stores industry
            position != null ? position : "",  // position field stores actual position/title
            enums.ApprovalStatus.PENDING
        );
        
        // Save to repository
        representativeRepository.save(representative);
    }
}
