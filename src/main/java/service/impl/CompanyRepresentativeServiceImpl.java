package service.impl;

import dto.ApplicationWithStudentDTO;
import enums.ApplicationStatus;
import enums.ApprovalStatus;
import enums.InternshipStatus;
import exception.BusinessRuleException;
import exception.ResourceNotFoundException;
import exception.UnauthorizedException;
import model.Application;
import model.CompanyRepresentative;
import model.InternshipOpportunity;
import model.Student;
import repository.ApplicationRepository;
import repository.InternshipOpportunityRepository;
import repository.StudentRepository;
import service.CompanyRepresentativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CompanyRepresentativeService.
 * Handles business logic for company representatives managing internship opportunities.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Service
public class CompanyRepresentativeServiceImpl implements CompanyRepresentativeService {

    @Autowired
    private InternshipOpportunityRepository opportunityRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public InternshipOpportunity createOpportunity(CompanyRepresentative representative, 
                                                   InternshipOpportunity opportunity) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        if (opportunity == null) {
            throw new IllegalArgumentException("Opportunity cannot be null");
        }
        
        // Check if representative is approved
        if (representative.getStatus() != ApprovalStatus.APPROVED) {
            throw new UnauthorizedException("Only approved representatives can create opportunities");
        }
        
        // Check 5-opportunity limit
        List<InternshipOpportunity> existingOpportunities = opportunityRepository.findAll().stream()
                .filter(opp -> opp.getCreatedBy() != null && opp.getCreatedBy().equals(representative.getEmail()))
                .toList();
        
        if (existingOpportunities.size() >= 5) {
            throw new BusinessRuleException("Representatives can only create up to 5 internship opportunities");
        }
        
        // Validate slots
        if (opportunity.getSlots() > 10) {
            throw new BusinessRuleException("Maximum 10 slots per internship");
        }
        
        // Validate dates
        if (opportunity.getClosingDate().isBefore(opportunity.getOpeningDate())) {
            throw new BusinessRuleException("Closing date must be after opening date");
        }
        
        // Set creation metadata
        opportunity.setCreatedBy(representative.getEmail());
        opportunity.setCompanyName(representative.getCompanyName());
        opportunity.setCompanyRepEmail(representative.getEmail());
        opportunity.setStatus(InternshipStatus.PENDING); // Pending staff approval
        opportunity.setVisibility(false); // Not visible until approved
        opportunity.setFilledSlots(0);
        
