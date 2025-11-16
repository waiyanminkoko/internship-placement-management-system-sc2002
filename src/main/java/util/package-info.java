/**
 * Utility classes for common operations across the application.
 * 
 * <p>This package contains utility classes that provide reusable helper methods
 * for various common operations such as CSV file handling, date manipulation,
 * validation, and ID generation.</p>
 * 
 * <h2>Utility Classes:</h2>
 * 
 * <h3>CSV Operations:</h3>
 * <ul>
 *   <li><b>CSVUtil</b> - High-level CSV operations (read, write, update, delete rows)</li>
 *   <li><b>CSVReader</b> - CSV reading utilities with header support</li>
 *   <li><b>CSVWriter</b> - CSV writing utilities with proper escaping</li>
 * </ul>
 * 
 * <h3>Data Generation:</h3>
 * <ul>
 *   <li><b>IdGenerator</b> - Unique ID generation for entities (STU-, INT-, APP-, etc.)</li>
 * </ul>
 * 
 * <h3>Validation:</h3>
 * <ul>
 *   <li><b>ValidationUtil</b> - Input validation for emails, passwords, names, etc.</li>
 * </ul>
 * 
 * <h3>Date/Time:</h3>
 * <ul>
 *   <li><b>DateUtil</b> - Date parsing, formatting, and validation utilities</li>
 * </ul>
 * 
 * <h2>Design Principles:</h2>
 * <p>All utility classes follow these principles:</p>
 * <ul>
 *   <li><b>Static Methods:</b> All methods are static for easy access</li>
 *   <li><b>No State:</b> Utility classes maintain no instance state</li>
 *   <li><b>Thread-Safe:</b> All operations are thread-safe</li>
 *   <li><b>Well-Documented:</b> Each method includes comprehensive Javadoc</li>
 *   <li><b>Exception Handling:</b> Proper exception handling with meaningful messages</li>
 * </ul>
 * 
 * <h2>CSV File Format:</h2>
 * <p>The CSV utilities support:</p>
 * <ul>
 *   <li>UTF-8 encoding</li>
 *   <li>Comma-separated values</li>
 *   <li>Quote escaping for special characters</li>
 *   <li>Header row with column names</li>
 *   <li>Empty value handling</li>
 * </ul>
 * 
 * <h2>ID Generation Patterns:</h2>
 * <ul>
 *   <li><b>STU-</b>XXXXXXXX - Student IDs</li>
 *   <li><b>CMP-</b>XXXXXXXX - Company Representative IDs</li>
 *   <li><b>STF-</b>XXXXXXXX - Career Center Staff IDs</li>
 *   <li><b>INT-</b>XXXXXXXX - Internship Opportunity IDs</li>
 *   <li><b>APP-</b>XXXXXXXX - Application IDs</li>
 *   <li><b>WDR-</b>XXXXXXXX - Withdrawal Request IDs</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 */
package util;
