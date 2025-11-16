/**
 * Individual student service implementations for demonstrating team contributions.
 * 
 * <p>This package contains specialized service classes that handle specific
 * student-related use cases. These classes demonstrate individual team member
 * contributions to the project and provide focused implementations of key
 * student workflows.</p>
 * 
 * <h2>Service Classes:</h2>
 * <ul>
 *   <li><b>StudentViewAvailInternshipService</b> - View and filter available internships</li>
 *   <li><b>StudentViewAppliedInternshipService</b> - View student's application history</li>
 *   <li><b>StudentApplyInternshipService</b> - Submit internship applications</li>
 *   <li><b>StudentAcceptInternshipService</b> - Accept successful internship placements</li>
 * </ul>
 * 
 * <h2>Purpose:</h2>
 * <p>These service classes were created to:</p>
 * <ul>
 *   <li>Demonstrate individual contributions in the team project</li>
 *   <li>Provide clear, focused implementations of student workflows</li>
 *   <li>Show different approaches to solving similar problems</li>
 *   <li>Enable parallel development by team members</li>
 * </ul>
 * 
 * <h2>Integration:</h2>
 * <p>While these services can be used independently for testing purposes,
 * the main {@link service.StudentService} consolidates all student operations
 * into a single, cohesive service for production use.</p>
 * 
 * <h2>Business Logic:</h2>
 * <p>Each service enforces relevant business rules:</p>
 * <ul>
 *   <li>Application limits (maximum 3 concurrent)</li>
 *   <li>Year-level restrictions (Year 1-2 → BASIC only)</li>
 *   <li>Eligibility checks (GPA, major requirements)</li>
 *   <li>Status validations (only SUCCESSFUL applications can be accepted)</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 * @see service.StudentService
 */
package service.indiv_contribution;
