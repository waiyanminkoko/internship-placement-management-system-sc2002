package exception;

/**
 * Exception thrown when a user attempts an unauthorized action.
 * 
 * <p>This exception is used when:</p>
 * <ul>
 *   <li>Invalid login credentials</li>
 *   <li>Company representative not authorized by staff</li>
 *   <li>Attempting to access resources without permission</li>
 *   <li>Session has expired or is invalid</li>
 *   <li>User tries to modify another user's data</li>
 * </ul>
 * 
 * <p>HTTP Status Code: 401 UNAUTHORIZED or 403 FORBIDDEN</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
