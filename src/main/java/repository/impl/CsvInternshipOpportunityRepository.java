package repository.impl;

import enums.InternshipLevel;
import enums.InternshipStatus;
import exception.DataPersistenceException;
import model.InternshipOpportunity;
import util.IdGenerator;
import repository.InternshipOpportunityRepository;
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
 * CSV-based implementation of InternshipOpportunityRepository.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Repository
public class CsvInternshipOpportunityRepository implements InternshipOpportunityRepository {
    
    @Value("${app.data.internships:src/main/resources/data/internship_opportunities.csv}")
    private String csvFilePath;
    
    private final Map<String, InternshipOpportunity> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    @PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (Exception e) {
            System.err.println("Warning: Could not load internships from CSV: " + e.getMessage());
        }
    }
    
    private void loadFromCsv() {
        lock.writeLock().lock();
        try {
            List<Map<String, String>> records = CSVReader.readCSVWithHeaders(csvFilePath);
            cache.clear();
            for (Map<String, String> record : records) {
                InternshipOpportunity internship = mapToInternship(record);
                cache.put(internship.getInternshipId(), internship);
            }
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to load internships from CSV", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private InternshipOpportunity mapToInternship(Map<String, String> record) {
        InternshipOpportunity internship = new InternshipOpportunity();
        internship.setInternshipId(record.getOrDefault("InternshipID", ""));
        internship.setTitle(record.getOrDefault("Title", ""));
        internship.setDescription(record.getOrDefault("Description", ""));
        
        String levelStr = record.getOrDefault("Level", "BASIC");
        try {
            internship.setLevel(InternshipLevel.valueOf(levelStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            internship.setLevel(InternshipLevel.BASIC);
        }
        
        internship.setPreferredMajor(record.getOrDefault("PreferredMajor", ""));
        internship.setOpeningDate(DateUtil.parseDate(record.getOrDefault("OpeningDate", "")));
        internship.setClosingDate(DateUtil.parseDate(record.getOrDefault("ClosingDate", "")));
        internship.setStartDate(DateUtil.parseDate(record.getOrDefault("StartDate", "")));
        internship.setEndDate(DateUtil.parseDate(record.getOrDefault("EndDate", "")));
        
        String statusStr = record.getOrDefault("Status", "PENDING");
        try {
            internship.setStatus(InternshipStatus.valueOf(statusStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            internship.setStatus(InternshipStatus.PENDING);
        }
        
        internship.setCompanyName(record.getOrDefault("CompanyName", ""));
        internship.setCompanyRepEmail(record.getOrDefault("CompanyRepEmail", ""));
        
        String slotsStr = record.getOrDefault("Slots", "1");
        internship.setSlots(Integer.parseInt(slotsStr));
        
        String filledStr = record.getOrDefault("FilledSlots", "0");
        internship.setFilledSlots(Integer.parseInt(filledStr));
        
        String visStr = record.getOrDefault("Visibility", "false");
        internship.setVisibility(Boolean.parseBoolean(visStr));
        
        internship.setCreatedBy(record.getOrDefault("CreatedBy", ""));
        
        return internship;
    }
    
    @Override
    public InternshipOpportunity save(InternshipOpportunity internship) {
        lock.writeLock().lock();
        try {
            // Generate ID if not present
            if (internship.getInternshipId() == null || internship.getInternshipId().trim().isEmpty()) {
                internship.setInternshipId(IdGenerator.generateInternshipId());
            }
            
            cache.put(internship.getInternshipId(), internship);
            persistToCSV();
            return internship;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<InternshipOpportunity> findById(String id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<InternshipOpportunity> findAll() {
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
    public List<InternshipOpportunity> findByStatus(InternshipStatus status) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(i -> i.getStatus() == status)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<InternshipOpportunity> findByRepresentativeId(String repId) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(i -> repId.equals(i.getCreatedBy()) || repId.equals(i.getCompanyRepEmail()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<InternshipOpportunity> findVisibleByMajorAndLevel(String major, InternshipLevel level) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
                    .filter(i -> i.isVisible())
                    .filter(i -> major == null || major.isEmpty() || "ANY".equalsIgnoreCase(major) ||
                            "ANY".equalsIgnoreCase(i.getPreferredMajor()) ||
                            major.equalsIgnoreCase(i.getPreferredMajor()))
                    .filter(i -> level == null || i.getLevel() == level)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<InternshipOpportunity> findAllVisible() {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
                    .filter(InternshipOpportunity::isVisible)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<InternshipOpportunity> findByCompanyName(String companyName) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(i -> companyName.equalsIgnoreCase(i.getCompanyName()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private void persistToCSV() {
        try {
            String[] headers = {"InternshipID", "Title", "Description", "Level", "PreferredMajor", 
                               "OpeningDate", "ClosingDate", "StartDate", "EndDate", "Status", "CompanyName", "CompanyRepEmail",
                               "Slots", "FilledSlots", "Visibility", "CreatedBy"};
            
            List<String[]> records = cache.values().stream()
                    .map(this::internshipToRecord)
                    .collect(Collectors.toList());
            
            CSVWriter.writeCSV(csvFilePath, records, headers);
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to persist internships to CSV", e);
        }
    }
    
    private String[] internshipToRecord(InternshipOpportunity internship) {
        return new String[] {
            internship.getInternshipId() != null ? internship.getInternshipId() : "",
            internship.getTitle() != null ? internship.getTitle() : "",
            internship.getDescription() != null ? internship.getDescription() : "",
            internship.getLevel() != null ? internship.getLevel().name() : "BASIC",
            internship.getPreferredMajor() != null ? internship.getPreferredMajor() : "",
            DateUtil.formatDate(internship.getOpeningDate()),
            DateUtil.formatDate(internship.getClosingDate()),
            DateUtil.formatDate(internship.getStartDate()),
            DateUtil.formatDate(internship.getEndDate()),
            internship.getStatus() != null ? internship.getStatus().name() : "PENDING",
            internship.getCompanyName() != null ? internship.getCompanyName() : "",
            internship.getCompanyRepEmail() != null ? internship.getCompanyRepEmail() : "",
            String.valueOf(internship.getSlots()),
            String.valueOf(internship.getFilledSlots()),
            String.valueOf(internship.isVisible()),
            internship.getCreatedBy() != null ? internship.getCreatedBy() : ""
        };
    }
}
