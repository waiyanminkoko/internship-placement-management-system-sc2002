package controller;

import dto.ApiResponse;
import dto.ChangePasswordRequest;
import dto.LoginRequest;
import dto.LoginResponse;
import dto.RegisterRepresentativeRequest;
import exception.BusinessRuleException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import exception.UnauthorizedException;
import model.User;
import service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for authentication operations.
 * Handles user login, logout, password management, and authentication status checking.
 * 
 * <p>This controller provides the following endpoints:</p>
 * <ul>
 *   <li>POST /api/auth/login - User login with credentials</li>
 *   <li>POST /api/auth/logout - User logout and session termination</li>
 *   <li>POST /api/auth/change-password - Change user password</li>
 *   <li>GET /api/auth/status - Check if user is authenticated</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 2.0
 * @since 2025-10-07
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    /**
     * Authenticates a user with their credentials.
     * 
     * <p><strong>Endpoint:</strong> POST /api/auth/login</p>
     * <p><strong>Request Body:</strong></p>
     * <pre>{@code
     * {
     *   "userId": "U2310001A",
     *   "password": "password"
     * }
     * }</pre>
     * 
     * <p><strong>Success Response (200 OK):</strong></p>
     * <pre>{@code
     * {
     *   "success": true,
     *   "data": {
     *     "userId": "U2310001A",
     *     "name": "Tan Wei Ling",
     *     "role": "STUDENT",
     *     "email": "tan001@e.ntu.edu.sg",
     *     "token": "auth-token-uuid"
     *   },
     *   "message": "Login successful",
     *   "timestamp": "2025-01-15T14:30:00"
     * }
     * }</pre>
     * 
     * @param request Login request containing userId and password
     * @return ResponseEntity with login response containing user data and auth token
     * @throws UnauthorizedException if credentials are invalid
     * @throws ResourceNotFoundException if user not found
     * @throws InvalidInputException if input validation fails
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            // Validate input
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                throw new InvalidInputException("User ID cannot be empty");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new InvalidInputException("Password cannot be empty");
            }
            
            // Authenticate user
            User user = authenticationService.login(request.getUserId(), request.getPassword());
            
            // Generate auth token (simple UUID for now - in production use JWT)
            String token = UUID.randomUUID().toString();
            
            // Build response
            LoginResponse response = new LoginResponse(
                user.getUserId(),
                user.getName(),
                user.getRole(),
                user.getEmail()
            );
            response.setToken(token);
            
            // Add role-specific fields
            if (user.getRole() == enums.UserRole.STUDENT) {
                model.Student student = (model.Student) user;
                response.setMajor(student.getMajor());
                response.setYear(student.getYear());
                response.setHasAcceptedPlacement(student.hasAcceptedPlacement());
            } else if (user.getRole() == enums.UserRole.COMPANY_REPRESENTATIVE) {
                model.CompanyRepresentative rep = (model.CompanyRepresentative) user;
                response.setCompanyName(rep.getCompanyName());
                response.setIndustry(rep.getIndustry());
                response.setPosition(rep.getPosition());
            } else if (user.getRole() == enums.UserRole.CAREER_CENTER_STAFF) {
                model.CareerCenterStaff staff = (model.CareerCenterStaff) user;
                response.setDepartment(staff.getDepartment());
            }
            
            return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
            
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred during login: " + e.getMessage()));
        }
    }
    
    /**
     * Logs out the current user and terminates their session.
     * 
     * <p><strong>Endpoint:</strong> POST /api/auth/logout</p>
     * <p><strong>Query Parameters:</strong></p>
     * <ul>
     *   <li>userId - The user ID to logout</li>
     * </ul>
     * 
     * <p><strong>Success Response (200 OK):</strong></p>
     * <pre>{@code
     * {
     *   "success": true,
     *   "data": null,
     *   "message": "Logout successful",
     *   "timestamp": "2025-01-15T14:35:00"
     * }
     * }</pre>
     * 
     * @param userId The ID of the user to logout
     * @return ResponseEntity with success message
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestParam String userId) {
        try {
            authenticationService.logout(userId);
            return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred during logout: " + e.getMessage()));
        }
    }
    
    /**
     * Changes a user's password.
     * 
     * <p><strong>Endpoint:</strong> POST /api/auth/change-password</p>
     * <p><strong>Query Parameters:</strong></p>
     * <ul>
     *   <li>userId - The user ID whose password to change</li>
     * </ul>
     * 
     * <p><strong>Request Body:</strong></p>
     * <pre>{@code
     * {
     *   "oldPassword": "currentPassword",
     *   "newPassword": "newPassword123"
     * }
     * }</pre>
     * 
     * <p><strong>Success Response (200 OK):</strong></p>
     * <pre>{@code
     * {
     *   "success": true,
     *   "data": null,
     *   "message": "Password changed successfully",
     *   "timestamp": "2025-01-15T14:40:00"
     * }
     * }</pre>
     * 
     * @param userId The user ID whose password to change
     * @param request Change password request containing old and new passwords
     * @return ResponseEntity with success message
     * @throws UnauthorizedException if old password is incorrect
     * @throws InvalidInputException if new password is invalid
     * @throws ResourceNotFoundException if user not found
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam String userId,
            @RequestBody ChangePasswordRequest request) {
        try {
            // Get user
            User user = authenticationService.login(userId, request.getOldPassword());
            
            // Change password
            authenticationService.changePassword(user, request.getOldPassword(), request.getNewPassword());
            
            return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
            
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(e.getMessage()));
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred while changing password: " + e.getMessage()));
        }
    }
    
    /**
     * Checks if a user is currently authenticated.
     * 
     * <p><strong>Endpoint:</strong> GET /api/auth/status</p>
     * <p><strong>Query Parameters:</strong></p>
     * <ul>
     *   <li>userId - The user ID to check</li>
     * </ul>
     * 
     * <p><strong>Success Response (200 OK):</strong></p>
     * <pre>{@code
     * {
     *   "success": true,
     *   "data": true,
     *   "message": "User is authenticated",
     *   "timestamp": "2025-01-15T14:45:00"
     * }
     * }</pre>
     * 
     * @param userId The user ID to check
     * @return ResponseEntity with authentication status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Boolean>> checkAuthStatus(@RequestParam String userId) {
        try {
            boolean isAuthenticated = authenticationService.isAuthenticated(userId);
            String message = isAuthenticated ? "User is authenticated" : "User is not authenticated";
            return ResponseEntity.ok(ApiResponse.success(isAuthenticated, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred while checking authentication status: " + e.getMessage()));
        }
    }
    
    /**
     * Registers a new company representative (pending approval).
     * 
     * <p><strong>Endpoint:</strong> POST /api/auth/register-representative</p>
     * <p><strong>Request Body:</strong></p>
     * <pre>{@code
     * {
     *   "representativeId": "charlielim@gmail.com",
     *   "name": "Charlie Lim",
     *   "email": "charlielim@gmail.com",
     *   "password": "password",
     *   "companyName": "Microsoft",
     *   "industry": "Technology",
     *   "position": "Sales Executive"
     * }
     * }</pre>
     * 
     * <p><strong>Success Response (200 OK):</strong></p>
     * <pre>{@code
     * {
     *   "success": true,
     *   "data": null,
     *   "message": "Registration submitted successfully. Please wait for staff approval.",
     *   "timestamp": "2025-01-15T15:00:00"
     * }
     * }</pre>
     * 
     * @param request Registration request containing representative details
     * @return ResponseEntity with success message
     * @throws InvalidInputException if input validation fails
     * @throws BusinessRuleException if representative already exists
     */
    @PostMapping("/register-representative")
    public ResponseEntity<ApiResponse<Void>> registerRepresentative(
            @RequestBody RegisterRepresentativeRequest request) {
        try {
            // Validate required fields
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                throw new InvalidInputException("Name cannot be empty");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new InvalidInputException("Email cannot be empty");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new InvalidInputException("Password cannot be empty");
            }
            if (request.getCompanyName() == null || request.getCompanyName().trim().isEmpty()) {
                throw new InvalidInputException("Company name cannot be empty");
            }
            if (request.getIndustry() == null || request.getIndustry().trim().isEmpty()) {
                throw new InvalidInputException("Industry cannot be empty");
            }
            
            // Call service to register
            authenticationService.registerRepresentative(
                request.getRepresentativeId(),
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getCompanyName(),
                request.getIndustry(),
                request.getPosition()
            );
            
            return ResponseEntity.ok(ApiResponse.success(
                null, "Registration submitted successfully. Please wait for staff approval."));
            
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred during registration: " + e.getMessage()));
        }
    }
}
