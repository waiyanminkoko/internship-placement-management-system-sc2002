package service;

import dto.ApplicationWithStudentDTO;
import model.Application;
import model.CompanyRepresentative;
import model.InternshipOpportunity;

import java.util.List;

/**
 * Service interface for company representative operations.
 * Handles internship opportunity creation and application management.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface CompanyRepresentativeService {
    
    /**
     * Creates a new internship opportunity.
     * Validates business rules:
     * - Representative is approved
     * - Maximum 5 opportunities per representative
     * - Valid opportunity data
     * 
     * @param rep The company representative creating the opportunity
     * @param opportunity The internship opportunity to create
     * @return The created InternshipOpportunity object
     * @throws exception.BusinessRuleException if business rules are violated
     * @throws exception.UnauthorizedException if representative not approved
     */
    InternshipOpportunity createOpportunity(CompanyRepresentative rep, InternshipOpportunity opportunity);
    
    /**
     * Views all applications for a specific internship opportunity with student details.
     * Only accessible by the representative who created the opportunity.
     * 
     * @param rep The company representative
     * @param opportunityId The ID of the internship opportunity
     * @return List of applications with student details for the opportunity
     * @throws exception.UnauthorizedException if representative doesn't own the opportunity
     * @throws exception.ResourceNotFoundException if opportunity not found
     */
    List<ApplicationWithStudentDTO> viewApplicationsForOpportunity(CompanyRepresentative rep, String opportunityId);
    
    /**
     * Approves or rejects a student application.
     * Validates:
     * - Application exists and is for rep's opportunity
     * - Application is in PENDING status
     * - Opportunity has available slots (for approval)
     * 
     * @param rep The company representative making the decision
     * @param applicationId The ID of the application
     * @param approve true to approve, false to reject
     * @throws exception.BusinessRuleException if rules are violated
     * @throws exception.UnauthorizedException if representative doesn't own the opportunity
     * @throws exception.ResourceNotFoundException if application not found
     */
    void handleApplicationDecision(CompanyRepresentative rep, String applicationId, boolean approve);
    
    /**
     * Toggles visibility of an internship opportunity.
     * Only approved opportunities can have visibility toggled.
     * 
     * @param rep The company representative
     * @param opportunityId The ID of the internship opportunity
     * @param visibility The new visibility setting
     * @throws exception.BusinessRuleException if opportunity not approved
     * @throws exception.UnauthorizedException if representative doesn't own the opportunity
     * @throws exception.ResourceNotFoundException if opportunity not found
     */
    void toggleVisibility(CompanyRepresentative rep, String opportunityId, boolean visibility);
    
    /**
     * Views all internship opportunities created by a representative.
     * 
     * @param rep The company representative
     * @return List of the representative's internship opportunities
     */
    List<InternshipOpportunity> viewMyOpportunities(CompanyRepresentative rep);
    
    /**
     * Updates an existing internship opportunity.
     * Can only update opportunities that are PENDING or REJECTED.
     * Rejected opportunities will be reset to PENDING status when edited and resubmitted.
     * Approved opportunities cannot be edited.
     * 
     * @param rep The company representative
     * @param opportunity The updated internship opportunity
     * @throws exception.BusinessRuleException if opportunity already approved
     * @throws exception.UnauthorizedException if representative doesn't own the opportunity
     * @throws exception.ResourceNotFoundException if opportunity not found
     */
    void updateOpportunity(CompanyRepresentative rep, InternshipOpportunity opportunity);
    
    /**
     * Deletes an internship opportunity.
     * Can delete opportunities that are:
     * - PENDING or REJECTED (before approval)
     * - APPROVED but past the closing date (no longer accepting applications)
     * 
     * @param rep The company representative
     * @param opportunityId The ID of the internship opportunity to delete
     * @throws exception.BusinessRuleException if opportunity is approved and still accepting applications
     * @throws exception.UnauthorizedException if representative doesn't own the opportunity
     * @throws exception.ResourceNotFoundException if opportunity not found
     */
    void deleteOpportunity(CompanyRepresentative rep, String opportunityId);
}
