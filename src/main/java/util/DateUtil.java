package util;

import exception.InvalidInputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date and time parsing, formatting, and validation operations.
 * Provides consistent date handling across the application.
 * 
 * <p>This class provides utilities for:
 * <ul>
 *   <li>Parsing dates from various string formats</li>
 *   <li>Formatting dates for display and storage</li>
 *   <li>Validating date ranges and business rules</li>
 *   <li>Calculating date differences</li>
 *   <li>Checking date relationships (before, after, between)</li>
 * </ul>
 * 
 * <p><strong>Supported Date Formats:</strong></p>
 * <ul>
 *   <li>ISO Date: yyyy-MM-dd (2025-01-15)</li>
 *   <li>ISO DateTime: yyyy-MM-dd'T'HH:mm:ss (2025-01-15T14:30:00)</li>
 *   <li>Display Format: dd/MM/yyyy (15/01/2025)</li>
 *   <li>Long Format: dd MMMM yyyy (15 January 2025)</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * LocalDate date = DateUtil.parseDate("2025-01-15");
 * String formatted = DateUtil.formatDate(date);
 * boolean isValid = DateUtil.isValidDateRange(startDate, endDate);
 * }</pre>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-01-15
 */
public class DateUtil {

    /**
     * ISO date format: yyyy-MM-dd (for parsing input)
     */
    public static final DateTimeFormatter ISO_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * ISO date-time format: yyyy-MM-dd'T'HH:mm:ss
     */
    public static final DateTimeFormatter ISO_DATETIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Display date format: dd-MM-yyyy (standard display format)
     */
    public static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Long date format: dd MMMM yyyy
     */
    public static final DateTimeFormatter LONG_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd MMMM yyyy");

