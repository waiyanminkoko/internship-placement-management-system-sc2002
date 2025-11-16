package exception;

/**
 * Custom exception thrown when a business rule is violated.
 * 
 * <p>This exception is used for violations of application-specific business rules:</p>
 * <ul>
 *   <li>Student exceeds maximum 3 applications</li>
 *   <li>Student tries to apply after accepting placement</li>
 *   <li>Year 1-2 student applies for non-BASIC internship</li>
 *   <li>Company rep exceeds maximum 5 internships</li>
 *   <li>Application after closing date</li>
 *   <li>Slots exceed maximum of 10</li>
 *   <li>Editing after approval</li>
 * </ul>
 * 
 * <p>HTTP Status Code: 422 UNPROCESSABLE ENTITY</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
public class BusinessRuleException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
