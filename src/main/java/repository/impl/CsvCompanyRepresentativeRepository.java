package repository.impl;

import enums.ApprovalStatus;
import exception.DataPersistenceException;
import model.CompanyRepresentative;
import repository.CompanyRepresentativeRepository;
import util.CSVReader;
import util.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * CSV-based implementation of CompanyRepresentativeRepository.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Repository
public class CsvCompanyRepresentativeRepository implements CompanyRepresentativeRepository {
    
    @Value("${app.data.representatives:src/main/resources/data/company_representative_list.csv}")
    private String csvFilePath;
    
    private final Map<String, CompanyRepresentative> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (Exception e) {
            System.err.println("Warning: Could not load representatives from CSV: " + e.getMessage());
        }
    }
    
    private void loadFromCsv() {
        lock.writeLock().lock();
        try {
            List<Map<String, String>> records = CSVReader.readCSVWithHeaders(csvFilePath);
            cache.clear();
            for (Map<String, String> record : records) {
                CompanyRepresentative rep = mapToRepresentative(record);
                cache.put(rep.getUserId(), rep);
            }
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to load representatives from CSV", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private CompanyRepresentative mapToRepresentative(Map<String, String> record) {
        CompanyRepresentative rep = new CompanyRepresentative();
        // Use Email as the primary user ID for login
        String email = record.getOrDefault("Email", "");
        rep.setUserId(email);
        rep.setEmail(email);
        rep.setName(record.getOrDefault("Name", ""));
        rep.setPassword(record.getOrDefault("Password", "password123"));
        rep.setCompanyName(record.getOrDefault("CompanyName", ""));
        rep.setIndustry(record.getOrDefault("Industry", ""));
        rep.setPosition(record.getOrDefault("Position", ""));
        rep.setRole(enums.UserRole.COMPANY_REPRESENTATIVE);  // Set the role explicitly
        
        String statusStr = record.getOrDefault("Status", "PENDING");
        try {
            // Handle both "active" and "APPROVED" status
            if ("active".equalsIgnoreCase(statusStr)) {
                rep.setStatus(ApprovalStatus.APPROVED);
            } else {
                rep.setStatus(ApprovalStatus.valueOf(statusStr.toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            rep.setStatus(ApprovalStatus.PENDING);
        }
        
        String oppIdsStr = record.getOrDefault("OpportunityIDs", "");
        if (!oppIdsStr.isEmpty()) {
            rep.setInternshipOpportunityIds(new ArrayList<>(Arrays.asList(oppIdsStr.split(";"))));
        }
        
        String regDateStr = record.getOrDefault("RegistrationDate", "");
        if (!regDateStr.isEmpty()) {
            try {
                rep.setRegistrationDate(LocalDateTime.parse(regDateStr, DATE_TIME_FORMATTER));
            } catch (Exception e) {
                rep.setRegistrationDate(LocalDateTime.now());
            }
        } else {
            rep.setRegistrationDate(LocalDateTime.now());
        }
        
        rep.setApprovedByStaffId(record.getOrDefault("ApprovedByStaffID", ""));
        
        return rep;
    }
    
    @Override
    public CompanyRepresentative save(CompanyRepresentative rep) {
        lock.writeLock().lock();
        try {
            cache.put(rep.getUserId(), rep);
            persistToCSV();
            return rep;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<CompanyRepresentative> findById(String id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<CompanyRepresentative> findAll() {
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
    public List<CompanyRepresentative> findByApprovalStatus(boolean approved) {
        lock.readLock().lock();
        try {
            ApprovalStatus status = approved ? ApprovalStatus.APPROVED : ApprovalStatus.PENDING;
            return cache.values().stream()
                    .filter(r -> r.getStatus() == status)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Optional<CompanyRepresentative> findByUserId(String userId) {
        return findById(userId);
    }
    
    @Override
    public List<CompanyRepresentative> findByCompanyName(String companyName) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(r -> companyName.equalsIgnoreCase(r.getCompanyName()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private void persistToCSV() {
        try {
            String[] headers = {"CompanyRepID", "Name", "CompanyName", "Industry", 
                               "Position", "Email", "Password", "Status"};
            
            List<String[]> records = cache.values().stream()
                    .map(this::repToRecord)
                    .collect(Collectors.toList());
            
            CSVWriter.writeCSV(csvFilePath, records, headers);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to persist representatives to CSV", e);
        }
    }
    
    private String[] repToRecord(CompanyRepresentative rep) {
        // Generate CompanyRepID from name if needed
        String companyRepId = "C" + String.format("%05d", Math.abs(rep.getEmail().hashCode() % 100000));
        
        return new String[] {
            companyRepId,
            rep.getName() != null ? rep.getName() : "",
            rep.getCompanyName() != null ? rep.getCompanyName() : "",
            rep.getIndustry() != null ? rep.getIndustry() : "",
            rep.getPosition() != null ? rep.getPosition() : "",
            rep.getEmail() != null ? rep.getEmail() : "",
            rep.getPassword() != null ? rep.getPassword() : "",
            rep.getStatus() != null ? (rep.getStatus() == ApprovalStatus.APPROVED ? "active" : rep.getStatus().name().toLowerCase()) : "pending"
        };
    }
}
