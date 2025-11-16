package dto;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper for consistent REST API responses.
 * Provides a standardized structure for all API responses including success status,
 * data payload, error messages, and timestamps.
 * 
 * <p>This wrapper ensures that all API endpoints return responses in a consistent format,
 * making it easier for frontend clients to handle responses uniformly.</p>
 * 
 * <p><strong>Success Response Example:</strong></p>
 * <pre>{@code
 * {
 *   "success": true,
 *   "data": {...},
 *   "message": "Operation completed successfully",
 *   "timestamp": "2025-01-15T14:30:00"
 * }
 * }</pre>
 * 
 * <p><strong>Error Response Example:</strong></p>
 * <pre>{@code
 * {
 *   "success": false,
 *   "data": null,
 *   "message": "Invalid input: Email is required",
 *   "timestamp": "2025-01-15T14:30:00"
 * }
 * }</pre>
 * 
 * @param <T> the type of data payload
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-01-15
 */
public class ApiResponse<T> {

    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;

    /**
     * The response data payload (null for errors).
     */
    private T data;

    /**
     * Human-readable message describing the result.
     */
    private String message;

    /**
     * Timestamp when the response was created.
     */
    private LocalDateTime timestamp;

    /**
     * Default constructor.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs an ApiResponse with specified values.
     * 
     * @param success whether the operation was successful
     * @param data the response data
     * @param message the response message
     */
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a successful response with data and default message.
     * 
     * @param <T> the type of data
     * @param data the response data
     * @return ApiResponse indicating success
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Operation successful");
    }

    /**
     * Creates a successful response with data and custom message.
     * 
     * @param <T> the type of data
     * @param data the response data
     * @param message custom success message
     * @return ApiResponse indicating success
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    /**
     * Creates an error response with message.
     * 
     * @param <T> the type of data (will be null)
     * @param message error message
     * @return ApiResponse indicating failure
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    /**
     * Creates an error response with default message.
     * 
     * @param <T> the type of data (will be null)
     * @return ApiResponse indicating failure
     */
    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(false, null, "Operation failed");
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("ApiResponse{success=%s, message='%s', timestamp=%s}", 
            success, message, timestamp);
    }
}
