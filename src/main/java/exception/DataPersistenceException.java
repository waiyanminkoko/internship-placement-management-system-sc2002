package exception;

/**
 * Exception thrown when CSV data persistence operations fail.
 * 
 * <p>This exception is used to indicate failures in data storage and retrieval operations,
 * including:
 * <ul>
 *   <li>CSV file read/write errors</li>
 *   <li>File system I/O failures</li>
 *   <li>Data corruption or integrity issues</li>
 *   <li>Permission or access problems</li>
 * </ul>
 * 
 * <p>This is a runtime exception to avoid cluttering business logic with
 * checked exception handling, while still providing detailed error information.</p>
 * 
 * @author SC2002 Group 6 - Member 2
 * @version 1.0
 * @since 2025-01-15
 */
public class DataPersistenceException extends RuntimeException {

    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DataPersistenceException with the specified detail message.
     * 
     * @param message the detail message explaining the persistence failure
     */
    public DataPersistenceException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataPersistenceException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the persistence failure
     * @param cause the underlying cause of the persistence failure
     */
    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DataPersistenceException with the specified cause.
     * 
     * @param cause the underlying cause of the persistence failure
     */
    public DataPersistenceException(Throwable cause) {
        super(cause);
    }
}
