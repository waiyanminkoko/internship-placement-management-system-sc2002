package repository;

import enums.ApplicationStatus;
import model.Application;
import java.util.List;

/**
 * Repository interface for Application entity operations.
 * Extends the generic Repository interface with application-specific queries.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface ApplicationRepository extends Repository<Application, String> {
    /**
     * Finds applications submitted by a specific student.
     * 
     * @param studentId The student ID to search for
     * @return List of applications by the student
     */
    List<Application> findByStudentId(String studentId);
    
    /**
     * Finds applications for a specific internship opportunity.
     * 
     * @param internshipId The internship ID to search for
     * @return List of applications for the internship
     */
    List<Application> findByInternshipId(String internshipId);
    
    /**
     * Finds applications by status.
     * 
     * @param status The application status to search for
     * @return List of applications with the specified status
     */
    List<Application> findByStatus(ApplicationStatus status);
    
    /**
     * Counts active applications for a specific student.
     * Active applications are those with PENDING or SUCCESSFUL status.
     * 
     * @param studentId The student ID to count applications for
     * @return The count of active applications
     */
    long countActiveByStudentId(String studentId);
    
    /**
     * Finds all accepted placements (applications where placement was accepted).
     * 
     * @return List of applications with accepted placements
     */
    List<Application> findAcceptedPlacements();
}
