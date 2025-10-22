package exception;

/**
 * Exception thrown when invalid input is provided to the system.
 * 
 * <p>This exception is used for validation failures such as:</p>
 * <ul>
 *   <li>Invalid email format</li>
 *   <li>Weak passwords</li>
 *   <li>Invalid student ID format</li>
 *   <li>Invalid date ranges</li>
 *   <li>Missing required fields</li>
 *   <li>Invalid numeric ranges</li>
 * </ul>
 * 
 * <p>HTTP Status Code: 400 BAD REQUEST</p>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
public class InvalidInputException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
