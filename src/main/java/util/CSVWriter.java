package util;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import exception.DataPersistenceException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Specialized CSV writing utility using OpenCSV library.
 * Provides robust CSV file writing with atomic operations and error handling.
 * 
 * <p>This class wraps OpenCSV's CSVWriter to provide:
 * <ul>
 *   <li>Atomic write operations (write to temp, then rename)</li>
 *   <li>Automatic backup creation</li>
 *   <li>Append and overwrite modes</li>
 *   <li>Comprehensive error handling</li>
 *   <li>Directory creation</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * String[] header = {"userId", "name", "email"};
 * List<String[]> records = List.of(
 *     new String[]{"S001", "John Doe", "john@example.com"},
 *     new String[]{"S002", "Jane Smith", "jane@example.com"}
 * );
 * CSVWriter.writeAllRecords("data/students.csv", header, records, false);
 * }</pre>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-01-15
 * @see CSVReader
 * @see CSVUtil
 */
public class CSVWriter {

    /**
     * Default CSV delimiter character.
     */
    private static final char DEFAULT_DELIMITER = ',';

    /**
     * Default CSV quote character.
     */
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Temporary file suffix for atomic writes.
     */
    private static final String TEMP_SUFFIX = ".tmp";

    /**
     * Backup file suffix.
     */
    private static final String BACKUP_SUFFIX = ".bak";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CSVWriter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Writes all records to a CSV file with header, replacing existing content.
     * Uses atomic write operation (write to temp file, then rename).
     * 
     * @param filePath the path to the CSV file
     * @param header the header row (column names)
     * @param records list of records to write
     * @param createBackup true to create a backup of existing file before overwriting
     * @throws DataPersistenceException if writing fails
     */
    public static void writeAllRecords(String filePath, String[] header, 
                                       List<String[]> records, boolean createBackup) {
        Path path = Paths.get(filePath);
        Path tempPath = Paths.get(filePath + TEMP_SUFFIX);
        
        // Ensure parent directory exists
        ensureDirectoryExists(path.getParent());

        // Create backup if requested and file exists
        if (createBackup && Files.exists(path)) {
            createBackup(path);
        }

        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(tempPath.toFile()))
                .withSeparator(DEFAULT_DELIMITER)
                .withQuoteChar(DEFAULT_QUOTE)
                .build()) {

            // Write header
            if (header != null && header.length > 0) {
                writer.writeNext(header);
            }

            // Write all records
            for (String[] record : records) {
                if (record != null && record.length > 0) {
                    writer.writeNext(record);
                }
            }

            writer.flush();

        } catch (IOException e) {
            // Clean up temp file on failure
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {
                // Ignore cleanup errors
            }
            throw new DataPersistenceException("Failed to write CSV file: " + filePath, e);
        }

        // Atomic rename: move temp file to actual file
        try {
            Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING, 
                      StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to finalize CSV write: " + filePath, e);
        }
    }

    /**
     * Appends a single record to an existing CSV file.
     * Creates the file with header if it doesn't exist.
     * 
     * @param filePath the path to the CSV file
     * @param header the header row (used only if file doesn't exist)
     * @param record the record to append
     * @throws DataPersistenceException if appending fails
     */
    public static void appendRecord(String filePath, String[] header, String[] record) {
        Path path = Paths.get(filePath);
        ensureDirectoryExists(path.getParent());

        boolean fileExists = Files.exists(path);
        boolean writeHeader = !fileExists && header != null && header.length > 0;

        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(path.toFile(), true))
                .withSeparator(DEFAULT_DELIMITER)
                .withQuoteChar(DEFAULT_QUOTE)
                .build()) {

            // Write header if file is new
            if (writeHeader) {
                writer.writeNext(header);
            }

            // Write the record
            if (record != null && record.length > 0) {
                writer.writeNext(record);
            }

            writer.flush();

        } catch (IOException e) {
            throw new DataPersistenceException("Failed to append to CSV file: " + filePath, e);
        }
    }

    /**
     * Appends multiple records to an existing CSV file.
     * Creates the file with header if it doesn't exist.
     * 
     * @param filePath the path to the CSV file
     * @param header the header row (used only if file doesn't exist)
     * @param records list of records to append
     * @throws DataPersistenceException if appending fails
     */
    public static void appendRecords(String filePath, String[] header, List<String[]> records) {
        Path path = Paths.get(filePath);
        ensureDirectoryExists(path.getParent());

        boolean fileExists = Files.exists(path);
        boolean writeHeader = !fileExists && header != null && header.length > 0;

        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(path.toFile(), true))
                .withSeparator(DEFAULT_DELIMITER)
                .withQuoteChar(DEFAULT_QUOTE)
                .build()) {

            // Write header if file is new
            if (writeHeader) {
                writer.writeNext(header);
            }

            // Write all records
            for (String[] record : records) {
                if (record != null && record.length > 0) {
                    writer.writeNext(record);
                }
            }

            writer.flush();

        } catch (IOException e) {
            throw new DataPersistenceException("Failed to append to CSV file: " + filePath, e);
        }
    }

    /**
     * Creates a new CSV file with only the header row.
     * Replaces any existing file.
     * 
     * @param filePath the path to the CSV file
     * @param header the header row (column names)
     * @throws DataPersistenceException if creation fails
     */
    public static void createWithHeader(String filePath, String[] header) {
        Path path = Paths.get(filePath);
        ensureDirectoryExists(path.getParent());

        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(path.toFile()))
                .withSeparator(DEFAULT_DELIMITER)
                .withQuoteChar(DEFAULT_QUOTE)
                .build()) {

            if (header != null && header.length > 0) {
                writer.writeNext(header);
            }

            writer.flush();

        } catch (IOException e) {
            throw new DataPersistenceException("Failed to create CSV file: " + filePath, e);
        }
    }

    /**
     * Ensures that the specified directory exists, creating it if necessary.
     * 
     * @param directory the directory path to ensure exists
     * @throws DataPersistenceException if directory creation fails
     */
    private static void ensureDirectoryExists(Path directory) {
        if (directory != null && !Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new DataPersistenceException(
                    "Failed to create directory: " + directory, e);
            }
        }
    }

    /**
     * Creates a backup of the specified file.
     * 
     * @param filePath the file to backup
     * @throws DataPersistenceException if backup creation fails
     */
    private static void createBackup(Path filePath) {
        if (!Files.exists(filePath)) {
            return;
        }

        Path backupPath = Paths.get(filePath.toString() + BACKUP_SUFFIX);

        try {
            Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DataPersistenceException(
                "Failed to create backup: " + backupPath, e);
        }
    }

    /**
     * Deletes a CSV file if it exists.
     * 
     * @param filePath the path to the CSV file to delete
     * @return true if file was deleted, false if it didn't exist
     * @throws DataPersistenceException if deletion fails
     */
    public static boolean deleteFile(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return false;
        }

        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to delete CSV file: " + filePath, e);
        }
    }

    /**
     * Checks if a CSV file exists.
     * 
     * @param filePath the path to check
     * @return true if the file exists, false otherwise
     */
    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Legacy method for backward compatibility.
     * Writes records to CSV file with headers.
     * 
     * @param filePath the path to the CSV file
     * @param records list of records to write
     * @param headers the header row
     * @throws IOException if writing fails
     */
    public static void writeCSV(String filePath, List<String[]> records, String[] headers) throws IOException {
        try {
            writeAllRecords(filePath, headers, records, false);
        } catch (DataPersistenceException e) {
            throw new IOException("Failed to write CSV: " + e.getMessage(), e);
        }
    }
}
