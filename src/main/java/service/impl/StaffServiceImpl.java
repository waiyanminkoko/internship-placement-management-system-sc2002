package service.impl;

import dto.ReportFilter;
import enums.ApprovalStatus;
import enums.ApplicationStatus;
import enums.InternshipLevel;
import enums.InternshipStatus;
import exception.BusinessRuleException;
import exception.ResourceNotFoundException;
import model.Application;
import model.CareerCenterStaff;
import model.CompanyRepresentative;
import model.InternshipOpportunity;
import model.Student;
import model.WithdrawalRequest;
import repository.ApplicationRepository;
import repository.CompanyRepresentativeRepository;
import repository.InternshipOpportunityRepository;
import repository.StudentRepository;
import service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of StaffService.
 * Handles business logic for Career Center Staff administrative operations.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private CompanyRepresentativeRepository representativeRepository;

    @Autowired
    private InternshipOpportunityRepository opportunityRepository;

    @Autowired
    private WithdrawalRequestService withdrawalRequestService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void authorizeRepresentative(CareerCenterStaff staff, String representativeId, boolean approve) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (representativeId == null || representativeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Representative ID cannot be empty");
        }

        // Find the representative
        CompanyRepresentative representative = representativeRepository.findById(representativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Company representative not found"));

        // Check if already processed
        if (representative.getStatus() != ApprovalStatus.PENDING) {
            throw new BusinessRuleException("Representative has already been processed");
        }

        // Update status
        if (approve) {
            representative.setStatus(ApprovalStatus.APPROVED);
            representative.setApprovedByStaffId(staff.getUserId());
        } else {
            representative.setStatus(ApprovalStatus.REJECTED);
            representative.setApprovedByStaffId(staff.getUserId());
        }

        representativeRepository.save(representative);
    }

    @Override
    public void approveOpportunity(CareerCenterStaff staff, String opportunityId, boolean approve) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (opportunityId == null || opportunityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Opportunity ID cannot be empty");
        }

        // Find the opportunity
        InternshipOpportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship opportunity not found"));

        // Check if already processed
        if (opportunity.getStatus() != InternshipStatus.PENDING) {
            throw new BusinessRuleException("Opportunity has already been processed");
        }

        // Update status
        if (approve) {
            opportunity.setStatus(InternshipStatus.APPROVED);
            opportunity.setVisibility(true); // Make visible when approved
        } else {
            opportunity.setStatus(InternshipStatus.REJECTED);
            opportunity.setVisibility(false); // Keep hidden when rejected
        }

        opportunityRepository.save(opportunity);
    }

    @Override
    public void approveWithdrawal(CareerCenterStaff staff, String withdrawalId, boolean approve) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (withdrawalId == null || withdrawalId.trim().isEmpty()) {
            throw new IllegalArgumentException("Withdrawal ID cannot be empty");
        }

        // Find the withdrawal request
        WithdrawalRequest request = withdrawalRequestService.findById(withdrawalId)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal request not found"));

        // Check if already processed
        if (request.getStatus() != ApprovalStatus.PENDING) {
            throw new BusinessRuleException("Withdrawal request has already been processed");
        }

        // Find the related application
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (approve) {
            // Approve withdrawal
            request.approve(staff.getUserId(), "Withdrawal approved by staff");
            
            // Update application status to WITHDRAWN
            application.setStatus(ApplicationStatus.WITHDRAWN);
            applicationRepository.save(application);

            // If application was successful, free up the slot
            if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
                InternshipOpportunity opportunity = opportunityRepository.findById(application.getInternshipId())
                        .orElse(null);
                if (opportunity != null && opportunity.getFilledSlots() > 0) {
                    opportunity.setFilledSlots(opportunity.getFilledSlots() - 1);
                    opportunityRepository.save(opportunity);
                }
            }

            // Update student's accepted placement if this was the accepted one
            Student student = studentRepository.findById(request.getStudentId()).orElse(null);
            if (student != null && student.getAcceptedPlacementId() != null 
                    && student.getAcceptedPlacementId().equals(request.getApplicationId())) {
                student.setAcceptedPlacementId(null);
                student.setHasAcceptedPlacement(false);
                studentRepository.save(student);
            }
        } else {
            // Reject withdrawal
            request.reject(staff.getUserId(), "Withdrawal rejected by staff");
        }

        withdrawalRequestService.save(request);
    }

    @Override
    public List<CompanyRepresentative> viewPendingRepresentatives(CareerCenterStaff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }

        return representativeRepository.findAll().stream()
                .filter(rep -> rep.getStatus() == ApprovalStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<InternshipOpportunity> viewPendingOpportunities(CareerCenterStaff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }

        return opportunityRepository.findAll().stream()
                .filter(opp -> opp.getStatus() == InternshipStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<WithdrawalRequest> viewPendingWithdrawals(CareerCenterStaff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }

        return withdrawalRequestService.findPendingRequests();
    }

    public Map<String, Object> generateReport(CareerCenterStaff staff, ReportFilter filter) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }

        Map<String, Object> report = new HashMap<>();

        // Get all applications
        List<Application> applications = applicationRepository.findAll();
        
        // Get all internships
        List<InternshipOpportunity> internships = opportunityRepository.findAll();

        // Apply filters to applications
        if (filter != null) {
            if (filter.getMajor() != null && !filter.getMajor().trim().isEmpty() 
                    && !filter.getMajor().equalsIgnoreCase("All Majors")) {
                String filterMajor = filter.getMajor();
                applications = applications.stream()
                        .filter(app -> {
                            Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                            return student != null && filterMajor.equalsIgnoreCase(student.getMajor());
                        })
                        .collect(Collectors.toList());
            }

            if (filter.getYear() != null && filter.getYear() > 0) {
                int filterYear = filter.getYear();
                applications = applications.stream()
                        .filter(app -> {
                            Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                            return student != null && student.getYear() == filterYear;
                        })
                        .collect(Collectors.toList());
            }

            if (filter.getCompanyName() != null && !filter.getCompanyName().trim().isEmpty()
                    && !filter.getCompanyName().equalsIgnoreCase("All Companies")) {
                String filterCompany = filter.getCompanyName();
                applications = applications.stream()
                        .filter(app -> {
                            InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                            return opp != null && filterCompany.equalsIgnoreCase(opp.getCompanyName());
                        })
                        .collect(Collectors.toList());
                
                // Also filter internships by company
                internships = internships.stream()
                        .filter(opp -> filterCompany.equalsIgnoreCase(opp.getCompanyName()))
                        .collect(Collectors.toList());
            }

            if (filter.getStartDate() != null && !filter.getStartDate().trim().isEmpty()) {
                try {
                    java.time.LocalDate startDate = java.time.LocalDate.parse(filter.getStartDate());
                    applications = applications.stream()
                            .filter(app -> {
                                InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                                return opp != null && !opp.getOpeningDate().isBefore(startDate);
                            })
                            .collect(Collectors.toList());
                    
                    internships = internships.stream()
                            .filter(opp -> !opp.getOpeningDate().isBefore(startDate))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    // Invalid date format, skip filter
                }
            }

            if (filter.getEndDate() != null && !filter.getEndDate().trim().isEmpty()) {
                try {
                    java.time.LocalDate endDate = java.time.LocalDate.parse(filter.getEndDate());
                    applications = applications.stream()
                            .filter(app -> {
                                InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                                return opp != null && !opp.getClosingDate().isAfter(endDate);
                            })
                            .collect(Collectors.toList());
                    
                    internships = internships.stream()
                            .filter(opp -> !opp.getClosingDate().isAfter(endDate))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    // Invalid date format, skip filter
                }
            }

            if (filter.getApplicationStatus() != null && !filter.getApplicationStatus().trim().isEmpty()
                    && !filter.getApplicationStatus().equalsIgnoreCase("All Application Statuses")) {
                String filterStatus = filter.getApplicationStatus().toUpperCase();
                try {
                    ApplicationStatus status = ApplicationStatus.valueOf(filterStatus);
                    applications = applications.stream()
                            .filter(app -> app.getStatus() == status)
                            .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    // Invalid status, skip filter
                }
            }

            if (filter.getInternshipLevel() != null && !filter.getInternshipLevel().trim().isEmpty()
                    && !filter.getInternshipLevel().equalsIgnoreCase("All Internship Levels")) {
                String filterLevel = filter.getInternshipLevel().toUpperCase();
                try {
                    InternshipLevel level = InternshipLevel.valueOf(filterLevel);
                    applications = applications.stream()
                            .filter(app -> {
                                InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                                return opp != null && opp.getLevel() == level;
                            })
                            .collect(Collectors.toList());
                    
                    internships = internships.stream()
                            .filter(opp -> opp.getLevel() == level)
                            .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    // Invalid level, skip filter
                }
            }

            if (filter.getInternshipStatus() != null && !filter.getInternshipStatus().trim().isEmpty()
                    && !filter.getInternshipStatus().equalsIgnoreCase("All Internship Statuses")) {
                String filterStatus = filter.getInternshipStatus().toUpperCase();
                try {
                    InternshipStatus status = InternshipStatus.valueOf(filterStatus);
                    applications = applications.stream()
                            .filter(app -> {
                                InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                                return opp != null && opp.getStatus() == status;
                            })
                            .collect(Collectors.toList());
                    
                    internships = internships.stream()
                            .filter(opp -> opp.getStatus() == status)
                            .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    // Invalid status, skip filter
                }
            }
        }

        // Generate basic statistics
        report.put("totalApplications", applications.size());
        report.put("pendingApplications", applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING).count());
        report.put("successfulApplications", applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL).count());
        report.put("acceptedApplications", applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL).count());
        report.put("rejectedApplications", applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count());
        report.put("withdrawnApplications", applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.WITHDRAWN).count());

        // Total internships (approved visible + filled regardless of visibility)
        long totalInternships = internships.stream()
                .filter(opp -> (opp.getStatus() == InternshipStatus.APPROVED && opp.isVisible()) 
                            || opp.getStatus() == InternshipStatus.FILLED
                            || opp.getStatus() == InternshipStatus.REJECTED
                            || opp.getStatus() == InternshipStatus.PENDING)
                .count();
        report.put("totalInternships", totalInternships);

        // Approved representatives
        long approvedReps = representativeRepository.findAll().stream()
                .filter(rep -> rep.getStatus() == ApprovalStatus.APPROVED)
                .count();
        report.put("approvedRepresentatives", approvedReps);

        // Pending withdrawals
        long pendingWithdrawals = withdrawalRequestService.findPendingRequests().size();
        report.put("pendingWithdrawals", pendingWithdrawals);

        // Get unique students with internships
        long studentsWithInternships = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .map(Application::getStudentId)
                .distinct()
                .count();
        report.put("studentsWithInternships", studentsWithInternships);

        // Total placements (students who accepted their placement)
        long totalPlacements = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL && app.isPlacementAccepted())
                .map(Application::getStudentId)
                .distinct()
                .count();
        report.put("totalPlacements", totalPlacements);

        // Placements by Company
        Map<String, Long> placementsByCompany = new HashMap<>();
        applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL && app.isPlacementAccepted())
                .forEach(app -> {
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    if (opp != null) {
                        placementsByCompany.merge(opp.getCompanyName(), 1L, Long::sum);
                    }
                });
        report.put("placementsByCompany", placementsByCompany);

        // Placement details
        List<Map<String, Object>> placementDetails = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL && app.isPlacementAccepted())
                .map(app -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("applicationId", app.getApplicationId());
                    detail.put("studentId", app.getStudentId());
                    detail.put("internshipId", app.getInternshipId());
                    detail.put("placementAcceptanceDate", app.getPlacementAcceptanceDate() != null 
                            ? app.getPlacementAcceptanceDate().toString() : "");
                    
                    Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                    if (student != null) {
                        detail.put("studentName", student.getName());
                        detail.put("studentEmail", student.getEmail());
                        detail.put("studentMajor", student.getMajor());
                        detail.put("studentYear", student.getYear());
                    }
                    
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    if (opp != null) {
                        detail.put("internshipTitle", opp.getTitle());
                        detail.put("companyName", opp.getCompanyName());
                        detail.put("internshipLevel", opp.getLevel().toString());
                        detail.put("startDate", opp.getStartDate() != null ? opp.getStartDate().toString() : "");
                        detail.put("endDate", opp.getEndDate() != null ? opp.getEndDate().toString() : "");
                    }
                    
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("placements", placementDetails);

        // Get unique companies
        long uniqueCompanies = applications.stream()
                .map(app -> {
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    return opp != null ? opp.getCompanyName() : null;
                })
                .filter(name -> name != null)
                .distinct()
                .count();
        report.put("uniqueCompanies", uniqueCompanies);

        // Applications by Major
        Map<String, Long> applicationsByMajor = new HashMap<>();
        for (Application app : applications) {
            Student student = studentRepository.findById(app.getStudentId()).orElse(null);
            if (student != null && student.getMajor() != null) {
                applicationsByMajor.merge(student.getMajor(), 1L, Long::sum);
            }
        }
        report.put("applicationsByMajor", applicationsByMajor);

        // Applications by Year
        Map<String, Long> applicationsByYear = new HashMap<>();
        for (Application app : applications) {
            Student student = studentRepository.findById(app.getStudentId()).orElse(null);
            if (student != null) {
                String year = String.valueOf(student.getYear());
                applicationsByYear.merge(year, 1L, Long::sum);
            }
        }
        report.put("applicationsByYear", applicationsByYear);

        // Internships by Company
        Map<String, Long> internshipsByCompany = new HashMap<>();
        for (InternshipOpportunity opp : internships) {
            // Include APPROVED (visible), FILLED (regardless of visibility), and REJECTED (if filtered)
            if (opp.getStatus() == InternshipStatus.APPROVED && opp.isVisible()) {
                internshipsByCompany.merge(opp.getCompanyName(), 1L, Long::sum);
            } else if (opp.getStatus() == InternshipStatus.FILLED) {
                internshipsByCompany.merge(opp.getCompanyName(), 1L, Long::sum);
            } else if (opp.getStatus() == InternshipStatus.REJECTED || opp.getStatus() == InternshipStatus.PENDING) {
                // Include other statuses if they're in the filtered list (e.g., when filtering by status)
                internshipsByCompany.merge(opp.getCompanyName(), 1L, Long::sum);
            }
        }
        report.put("internshipsByCompany", internshipsByCompany);

        // Include filtered applications with detailed info
        List<Map<String, Object>> applicationDetails = applications.stream()
                .map(app -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("applicationId", app.getApplicationId());
                    detail.put("studentId", app.getStudentId());
                    detail.put("internshipId", app.getInternshipId());
                    detail.put("status", app.getStatus().toString());
                    detail.put("submissionDate", app.getSubmissionDate() != null ? app.getSubmissionDate().toString() : "");
                    
                    Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                    if (student != null) {
                        detail.put("studentName", student.getName());
                        detail.put("studentMajor", student.getMajor());
                        detail.put("studentYear", student.getYear());
                    }
                    
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    if (opp != null) {
                        detail.put("internshipTitle", opp.getTitle());
                        detail.put("companyName", opp.getCompanyName());
                    }
                    
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("applications", applicationDetails);
        
        // Add summary of students by status (after application details)
        List<Map<String, Object>> successfulStudents = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .map(app -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("studentId", app.getStudentId());
                    detail.put("applicationId", app.getApplicationId());
                    detail.put("internshipId", app.getInternshipId());
                    detail.put("placementAccepted", app.isPlacementAccepted());
                    
                    Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                    if (student != null) {
                        detail.put("studentName", student.getName());
                        detail.put("studentMajor", student.getMajor());
                        detail.put("studentYear", student.getYear());
                    }
                    
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    if (opp != null) {
                        detail.put("internshipTitle", opp.getTitle());
                        detail.put("companyName", opp.getCompanyName());
                    }
                    
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("successfulStudents", successfulStudents);
        
        List<Map<String, Object>> withdrawnStudents = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.WITHDRAWN)
                .map(app -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("studentId", app.getStudentId());
                    detail.put("applicationId", app.getApplicationId());
                    detail.put("internshipId", app.getInternshipId());
                    
                    Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                    if (student != null) {
                        detail.put("studentName", student.getName());
                        detail.put("studentMajor", student.getMajor());
                        detail.put("studentYear", student.getYear());
                    }
                    
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    if (opp != null) {
                        detail.put("internshipTitle", opp.getTitle());
                        detail.put("companyName", opp.getCompanyName());
                    }
                    
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("withdrawnStudents", withdrawnStudents);
        
        List<Map<String, Object>> rejectedStudents = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.REJECTED)
                .map(app -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("studentId", app.getStudentId());
                    detail.put("applicationId", app.getApplicationId());
                    detail.put("internshipId", app.getInternshipId());
                    
                    Student student = studentRepository.findById(app.getStudentId()).orElse(null);
                    if (student != null) {
                        detail.put("studentName", student.getName());
                        detail.put("studentMajor", student.getMajor());
                        detail.put("studentYear", student.getYear());
                    }
                    
                    InternshipOpportunity opp = opportunityRepository.findById(app.getInternshipId()).orElse(null);
                    if (opp != null) {
                        detail.put("internshipTitle", opp.getTitle());
                        detail.put("companyName", opp.getCompanyName());
                    }
                    
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("rejectedStudents", rejectedStudents);
        
        // Summary counts
        report.put("successfulStudentsCount", successfulStudents.size());
        report.put("withdrawnStudentsCount", withdrawnStudents.size());
        report.put("rejectedStudentsCount", rejectedStudents.size());
        
        // Include internship details
        List<Map<String, Object>> internshipDetails = internships.stream()
                .filter(opp -> (opp.getStatus() == InternshipStatus.APPROVED && opp.isVisible()) 
                            || opp.getStatus() == InternshipStatus.FILLED
                            || opp.getStatus() == InternshipStatus.REJECTED
                            || opp.getStatus() == InternshipStatus.PENDING)
                .map(opp -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("internshipId", opp.getInternshipId());
                    detail.put("title", opp.getTitle());
                    detail.put("companyName", opp.getCompanyName());
                    detail.put("level", opp.getLevel().toString());
                    detail.put("preferredMajor", opp.getPreferredMajor());
                    detail.put("slots", opp.getSlots());
                    detail.put("filledSlots", opp.getFilledSlots());
                    detail.put("openingDate", opp.getOpeningDate() != null ? opp.getOpeningDate().toString() : "");
                    detail.put("closingDate", opp.getClosingDate() != null ? opp.getClosingDate().toString() : "");
                    detail.put("status", opp.getStatus().toString());
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("internships", internshipDetails);

        // Get all withdrawal requests
        List<WithdrawalRequest> withdrawalRequests = withdrawalRequestService.findAll();
        
        // Filter withdrawal requests based on student, internship, and date filters
        List<WithdrawalRequest> filteredWithdrawals = withdrawalRequests;
        
        if (filter != null) {
            // Filter by major
            if (filter.getMajor() != null && !filter.getMajor().trim().isEmpty() 
                    && !filter.getMajor().equalsIgnoreCase("All Majors")) {
                String filterMajor = filter.getMajor();
                filteredWithdrawals = filteredWithdrawals.stream()
                        .filter(wd -> {
                            Student student = studentRepository.findById(wd.getStudentId()).orElse(null);
                            return student != null && filterMajor.equalsIgnoreCase(student.getMajor());
                        })
                        .collect(Collectors.toList());
            }
            
            // Filter by year
            if (filter.getYear() != null && filter.getYear() > 0) {
                int filterYear = filter.getYear();
                filteredWithdrawals = filteredWithdrawals.stream()
                        .filter(wd -> {
                            Student student = studentRepository.findById(wd.getStudentId()).orElse(null);
                            return student != null && student.getYear() == filterYear;
                        })
                        .collect(Collectors.toList());
            }
            
            // Filter by company
            if (filter.getCompanyName() != null && !filter.getCompanyName().trim().isEmpty()
                    && !filter.getCompanyName().equalsIgnoreCase("All Companies")) {
                String filterCompany = filter.getCompanyName();
                filteredWithdrawals = filteredWithdrawals.stream()
                        .filter(wd -> {
                            InternshipOpportunity opp = opportunityRepository.findById(wd.getInternshipId()).orElse(null);
                            return opp != null && filterCompany.equalsIgnoreCase(opp.getCompanyName());
                        })
                        .collect(Collectors.toList());
            }
            
            // Filter by date range (using request date)
            if (filter.getStartDate() != null && !filter.getStartDate().trim().isEmpty()) {
                try {
                    java.time.LocalDate startDate = java.time.LocalDate.parse(filter.getStartDate());
                    filteredWithdrawals = filteredWithdrawals.stream()
                            .filter(wd -> {
                                if (wd.getRequestDate() == null) return false;
                                java.time.LocalDate requestDate = wd.getRequestDate().toLocalDate();
                                return !requestDate.isBefore(startDate);
                            })
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    // Invalid date format, skip filter
                }
            }
            
            if (filter.getEndDate() != null && !filter.getEndDate().trim().isEmpty()) {
                try {
                    java.time.LocalDate endDate = java.time.LocalDate.parse(filter.getEndDate());
                    filteredWithdrawals = filteredWithdrawals.stream()
                            .filter(wd -> {
                                if (wd.getRequestDate() == null) return false;
                                java.time.LocalDate requestDate = wd.getRequestDate().toLocalDate();
                                return !requestDate.isAfter(endDate);
                            })
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    // Invalid date format, skip filter
                }
            }
        }
        
        // Include withdrawal details
        List<Map<String, Object>> withdrawalDetails = filteredWithdrawals.stream()
                .map(wd -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("withdrawalId", wd.getRequestId());
                    detail.put("studentId", wd.getStudentId());
                    detail.put("applicationId", wd.getApplicationId());
                    detail.put("internshipId", wd.getInternshipId());
                    detail.put("reason", wd.getReason());
                    detail.put("status", wd.getStatus().toString());
                    detail.put("requestDate", wd.getRequestDate() != null ? wd.getRequestDate().toString() : "");
                    detail.put("processedBy", wd.getProcessedBy());
                    detail.put("processedDate", wd.getProcessedDate() != null ? wd.getProcessedDate().toString() : "");
                    
                    Student student = studentRepository.findById(wd.getStudentId()).orElse(null);
                    if (student != null) {
                        detail.put("studentName", student.getName());
                        detail.put("studentMajor", student.getMajor());
                        detail.put("studentYear", student.getYear());
                    }
                    
                    InternshipOpportunity opp = opportunityRepository.findById(wd.getInternshipId()).orElse(null);
                    if (opp != null) {
                        detail.put("internshipTitle", opp.getTitle());
                        detail.put("companyName", opp.getCompanyName());
                    }
                    
                    return detail;
                })
                .collect(Collectors.toList());
        report.put("withdrawals", withdrawalDetails);

        return report;
    }
    
    @Override
    public Map<String, Object> createStudent(CareerCenterStaff staff, Map<String, Object> studentData) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (studentData == null || studentData.isEmpty()) {
            throw new IllegalArgumentException("Student data cannot be empty");
        }
        
        // Extract student data
        String studentId = (String) studentData.get("studentId");
        String name = (String) studentData.get("name");
        String major = (String) studentData.get("major");
        Integer year = studentData.get("year") != null ? 
                Integer.parseInt(studentData.get("year").toString()) : null;
        String email = (String) studentData.get("email");
        String password = (String) studentData.get("password");
        
        // Validate required fields
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Check if student ID already exists
        if (studentRepository.findById(studentId).isPresent()) {
            throw new BusinessRuleException("Student ID already exists");
        }
        
        // Create new student
        Student student = new Student();
        student.setUserId(studentId);
        student.setName(name);
        student.setMajor(major);
        if (year != null) {
            student.setYear(year);
        }
        student.setEmail(email);
        student.setPassword(password != null ? password : "password"); // Default password
        student.setRole(enums.UserRole.STUDENT); // Set role to STUDENT
        
        // Save student
        Student savedStudent = studentRepository.save(student);
        
        // Return created student data
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", savedStudent.getUserId());
        result.put("name", savedStudent.getName());
        result.put("major", savedStudent.getMajor());
        result.put("year", savedStudent.getYear());
        result.put("email", savedStudent.getEmail());
        
        return result;
    }
    
    @Override
    public Map<String, Object> createRepresentative(CareerCenterStaff staff, Map<String, Object> representativeData) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (representativeData == null || representativeData.isEmpty()) {
            throw new IllegalArgumentException("Representative data cannot be empty");
        }
        
        // Extract representative data
        String companyRepId = (String) representativeData.get("companyRepId");
        String name = (String) representativeData.get("name");
        String companyName = (String) representativeData.get("companyName");
        String industry = (String) representativeData.get("industry");
        String position = (String) representativeData.get("position");
        String email = (String) representativeData.get("email");
        String password = (String) representativeData.get("password");
        
        // Validate required fields
        if (companyRepId == null || companyRepId.trim().isEmpty()) {
            throw new IllegalArgumentException("Company Representative ID is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Check if representative ID already exists
        if (representativeRepository.findById(companyRepId).isPresent()) {
            throw new BusinessRuleException("Company Representative ID already exists");
        }
        
        // Create new representative
        CompanyRepresentative representative = new CompanyRepresentative();
        representative.setUserId(companyRepId);
        representative.setName(name);
        representative.setCompanyName(companyName);
        representative.setIndustry(industry);
        representative.setPosition(position);
        representative.setEmail(email);
        representative.setPassword(password != null ? password : "password"); // Default password
        representative.setRole(enums.UserRole.COMPANY_REPRESENTATIVE); // Set role to COMPANY_REPRESENTATIVE
        representative.setStatus(ApprovalStatus.APPROVED); // Staff-created accounts are auto-approved
        representative.setAuthorized(true); // Auto-authorize staff-created accounts
        representative.setApprovedByStaffId(staff.getUserId());
        
        // Save representative
        CompanyRepresentative savedRep = representativeRepository.save(representative);
        
        // Return created representative data
        Map<String, Object> result = new HashMap<>();
        result.put("companyRepId", savedRep.getUserId());
        result.put("name", savedRep.getName());
        result.put("companyName", savedRep.getCompanyName());
        result.put("industry", savedRep.getIndustry());
        result.put("position", savedRep.getPosition());
        result.put("email", savedRep.getEmail());
        result.put("status", savedRep.getStatus().toString());
        
        return result;
    }
    
    @Override
    public List<String> getAllCompanies(CareerCenterStaff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        
        // Get all approved representatives and extract unique company names
        return representativeRepository.findAll().stream()
                .filter(rep -> rep.getStatus() == ApprovalStatus.APPROVED)
                .map(CompanyRepresentative::getCompanyName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<dto.WithdrawalDetailsDTO> viewPendingWithdrawalsWithDetails(CareerCenterStaff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        
        List<WithdrawalRequest> pendingRequests = withdrawalRequestService.findPendingRequests();
        
        return pendingRequests.stream()
                .map(wr -> {
                    dto.WithdrawalDetailsDTO dto = new dto.WithdrawalDetailsDTO();
                    dto.setWithdrawalId(wr.getRequestId());
                    dto.setStudentId(wr.getStudentId());
                    dto.setApplicationId(wr.getApplicationId());
                    dto.setInternshipId(wr.getInternshipId());
                    dto.setReason(wr.getReason());
                    dto.setStatus(wr.getStatus().toString());
                    dto.setRequestDate(wr.getRequestDate() != null ? wr.getRequestDate().toString() : "");
                    dto.setProcessedBy(wr.getProcessedBy());
                    dto.setProcessedDate(wr.getProcessedDate() != null ? wr.getProcessedDate().toString() : "");
                    
                    // Enrich with student details
                    Student student = studentRepository.findById(wr.getStudentId()).orElse(null);
                    if (student != null) {
                        dto.setStudentName(student.getName());
                        dto.setStudentMajor(student.getMajor());
                        dto.setStudentYear(student.getYear());
                    }
                    
                    // Enrich with internship details
                    InternshipOpportunity opp = opportunityRepository.findById(wr.getInternshipId()).orElse(null);
                    if (opp != null) {
                        dto.setInternshipTitle(opp.getTitle());
                        dto.setCompanyName(opp.getCompanyName());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
