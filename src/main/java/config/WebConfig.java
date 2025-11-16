package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for Cross-Origin Resource Sharing (CORS).
 * Enables the React frontend to communicate with the Spring Boot backend.
 * 
 * <p>This configuration allows requests from the frontend development server
 * (http://localhost:5173) to access all backend API endpoints.</p>
 * 
 * <p><strong>CORS Settings:</strong></p>
 * <ul>
 *   <li>Allowed Origins: http://localhost:5173 (Vite dev server)</li>
 *   <li>Allowed Methods: GET, POST, PUT, DELETE, OPTIONS</li>
 *   <li>Allowed Headers: All (*)</li>
 *   <li>Allow Credentials: true</li>
 *   <li>Max Age: 3600 seconds</li>
 * </ul>
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-01-15
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for the application.
     * 
     * @param registry the CORS registry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
