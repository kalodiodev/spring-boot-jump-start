package eu.kalodiodev.springjumpstart.repository;

import org.springframework.data.repository.CrudRepository;

import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Domain repository for {@link User}
 * 
 * @author Athanasios Raptodimos
 */
public interface UserRepository extends CrudRepository<User, Long> {
	
	/**
	 * Find user by email
	 * 
	 * @param email User's email
	 * @return user
	 */
    User findByEmail(String email);
}
