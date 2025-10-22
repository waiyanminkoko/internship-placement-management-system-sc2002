package exception;

/**
 * Exception thrown when CSV parsing operations fail.
 * 
 * <p>This exception is used to indicate specific failures in CSV data parsing, including:
 * <ul>
 *   <li>Invalid CSV format or structure</li>
 *   <li>Missing required columns</li>
 *   <li>Data type conversion errors</li>
 *   <li>Malformed CSV records</li>
 *   <li>Header validation failures</li>
 * </ul>
 * 
 * <p>This is a runtime exception that provides detailed context about parsing
 * failures to help identify and fix data quality issues.</p>
 * 
 * @author SC2002 Group 6 - Member 2
 * @version 1.0
 * @since 2025-01-15
 */
public class CsvParseException extends RuntimeException {

    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The line number where the parsing error occurred (if applicable).
     */
    private final int lineNumber;

    /**
     * The field/column name where the parsing error occurred (if applicable).
     */
    private final String fieldName;

    /**
     * Constructs a new CsvParseException with the specified detail message.
     * 
     * @param message the detail message explaining the parsing failure
     */
    public CsvParseException(String message) {
        super(message);
        this.lineNumber = -1;
        this.fieldName = null;
    }

    /**
     * Constructs a new CsvParseException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the parsing failure
     * @param cause the underlying cause of the parsing failure
     */
    public CsvParseException(String message, Throwable cause) {
        super(message, cause);
        this.lineNumber = -1;
        this.fieldName = null;
    }

    /**
     * Constructs a new CsvParseException with message, line number, and field name.
     * 
     * @param message the detail message explaining the parsing failure
     * @param lineNumber the line number where the error occurred
     * @param fieldName the field/column name where the error occurred
     */
    public CsvParseException(String message, int lineNumber, String fieldName) {
        super(String.format("%s [Line: %d, Field: %s]", message, lineNumber, fieldName));
        this.lineNumber = lineNumber;
        this.fieldName = fieldName;
    }

    /**
     * Constructs a new CsvParseException with message and line number.
     * 
     * @param message the detail message explaining the parsing failure
     * @param lineNumber the line number where the error occurred
     */
    public CsvParseException(String message, int lineNumber) {
        super(String.format("%s [Line: %d]", message, lineNumber));
        this.lineNumber = lineNumber;
        this.fieldName = null;
    }

    /**
     * Gets the line number where the parsing error occurred.
     * 
     * @return the line number, or -1 if not applicable
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Gets the field/column name where the parsing error occurred.
     * 
     * @return the field name, or null if not applicable
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Checks if this exception has line number information.
     * 
     * @return true if line number is available, false otherwise
     */
    public boolean hasLineNumber() {
        return lineNumber >= 0;
    }

    /**
     * Checks if this exception has field name information.
     * 
     * @return true if field name is available, false otherwise
     */
    public boolean hasFieldName() {
        return fieldName != null && !fieldName.trim().isEmpty();
    }
}
