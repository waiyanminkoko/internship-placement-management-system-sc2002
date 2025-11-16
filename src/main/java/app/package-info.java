/**
 * Main application entry points for the Internship Placement Management System.
 * 
 * <p>This package contains the primary application launcher classes that provide
 * different ways to run and interact with the system:</p>
 * 
 * <ul>
 *   <li><b>InternshipPlacementApplication</b> - Spring Boot REST API server (Production)</li>
 *   <li><b>InternshipPlacementApplication_terminal</b> - Terminal-based testing interface (Development)</li>
 * </ul>
 * 
 * <h2>Spring Boot Application</h2>
 * <p>The main application provides a RESTful API backend that runs on port 6060
 * and integrates with the React frontend on port 5173. It includes:</p>
 * <ul>
 *   <li>REST API endpoints for all user roles</li>
 *   <li>CSV-based data persistence</li>
 *   <li>Business logic validation</li>
 *   <li>Exception handling and error responses</li>
 *   <li>CORS configuration for frontend integration</li>
 * </ul>
 * 
 * <h2>Terminal Application</h2>
 * <p>The terminal application provides a command-line interface for testing
 * backend functionality without requiring the full stack setup. It's useful for:</p>
 * <ul>
 *   <li>Unit testing business logic</li>
 *   <li>Testing data operations</li>
 *   <li>Validating business rules</li>
 *   <li>Quick prototyping and debugging</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 */
package app;
