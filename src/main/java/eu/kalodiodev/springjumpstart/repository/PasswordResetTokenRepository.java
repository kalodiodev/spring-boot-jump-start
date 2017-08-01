package eu.kalodiodev.springjumpstart.repository;

import java.util.Set;

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

	/**
	 * Find all password reset tokens of user
	 * 
	 * @param userId User's id
	 * @return Password reset tokens
	 */
	Set<PasswordResetToken> findAllByUserId(Long userId);
}
