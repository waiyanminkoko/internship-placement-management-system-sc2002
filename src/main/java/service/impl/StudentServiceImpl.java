package service.impl;

import dto.ApplyInternshipDTO;
import enums.ApplicationStatus;
import enums.InternshipStatus;
import exception.BusinessRuleException;
import exception.ResourceNotFoundException;
import model.Application;
import model.InternshipOpportunity;
import model.Student;
import model.WithdrawalRequest;
import repository.ApplicationRepository;
import repository.InternshipOpportunityRepository;
import repository.StudentRepository;
import util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of StudentService interface.
 * Handles all student-related business logic for internship applications.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Service
public class StudentServiceImpl {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private InternshipOpportunityRepository internshipRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private WithdrawalRequestService withdrawalRequestService;
    
    public List<InternshipOpportunity> viewAvailableOpportunities(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        // Get all visible and approved opportunities
        List<InternshipOpportunity> allOpportunities = internshipRepository.findAllVisible();
        
        // Filter by student eligibility
        return allOpportunities.stream()
                .filter(opp -> opp.isEligibleForStudent(student))
                .filter(InternshipOpportunity::isApplicationOpen)
                .filter(InternshipOpportunity::hasAvailableSlots)
                .collect(Collectors.toList());
    }
    
    public Application applyForOpportunity(Student student, String opportunityId) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (opportunityId == null || opportunityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Opportunity ID cannot be empty");
        }
        
        // Check if student has already accepted a placement
        if (student.isHasAcceptedPlacement()) {
            throw new BusinessRuleException("Cannot apply - you have already accepted a placement");
        }
        
        // Check application limit (maximum 3 concurrent applications)
        long activeCount = applicationRepository.countActiveByStudentId(student.getUserId());
        if (activeCount >= 3) {
            throw new BusinessRuleException("Maximum 3 concurrent applications allowed");
        }
        
        // Find the internship opportunity
        InternshipOpportunity opportunity = internshipRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found: " + opportunityId));
        
        // Validate opportunity status
        if (opportunity.getStatus() != InternshipStatus.APPROVED) {
            throw new BusinessRuleException("This internship opportunity is not available for applications");
        }
        
        // Check if application period is open
        if (!opportunity.isApplicationOpen()) {
            throw new BusinessRuleException("Application period is not open for this opportunity");
        }
        
        // Check if slots are available
        if (!opportunity.hasAvailableSlots()) {
            throw new BusinessRuleException("No available slots for this opportunity");
        }
        
        // Check student eligibility (only check year level, not major)
        if (!opportunity.isEligibleForStudent(student)) {
            if (student.isJunior() && opportunity.getLevel() != enums.InternshipLevel.BASIC) {
                throw new BusinessRuleException("Junior students (Year 1-2) can only apply for BASIC level internships");
            }
            // Note: Major restriction removed - all students can apply regardless of major
        }
        
        // Check if student has already applied for this opportunity
        List<Application> existingApplications = applicationRepository.findByStudentId(student.getUserId());
        boolean alreadyApplied = existingApplications.stream()
                .anyMatch(app -> app.getInternshipId().equals(opportunityId) && app.isActive());
        
        if (alreadyApplied) {
            throw new BusinessRuleException("You have already applied for this opportunity");
        }
        
        // Create the application
        Application application = new Application();
        application.setApplicationId(IdGenerator.generateApplicationId());
        application.setStudentId(student.getUserId());
        application.setInternshipId(opportunityId);
        application.setStatus(ApplicationStatus.PENDING);
        application.setApplicationDate(LocalDate.now());
        application.setPlacementAccepted(false);
        
        // Save the application
        application = applicationRepository.save(application);
        
        // Update student's application list
        student.addApplication(application.getApplicationId());
        studentRepository.save(student);
        
