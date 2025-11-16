package repository.impl;

import enums.ApplicationStatus;
import exception.DataPersistenceException;
import model.Application;
import repository.ApplicationRepository;
import util.CSVReader;
import util.CSVWriter;
import util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * CSV-based implementation of ApplicationRepository.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Repository
public class CsvApplicationRepository implements ApplicationRepository {
    
    @Value("${app.data.applications:src/main/resources/data/applications.csv}")
    private String csvFilePath;
    
    private final Map<String, Application> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    @PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (Exception e) {
            System.err.println("Warning: Could not load applications from CSV: " + e.getMessage());
        }
    }
    
    private void loadFromCsv() {
        lock.writeLock().lock();
        try {
            List<Map<String, String>> records = CSVReader.readCSVWithHeaders(csvFilePath);
            cache.clear();
            for (Map<String, String> record : records) {
                Application application = mapToApplication(record);
                cache.put(application.getApplicationId(), application);
            }
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to load applications from CSV", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private Application mapToApplication(Map<String, String> record) {
        Application application = new Application();
        application.setApplicationId(record.getOrDefault("ApplicationID", ""));
        application.setStudentId(record.getOrDefault("StudentID", ""));
        application.setInternshipId(record.getOrDefault("InternshipID", ""));
        
        String statusStr = record.getOrDefault("Status", "PENDING");
        try {
            application.setStatus(ApplicationStatus.valueOf(statusStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            application.setStatus(ApplicationStatus.PENDING);
        }
        
        // Set submission date
        String submissionDateStr = record.getOrDefault("SubmissionDate", "");
        if (submissionDateStr.isEmpty()) {
            // Fallback to ApplicationDate for backwards compatibility
            submissionDateStr = record.getOrDefault("ApplicationDate", "");
        }
        application.setSubmissionDate(DateUtil.parseCsvDateTime(submissionDateStr));
        
        // Set status update date
        String statusUpdateDateStr = record.getOrDefault("StatusUpdateDate", "");
        if (!statusUpdateDateStr.isEmpty()) {
            application.setStatusUpdateDate(DateUtil.parseCsvDateTime(statusUpdateDateStr));
        }
        
        // Set placement accepted flag
        String acceptedStr = record.getOrDefault("PlacementAccepted", "false");
        application.setPlacementAccepted(Boolean.parseBoolean(acceptedStr));
        
        // Set placement acceptance date
        String acceptanceDateStr = record.getOrDefault("PlacementAcceptanceDate", "");
        if (!acceptanceDateStr.isEmpty()) {
            application.setPlacementAcceptanceDate(DateUtil.parseCsvDateTime(acceptanceDateStr));
        }
        
        // Set representative comments
        String comments = record.getOrDefault("RepresentativeComments", "");
        if (!comments.isEmpty()) {
            application.setRepresentativeComments(comments);
        }
        
        return application;
    }
    
    @Override
    public Application save(Application application) {
        lock.writeLock().lock();
        try {
            cache.put(application.getApplicationId(), application);
            persistToCSV();
            return application;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<Application> findById(String id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Application> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(cache.values());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void deleteById(String id) {
        lock.writeLock().lock();
        try {
            cache.remove(id);
            persistToCSV();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean existsById(String id) {
        lock.readLock().lock();
        try {
            return cache.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public long count() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Application> findByStudentId(String studentId) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(a -> studentId.equals(a.getStudentId()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Application> findByInternshipId(String internshipId) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(a -> internshipId.equals(a.getInternshipId()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Application> findByStatus(ApplicationStatus status) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(a -> a.getStatus() == status)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public long countActiveByStudentId(String studentId) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(a -> studentId.equals(a.getStudentId()))
                    .filter(Application::isActive)
                    .count();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Application> findAcceptedPlacements() {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(Application::isPlacementAccepted)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private void persistToCSV() {
        try {
            String[] headers = {"ApplicationID", "StudentID", "InternshipID", "Status", 
                               "SubmissionDate", "StatusUpdateDate", "PlacementAccepted", 
                               "PlacementAcceptanceDate", "RepresentativeComments"};
            
            List<String[]> records = cache.values().stream()
                    .map(this::applicationToRecord)
                    .collect(Collectors.toList());
            
            CSVWriter.writeCSV(csvFilePath, records, headers);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to persist applications to CSV", e);
        }
    }
    
    private String[] applicationToRecord(Application application) {
        return new String[] {
            application.getApplicationId() != null ? application.getApplicationId() : "",
            application.getStudentId() != null ? application.getStudentId() : "",
            application.getInternshipId() != null ? application.getInternshipId() : "",
            application.getStatus() != null ? application.getStatus().name() : "PENDING",
            DateUtil.formatDateTimeForCsv(application.getSubmissionDate()),
            DateUtil.formatDateTimeForCsv(application.getStatusUpdateDate()),
            String.valueOf(application.isPlacementAccepted()),
            DateUtil.formatDateTimeForCsv(application.getPlacementAcceptanceDate()),
            application.getRepresentativeComments() != null ? application.getRepresentativeComments() : ""
        };
    }
}
