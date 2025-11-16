/**
 * CSV-based implementations of repository interfaces.
 * 
 * <p>This package contains concrete implementations of all repository interfaces
 * using CSV files as the data storage mechanism. These implementations handle
 * reading from and writing to CSV files while maintaining data integrity.</p>
 * 
 * <h2>CSV Repository Implementations:</h2>
 * <ul>
 *   <li><b>CsvStudentRepository</b> - Student data persistence</li>
 *   <li><b>CsvCompanyRepresentativeRepository</b> - Company representative data persistence</li>
 *   <li><b>CsvCareerCenterStaffRepository</b> - Staff data persistence</li>
 *   <li><b>CsvInternshipOpportunityRepository</b> - Internship data persistence</li>
 *   <li><b>CsvApplicationRepository</b> - Application data persistence</li>
 * </ul>
 * 
 * <h2>CSV File Structure:</h2>
 * <p>All CSV files are stored in {@code src/main/resources/data/} directory with:</p>
 * <ul>
 *   <li>Header row defining column names</li>
 *   <li>UTF-8 encoding for international character support</li>
 *   <li>Comma-separated values with quote escaping</li>
 *   <li>Consistent date format (yyyy-MM-dd)</li>
 * </ul>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Thread-safe read/write operations</li>
 *   <li>Automatic ID generation for new entities</li>
 *   <li>Data validation before persistence</li>
 *   <li>Referential integrity checks</li>
 *   <li>Exception handling for file I/O errors</li>
 * </ul>
 * 
 * <p>All implementations use the {@link util.CSVUtil} utility class for
 * low-level CSV operations and OpenCSV library for robust CSV parsing.</p>
 * 
 * @since 1.0.0
 * @author SC2002 SCED Group-6
 * @see repository
 * @see util.CSVUtil
 */
package repository.impl;
