/**
 * Domain model classes representing core business entities.
 * 
 * <p>This package contains all domain model classes that represent the core
 * business entities of the Internship Placement Management System. These classes
 * encapsulate business data and logic, enforcing business rules and constraints.</p>
 * 
 * <h2>User Entities:</h2>
 * <ul>
 *   <li><b>User</b> - Abstract base class for all user types</li>
 *   <li><b>Student</b> - Student entity with application limits and year-level constraints</li>
 *   <li><b>CompanyRepresentative</b> - Company representative with internship posting limits</li>
 *   <li><b>CareerCenterStaff</b> - Career center staff with approval permissions</li>
 * </ul>
 * 
 * <h2>Internship Entities:</h2>
 * <ul>
 *   <li><b>InternshipOpportunity</b> - Internship posting with slots and approval workflow</li>
 *   <li><b>Application</b> - Student application for an internship with status tracking</li>
 *   <li><b>WithdrawalRequest</b> - Student withdrawal request with approval workflow</li>
 * </ul>
 * 
 * <h2>Key Business Rules:</h2>
 * <ul>
 *   <li>Students can apply to maximum 3 internships concurrently</li>
 *   <li>Year 1-2 students can only apply to BASIC level internships</li>
 *   <li>Company representatives can create maximum 5 internships</li>
 *   <li>Internship opportunities have maximum 10 available slots</li>
 *   <li>Applications require approval from career center staff</li>
 *   <li>Withdrawals require approval and have status tracking</li>
 * </ul>
 * 
 * <p>All model classes use Lombok annotations to reduce boilerplate code and
 * implement proper equals/hashCode/toString methods.</p>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 */
package model;
