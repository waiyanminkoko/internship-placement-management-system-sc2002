package exception;

/**
 * Exception thrown when a requested resource cannot be found in the system.
 * 
 * <p>This exception is typically used when:</p>
 * <ul>
 *   <li>A user ID does not exist</li>
 *   <li>An internship opportunity cannot be found</li>
 *   <li>An application ID is invalid</li>
 *   <li>A withdrawal request does not exist</li>
 * </ul>
 * 
 * <p>HTTP Status Code: 404 NOT FOUND</p>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * 
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a ResourceNotFoundException for a specific resource type and ID.
     * 
     * @param resourceType the type of resource (e.g., "Student", "Internship")
     * @param resourceId the ID of the resource
     * @return a new ResourceNotFoundException
     */
    public static ResourceNotFoundException forResource(String resourceType, String resourceId) {
        return new ResourceNotFoundException(
            String.format("%s with ID '%s' not found", resourceType, resourceId)
        );
    }
}
