package repository.impl;

import enums.Major;
import exception.DataPersistenceException;
import model.Student;
import repository.StudentRepository;
import util.CSVReader;
import util.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * CSV-based implementation of StudentRepository.
 * Maintains in-memory cache with periodic sync to CSV file.
 * Thread-safe implementation using read-write locks.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Repository
public class CsvStudentRepository implements StudentRepository {
    
    @Value("${app.data.students:src/main/resources/data/student_list.csv}")
    private String csvFilePath;
    
    // In-memory cache for performance
    private final Map<String, Student> cache = new ConcurrentHashMap<>();
    
    // Read-write lock for thread safety
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    /**
     * Initializes repository by loading data from CSV file.
     */
    @PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (Exception e) {
            // Log warning but don't fail if file doesn't exist yet
            System.err.println("Warning: Could not load students from CSV: " + e.getMessage());
        }
    }
    
    /**
     * Loads student data from CSV file into memory cache.
     */
    private void loadFromCsv() {
        lock.writeLock().lock();
        try {
            List<Map<String, String>> records = CSVReader.readCSVWithHeaders(csvFilePath);
            
            cache.clear();
            for (Map<String, String> record : records) {
                Student student = mapToStudent(record);
                cache.put(student.getUserId(), student);
            }
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to load students from CSV", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Converts CSV record map to Student object.
     */
    private Student mapToStudent(Map<String, String> record) {
        Student student = new Student();
        student.setUserId(record.getOrDefault("StudentID", record.getOrDefault("userId", "")));
        student.setName(record.getOrDefault("Name", record.getOrDefault("name", "")));
        student.setPassword(record.getOrDefault("Password", record.getOrDefault("password", "password123")));
        student.setMajor(record.getOrDefault("Major", record.getOrDefault("major", "")));
        student.setRole(enums.UserRole.STUDENT);  // Set the role explicitly
        
        String yearStr = record.getOrDefault("Year", record.getOrDefault("year", "1"));
        student.setYear(parseIntSafe(yearStr, 1));
        
        student.setEmail(record.getOrDefault("Email", record.getOrDefault("email", "")));
        
        // Parse application IDs (semicolon-separated)
        String appIdsStr = record.getOrDefault("ApplicationIDs", record.getOrDefault("applicationIds", ""));
        if (!appIdsStr.isEmpty()) {
            List<String> appIds = Arrays.asList(appIdsStr.split(";"));
            student.setApplicationIds(new ArrayList<>(appIds));
        }
        
        student.setAcceptedPlacementId(record.getOrDefault("AcceptedPlacementID", record.getOrDefault("acceptedPlacementId", "")));
        
        String hasAcceptedStr = record.getOrDefault("HasAcceptedPlacement", record.getOrDefault("hasAcceptedPlacement", "false"));
        student.setHasAcceptedPlacement(Boolean.parseBoolean(hasAcceptedStr));
        
        return student;
    }
    
    /**
     * Safely parses an integer with a default value.
     */
    private int parseIntSafe(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public Student save(Student student) {
        lock.writeLock().lock();
        try {
            cache.put(student.getUserId(), student);
            persistToCSV();
            return student;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<Student> findById(String id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Student> findAll() {
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
    
    /**
     * Persists current cache state to CSV file.
     */
    private void persistToCSV() {
        try {
            String[] headers = {"StudentID", "Name", "Password", "Major", "Year", "Email", 
                               "ApplicationIDs", "AcceptedPlacementID", "HasAcceptedPlacement"};
            
            List<String[]> records = cache.values().stream()
                    .map(this::studentToRecord)
                    .collect(Collectors.toList());
            
            CSVWriter.writeCSV(csvFilePath, records, headers);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to persist students to CSV", e);
        }
    }
    
    /**
     * Converts Student object to CSV record array.
     */
    private String[] studentToRecord(Student student) {
        String appIds = student.getApplicationIds() != null ? 
            String.join(";", student.getApplicationIds()) : "";
        
        return new String[] {
            student.getUserId() != null ? student.getUserId() : "",
            student.getName() != null ? student.getName() : "",
            student.getPassword() != null ? student.getPassword() : "",
            student.getMajor() != null ? student.getMajor() : "",
            String.valueOf(student.getYear()),
            student.getEmail() != null ? student.getEmail() : "",
            appIds,
            student.getAcceptedPlacementId() != null ? student.getAcceptedPlacementId() : "",
            String.valueOf(student.isHasAcceptedPlacement())
        };
    }
    
    @Override
    public Optional<Student> findByUserId(String userId) {
        return findById(userId);
    }
    
    @Override
    public List<Student> findByMajor(Major major) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(s -> major.toString().equalsIgnoreCase(s.getMajor()) ||
                                major.name().equalsIgnoreCase(s.getMajor()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Student> findByYearOfStudy(int year) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(s -> s.getYear() == year)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
}
