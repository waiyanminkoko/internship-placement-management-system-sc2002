package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the Internship Placement Management System.
 * 
 * <p>This class intercepts all exceptions thrown by controllers and converts them
 * into appropriate HTTP responses with consistent error message formatting.</p>
 * 
 * <p><b>Error Response Format:</b></p>
 * <pre>
 * {
 *   "timestamp": "2025-10-14T10:30:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Student with ID 'U1234567A' not found",
 *   "path": "/api/students/U1234567A"
 * }
 * </pre>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException (404 NOT FOUND).
     * 
     * @param ex the exception
     * @param request the web request
     * @return error response entity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidInputException (400 BAD REQUEST).
     * 
     * @param ex the exception
     * @param request the web request
     * @return error response entity
     */
    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidInputException(
            InvalidInputException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UnauthorizedException (401 UNAUTHORIZED).
     * 
     * @param ex the exception
     * @param request the web request
     * @return error response entity
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles BusinessRuleException (422 UNPROCESSABLE ENTITY).
     * 
     * @param ex the exception
     * @param request the web request
     * @return error response entity
     */
    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleBusinessRuleException(
            BusinessRuleException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles all other exceptions (500 INTERNAL SERVER ERROR).
     * 
     * @param ex the exception
     * @param request the web request
     * @return error response entity
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred: " + ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a standardized error response body.
     * 
     * @param status the HTTP status
     * @param message the error message
     * @param path the request path
     * @return a map containing error details
     */
    private Map<String, Object> createErrorBody(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path.replace("uri=", ""));
        return body;
    }
}
