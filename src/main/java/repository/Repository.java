package repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for CRUD operations.
 * Provides a standard contract for data access operations across all entities.
 * 
 * <p>This interface follows the Repository pattern to abstract data persistence
 * and retrieval operations. Implementations should handle CSV file operations
 * transparently.</p>
 * 
 * @param <T> Entity type
 * @param <ID> Identifier type
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-07
 */
public interface Repository<T, ID> {
    /**
     * Saves an entity (creates new or updates existing).
     * If the entity already exists (same ID), it will be updated.
     * Otherwise, a new entity will be created.
     * 
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);
    
    /**
     * Finds an entity by its identifier.
     * 
     * @param id The identifier to search for
     * @return Optional containing the entity if found, empty otherwise
     */
    Optional<T> findById(ID id);
    
    /**
     * Retrieves all entities.
     * 
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Deletes an entity by its identifier.
     * 
     * @param id The identifier of the entity to delete
     */
    void deleteById(ID id);
    
    /**
     * Checks if an entity exists with the given identifier.
     * 
     * @param id The identifier to check
     * @return true if entity exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Counts the total number of entities.
     * 
     * @return The total count of entities
     */
    long count();
}
