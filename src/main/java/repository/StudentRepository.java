package repository;

import enums.Major;
import model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Student entity operations.
 * Extends the generic Repository interface with student-specific queries.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface StudentRepository extends Repository<Student, String> {
    
    /**
     * Finds a student by their user ID.
     * 
     * @param userId The user ID (student ID) to search for
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<Student> findByUserId(String userId);
    
    /**
     * Finds students by major.
     * 
     * @param major The major to search for
     * @return List of students with the specified major
     */
    List<Student> findByMajor(Major major);
    
    /**
     * Finds students by year of study.
     * 
     * @param year The year to search for (1-4)
     * @return List of students in the specified year
     */
    List<Student> findByYearOfStudy(int year);
}