    /**
     * CSV storage format: yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter CSV_DATETIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private DateUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== PARSING METHODS ====================

    /**
     * Parses a date string in multiple formats (dd-MM-yyyy or yyyy-MM-dd).
     * Tries display format first, then ISO format.
     * 
     * @param dateString the date string to parse
     * @return LocalDate object, or null if string is null/empty
     * @throws InvalidInputException if date format is invalid
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // Try display format first (dd-MM-yyyy)
        try {
            return LocalDate.parse(dateString.trim(), DISPLAY_DATE_FORMATTER);
        } catch (DateTimeParseException e1) {
            // Try ISO format (yyyy-MM-dd)
            try {
                return LocalDate.parse(dateString.trim(), ISO_DATE_FORMATTER);
            } catch (DateTimeParseException e2) {
                throw new InvalidInputException("Invalid date format. Expected: dd-MM-yyyy or yyyy-MM-dd, got: " + dateString);
            }
        }
    }

    /**
     * Parses a date-time string in ISO format (yyyy-MM-dd'T'HH:mm:ss).
     * 
     * @param dateTimeString the date-time string to parse
     * @return LocalDateTime object, or null if string is null/empty
     * @throws InvalidInputException if date-time format is invalid
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateTimeString.trim(), ISO_DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date-time format. Expected: yyyy-MM-dd'T'HH:mm:ss, got: " + dateTimeString);
        }
    }

    /**
     * Parses a date string in CSV storage format (yyyy-MM-dd HH:mm:ss).
     * 
     * @param dateTimeString the date-time string from CSV
     * @return LocalDateTime object, or null if string is null/empty
     * @throws InvalidInputException if format is invalid
     */
    public static LocalDateTime parseCsvDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateTimeString.trim(), CSV_DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid CSV date-time format. Expected: yyyy-MM-dd HH:mm:ss, got: " + dateTimeString);
        }
    }

    /**
     * Attempts to parse a date string from multiple common formats.
     * Tries ISO, display, and long formats in sequence.
     * 
     * @param dateString the date string to parse
     * @return LocalDate object, or null if string is null/empty
     * @throws InvalidInputException if all parsing attempts fail
     */
    public static LocalDate parseDateFlexible(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // Try ISO format
        try {
            return LocalDate.parse(dateString.trim(), ISO_DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        // Try display format
        try {
            return LocalDate.parse(dateString.trim(), DISPLAY_DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        // Try long format
        try {
            return LocalDate.parse(dateString.trim(), LONG_DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        throw new InvalidInputException("Unable to parse date: " + dateString);
    }

    // ==================== FORMATTING METHODS ====================

    /**
     * Formats a LocalDate to display format string (dd-MM-yyyy).
     * Used for CSV storage and display.
     * 
     * @param date the date to format
     * @return formatted date string, or empty string if date is null
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DISPLAY_DATE_FORMATTER) : "";
    }

    /**
     * Formats a LocalDateTime to ISO format string (yyyy-MM-dd'T'HH:mm:ss).
     * 
     * @param dateTime the date-time to format
     * @return formatted date-time string, or empty string if null
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(ISO_DATETIME_FORMATTER) : "";
    }

    /**
     * Formats a LocalDate to display format (dd/MM/yyyy).
     * 
     * @param date the date to format
     * @return formatted date string, or empty string if date is null
     */
    public static String formatDateForDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_DATE_FORMATTER) : "";
    }

    /**
     * Formats a LocalDate to long format (dd MMMM yyyy).
     * 
     * @param date the date to format
     * @return formatted date string, or empty string if date is null
     */
    public static String formatDateLong(LocalDate date) {
        return date != null ? date.format(LONG_DATE_FORMATTER) : "";
    }

    /**
     * Formats a LocalDateTime to CSV storage format (yyyy-MM-dd HH:mm:ss).
     * 
     * @param dateTime the date-time to format
     * @return formatted date-time string, or empty string if null
     */
    public static String formatDateTimeForCsv(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(CSV_DATETIME_FORMATTER) : "";
    }

    // ==================== VALIDATION METHODS ====================

    /**
     * Validates that end date is after start date.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return true if valid range (end after start), false otherwise
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return endDate.isAfter(startDate) || endDate.isEqual(startDate);
    }

    /**
     * Validates that a date is in the future (after today).
     * 
     * @param date the date to check
     * @return true if date is in the future, false otherwise
     */
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * Validates that a date is in the past (before today).
     * 
     * @param date the date to check
     * @return true if date is in the past, false otherwise
     */
    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Validates that a date is today.
     * 
     * @param date the date to check
     * @return true if date is today, false otherwise
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }

    /**
     * Checks if a date falls within a specified range (inclusive).
     * 
     * @param date the date to check
     * @param startDate the start of the range
     * @param endDate the end of the range
     * @return true if date is within range, false otherwise
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    // ==================== CALCULATION METHODS ====================

    /**
     * Calculates the number of days between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return number of days between dates (positive if end is after start)
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculates the number of days from today to the specified date.
     * 
     * @param date the target date
     * @return number of days (positive for future, negative for past)
     */
    public static long daysFromToday(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    /**
     * Adds a specified number of days to a date.
     * 
     * @param date the base date
     * @param days the number of days to add (can be negative)
     * @return new LocalDate with days added, or null if input is null
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date != null ? date.plusDays(days) : null;
    }

    /**
     * Adds a specified number of months to a date.
     * 
     * @param date the base date
     * @param months the number of months to add (can be negative)
     * @return new LocalDate with months added, or null if input is null
     */
    public static LocalDate addMonths(LocalDate date, long months) {
        return date != null ? date.plusMonths(months) : null;
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Gets the current date.
     * 
     * @return today's date as LocalDate
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Gets the current date and time.
     * 
     * @return current date-time as LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Converts LocalDate to LocalDateTime at start of day (00:00:00).
     * 
     * @param date the date to convert
     * @return LocalDateTime at midnight, or null if input is null
     */
    public static LocalDateTime toStartOfDay(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    /**
     * Extracts LocalDate from LocalDateTime.
     * 
     * @param dateTime the date-time to extract from
     * @return LocalDate portion, or null if input is null
     */
    public static LocalDate toDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}
