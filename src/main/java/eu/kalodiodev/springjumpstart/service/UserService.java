package eu.kalodiodev.springjumpstart.service;

import eu.kalodiodev.springjumpstart.command.UserForm;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.exception.EmailExistsException;

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
	
	/**
	 * Register user
	 * 
	 * @param userForm user form to be saved
	 * @return saved user
	 * @throws EmailExistsException If account with this email already exists 
	 */
	User register(UserForm userForm) throws EmailExistsException;
}
