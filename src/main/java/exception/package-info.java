/**
 * Custom exception classes for error handling.
 * 
 * <p>This package contains all custom exception classes and the global exception
 * handler for the Internship Placement Management System. These exceptions provide
 * meaningful error messages and proper HTTP status codes for various error scenarios.</p>
 * 
 * <h2>Exception Hierarchy:</h2>
 * 
 * <h3>Business Logic Exceptions:</h3>
 * <ul>
 *   <li><b>BusinessRuleException</b> - Business rule violations (e.g., exceeding application limits)</li>
 *   <li><b>InvalidInputException</b> - Invalid user input or validation failures</li>
 *   <li><b>UnauthorizedException</b> - Authentication or authorization failures</li>
 * </ul>
 * 
 * <h3>Data Exceptions:</h3>
 * <ul>
 *   <li><b>ResourceNotFoundException</b> - Requested resource not found (HTTP 404)</li>
 *   <li><b>DataPersistenceException</b> - CSV file operation failures</li>
 *   <li><b>CsvParseException</b> - CSV parsing or format errors</li>
 * </ul>
 * 
 * <h3>Exception Handler:</h3>
 * <ul>
 *   <li><b>GlobalExceptionHandler</b> - Centralized exception handling with standardized responses</li>
 * </ul>
 * 
 * <p>All exceptions extend RuntimeException and are properly handled by the
 * GlobalExceptionHandler to return appropriate HTTP status codes and error messages
 * wrapped in ApiResponse objects.</p>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 */
package exception;
