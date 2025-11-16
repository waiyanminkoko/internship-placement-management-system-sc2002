/**
 * Data Transfer Objects (DTOs) for API communication.
 * 
 * <p>This package contains all DTO classes used for transferring data between
 * the client (React frontend) and the server (Spring Boot backend). DTOs provide
 * a clean separation between the API layer and the domain model layer.</p>
 * 
 * <h2>DTO Categories:</h2>
 * 
 * <h3>Authentication DTOs:</h3>
 * <ul>
 *   <li><b>LoginRequest</b> - User login credentials</li>
 *   <li><b>LoginResponse</b> - Login result with user information</li>
 *   <li><b>ChangePasswordRequest</b> - Password change request</li>
 * </ul>
 * 
 * <h3>Internship DTOs:</h3>
 * <ul>
 *   <li><b>CreateInternshipRequest</b> - Create new internship opportunity</li>
 *   <li><b>InternshipDTO</b> - Internship information transfer</li>
 * </ul>
 * 
 * <h3>Application DTOs:</h3>
 * <ul>
 *   <li><b>ApplyInternshipRequest</b> - Submit internship application</li>
 *   <li><b>ApplyInternshipDTO</b> - Application with enriched internship details</li>
 *   <li><b>ApplicationWithStudentDTO</b> - Application with student information</li>
 * </ul>
 * 
 * <h3>Withdrawal DTOs:</h3>
 * <ul>
 *   <li><b>WithdrawalRequestDTO</b> - Submit withdrawal request</li>
 *   <li><b>WithdrawalDetailsDTO</b> - Withdrawal details with enriched information</li>
 * </ul>
 * 
 * <h3>Other DTOs:</h3>
 * <ul>
 *   <li><b>ApiResponse</b> - Generic API response wrapper</li>
 *   <li><b>RegisterRepresentativeRequest</b> - Company representative registration</li>
 *   <li><b>ApprovalDecisionRequest</b> - Approval/rejection decisions</li>
 *   <li><b>ReportFilter</b> & <b>ReportFilterRequest</b> - Report filtering criteria</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 */
package dto;
