package eu.kalodiodev.springjumpstart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;

/**
 * Repository for {@link PasswordResetToken}
 * 
 * @author Athanasios Raptodimos
 */
@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

	/**
	 * Find Password reset token by token
	 * 
	 * @param token
	 * @return passwordResetToken
	 */
	PasswordResetToken findByToken(String token);

}
