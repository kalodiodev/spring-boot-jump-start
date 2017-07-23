package eu.kalodiodev.springjumpstart.service;

import java.util.List;

/**
 * CRUD generic service contract
 * 
 * @author Athanasios Raptodimos
 */
public interface CrudService<T> {
	
	/**
     * List all entries
     *
     * @return entries list
     */
    List<T> all();

    /**
     * Save or Update domain object
     *
     * @param domainObject domain object to be saved
     * @return saved domain object
     */
    T saveOrUpdate(T domainObject);

    /**
     * Find by id
     *
     * @param id id of object
     * @return object
     */
    T find(Long id);

    /**
     * Delete domain object
     *
     * @param id domain object id
     */
    void delete(Long id);
}
