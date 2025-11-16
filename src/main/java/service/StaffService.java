/*
 * StaffService.java
 * Contains the key logic behind the services that Career Centre Staff is expected to do. These functionalities are: 
 * 1) Authorising Representatives 
 * 2) Revoking authority of representatives 
 * 3) Approving Internship Entries
 * 4) Approving Withdrawal Request
 * 5) Generating Report
 * 
 * Last amended: 11 November 2025
 * Person amended: Charles 
 */

package service;

import java.util.List;
import java.util.Map;

import dto.ApprovalDecisionRequest;
import model.CareerCenterStaff;
import model.CompanyRepresentative;
import model.InternshipOpportunity;
import model.WithdrawalRequest;
import dto.ReportFilterRequest;

/**
 * Interface for Career Center Staff services.
 */
public interface StaffService {

    /**
     * Authorize or reject a company representative.
     * 
     * @param staff the staff performing authorisation 
     * @param representativeId the ID of the representative to authorize/reject
     * @param approve true to approve, false to reject
     */
    void authorizeRepresentative(CareerCenterStaff staff, String representativeId, boolean approve);

    /**
     * Authorize or reject a company representative.
     * 
     * @param staff the staff performing authorisation 
     * @param applicant the company representative applying for authorisation 
     * @param request DTO containing the approval decision and optional reason 
     * @return message indicating result of authorisation
     */
    default String authorizeRepresentative(CareerCenterStaff staff, CompanyRepresentative applicant, ApprovalDecisionRequest request) {
        if (request.isApprove()) {
            applicant.authorize();
            return String.format("Representative %s from %s approved by %s.",
                    applicant.getName(), applicant.getCompanyName(), staff.getName());
        } else {
            return String.format("Representative %s from %s rejected by %s. Reason: %s",
                    applicant.getName(), applicant.getCompanyName(), staff.getName(),
                    request.getReason() != null ? request.getReason() : "No reason provided");
        }
    }

    /**
     * Approve or reject an internship opportunity.
     * 
     * @param staff the staff performing the approval
     * @param opportunityId the ID of the internship opportunity
     * @param approve true to approve, false to reject
     */
    void approveOpportunity(CareerCenterStaff staff, String opportunityId, boolean approve);

    /**
     * Approve or reject a student's withdrawal request.
     * 
     * @param staff the staff processing the withdrawal
     * @param withdrawalId the ID of the withdrawal request
     * @param approve true to approve, false to reject
     */
    void approveWithdrawal(CareerCenterStaff staff, String withdrawalId, boolean approve);

    /**
     * View all pending company representatives awaiting authorization.
     * 
     * @param staff the staff member requesting the list
     * @return list of pending representatives
     */
    List<CompanyRepresentative> viewPendingRepresentatives(CareerCenterStaff staff);

    /**
     * View all pending internship opportunities awaiting approval.
     * 
     * @param staff the staff member requesting the list
     * @return list of pending opportunities
     */
    List<InternshipOpportunity> viewPendingOpportunities(CareerCenterStaff staff);

    /**
     * View all pending withdrawal requests.
     * 
     * @param staff the staff member requesting the list
     * @return list of pending withdrawal requests
     */
    List<WithdrawalRequest> viewPendingWithdrawals(CareerCenterStaff staff);

    /**
     * Create a new student account.
     * 
     * @param staff the staff member creating the student
     * @param studentData the student data
     * @return created student data
     */
    Map<String, Object> createStudent(CareerCenterStaff staff, Map<String, Object> studentData);

    /**
     * Create a new company representative account.
     * 
     * @param staff the staff member creating the representative
     * @param representativeData the representative data
     * @return created representative data
     */
    Map<String, Object> createRepresentative(CareerCenterStaff staff, Map<String, Object> representativeData);

    /**
     * Get all unique company names from approved representatives.
     * 
     * @param staff the staff member requesting the list
     * @return list of unique company names
     */
    List<String> getAllCompanies(CareerCenterStaff staff);

    /**
     * View pending withdrawal requests with enriched details.
     * 
     * @param staff the staff member requesting the list
     * @return list of withdrawal details with student and internship information
     */
    List<dto.WithdrawalDetailsDTO> viewPendingWithdrawalsWithDetails(CareerCenterStaff staff);

