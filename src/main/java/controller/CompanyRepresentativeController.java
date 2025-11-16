package controller;

import dto.ApiResponse;
import dto.ApplicationWithStudentDTO;
import dto.ApprovalDecisionRequest;
import dto.CreateInternshipRequest;
import model.Application;
import model.CompanyRepresentative;
import model.InternshipOpportunity;
import repository.CompanyRepresentativeRepository;
import service.CompanyRepresentativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for company representative operations.
 * Handles internship opportunity creation and application management.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@RestController
@RequestMapping("/api/representatives")
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyRepresentativeController {
    
    @Autowired
    private CompanyRepresentativeService representativeService;
    
    @Autowired
    private CompanyRepresentativeRepository representativeRepository;
    
    /**
     * Create a new internship opportunity.
     * 
     * @param repId The representative's ID
     * @param request The internship creation request
     * @return The created internship opportunity
     */
    @PostMapping("/internships")
    public ResponseEntity<ApiResponse<InternshipOpportunity>> createInternship(
            @RequestParam String repId,
            @RequestBody CreateInternshipRequest request) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        // Create InternshipOpportunity from request
        InternshipOpportunity opportunity = new InternshipOpportunity();
        opportunity.setTitle(request.getTitle());
        opportunity.setDescription(request.getDescription());
        opportunity.setLevel(request.getLevel());
        opportunity.setPreferredMajor(request.getPreferredMajor());
        opportunity.setOpeningDate(request.getOpeningDate());
        opportunity.setClosingDate(request.getClosingDate());
        opportunity.setStartDate(request.getStartDate());
        opportunity.setEndDate(request.getEndDate());
        opportunity.setSlots(request.getSlots());
        
        InternshipOpportunity created = representativeService.createOpportunity(rep, opportunity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Internship opportunity created successfully"));
    }
    
    /**
     * Get all internship opportunities created by the representative.
     * 
     * @param repId The representative's ID
     * @return List of representative's internship opportunities
     */
    @GetMapping("/internships")
    public ResponseEntity<ApiResponse<List<InternshipOpportunity>>> getMyInternships(
            @RequestParam String repId) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        List<InternshipOpportunity> opportunities = representativeService.viewMyOpportunities(rep);
        return ResponseEntity.ok(ApiResponse.success(opportunities, "Internships retrieved"));
    }
    
    /**
     * Update an existing internship opportunity (only if pending).
     * 
     * @param repId The representative's ID
     * @param opportunityId The internship opportunity ID
     * @param request The updated internship data
     * @return Success response
     */
    @PutMapping("/internships/{opportunityId}")
    public ResponseEntity<ApiResponse<Void>> updateInternship(
            @RequestParam String repId,
            @PathVariable String opportunityId,
            @RequestBody CreateInternshipRequest request) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        // Create updated opportunity object
        InternshipOpportunity opportunity = new InternshipOpportunity();
        opportunity.setInternshipId(opportunityId);
        opportunity.setTitle(request.getTitle());
        opportunity.setDescription(request.getDescription());
        opportunity.setLevel(request.getLevel());
        opportunity.setPreferredMajor(request.getPreferredMajor());
        opportunity.setOpeningDate(request.getOpeningDate());
        opportunity.setClosingDate(request.getClosingDate());
        opportunity.setStartDate(request.getStartDate());
        opportunity.setEndDate(request.getEndDate());
        opportunity.setSlots(request.getSlots());
        
        representativeService.updateOpportunity(rep, opportunity);
        return ResponseEntity.ok(ApiResponse.success(null, "Internship updated successfully"));
    }
    
    /**
     * Delete an internship opportunity.
     * Can only delete PENDING or REJECTED opportunities.
     * 
     * @param repId The representative's ID
     * @param opportunityId The internship opportunity ID
     * @return Success response
     */
    @DeleteMapping("/internships/{opportunityId}")
    public ResponseEntity<ApiResponse<Void>> deleteOpportunity(
            @RequestParam String repId,
            @PathVariable String opportunityId) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        representativeService.deleteOpportunity(rep, opportunityId);
        return ResponseEntity.ok(ApiResponse.success(null, "Internship deleted successfully"));
    }
    
    /**
     * Get all applications for a specific internship opportunity with student details.
     * 
     * @param repId The representative's ID
     * @param opportunityId The internship opportunity ID
     * @return List of applications with student details for the opportunity
     */
    @GetMapping("/internships/{opportunityId}/applications")
    public ResponseEntity<ApiResponse<List<ApplicationWithStudentDTO>>> getApplicationsForOpportunity(
            @RequestParam String repId,
            @PathVariable String opportunityId) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        List<ApplicationWithStudentDTO> applications = representativeService.viewApplicationsForOpportunity(rep, opportunityId);
        return ResponseEntity.ok(ApiResponse.success(applications, "Applications retrieved"));
    }
    
    /**
     * Approve an application.
     * 
     * @param repId The representative's ID
     * @param applicationId The application ID
     * @param decision The approval decision
     * @return Success response
     */
    @PostMapping("/applications/{applicationId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveApplication(
            @RequestParam String repId,
            @PathVariable String applicationId,
            @RequestBody ApprovalDecisionRequest decision) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        representativeService.handleApplicationDecision(rep, applicationId, decision.isApprove());
        
        String message = decision.isApprove() ? 
                "Application approved successfully" : 
                "Application rejected successfully";
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }
    
    /**
     * Reject an application.
     * 
     * @param repId The representative's ID
     * @param applicationId The application ID
     * @return Success response
     */
    @PostMapping("/applications/{applicationId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectApplication(
            @RequestParam String repId,
            @PathVariable String applicationId) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        representativeService.handleApplicationDecision(rep, applicationId, false);
        return ResponseEntity.ok(ApiResponse.success(null, "Application rejected successfully"));
    }
    
    /**
     * Toggle visibility of an internship opportunity.
     * 
     * @param repId The representative's ID
     * @param opportunityId The internship opportunity ID
     * @param visibility The new visibility status
     * @return Success response
     */
    @PatchMapping("/internships/{opportunityId}/visibility")
    public ResponseEntity<ApiResponse<Void>> toggleVisibility(
            @RequestParam String repId,
            @PathVariable String opportunityId,
            @RequestParam boolean visibility) {
        CompanyRepresentative rep = representativeRepository.findById(repId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));
        
        representativeService.toggleVisibility(rep, opportunityId, visibility);
        return ResponseEntity.ok(ApiResponse.success(null, "Visibility updated successfully"));
    }
}
