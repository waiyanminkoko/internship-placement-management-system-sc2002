package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main Spring Boot application class for the Internship Placement Management System.
 * 
 * <p>This is the entry point for the SC2002 SCED Group 6 project - a centralized hub
 * for Students, Company Representatives, and Career Center Staff to manage
 * internship opportunities, applications, and placements.</p>
 * 
 * <p>Key Features:</p>
 * <ul>
 *   <li>Student internship browsing and application management</li>
 *   <li>Company representative internship posting and application review</li>
 *   <li>Career center staff approval workflows and reporting</li>
 *   <li>CSV-based data persistence (no database required)</li>
 * </ul>
 * 
 * <p>Architecture:</p>
 * <ul>
 *   <li>Spring Boot REST API backend (Port 6060)</li>
 *   <li>React frontend (Port 5173)</li>
 *   <li>CSV file storage for all data</li>
 * </ul>
 * 
 * <p>Note: We exclude database-related auto-configurations since we use CSV file storage.
 * We explicitly define component scanning to avoid scanning Spring's internal packages.</p>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0.0
 * @since 2025-10-14
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    R2dbcAutoConfiguration.class,
    DataSourceAutoConfiguration.class
})
@ComponentScan(basePackages = {
    "controller",
    "service", 
    "repository",
    "config",
    "exception"
})
public class InternshipPlacementApplication {

    /**
     * Main method to start the Spring Boot application.
     * 
     * <p>Initializes the Spring IoC container, auto-configures beans,
     * and starts the embedded Tomcat server on port 6060.</p>
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(InternshipPlacementApplication.class, args);
        System.out.println("========================================");
        System.out.println("Internship Placement Management System");
        System.out.println("SC2002 Group 6");
        System.out.println("Server running on http://localhost:6060");
        System.out.println("API Base: http://localhost:6060/api");
        System.out.println("========================================");
    }
}
