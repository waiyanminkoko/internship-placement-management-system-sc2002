package controller;

import dto.ApiResponse;
import dto.ApplyInternshipDTO;
import dto.ApplyInternshipRequest;
import dto.WithdrawalRequestDTO;
import model.Application;
import model.InternshipOpportunity;
import model.Student;
import model.WithdrawalRequest;
import repository.StudentRepository;
import service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for student operations.
 * Handles internship browsing, application submission, and withdrawal requests.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    
    @Autowired
    private StudentServiceImpl studentService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all available internship opportunities for a student.
     * 
     * @param studentId The student's ID
     * @return List of available internships
     */
    @GetMapping("/internships")
    public ResponseEntity<ApiResponse<List<InternshipOpportunity>>> getAvailableInternships(
            @RequestParam String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        List<InternshipOpportunity> opportunities = studentService.viewAvailableOpportunities(student);
        return ResponseEntity.ok(ApiResponse.success(opportunities, "Available internships retrieved"));
    }
    
    /**
     * Get all applications for a student.
     * 
     * @param studentId The student's ID
     * @return List of student's applications with internship details
     */
    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<ApplyInternshipDTO>>> getMyApplications(
            @RequestParam String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        List<ApplyInternshipDTO> applications = studentService.viewApplications(student);
        return ResponseEntity.ok(ApiResponse.success(applications, "Applications retrieved"));
    }
    
    /**
     * Apply for an internship opportunity.
     * 
     * @param studentId The student's ID
     * @param request The application request containing internship ID
     * @return The created application
     */
    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<Application>> applyForInternship(
            @RequestParam String studentId,
            @RequestBody ApplyInternshipRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Application application = studentService.applyForOpportunity(student, request.getInternshipId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(application, "Application submitted successfully"));
    }
    
    /**
     * Accept a placement offer.
     * 
     * @param studentId The student's ID
     * @param applicationId The application ID to accept
     * @return Success response
     */
    @PostMapping("/applications/{applicationId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptPlacement(
            @RequestParam String studentId,
            @PathVariable String applicationId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        studentService.acceptPlacement(student, applicationId);
        return ResponseEntity.ok(ApiResponse.success(null, "Placement accepted successfully"));
    }
    
    /**
     * Request withdrawal from an application.
     * 
     * @param studentId The student's ID
     * @param request The withdrawal request containing application ID and reason
     * @return The created withdrawal request
     */
    @PostMapping("/applications/withdraw")
    public ResponseEntity<ApiResponse<WithdrawalRequest>> requestWithdrawal(
            @RequestParam String studentId,
            @RequestBody WithdrawalRequestDTO request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        WithdrawalRequest withdrawal = studentService.requestWithdrawal(
                student, request.getApplicationId(), request.getReason());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(withdrawal, "Withdrawal request submitted successfully"));
    }
    
    /**
     * Get student profile information.
     * 
     * @param studentId The student's ID
     * @return Student profile data
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Student>> getProfile(@RequestParam String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return ResponseEntity.ok(ApiResponse.success(student, "Profile retrieved"));
    }
    
    /**
     * Get active application count for a student.
     * 
     * @param studentId The student's ID
     * @return Count of active applications
     */
    @GetMapping("/applications/count")
    public ResponseEntity<ApiResponse<Long>> getActiveApplicationCount(
            @RequestParam String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        long count = studentService.getActiveApplicationCount(student);
        return ResponseEntity.ok(ApiResponse.success(count, "Active application count"));
    }
    
    /**
     * Get all withdrawal requests for a student.
     * 
     * @param studentId The student's ID
     * @return List of withdrawal requests
     */
    @GetMapping("/withdrawal-requests")
    public ResponseEntity<ApiResponse<List<WithdrawalRequest>>> getWithdrawalRequests(
            @RequestParam String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        List<WithdrawalRequest> requests = studentService.getWithdrawalRequests(student);
        return ResponseEntity.ok(ApiResponse.success(requests, "Withdrawal requests retrieved"));
    }
    
    /**
     * Update a pending withdrawal request.
     * 
     * @param studentId The student's ID
     * @param withdrawalId The withdrawal request ID
     * @param request The updated withdrawal request data
     * @return Updated withdrawal request
     */
    @PutMapping("/withdrawal-requests/{withdrawalId}")
    public ResponseEntity<ApiResponse<WithdrawalRequest>> updateWithdrawalRequest(
            @RequestParam String studentId,
            @PathVariable String withdrawalId,
            @RequestBody WithdrawalRequestDTO request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        WithdrawalRequest updated = studentService.updateWithdrawalRequest(
                student, withdrawalId, request.getReason());
        
        return ResponseEntity.ok(ApiResponse.success(updated, "Withdrawal request updated successfully"));
    }
    
    /**
     * Cancel a pending withdrawal request.
     * 
     * @param studentId The student's ID
     * @param withdrawalId The withdrawal request ID
     * @param request The cancellation reason
     * @return Success message
     */
    @DeleteMapping("/withdrawal-requests/{withdrawalId}")
    public ResponseEntity<ApiResponse<String>> cancelWithdrawalRequest(
            @RequestParam String studentId,
            @PathVariable String withdrawalId,
            @RequestBody WithdrawalRequestDTO request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        studentService.cancelWithdrawalRequest(student, withdrawalId, request.getReason());
        
        return ResponseEntity.ok(ApiResponse.success(null, "Withdrawal request cancelled successfully"));
    }
}
