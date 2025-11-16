package service.impl;

import enums.ApprovalStatus;
import exception.BusinessRuleException;
import model.Application;
import model.Student;
import model.WithdrawalRequest;
import util.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import util.CSVReader;
import util.CSVWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Service for managing withdrawal requests.
 * Handles creation, retrieval, and persistence of withdrawal requests.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Service
public class WithdrawalRequestService {
    
    @Value("${app.data.withdrawals:src/main/resources/data/withdrawal_requests.csv}")
    private String csvFilePath;
    
    private final Map<String, WithdrawalRequest> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Initializes the service by loading withdrawal requests from CSV.
     */
    @jakarta.annotation.PostConstruct
    public void init() {
        try {
            loadFromCsv();
        } catch (Exception e) {
            System.err.println("Warning: Could not load withdrawal requests from CSV: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new withdrawal request.
     * 
     * @param student The student requesting withdrawal
     * @param application The application to withdraw from
     * @param reason The reason for withdrawal
     * @return The created WithdrawalRequest
     */
    public WithdrawalRequest createWithdrawalRequest(Student student, Application application, String reason) {
        lock.writeLock().lock();
        try {
            // Check for existing withdrawal requests for this application
            List<WithdrawalRequest> existingRequests = cache.values().stream()
                    .filter(wr -> wr.getApplicationId().equals(application.getApplicationId()))
                    .toList();
            
            // Check if there's already a pending withdrawal
            Optional<WithdrawalRequest> pending = existingRequests.stream()
                    .filter(wr -> wr.getStatus() == ApprovalStatus.PENDING)
                    .findFirst();
            
            if (pending.isPresent()) {
                throw new BusinessRuleException("A withdrawal request for this application is already pending. " +
                        "Please edit the existing request instead of creating a new one.");
            }
            
            // Check if there's an approved withdrawal
            Optional<WithdrawalRequest> approved = existingRequests.stream()
                    .filter(wr -> wr.getStatus() == ApprovalStatus.APPROVED)
                    .findFirst();
            
            if (approved.isPresent()) {
                throw new BusinessRuleException("A withdrawal request for this application has already been approved. " +
                        "You cannot submit another withdrawal request for the same application.");
            }
            
            // If there's a rejected request, allow new submission (no error thrown)
            
            WithdrawalRequest request = new WithdrawalRequest();
            request.setWithdrawalId(IdGenerator.generateWithdrawalRequestId());
            request.setApplicationId(application.getApplicationId());
            request.setStudentId(student.getUserId());
            request.setInternshipId(application.getInternshipId());
            request.setReason(reason);
            request.setStatus(ApprovalStatus.PENDING);
            request.setRequestDate(LocalDate.now());
            
            cache.put(request.getWithdrawalId(), request);
            persistToCSV();
            
            return request;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Finds a withdrawal request by ID.
     * 
     * @param withdrawalId The withdrawal request ID
     * @return Optional containing the request if found
     */
    public Optional<WithdrawalRequest> findById(String withdrawalId) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(cache.get(withdrawalId));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Finds all withdrawal requests for a student.
     * 
     * @param studentId The student ID
     * @return List of withdrawal requests
     */
    public List<WithdrawalRequest> findByStudentId(String studentId) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(wr -> wr.getStudentId().equals(studentId))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Finds all pending withdrawal requests.
     * 
     * @return List of pending requests
     */
    public List<WithdrawalRequest> findPendingRequests() {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(wr -> wr.getStatus() == ApprovalStatus.PENDING)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Finds all withdrawal requests.
     * 
     * @return List of all withdrawal requests
     */
    public List<WithdrawalRequest> findAll() {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Updates a withdrawal request.
     * 
     * @param request The request to update
     * @return The updated request
     */
    public WithdrawalRequest save(WithdrawalRequest request) {
        lock.writeLock().lock();
        try {
            cache.put(request.getWithdrawalId(), request);
            persistToCSV();
            return request;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Deletes a withdrawal request.
     * Allows students to cancel their pending withdrawal requests.
     * 
     * @param withdrawalId The withdrawal request ID to delete
     */
    public void delete(String withdrawalId) {
        lock.writeLock().lock();
        try {
            cache.remove(withdrawalId);
            persistToCSV();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Loads withdrawal requests from CSV file.
     */
    private void loadFromCsv() {
        lock.writeLock().lock();
        try {
            // Check if file exists first
            if (!CSVWriter.exists(csvFilePath)) {
                cache.clear();
                return;
            }

            List<String[]> records = CSVReader.readAllRecords(csvFilePath, true);
            cache.clear();
            
            String[] headers = CSVReader.readHeader(csvFilePath);
            
            for (String[] record : records) {
                WithdrawalRequest request = mapToWithdrawalRequest(record, headers);
                cache.put(request.getWithdrawalId(), request);
            }
        } catch (Exception e) {
            // File might not exist yet, which is OK
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Maps CSV record to WithdrawalRequest object.
     */
    private WithdrawalRequest mapToWithdrawalRequest(String[] record, String[] headers) {
        WithdrawalRequest request = new WithdrawalRequest();
        
        // Helper method to get field by header name
        int withdrawalIdIdx = findHeaderIndex(headers, "WithdrawalID");
        int applicationIdIdx = findHeaderIndex(headers, "ApplicationID");
        int studentIdIdx = findHeaderIndex(headers, "StudentID");
        int internshipIdIdx = findHeaderIndex(headers, "InternshipID");
        int reasonIdx = findHeaderIndex(headers, "Reason");
        int statusIdx = findHeaderIndex(headers, "Status");
        int requestDateIdx = findHeaderIndex(headers, "RequestDate");
        int processedByIdx = findHeaderIndex(headers, "ProcessedBy");
        int processedDateIdx = findHeaderIndex(headers, "ProcessedDate");
        int staffCommentsIdx = findHeaderIndex(headers, "StaffComments");
        
        request.setWithdrawalId(getField(record, withdrawalIdIdx, ""));
        request.setApplicationId(getField(record, applicationIdIdx, ""));
        request.setStudentId(getField(record, studentIdIdx, ""));
        request.setInternshipId(getField(record, internshipIdIdx, ""));
        request.setReason(getField(record, reasonIdx, ""));
        
        String statusStr = getField(record, statusIdx, "PENDING");
        try {
            request.setStatus(ApprovalStatus.valueOf(statusStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            request.setStatus(ApprovalStatus.PENDING);
        }
        
        String requestDateStr = getField(record, requestDateIdx, "");
        if (!requestDateStr.isEmpty()) {
            try {
                request.setRequestDate(LocalDate.parse(requestDateStr, DATE_FORMATTER));
            } catch (Exception e) {
                request.setRequestDate(LocalDate.now());
            }
        } else {
            request.setRequestDate(LocalDate.now());
        }
        
        request.setProcessedBy(getField(record, processedByIdx, ""));
        
        // Parse processed date if present
        String processedDateStr = getField(record, processedDateIdx, "");
        if (!processedDateStr.isEmpty()) {
            try {
                request.setProcessedDate(java.time.LocalDateTime.parse(processedDateStr, 
                        java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (Exception e) {
                // If parsing fails, leave processedDate as null
            }
        }
        
        request.setStaffComments(getField(record, staffCommentsIdx, ""));
        
        return request;
    }
    
    /**
     * Helper method to find the index of a header.
     */
    private int findHeaderIndex(String[] headers, String headerName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(headerName)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Helper method to safely get a field from a record.
     */
    private String getField(String[] record, int index, String defaultValue) {
        if (index >= 0 && index < record.length) {
            return record[index] != null ? record[index] : defaultValue;
        }
        return defaultValue;
    }
    
    /**
     * Persists withdrawal requests to CSV file.
     */
    private void persistToCSV() {
        try {
            String[] headers = {"WithdrawalID", "ApplicationID", "StudentID", "InternshipID", 
                               "Reason", "Status", "RequestDate", "ProcessedBy", "ProcessedDate", "StaffComments"};
            
            List<String[]> records = cache.values().stream()
                    .map(this::requestToRecord)
                    .collect(Collectors.toList());
            
            CSVWriter.writeAllRecords(csvFilePath, headers, records, false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist withdrawal requests to CSV", e);
        }
    }
    
    /**
     * Converts WithdrawalRequest to CSV record.
     */
    private String[] requestToRecord(WithdrawalRequest request) {
        String requestDate = request.getRequestDate() != null ? 
                request.getRequestDate().format(DATE_FORMATTER) : "";
        
        String processedDate = "";
        if (request.getProcessedDate() != null) {
            processedDate = request.getProcessedDate().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        return new String[] {
            request.getWithdrawalId() != null ? request.getWithdrawalId() : "",
            request.getApplicationId() != null ? request.getApplicationId() : "",
            request.getStudentId() != null ? request.getStudentId() : "",
            request.getInternshipId() != null ? request.getInternshipId() : "",
            request.getReason() != null ? request.getReason() : "",
            request.getStatus() != null ? request.getStatus().name() : "PENDING",
            requestDate,
            request.getProcessedBy() != null ? request.getProcessedBy() : "",
            processedDate,
            request.getStaffComments() != null ? request.getStaffComments() : ""
        };
    }
}
