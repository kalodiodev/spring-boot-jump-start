package eu.kalodiodev.springjumpstart.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Domain repository for {@link User}
 * 
 * @author Athanasios Raptodimos
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	/**
	 * Find user by email
	 * 
	 * @param email User's email
	 * @return user
	 */
    User findByEmail(String email);
    
    /**
     * Update user's password
     * 
     * @param userId Id of user
     * @param password New password
     */
    @Transactional
    @Modifying
    @Query("update User u set u.encryptedPassword = :password where u.id = :userId")
    void updateUserPassword(@Param("userId") Long userId, @Param("password") String password);
}
