package util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for CSV file operations in the Internship Placement Management System.
 * 
 * <p>This class provides thread-safe methods for reading from and writing to CSV files,
 * which serve as the data storage mechanism for the application (as per assignment requirements,
 * no databases are allowed).</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Read all records from CSV files</li>
 *   <li>Write complete datasets to CSV files</li>
 *   <li>Append single records to existing CSV files</li>
 *   <li>Validate CSV file headers</li>
 *   <li>Count rows in CSV files</li>
 *   <li>Create CSV files if they don't exist</li>
 * </ul>
 * 
 * <p><b>Thread Safety:</b> All methods are synchronized to prevent concurrent
 * modification issues when multiple users access the system.</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
public class CSVUtil {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private CSVUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Reads all records from a CSV file.
     * 
     * <p>Returns a list of string arrays, where each array represents one row
     * in the CSV file. The first row typically contains headers.</p>
     * 
     * @param filePath the path to the CSV file to read
     * @return a list of string arrays representing all rows in the file
     * @throws IOException if the file cannot be read
     */
    public static synchronized List<String[]> readCSV(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        // Create file if it doesn't exist
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return new ArrayList<>();
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            return reader.readAll();
        } catch (CsvException e) {
            throw new IOException("Error parsing CSV file: " + filePath, e);
        }
    }

    /**
     * Writes a complete dataset to a CSV file, overwriting any existing content.
     * 
     * <p>The first row in the data list should contain column headers.
     * All existing content in the file will be replaced.</p>
     * 
     * @param filePath the path to the CSV file to write
     * @param data the list of string arrays to write (including headers as first row)
     * @throws IOException if the file cannot be written
     */
    public static synchronized void writeCSV(String filePath, List<String[]> data) throws IOException {
        Path path = Paths.get(filePath);
        
        // Create directories if they don't exist
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.writeAll(data);
            writer.flush();
        }
    }

    /**
     * Appends a single record to an existing CSV file.
     * 
     * <p>If the file doesn't exist, it will be created. If headers are provided
     * and the file is empty, headers will be written first.</p>
     * 
     * @param filePath the path to the CSV file
     * @param record the string array representing one row to append
     * @param headers optional headers to write if file is new (can be null)
     * @throws IOException if the file cannot be written
     */
    public static synchronized void appendToCSV(String filePath, String[] record, String[] headers) 
            throws IOException {
        Path path = Paths.get(filePath);
        boolean fileExists = Files.exists(path);
        boolean isEmpty = !fileExists || Files.size(path) == 0;

        // Create file and directories if needed
        if (!fileExists) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            
            // Write headers if file is new and headers provided
            if (isEmpty && headers != null && headers.length > 0) {
                writer.writeNext(headers);
            }
            
            // Write the record
            writer.writeNext(record);
            writer.flush();
        }
    }

    /**
     * Validates that a CSV file has the expected headers.
     * 
     * <p>Compares the first row of the CSV file with the expected headers.
     * Comparison is case-insensitive and ignores leading/trailing whitespace.</p>
     * 
     * @param filePath the path to the CSV file
     * @param expectedHeaders the expected column headers
     * @return true if headers match, false otherwise
     * @throws IOException if the file cannot be read
     */
    public static synchronized boolean validateHeaders(String filePath, String[] expectedHeaders) 
            throws IOException {
        List<String[]> data = readCSV(filePath);
        
        if (data.isEmpty()) {
            return false;
        }

        String[] actualHeaders = data.get(0);
        
        if (actualHeaders.length != expectedHeaders.length) {
            return false;
        }

        for (int i = 0; i < expectedHeaders.length; i++) {
            if (!actualHeaders[i].trim().equalsIgnoreCase(expectedHeaders[i].trim())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Counts the number of rows in a CSV file (excluding the header row).
     * 
     * @param filePath the path to the CSV file
     * @return the number of data rows (total rows minus header)
     * @throws IOException if the file cannot be read
     */
    public static synchronized int countRows(String filePath) throws IOException {
        List<String[]> data = readCSV(filePath);
        return Math.max(0, data.size() - 1); // Subtract header row
    }

    /**
     * Checks if a CSV file exists and is not empty.
     * 
     * @param filePath the path to the CSV file
     * @return true if file exists and has content, false otherwise
     */
    public static synchronized boolean fileExistsAndNotEmpty(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.exists(path) && Files.size(path) > 0;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Ensures a CSV file exists with the specified headers.
     * 
     * <p>If the file doesn't exist or is empty, it will be created with the
     * provided headers. If the file exists with content, no action is taken.</p>
     * 
     * @param filePath the path to the CSV file
     * @param headers the column headers to write
     * @throws IOException if the file cannot be created or written
     */
    public static synchronized void ensureFileWithHeaders(String filePath, String[] headers) 
            throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path) || Files.size(path) == 0) {
            List<String[]> data = new ArrayList<>();
            data.add(headers);
            writeCSV(filePath, data);
        }
    }

    /**
     * Reads all data rows from a CSV file (excluding the header row).
     * 
     * @param filePath the path to the CSV file
     * @return a list of string arrays representing data rows only
     * @throws IOException if the file cannot be read
     */
    public static synchronized List<String[]> readDataRows(String filePath) throws IOException {
        List<String[]> allRows = readCSV(filePath);
        
        if (allRows.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Return all rows except the first (header)
        return new ArrayList<>(allRows.subList(1, allRows.size()));
    }

    /**
     * Searches for rows in a CSV file that match a specific value in a column.
     * 
     * @param filePath the path to the CSV file
     * @param columnIndex the index of the column to search (0-based, excluding header)
     * @param searchValue the value to search for
     * @return a list of matching rows (excluding header)
     * @throws IOException if the file cannot be read
     */
    public static synchronized List<String[]> findRowsByColumnValue(String filePath, 
            int columnIndex, String searchValue) throws IOException {
        List<String[]> dataRows = readDataRows(filePath);
        List<String[]> matchingRows = new ArrayList<>();

        for (String[] row : dataRows) {
            if (row.length > columnIndex && row[columnIndex].equals(searchValue)) {
                matchingRows.add(row);
            }
        }

        return matchingRows;
    }

    /**
     * Deletes a row from a CSV file based on a unique identifier in a specific column.
     * 
     * @param filePath the path to the CSV file
     * @param columnIndex the index of the column containing the unique identifier
     * @param identifier the identifier of the row to delete
     * @return true if a row was deleted, false if not found
     * @throws IOException if the file cannot be read or written
     */
    public static synchronized boolean deleteRowByIdentifier(String filePath, 
            int columnIndex, String identifier) throws IOException {
        List<String[]> allRows = readCSV(filePath);
        
        if (allRows.isEmpty()) {
            return false;
        }

        String[] headers = allRows.get(0);
        List<String[]> newData = new ArrayList<>();
        newData.add(headers);
        
        boolean deleted = false;
        for (int i = 1; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row.length > columnIndex && row[columnIndex].equals(identifier)) {
                deleted = true;
                continue; // Skip this row (delete it)
            }
            newData.add(row);
        }

        if (deleted) {
            writeCSV(filePath, newData);
        }

        return deleted;
    }

    /**
     * Updates a row in a CSV file based on a unique identifier.
     * 
     * @param filePath the path to the CSV file
     * @param columnIndex the index of the column containing the unique identifier
     * @param identifier the identifier of the row to update
     * @param newRow the new data for the row
     * @return true if a row was updated, false if not found
     * @throws IOException if the file cannot be read or written
     */
    public static synchronized boolean updateRowByIdentifier(String filePath, 
            int columnIndex, String identifier, String[] newRow) throws IOException {
        List<String[]> allRows = readCSV(filePath);
        
        if (allRows.isEmpty()) {
            return false;
        }

        boolean updated = false;
        for (int i = 1; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row.length > columnIndex && row[columnIndex].equals(identifier)) {
                allRows.set(i, newRow);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeCSV(filePath, allRows);
        }

        return updated;
    }
}
