package eu.kalodiodev.springjumpstart.service;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;

/**
 * Service contract for {@link PasswordResetToken}
 * 
 * @author Athanasios Raptodimos
 */
public interface PasswordResetTokenService {

	/**
	 * Find {@link PasswordResetToken} by token
	 * 
	 * @param token token
	 * @return PasswordResetToken
	 */
	PasswordResetToken findByToken(String token);

	/**
	 * Create password reset token for user
	 * 
	 * @param email User's email
	 * @return a new Password Reset Token for the user or null if user not found
	 */
	PasswordResetToken createPasswordResetToken(String email);

}
