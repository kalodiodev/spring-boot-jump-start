package eu.kalodiodev.springjumpstart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Test for {@link PasswordResetTokenRepository}
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PasswordResetTokenRepositoryIT extends AbstractRepositoryIT {
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Test
	public void whenFindByToken_thenReturnToken() {
		// given
	    User user = persistTestUser();
	    PasswordResetToken prToken = persistTestPasswordResetToken(user);
		
		// when
		PasswordResetToken found = passwordResetTokenRepository.findByToken(user.getEmail());
		
		// then
		assertNotNull("Found password reset token must not be null", found);
		assertThat(found.getUser().getEmail()).isEqualTo(prToken.getUser().getEmail());
	}
	
	@Test
	public void whenFindAllByUser_thenReturnTokens() {
		// given
		User user = persistTestUser();
		persistTestPasswordResetToken(user);
		
		// when
		Set<PasswordResetToken> tokens = passwordResetTokenRepository.findAllByUserId(user.getId());
		
		// then
		assertEquals("Set of tokens must have a size of 1", 1, tokens.size());
	}
}
