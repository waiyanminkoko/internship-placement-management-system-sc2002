package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Utility class for validation and ID generation in the Internship Placement Management System.
 * 
 * <p>This class provides methods for:</p>
 * <ul>
 *   <li>Generating unique IDs for various entities</li>
 *   <li>Validating user IDs, passwords, and email addresses</li>
 *   <li>Parsing and formatting dates</li>
 *   <li>Validating business rules and constraints</li>
 * </ul>
 * 
 * <p><b>ID Generation:</b></p>
 * <ul>
 *   <li>Student IDs: U followed by 7 digits and a letter (e.g., U2345123F)</li>
 *   <li>Opportunity IDs: INTERN- followed by timestamp and random digits</li>
 *   <li>Application IDs: APP- followed by UUID</li>
 *   <li>Request IDs: REQ- followed by UUID</li>
 * </ul>
 * 
 * @author SC2002 Group 6
 * @version 1.0.0
 * @since 2025-10-14
 */
public class ValidationUtil {

    // Regular expression patterns
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^U\\d{7}[A-Z]$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{6,}$"); // At least 6 characters

    // Date formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Atomic counters for unique ID generation
    private static final AtomicInteger opportunityCounter = new AtomicInteger(1000);
    private static final Random random = new Random();

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ========== ID Generation Methods ==========

    /**
     * Generates a unique student ID in the format: U followed by 7 digits and a letter.
     * Example: U2345123F
     * 
     * @return a newly generated student ID
     */
    public static String generateStudentId() {
        int number = 1000000 + random.nextInt(9000000); // 7 digit number
        char letter = (char) ('A' + random.nextInt(26)); // Random letter A-Z
        return "U" + number + letter;
    }

    /**
     * Generates a unique opportunity ID for internships.
     * Format: INTERN-{timestamp}-{counter}
     * 
     * @return a newly generated opportunity ID
     */
    public static String generateOpportunityId() {
        long timestamp = System.currentTimeMillis() % 1000000;
        int counter = opportunityCounter.getAndIncrement();
        return String.format("INTERN-%d-%04d", timestamp, counter);
    }

    /**
     * Generates a unique application ID.
     * Format: APP-{UUID}
     * 
     * @return a newly generated application ID
     */
    public static String generateApplicationId() {
        return "APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Generates a unique withdrawal request ID.
     * Format: REQ-{UUID}
     * 
     * @return a newly generated request ID
     */
    public static String generateRequestId() {
        return "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Generates a session ID for user authentication.
     * Format: SESSION-{UUID}
     * 
     * @return a newly generated session ID
     */
    public static String generateSessionId() {
        return "SESSION-" + UUID.randomUUID().toString();
    }

    // ========== Validation Methods ==========

    /**
     * Validates if a string is a valid student ID.
     * Format: U followed by 7 digits and a letter (e.g., U2345123F)
     * 
     * @param studentId the student ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidStudentId(String studentId) {
        return studentId != null && STUDENT_ID_PATTERN.matcher(studentId).matches();
    }

    /**
     * Validates if a string is a valid email address.
     * 
     * @param email the email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if a password meets minimum requirements.
     * Must be at least 6 characters long.
     * 
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates if a password meets strong requirements.
     * Must be at least 8 characters with letters and numbers.
     * 
     * @param password the password to validate
     * @return true if strong password, false otherwise
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }

    /**
     * Validates if a user ID is in the correct format based on expected role.
     * 
     * @param userId the user ID to validate
     * @param isStudent true if checking student ID format
     * @return true if valid format, false otherwise
     */
    public static boolean isValidUserId(String userId, boolean isStudent) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        if (isStudent) {
            return isValidStudentId(userId);
        }
        // Company rep or staff - should be email or valid string
        return userId.length() >= 3 && (isValidEmail(userId) || userId.matches("^[A-Za-z0-9_]+$"));
    }

    /**
     * Validates if a string is not null or empty.
     * 
     * @param value the string to validate
     * @return true if not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if a numeric value is within a specified range.
     * 
     * @param value the value to check
     * @param min minimum value (inclusive)
     * @param max maximum value (inclusive)
     * @return true if within range, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    // ========== Date Utility Methods ==========

    /**
     * Parses a date string in the format yyyy-MM-dd.
     * 
     * @param dateString the date string to parse
     * @return the parsed LocalDate, or null if invalid
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Formats a LocalDate to string in the format yyyy-MM-dd.
     * 
     * @param date the date to format
     * @return the formatted date string, or empty string if null
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Parses a datetime string in the format yyyy-MM-dd HH:mm:ss.
     * 
     * @param datetimeString the datetime string to parse
     * @return the parsed LocalDateTime, or null if invalid
     */
    public static LocalDateTime parseDateTime(String datetimeString) {
        if (datetimeString == null || datetimeString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(datetimeString, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Formats a LocalDateTime to string in the format yyyy-MM-dd HH:mm:ss.
     * 
     * @param datetime the datetime to format
     * @return the formatted datetime string, or empty string if null
     */
    public static String formatDateTime(LocalDateTime datetime) {
        return datetime != null ? datetime.format(DATETIME_FORMATTER) : "";
    }

    /**
     * Validates if a date range is valid (opening date before closing date).
     * 
     * @param openingDate the opening date
     * @param closingDate the closing date
     * @return true if valid range, false otherwise
     */
    public static boolean isValidDateRange(LocalDate openingDate, LocalDate closingDate) {
        if (openingDate == null || closingDate == null) {
            return false;
        }
        return !closingDate.isBefore(openingDate);
    }

    /**
     * Checks if a date is in the future.
     * 
     * @param date the date to check
     * @return true if date is after today, false otherwise
     */
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * Checks if a date is in the past.
     * 
     * @param date the date to check
     * @return true if date is before today, false otherwise
     */
    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Checks if a date is today.
     * 
     * @param date the date to check
     * @return true if date is today, false otherwise
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }

    // ========== Business Rule Validation Methods ==========

    /**
     * Validates if a year of study is valid (1-4).
     * 
     * @param year the year to validate
     * @return true if year is between 1 and 4, false otherwise
     */
    public static boolean isValidYearOfStudy(int year) {
        return isInRange(year, 1, 4);
    }

    /**
     * Validates if a slot count is valid for internships (1-10).
     * 
     * @param slots the slot count to validate
     * @return true if slots is between 1 and 10, false otherwise
     */
    public static boolean isValidSlotCount(int slots) {
        return isInRange(slots, 1, 10);
    }

    /**
     * Validates if a major code is valid.
     * Accepts "Any" or 2-4 letter codes.
     * 
     * @param major the major code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            return false;
        }
        if ("Any".equalsIgnoreCase(major.trim())) {
            return true;
        }
        return major.trim().length() >= 2 && major.trim().length() <= 4 
               && major.matches("^[A-Z]+$");
    }

    /**
     * Sanitizes a string by removing special characters and trimming.
     * Useful for preventing CSV injection and ensuring clean data.
     * 
     * @param input the string to sanitize
     * @return the sanitized string
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        // Remove leading special characters that could cause CSV issues
        String sanitized = input.trim();
        if (sanitized.startsWith("=") || sanitized.startsWith("+") || 
            sanitized.startsWith("-") || sanitized.startsWith("@")) {
            sanitized = "'" + sanitized;
        }
        return sanitized;
    }

    /**
     * Escapes special characters in CSV fields.
     * 
     * @param field the field to escape
     * @return the escaped field
     */
    public static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