    /**
     * Revoke authorization of an existing representative.
     * 
     * @param staff the staff performing authorisation 
     * @param representatives the list of all company representatives
     * @param repEmail the email of the representative to revoke
     * @param confirm whether the revocation is confirmed
     * @return message indicating outcome of revocation 
     */
    default String revokeAuthorization(CareerCenterStaff staff, List<CompanyRepresentative> representatives, String repEmail, boolean confirm) {
        for (CompanyRepresentative rep : representatives) {
            if (rep.getEmail().equalsIgnoreCase(repEmail)) {
                if (confirm) {
                    rep.revokeAuthorization();
                    return String.format("Authorization revoked for %s (%s) by %s.",
                            rep.getName(), rep.getCompanyName(), staff.getName());
                } else {
                    return String.format("Revocation cancelled for %s.", rep.getName());
                }
            }
        }
        return String.format("Representative with email %s not found.", repEmail);
    }

    /**
     * Approve or reject an internship opportunity.
     * @param staff the staff performing authorisation 
     * @param internship the internship opportunity under review
     * @param request contains the approval decision and optional reason
     * @return a message indicating the approval result
     */
    default String approveInternship(CareerCenterStaff staff, InternshipOpportunity internship, ApprovalDecisionRequest request) {
        if (request.isApprove()) {
            internship.approve();
            return String.format("Internship '%s' approved by %s.",
                    internship.getTitle(), staff.getName());
        } else {
            internship.reject();
            return String.format("Internship '%s' rejected by %s. Reason: %s",
                    internship.getTitle(), staff.getName(),
                    request.getReason() != null ? request.getReason() : "No reason provided");
        }
    }

    /**
     * Approve or reject a student's withdrawal request.
     * @param staff the staff processing the withdrawal
     * @param withdrawal the withdrawal request to approve or reject
     * @param request contains the approval decision and optional reason
     * @return a message indicating the result of the withdrawal decision
     */
    default String approveWithdrawal(CareerCenterStaff staff, WithdrawalRequest withdrawal, ApprovalDecisionRequest request) {
        if (request.isApprove()) {
            withdrawal.approve(staff.getUserId(), request.getReason());
            return String.format("Withdrawal request approved by %s.", staff.getName());
        } else {
            withdrawal.reject(staff.getUserId(), request.getReason());
            return String.format("Withdrawal request rejected by %s. Reason: %s",
                    staff.getName(),
                    request.getReason() != null ? request.getReason() : "No reason provided");
        }
    }

    default List<InternshipOpportunity> generateReport(List<InternshipOpportunity> allOpportunities, ReportFilterRequest filter) {
        return allOpportunities.stream()
                // Status filter
                .filter(op -> filter.getStatuses() == null || filter.getStatuses().isEmpty() ||
                        filter.getStatuses().contains(op.getStatus().name()))
                // Preferred major filter
                .filter(op -> filter.getPreferredMajors() == null || filter.getPreferredMajors().isEmpty() ||
                        filter.getPreferredMajors().contains(op.getPreferredMajor()))
                // Level filter
                .filter(op -> filter.getLevels() == null || filter.getLevels().isEmpty() ||
                        filter.getLevels().contains(op.getLevel().name()))
                // Created date range filter (using openingDate as created date)
                .filter(op -> filter.getCreatedFrom() == null || !op.getOpeningDate().isBefore(filter.getCreatedFrom()))
                .filter(op -> filter.getCreatedTo() == null || !op.getOpeningDate().isAfter(filter.getCreatedTo()))
                // Created by filter (representativeId)
                .filter(op -> filter.getCreatedBy() == null || filter.getCreatedBy().isBlank() ||
                        op.getRepresentativeId().equalsIgnoreCase(filter.getCreatedBy()))
                // Visibility filter
                .filter(op -> filter.getVisible() == null || op.isVisible() == filter.getVisible())
                .toList();
    }

    default String generateReportSummary(List<InternshipOpportunity> filtered) {
        if (filtered.isEmpty()) return "No internship opportunities match the given filters.";

        StringBuilder sb = new StringBuilder("=== Internship Report ===\n");
        for (InternshipOpportunity op : filtered) {
            sb.append(String.format("%s | %s | %s | %s | Level: %s | Created: %s\n",
                    op.getTitle(),
                    op.getCompanyName(),
                    op.getStatus(),
                    op.getPreferredMajor(),
                    op.getLevel(),
                    op.getOpeningDate())); // using openingDate as created date
        }
        sb.append("===========================\n");
        return sb.toString();
    }

    default String exportReportToCSV(List<InternshipOpportunity> filtered, String filePath) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
            writer.append("Title,Company,Status,Preferred Major,Level,Created Date\n");
            for (InternshipOpportunity op : filtered) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s\n",
                        op.getTitle(),
                        op.getCompanyName(),
                        op.getStatus(),
                        op.getPreferredMajor(),
                        op.getLevel(),
                        op.getOpeningDate()));
            }
            return "Report successfully exported to: " + filePath;
        } catch (java.io.IOException e) {
            return "Error exporting report: " + e.getMessage();
        }
    }
}