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
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dto.ApprovalDecisionRequest;
import model.CareerCenterStaff;
import model.CompanyRepresentative;
import model.InternshipOpportunity;
import model.WithdrawalRequest;
import dto.ReportFilterRequest;

@Service
public class StaffService {

    /**
     * Authorize or reject a company representative.
     * 
     * @param staff the staff performing authorisation 
     * @param application the company representative applying for authorisation 
     * @param request DTO containing the approval decision and optional reason 
     * @return message indicating result of authorisation
     */
    public String authorizeRepresentative(CareerCenterStaff staff, CompanyRepresentative applicant, ApprovalDecisionRequest request) {
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
     * Revoke authorization of an existing representative.
     * 
     * @param staff the staff performing authorisation 
     * @param representatives the list of all company representatives
     * @param repEmail the email of the representative to revoke
     * @param confirm whether the revocation is confirmed
     * @return message indicating outcome of revocation 
     */
    public String revokeAuthorization(CareerCenterStaff staff, List<CompanyRepresentative> representatives, String repEmail, boolean confirm) {
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
     * param internship the internship opportunity under review
     * @param request contains the approval decision and optional reason
     * @return a message indicating the approval result
     * 
     */
    public String approveInternship(CareerCenterStaff staff, InternshipOpportunity internship, ApprovalDecisionRequest request) {
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
    public String approveWithdrawal(CareerCenterStaff staff, WithdrawalRequest withdrawal, ApprovalDecisionRequest request) {
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

public List<InternshipOpportunity> generateReport(List<InternshipOpportunity> allOpportunities, ReportFilterRequest filter) {
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
            .collect(Collectors.toList());
}

public String generateReportSummary(List<InternshipOpportunity> filtered) {
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

public String exportReportToCSV(List<InternshipOpportunity> filtered, String filePath) {
    try (FileWriter writer = new FileWriter(filePath)) {
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
    } catch (IOException e) {
        return "Error exporting report: " + e.getMessage();
    }
}
}