        return opportunityRepository.save(opportunity);
    }

    @Override
    public List<ApplicationWithStudentDTO> viewApplicationsForOpportunity(CompanyRepresentative representative, 
                                                            String opportunityId) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        if (opportunityId == null || opportunityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Opportunity ID cannot be empty");
        }
        
        // Verify ownership
        InternshipOpportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found"));
        
        if (!opportunity.getCreatedBy().equals(representative.getEmail())) {
            throw new UnauthorizedException("You can only view applications for your own opportunities");
        }
        
        List<Application> applications = applicationRepository.findByInternshipId(opportunityId);
        
        // Enrich applications with student details
        return applications.stream()
            .map(this::convertToApplicationWithStudentDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Converts an Application to ApplicationWithStudentDTO with student details.
     * 
     * @param application the application to convert
     * @return ApplicationWithStudentDTO with student details
     */
    private ApplicationWithStudentDTO convertToApplicationWithStudentDTO(Application application) {
        Student student = studentRepository.findById(application.getStudentId())
            .orElse(null);
        
        return ApplicationWithStudentDTO.builder()
            .applicationId(application.getApplicationId())
            .studentId(application.getStudentId())
            .studentName(student != null ? student.getName() : "Unknown")
            .studentMajor(student != null ? student.getMajor() : "N/A")
            .studentYear(student != null ? student.getYear() : 0)
            .studentEmail(student != null ? student.getEmail() : "N/A")
            .internshipId(application.getInternshipId())
            .status(application.getStatus())
            .submissionDate(application.getSubmissionDate())
            .statusUpdateDate(application.getStatusUpdateDate())
            .placementAccepted(application.isPlacementAccepted())
            .placementAcceptanceDate(application.getPlacementAcceptanceDate())
            .representativeComments(application.getRepresentativeComments())
            .build();
    }

    @Override
    public void handleApplicationDecision(CompanyRepresentative representative, 
                                         String applicationId, boolean approve) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID cannot be empty");
        }
        
        // Find the application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        // Verify ownership
        InternshipOpportunity opportunity = opportunityRepository.findById(application.getInternshipId())
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found"));
        
        if (!opportunity.getCreatedBy().equals(representative.getEmail())) {
            throw new UnauthorizedException("You can only handle applications for your own opportunities");
        }
        
        // Check application status
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleException("Only pending applications can be processed");
        }
        
        // Check if opportunity has available slots
        if (opportunity.getFilledSlots() >= opportunity.getSlots()) {
            throw new BusinessRuleException("No available slots for this internship");
        }
        
        if (approve) {
            // Approve the application
            application.setStatus(ApplicationStatus.SUCCESSFUL);
            application.setStatusUpdateDate(java.time.LocalDateTime.now());
            applicationRepository.save(application);
            
        } else {
            // Reject the application
            application.setStatus(ApplicationStatus.UNSUCCESSFUL);
            application.setStatusUpdateDate(java.time.LocalDateTime.now());
            applicationRepository.save(application);
        }
    }

    @Override
    public void toggleVisibility(CompanyRepresentative representative, String opportunityId, boolean visibility) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        if (opportunityId == null || opportunityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Opportunity ID cannot be empty");
        }
        
        // Find the opportunity
        InternshipOpportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found"));
        
        // Verify ownership
        if (!opportunity.getCreatedBy().equals(representative.getEmail())) {
            throw new UnauthorizedException("You can only modify your own opportunities");
        }
        
        // Check if opportunity is approved (status should be APPROVED not PENDING)
        if (opportunity.getStatus() == InternshipStatus.PENDING) {
            throw new BusinessRuleException("Only approved opportunities can have their visibility toggled");
        }
        
        // Set visibility
        opportunity.setVisibility(visibility);
        opportunityRepository.save(opportunity);
    }

    @Override
    public List<InternshipOpportunity> viewMyOpportunities(CompanyRepresentative representative) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        
        List<InternshipOpportunity> opportunities = opportunityRepository.findAll().stream()
                .filter(opp -> opp.getCreatedBy() != null && opp.getCreatedBy().equals(representative.getEmail()))
                .toList();
        
        // Calculate and set pending application count for each opportunity
        for (InternshipOpportunity opp : opportunities) {
            long pendingCount = applicationRepository.findByInternshipId(opp.getInternshipId()).stream()
                    .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                    .count();
            opp.setPendingApplicationCount((int) pendingCount);
        }
        
        return opportunities;
    }

    @Override
    public void updateOpportunity(CompanyRepresentative representative, InternshipOpportunity opportunity) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        if (opportunity == null) {
            throw new IllegalArgumentException("Opportunity cannot be null");
        }
        
        // Find the existing opportunity
        InternshipOpportunity existing = opportunityRepository.findById(opportunity.getInternshipId())
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found"));
        
        // Verify ownership
        if (!existing.getCreatedBy().equals(representative.getEmail())) {
            throw new UnauthorizedException("You can only update your own opportunities");
        }
        
        // Check if opportunity is already approved - cannot update after approval
        // Rejected opportunities can be edited and resubmitted
        if (existing.getStatus() == InternshipStatus.APPROVED) {
            throw new BusinessRuleException("Cannot edit opportunities after they have been approved by Career Center Staff");
        }
        
        // If opportunity was rejected, reset status to PENDING when edited
        if (existing.getStatus() == InternshipStatus.REJECTED) {
            existing.setStatus(InternshipStatus.PENDING);
        }
        
        // Update fields from the provided opportunity
        existing.setTitle(opportunity.getTitle());
        existing.setDescription(opportunity.getDescription());
        existing.setLevel(opportunity.getLevel());
        existing.setPreferredMajor(opportunity.getPreferredMajor());
        existing.setOpeningDate(opportunity.getOpeningDate());
        existing.setClosingDate(opportunity.getClosingDate());
        existing.setSlots(opportunity.getSlots());
        
        opportunityRepository.save(existing);
    }

    @Override
    public void deleteOpportunity(CompanyRepresentative representative, String opportunityId) {
        if (representative == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }
        if (opportunityId == null || opportunityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Opportunity ID cannot be null or empty");
        }
        
        // Find the existing opportunity
        InternshipOpportunity existing = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found"));
        
        // Verify ownership
        if (!existing.getCreatedBy().equals(representative.getEmail())) {
            throw new UnauthorizedException("You can only delete your own opportunities");
        }
        
        // Check if opportunity is approved - cannot delete after approval
        if (existing.getStatus() == InternshipStatus.APPROVED) {
            throw new BusinessRuleException("Cannot delete opportunities after they have been approved by Career Center Staff");
        }
        
        // Delete the opportunity
        opportunityRepository.deleteById(opportunityId);
    }
}
