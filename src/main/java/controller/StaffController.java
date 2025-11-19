package controller;

import dto.ApiResponse;
import dto.ApprovalDecisionRequest;
import dto.ReportFilter;
import model.CareerCenterStaff;
import model.CompanyRepresentative;
import model.InternshipOpportunity;
import repository.CareerCenterStaffRepository;
import service.impl.StaffServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Career Center Staff operations.
 * Handles authorization and approval workflows.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:5173")
public class StaffController {
    
    @Autowired
    private StaffServiceImpl staffService;
    
    @Autowired
    private CareerCenterStaffRepository staffRepository;
    
    /**
     * Get all pending company representatives awaiting authorization.
     * 
     * @param staffId The staff member's ID
     * @return List of pending representatives
     */
    @GetMapping("/representatives/pending")
    public ResponseEntity<ApiResponse<List<CompanyRepresentative>>> getPendingRepresentatives(
            @RequestParam String staffId) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        List<CompanyRepresentative> pending = staffService.viewPendingRepresentatives(staff);
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending representatives retrieved"));
    }
    
    /**
     * Authorize or reject a company representative.
     * 
     * @param staffId The staff member's ID
     * @param representativeId The representative's ID
     * @param decision The authorization decision
     * @return Success response
     */
    @PostMapping("/representatives/{representativeId}/authorize")
    public ResponseEntity<ApiResponse<Void>> authorizeRepresentative(
            @RequestParam String staffId,
            @PathVariable String representativeId,
            @RequestBody ApprovalDecisionRequest decision) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        staffService.authorizeRepresentative(staff, representativeId, decision.isApprove());
        
        String message = decision.isApprove() ? 
                "Representative authorized successfully" : 
                "Representative rejected successfully";
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }
    
    /**
     * Get all pending internship opportunities awaiting approval.
     * 
     * @param staffId The staff member's ID
     * @return List of pending opportunities
     */
    @GetMapping("/internships/pending")
    public ResponseEntity<ApiResponse<List<InternshipOpportunity>>> getPendingOpportunities(
            @RequestParam String staffId) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        List<InternshipOpportunity> pending = staffService.viewPendingOpportunities(staff);
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending opportunities retrieved"));
    }
    
    /**
     * Approve or reject an internship opportunity.
     * 
     * @param staffId The staff member's ID
     * @param opportunityId The internship opportunity ID
     * @param decision The approval decision
     * @return Success response
     */
    @PostMapping("/internships/{opportunityId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveOpportunity(
            @RequestParam String staffId,
            @PathVariable String opportunityId,
            @RequestBody ApprovalDecisionRequest decision) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        staffService.approveOpportunity(staff, opportunityId, decision.isApprove());
        
        String message = decision.isApprove() ? 
                "Opportunity approved successfully" : 
                "Opportunity rejected successfully";
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }
    
    /**
     * Get all pending withdrawal requests.
     * 
     * @param staffId The staff member's ID
     * @return List of pending withdrawal requests
     */
    @GetMapping("/withdrawals/pending")
    public ResponseEntity<ApiResponse<List<dto.WithdrawalDetailsDTO>>> getPendingWithdrawals(
            @RequestParam String staffId) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        List<dto.WithdrawalDetailsDTO> pending = staffService.viewPendingWithdrawalsWithDetails(staff);
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending withdrawals retrieved"));
    }
    
    /**
     * Process a withdrawal request (approve or reject).
     * 
     * @param staffId The staff member's ID
     * @param withdrawalId The withdrawal request ID
     * @param decision The approval decision
     * @return Success response
     */
    @PostMapping("/withdrawals/{withdrawalId}/process")
    public ResponseEntity<ApiResponse<Void>> processWithdrawal(
            @RequestParam String staffId,
            @PathVariable String withdrawalId,
            @RequestBody ApprovalDecisionRequest decision) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        staffService.approveWithdrawal(staff, withdrawalId, decision.isApprove());
        
        String message = decision.isApprove() ? 
                "Withdrawal approved successfully" : 
                "Withdrawal rejected successfully";
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }
    
    /**
     * Generate a report with optional filtering.
     * 
     * @param staffId The staff member's ID
     * @param major Optional major filter
     * @param year Optional year filter
     * @param companyName Optional company name filter
     * @param startDate Optional start date filter
     * @param endDate Optional end date filter
     * @param applicationStatus Optional application status filter
     * @param internshipLevel Optional internship level filter
     * @param internshipStatus Optional internship status filter
     * @return Report data with statistics
     */
    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateReport(
            @RequestParam String staffId,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String applicationStatus,
            @RequestParam(required = false) String internshipLevel,
            @RequestParam(required = false) String internshipStatus) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        // Build filter from query parameters
        ReportFilter filter = new ReportFilter();
        filter.setMajor(major);
        filter.setYear(year);
        filter.setCompanyName(companyName);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setApplicationStatus(applicationStatus);
        filter.setInternshipLevel(internshipLevel);
        filter.setInternshipStatus(internshipStatus);
        
        Map<String, Object> report = staffService.generateReport(staff, filter);
        return ResponseEntity.ok(ApiResponse.success(report, "Report generated successfully"));
    }
    
    /**
     * Get list of all companies from approved company representatives.
     * 
     * @param staffId The staff member's ID
     * @return List of unique company names
     */
    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<List<String>>> getCompanies(@RequestParam String staffId) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        List<String> companies = staffService.getAllCompanies(staff);
        return ResponseEntity.ok(ApiResponse.success(companies, "Companies retrieved successfully"));
    }
    
    /**
     * Create a new student account.
     * 
     * @param staffId The staff member's ID
     * @param studentData The student data
     * @return Success response with created student
     */
    @PostMapping("/students/create")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createStudent(
            @RequestParam String staffId,
            @RequestBody Map<String, Object> studentData) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        // Create student using the service
        Map<String, Object> createdStudent = staffService.createStudent(staff, studentData);
        
        return ResponseEntity.ok(ApiResponse.success(createdStudent, "Student account created successfully"));
    }
    
    /**
     * Create a new company representative account.
     * 
     * @param staffId The staff member's ID
     * @param representativeData The representative data
     * @return Success response with created representative
     */
    @PostMapping("/representatives/create")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createRepresentative(
            @RequestParam String staffId,
            @RequestBody Map<String, Object> representativeData) {
        CareerCenterStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        // Create representative using the service
        Map<String, Object> createdRep = staffService.createRepresentative(staff, representativeData);
        
        return ResponseEntity.ok(ApiResponse.success(createdRep, "Company representative account created successfully"));
    }
}
