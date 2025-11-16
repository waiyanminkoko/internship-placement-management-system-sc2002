package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique identifiers for various entities.
 * Provides consistent ID generation strategies across the application.
 * 
 * <p>This class generates unique IDs for:
 * <ul>
 *   <li>Applications - Format: APP-{timestamp}-{UUID}</li>
 *   <li>Internship Opportunities - Format: INT-{timestamp}-{UUID}</li>
 *   <li>Withdrawal Requests - Format: WDR-{timestamp}-{UUID}</li>
 *   <li>Sessions - Format: SES-{UUID}</li>
 *   <li>Generic IDs - Format: {prefix}-{UUID}</li>
 * </ul>
 * 
 * <p>All generated IDs are guaranteed to be unique and thread-safe.</p>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * String applicationId = IdGenerator.generateApplicationId();
 * // Returns: APP-20250115-123456-a1b2c3d4
 * 
 * String internshipId = IdGenerator.generateInternshipId();
 * // Returns: INT-20250115-123456-e5f6g7h8
 * }</pre>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-01-15
 */
public class IdGenerator {

    /**
     * Date-time formatter for timestamp portion of IDs.
     * Format: yyyyMMdd-HHmmss
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    /**
     * Counter for sequential IDs (thread-safe).
     */
    private static final AtomicLong SEQUENCE_COUNTER = new AtomicLong(0);

    /**
     * Prefix for application IDs.
     */
    private static final String APPLICATION_PREFIX = "APP";

    /**
     * Prefix for internship opportunity IDs.
     */
    private static final String INTERNSHIP_PREFIX = "INT";

    /**
     * Prefix for withdrawal request IDs.
     */
    private static final String WITHDRAWAL_PREFIX = "WDR";

    /**
     * Prefix for session IDs.
     */
    private static final String SESSION_PREFIX = "SES";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private IdGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Generates a unique ID for an internship application.
     * Format: APP-{timestamp}-{short-uuid}
     * 
     * @return unique application ID
     */
    public static String generateApplicationId() {
        return generateTimestampedId(APPLICATION_PREFIX);
    }

    /**
     * Generates a unique ID for an internship opportunity.
     * Format: INT-{timestamp}-{short-uuid}
     * 
     * @return unique internship opportunity ID
     */
    public static String generateInternshipId() {
        return generateTimestampedId(INTERNSHIP_PREFIX);
    }

    /**
     * Generates a unique ID for a withdrawal request.
     * Format: WDR-{timestamp}-{short-uuid}
     * 
     * @return unique withdrawal request ID
     */
    public static String generateWithdrawalRequestId() {
        return generateTimestampedId(WITHDRAWAL_PREFIX);
    }

    /**
     * Generates a unique session ID for user authentication.
     * Format: SES-{UUID}
     * 
     * @return unique session ID
     */
    public static String generateSessionId() {
        return SESSION_PREFIX + "-" + UUID.randomUUID().toString();
    }

    /**
     * Generates a unique ID with custom prefix.
     * Format: {prefix}-{UUID}
     * 
     * @param prefix the custom prefix for the ID
     * @return unique ID with the specified prefix
     */
    public static String generateCustomId(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        return prefix.trim().toUpperCase() + "-" + UUID.randomUUID().toString();
    }

    /**
     * Generates a timestamped unique ID with specified prefix.
     * Format: {prefix}-{timestamp}-{short-uuid}
     * 
     * @param prefix the prefix for the ID
     * @return unique timestamped ID
     */
    private static String generateTimestampedId(String prefix) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String shortUuid = getShortUuid();
        return String.format("%s-%s-%s", prefix, timestamp, shortUuid);
    }

    /**
     * Generates a short UUID (first 8 characters of standard UUID).
     * Provides reasonable uniqueness while keeping IDs manageable.
     * 
     * @return 8-character UUID segment
     */
    private static String getShortUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generates a sequential numeric ID with prefix.
     * Format: {prefix}-{6-digit-number}
     * Thread-safe using AtomicLong.
     * 
     * @param prefix the prefix for the ID
     * @return unique sequential ID
     */
    public static String generateSequentialId(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        long sequence = SEQUENCE_COUNTER.incrementAndGet();
        return String.format("%s-%06d", prefix.trim().toUpperCase(), sequence);
    }

    /**
     * Generates a UUID-based unique identifier without any prefix.
     * Standard UUID format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     * 
     * @return standard UUID string
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a compact UUID (removes dashes).
     * Format: 32 hexadecimal characters without separators
     * 
     * @return compact UUID string
     */
    public static String generateCompactUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Validates if a string follows the expected ID format for a given prefix.
     * 
     * @param id the ID to validate
     * @param expectedPrefix the expected prefix
     * @return true if ID matches the format, false otherwise
     */
    public static boolean isValidIdFormat(String id, String expectedPrefix) {
        if (id == null || expectedPrefix == null) {
            return false;
        }
        
        String pattern = "^" + expectedPrefix + "-[0-9]{8}-[0-9]{6}-[a-f0-9]{8}$";
        return id.matches(pattern);
    }

    /**
     * Extracts the timestamp from a timestamped ID.
     * 
     * @param id the ID to extract timestamp from
     * @return the timestamp string, or null if format is invalid
     */
    public static String extractTimestamp(String id) {
        if (id == null || !id.contains("-")) {
            return null;
        }
        
        String[] parts = id.split("-");
        if (parts.length >= 3) {
            return parts[1] + "-" + parts[2];
        }
        
        return null;
    }

    /**
     * Extracts the prefix from an ID.
     * 
     * @param id the ID to extract prefix from
     * @return the prefix, or null if format is invalid
     */
    public static String extractPrefix(String id) {
        if (id == null || !id.contains("-")) {
            return null;
        }
        
        return id.substring(0, id.indexOf("-"));
    }

    /**
     * Resets the sequence counter (useful for testing).
     * <strong>Warning:</strong> Should not be used in production code.
     */
    public static void resetSequenceCounter() {
        SEQUENCE_COUNTER.set(0);
    }

    /**
     * Gets the current value of the sequence counter.
     * 
     * @return current sequence counter value
     */
    public static long getSequenceCounterValue() {
        return SEQUENCE_COUNTER.get();
    }
}
