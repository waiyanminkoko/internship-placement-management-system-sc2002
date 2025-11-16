package controller;

import dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for the application.
 * Provides endpoints to verify that the server is running correctly.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * Health check endpoint.
     * Returns basic server status information.
     * 
     * @return ApiResponse containing server status
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("application", "Internship Placement Management System");
        status.put("version", "1.0.0");
        
        return ApiResponse.success(status, "Server is running");
    }

    /**
     * Root endpoint.
     * Provides welcome message and API information.
     * 
     * @return ApiResponse with welcome message
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> root() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "Internship Placement Management System");
        info.put("version", "1.0.0");
        info.put("description", "Web-based platform for managing internship opportunities and applications");
        info.put("apiBasePath", "/api");
        
        return ApiResponse.success(info, "Welcome to the Internship Placement Management System");
    }
}
