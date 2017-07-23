package eu.kalodiodev.springjumpstart.service;

import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Service contract for {@link User}
 * 
 * @author Athanasios Raptodimos
 */
public interface UserService extends CrudService<User> {
	
	/**
	 * Find user by email
	 * 
	 * @param email User's email
	 * @return user
	 */
	User findByEmail(String email);
}
