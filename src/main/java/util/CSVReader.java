package util;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import exception.CsvParseException;
import exception.DataPersistenceException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Specialized CSV reading utility using OpenCSV library.
 * Provides robust CSV file reading with error handling and validation.
 * 
 * <p>This class wraps OpenCSV's CSVReader to provide:
 * <ul>
 *   <li>Automatic resource management</li>
 *   <li>Comprehensive error handling</li>
 *   <li>Header validation</li>
 *   <li>Empty file handling</li>
 *   <li>Line-by-line and batch reading</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * List<String[]> records = CSVReader.readAllRecords("data/students.csv", true);
 * for (String[] record : records) {
 *     String userId = record[0];
 *     String name = record[1];
 *     // Process record...
 * }
 * }</pre>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-01-15
 * @see CSVWriter
 * @see CSVUtil
 */
public class CSVReader {

    /**
     * Default CSV delimiter character.
     */
    private static final char DEFAULT_DELIMITER = ',';

    /**
     * Default CSV quote character.
     */
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CSVReader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Reads all records from a CSV file, optionally skipping the header row.
     * 
     * @param filePath the path to the CSV file
     * @param skipHeader true to skip the first row (header), false to include it
     * @return list of string arrays, each representing one CSV record
     * @throws DataPersistenceException if file cannot be read or parsing fails
     */
    public static List<String[]> readAllRecords(String filePath, boolean skipHeader) {
        Path path = Paths.get(filePath);
        
        // Check if file exists
        if (!Files.exists(path)) {
            throw new DataPersistenceException("CSV file not found: " + filePath);
        }

        // Check if file is readable
        if (!Files.isReadable(path)) {
            throw new DataPersistenceException("CSV file is not readable: " + filePath);
        }

        List<String[]> records = new ArrayList<>();

        try (com.opencsv.CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(DEFAULT_DELIMITER)
                        .withQuoteChar(DEFAULT_QUOTE)
                        .build())
                .build()) {

            String[] nextRecord;
            boolean isFirstRow = true;

            while ((nextRecord = reader.readNext()) != null) {
                // Skip header if requested
                if (skipHeader && isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                // Skip empty rows
                if (isEmptyRecord(nextRecord)) {
                    continue;
                }

                records.add(nextRecord);
            }

        } catch (IOException e) {
            throw new DataPersistenceException("Failed to read CSV file: " + filePath, e);
        } catch (Exception e) {
            throw new DataPersistenceException("Unexpected error while reading CSV: " + filePath, e);
        }

        return records;
    }

    /**
     * Reads the header row from a CSV file.
     * 
     * @param filePath the path to the CSV file
     * @return array of header column names, or empty array if file is empty
     * @throws DataPersistenceException if file cannot be read
     */
    public static String[] readHeader(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new DataPersistenceException("CSV file not found: " + filePath);
        }

        try (com.opencsv.CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(DEFAULT_DELIMITER)
                        .withQuoteChar(DEFAULT_QUOTE)
                        .build())
                .build()) {

            String[] header = reader.readNext();
            return header != null ? header : new String[0];

        } catch (CsvValidationException e) {
            throw new CsvParseException("Failed to parse CSV header: " + filePath, e);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to read CSV header: " + filePath, e);
        }
    }

    /**
     * Reads a single record by its row index (0-based, excluding header).
     * 
     * @param filePath the path to the CSV file
     * @param recordIndex the 0-based index of the record to read
     * @param hasHeader true if the file has a header row
     * @return the record as a string array, or null if index out of bounds
     * @throws DataPersistenceException if file cannot be read
     */
    public static String[] readRecordByIndex(String filePath, int recordIndex, boolean hasHeader) {
        List<String[]> records = readAllRecords(filePath, hasHeader);
        
        if (recordIndex < 0 || recordIndex >= records.size()) {
            return null;
        }
        
        return records.get(recordIndex);
    }

    /**
     * Counts the number of records in a CSV file, excluding header if specified.
     * 
     * @param filePath the path to the CSV file
     * @param hasHeader true if the file has a header row to exclude from count
     * @return the number of data records in the file
     * @throws DataPersistenceException if file cannot be read
     */
    public static int countRecords(String filePath, boolean hasHeader) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new DataPersistenceException("CSV file not found: " + filePath);
        }

        int count = 0;
        boolean isFirstRow = true;

        try (com.opencsv.CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(DEFAULT_DELIMITER)
                        .withQuoteChar(DEFAULT_QUOTE)
                        .build())
                .build()) {

            while (reader.readNext() != null) {
                if (hasHeader && isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                count++;
            }

        } catch (CsvValidationException e) {
            throw new CsvParseException("Failed to parse CSV records: " + filePath, e);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to count CSV records: " + filePath, e);
        }

        return count;
    }

    /**
     * Validates that a CSV file has the expected header columns.
     * 
     * @param filePath the path to the CSV file
     * @param expectedHeaders the expected header column names
     * @return true if headers match exactly, false otherwise
     * @throws DataPersistenceException if file cannot be read
     */
    public static boolean validateHeaders(String filePath, String[] expectedHeaders) {
        String[] actualHeaders = readHeader(filePath);

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
     * Checks if a record is empty (all fields are null or whitespace).
     * 
     * @param record the CSV record to check
     * @return true if the record is empty, false otherwise
     */
    private static boolean isEmptyRecord(String[] record) {
        if (record == null || record.length == 0) {
            return true;
        }

        for (String field : record) {
            if (field != null && !field.trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reads all records and returns them as a list of lists (for easier manipulation).
     * 
     * @param filePath the path to the CSV file
     * @param skipHeader true to skip the first row (header)
     * @return list of lists, each inner list represents one CSV record
     * @throws DataPersistenceException if file cannot be read
     */
    public static List<List<String>> readAllRecordsAsList(String filePath, boolean skipHeader) {
        List<String[]> records = readAllRecords(filePath, skipHeader);
        List<List<String>> result = new ArrayList<>();

        for (String[] record : records) {
            List<String> recordList = new ArrayList<>();
            for (String field : record) {
                recordList.add(field);
            }
            result.add(recordList);
        }

        return result;
    }

    /**
     * Reads all records from a CSV file and returns them as a list of maps,
     * where each map represents a record with column names as keys.
     * 
     * @param filePath the path to the CSV file
     * @return list of maps, each map represents one CSV record with headers as keys
     * @throws DataPersistenceException if file cannot be read or has no header
     */
    public static List<Map<String, String>> readCSVWithHeaders(String filePath) {
        String[] headers = readHeader(filePath);
        
        if (headers.length == 0) {
            throw new DataPersistenceException("CSV file has no headers: " + filePath);
        }
        
        List<String[]> records = readAllRecords(filePath, true); // Skip header
        List<Map<String, String>> result = new ArrayList<>();
        
        for (String[] record : records) {
            Map<String, String> recordMap = new LinkedHashMap<>();
            for (int i = 0; i < headers.length && i < record.length; i++) {
                recordMap.put(headers[i], record[i] != null ? record[i] : "");
            }
            // Add empty strings for missing columns
            for (int i = record.length; i < headers.length; i++) {
                recordMap.put(headers[i], "");
            }
            result.add(recordMap);
        }
        
        return result;
    }
}
