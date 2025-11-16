package repository.impl;

import exception.DataPersistenceException;
import model.CareerCenterStaff;
import repository.CareerCenterStaffRepository;
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
 * CSV-based implementation of CareerCenterStaffRepository.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Repository
public class CsvCareerCenterStaffRepository implements CareerCenterStaffRepository {
    
    @Value("${app.data.staff:src/main/resources/data/staff_list.csv}")
    private String csvFilePath;
    
    private final Map<String, CareerCenterStaff> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    @PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (Exception e) {
            System.err.println("Warning: Could not load staff from CSV: " + e.getMessage());
        }
    }
    
    private void loadFromCsv() {
        lock.writeLock().lock();
        try {
            List<Map<String, String>> records = CSVReader.readCSVWithHeaders(csvFilePath);
            cache.clear();
            for (Map<String, String> record : records) {
                CareerCenterStaff staff = mapToStaff(record);
                cache.put(staff.getUserId(), staff);
            }
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to load staff from CSV", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private CareerCenterStaff mapToStaff(Map<String, String> record) {
        CareerCenterStaff staff = new CareerCenterStaff();
        staff.setUserId(record.getOrDefault("StaffID", record.getOrDefault("userId", "")));
        staff.setName(record.getOrDefault("Name", ""));
        staff.setPassword(record.getOrDefault("Password", "password123"));
        staff.setDepartment(record.getOrDefault("Department", record.getOrDefault("Role", "")));
        staff.setEmail(record.getOrDefault("Email", ""));
        staff.setRole(enums.UserRole.CAREER_CENTER_STAFF);  // Set the role explicitly
        
        String repIdsStr = record.getOrDefault("ApprovedRepIDs", "");
        if (!repIdsStr.isEmpty()) {
            staff.setApprovedRepresentativeIds(new ArrayList<>(Arrays.asList(repIdsStr.split(";"))));
        }
        
        String intIdsStr = record.getOrDefault("ApprovedInternshipIDs", "");
        if (!intIdsStr.isEmpty()) {
            staff.setApprovedInternshipIds(new ArrayList<>(Arrays.asList(intIdsStr.split(";"))));
        }
        
        return staff;
    }
    
    @Override
    public CareerCenterStaff save(CareerCenterStaff staff) {
        lock.writeLock().lock();
        try {
            cache.put(staff.getUserId(), staff);
            persistToCSV();
            return staff;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<CareerCenterStaff> findById(String id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<CareerCenterStaff> findAll() {
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
    public Optional<CareerCenterStaff> findByUserId(String userId) {
        return findById(userId);
    }
    
    @Override
    public Optional<CareerCenterStaff> findByEmail(String email) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(s -> email.equalsIgnoreCase(s.getEmail()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private void persistToCSV() {
        try {
            String[] headers = {"StaffID", "Name", "Password", "Department", "Email", 
                               "ApprovedRepIDs", "ApprovedInternshipIDs"};
            
            List<String[]> records = cache.values().stream()
                    .map(this::staffToRecord)
                    .collect(Collectors.toList());
            
            CSVWriter.writeCSV(csvFilePath, records, headers);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to persist staff to CSV", e);
        }
    }
    
    private String[] staffToRecord(CareerCenterStaff staff) {
        String repIds = staff.getApprovedRepresentativeIds() != null ? 
            String.join(";", staff.getApprovedRepresentativeIds()) : "";
        String intIds = staff.getApprovedInternshipIds() != null ? 
            String.join(";", staff.getApprovedInternshipIds()) : "";
        
        return new String[] {
            staff.getUserId() != null ? staff.getUserId() : "",
            staff.getName() != null ? staff.getName() : "",
            staff.getPassword() != null ? staff.getPassword() : "",
            staff.getDepartment() != null ? staff.getDepartment() : "",
            staff.getEmail() != null ? staff.getEmail() : "",
            repIds,
            intIds
        };
    }
}
