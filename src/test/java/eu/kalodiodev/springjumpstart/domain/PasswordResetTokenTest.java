package eu.kalodiodev.springjumpstart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;

import org.junit.Test;

/**
 * Unit test for {@link PasswordResetToken}
 * 
 * @author Athanasios Raptodimos
 */
public class PasswordResetTokenTest {
	
	private String token = "token uuid";
	private User user = new User();
	private ZonedDateTime creationDate = ZonedDateTime.now();
	private int expiryLength = 120;
	
	@Test
	public void shouldInstantiatePasswordResetToken() {		
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, creationDate, expiryLength);
		
		assertThat(passwordResetToken.getToken()).isEqualTo(token);
		assertThat(passwordResetToken.getUser()).isEqualTo(user);
		assertThat(passwordResetToken.getExpiryDate()).isEqualTo(creationDate.plusMinutes(expiryLength));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenUserNull() {
		new PasswordResetToken(token, null, creationDate, expiryLength);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenTokenNull() {
		new PasswordResetToken(null, user, creationDate, expiryLength);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenCreationDateNull() {
		new PasswordResetToken(token, user, null, expiryLength);
	}
}