        return application;
    }
    
    public List<ApplyInternshipDTO> viewApplications(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        List<Application> applications = applicationRepository.findByStudentId(student.getUserId());
        
        // Enrich applications with internship details
        return applications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Converts an Application to ApplyInternshipDTO with enriched internship details.
     * 
     * @param application the application to convert
     * @return ApplyInternshipDTO with internship details
     */
    private ApplyInternshipDTO convertToDTO(Application application) {
        InternshipOpportunity internship = internshipRepository.findById(application.getInternshipId())
            .orElse(null);
        
        return ApplyInternshipDTO.builder()
            .applicationId(application.getApplicationId())
            .studentId(application.getStudentId())
            .internshipId(application.getInternshipId())
            .internshipTitle(internship != null ? internship.getTitle() : "N/A")
            .companyName(internship != null ? internship.getCompanyName() : "N/A")
            .status(application.getStatus())
            .submissionDate(application.getSubmissionDate())
            .statusUpdateDate(application.getStatusUpdateDate())
            .placementAccepted(application.isPlacementAccepted())
            .placementAcceptanceDate(application.getPlacementAcceptanceDate())
            .representativeComments(application.getRepresentativeComments())
            .staffComments(application.getRepresentativeComments()) // Using same field for now
            .build();
    }
    
    public void acceptPlacement(Student student, String applicationId) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID cannot be empty");
        }
        
        // Check if student has already accepted a placement
        if (student.isHasAcceptedPlacement()) {
            throw new BusinessRuleException("You have already accepted a placement");
        }
        
        // Find the application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));
        
        // Verify application belongs to student
        if (!application.getStudentId().equals(student.getUserId())) {
            throw new BusinessRuleException("This application does not belong to you");
        }
        
        // Verify application is successful
        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            throw new BusinessRuleException("Can only accept SUCCESSFUL applications");
        }
        
        // Check if placement already accepted
        if (application.isPlacementAccepted()) {
            throw new BusinessRuleException("This placement has already been accepted");
        }
        
        // Accept the placement
        application.setPlacementAccepted(true);
        applicationRepository.save(application);
        
        // Update student record
        student.acceptPlacement(applicationId);
        studentRepository.save(student);
        
        // Update internship filled slots
        InternshipOpportunity opportunity = internshipRepository.findById(application.getInternshipId())
                .orElse(null);
        if (opportunity != null) {
            opportunity.incrementFilledSlots();
            internshipRepository.save(opportunity);
        }
        
        // Withdraw all other non-final applications
        // Only withdraw PENDING or SUCCESSFUL (not accepted) applications
        // REJECTED applications should remain REJECTED (they are already in a final state)
        List<Application> otherApplications = applicationRepository.findByStudentId(student.getUserId());
        for (Application other : otherApplications) {
            if (!other.getApplicationId().equals(applicationId)) {
                // Only withdraw applications that can be withdrawn (PENDING or SUCCESSFUL without acceptance)
                if (other.getStatus() == ApplicationStatus.PENDING || 
                    (other.getStatus() == ApplicationStatus.SUCCESSFUL && !other.isPlacementAccepted())) {
                    other.setStatus(ApplicationStatus.WITHDRAWN);
                    applicationRepository.save(other);
                }
                // REJECTED and already WITHDRAWN applications remain unchanged
            }
        }
    }
    
    public WithdrawalRequest requestWithdrawal(Student student, String applicationId, String reason) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID cannot be empty");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Withdrawal reason cannot be empty");
        }
        
        // Find the application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));
        
        // Verify application belongs to student
        if (!application.getStudentId().equals(student.getUserId())) {
            throw new BusinessRuleException("This application does not belong to you");
        }
        
        // Check if application can be withdrawn
        if (!application.canBeWithdrawn()) {
            throw new BusinessRuleException("This application cannot be withdrawn. " +
                    "Only PENDING applications or SUCCESSFUL applications before acceptance can be withdrawn.");
        }
        
        // Create withdrawal request using the withdrawal service
        return withdrawalRequestService.createWithdrawalRequest(student, application, reason);
    }
    
    public long getActiveApplicationCount(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        return applicationRepository.countActiveByStudentId(student.getUserId());
    }
    
    public List<WithdrawalRequest> getWithdrawalRequests(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        // Get all withdrawal requests for this student
        return withdrawalRequestService.findByStudentId(student.getUserId());
    }
    
    public WithdrawalRequest updateWithdrawalRequest(Student student, String withdrawalId, String newReason) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (withdrawalId == null || withdrawalId.trim().isEmpty()) {
            throw new IllegalArgumentException("Withdrawal ID cannot be empty");
        }
        if (newReason == null || newReason.trim().isEmpty()) {
            throw new IllegalArgumentException("Withdrawal reason cannot be empty");
        }
        
        // Find the withdrawal request
        WithdrawalRequest request = withdrawalRequestService.findById(withdrawalId)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal request not found: " + withdrawalId));
        
        // Verify ownership
        if (!request.getStudentId().equals(student.getUserId())) {
            throw new BusinessRuleException("This withdrawal request does not belong to you");
        }
        
        // Only pending requests can be updated
        if (!request.isPending()) {
            throw new BusinessRuleException("Only pending withdrawal requests can be updated");
        }
        
        // Update the reason
        request.setReason(newReason);
        
        // Save and return
        return withdrawalRequestService.save(request);
    }
    
    public void cancelWithdrawalRequest(Student student, String withdrawalId, String cancellationReason) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (withdrawalId == null || withdrawalId.trim().isEmpty()) {
            throw new IllegalArgumentException("Withdrawal ID cannot be empty");
        }
        if (cancellationReason == null || cancellationReason.trim().isEmpty()) {
            throw new IllegalArgumentException("Cancellation reason cannot be empty");
        }
        
        // Find the withdrawal request
        WithdrawalRequest request = withdrawalRequestService.findById(withdrawalId)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal request not found: " + withdrawalId));
        
        // Verify ownership
        if (!request.getStudentId().equals(student.getUserId())) {
            throw new BusinessRuleException("This withdrawal request does not belong to you");
        }
        
        // Only pending requests can be cancelled
        if (!request.isPending()) {
            throw new BusinessRuleException("Only pending withdrawal requests can be cancelled");
        }
        
        // Update status to CANCELLED and add cancellation reason
        request.setStatus(enums.ApprovalStatus.CANCELLED);
        request.setStaffComments("Cancelled by student. Reason: " + cancellationReason);
        request.setProcessedBy(student.getUserId());
        request.setProcessedDate(java.time.LocalDateTime.now());
        
        // Save the updated request
        withdrawalRequestService.save(request);
    }
}
