/**
 * Repository interfaces for data access operations.
 * 
 * <p>This package contains all repository interface definitions that provide
 * an abstraction layer for data access. These interfaces define CRUD operations
 * and custom queries for all domain entities.</p>
 * 
 * <h2>Repository Pattern:</h2>
 * <p>The repository pattern provides a collection-like interface for accessing
 * domain objects, abstracting the underlying data storage mechanism (CSV files).</p>
 * 
 * <h2>Repository Interfaces:</h2>
 * <ul>
 *   <li><b>Repository&lt;T, ID&gt;</b> - Generic base repository interface with CRUD operations</li>
 *   <li><b>UserRepository</b> - User data access with authentication support</li>
 *   <li><b>StudentRepository</b> - Student-specific queries and operations</li>
 *   <li><b>CompanyRepresentativeRepository</b> - Company representative data access</li>
 *   <li><b>CareerCenterStaffRepository</b> - Staff data access</li>
 *   <li><b>InternshipOpportunityRepository</b> - Internship queries and filtering</li>
 *   <li><b>ApplicationRepository</b> - Application tracking and status management</li>
 * </ul>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Generic CRUD operations (Create, Read, Update, Delete)</li>
 *   <li>Custom query methods for business-specific searches</li>
 *   <li>Filtering and sorting capabilities</li>
 *   <li>Relationship management between entities</li>
 * </ul>
 * 
 * <p>All repository interfaces are implemented by CSV-based repositories
 * in the {@link repository.impl} package.</p>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 * @see repository.impl
 */
package repository;
