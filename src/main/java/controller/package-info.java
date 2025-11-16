/**
 * REST API controllers for the Internship Placement Management System.
 * 
 * <p>This package contains all REST controller classes that handle HTTP requests
 * and responses for the application. Each controller is responsible for handling
 * requests for a specific user role or functionality.</p>
 * 
 * <h2>Controllers:</h2>
 * <ul>
 *   <li><b>AuthenticationController</b> - Login, logout, and password management</li>
 *   <li><b>StudentController</b> - Student operations (view/apply/withdraw internships)</li>
 *   <li><b>CompanyRepresentativeController</b> - Company representative operations (create/manage internships)</li>
 *   <li><b>StaffController</b> - Career center staff operations (approvals, reports)</li>
 *   <li><b>HealthController</b> - System health check endpoint</li>
 * </ul>
 * 
 * <h2>Features:</h2>
 * <ul>
 *   <li>RESTful API design with standard HTTP methods</li>
 *   <li>JSON request/response format</li>
 *   <li>Role-based access control</li>
 *   <li>Standardized error responses via ApiResponse wrapper</li>
 *   <li>Input validation and business rule enforcement</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 */
package controller;
