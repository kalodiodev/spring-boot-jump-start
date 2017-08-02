package eu.kalodiodev.springjumpstart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Test for {@link UserRepository}
 * 
 * @author Athanasios Raptodimos
 */
public class UserRepositoryIT extends AbstractRepositoryIT {
		
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void whenFindByEmail_thenReturnUser() {
	    // given
	    User user = persistTestUser();
	 
	    // when
	    User found = userRepository.findByEmail(user.getEmail());
	 
	    // then
	    assertThat(found.getEmail()).isEqualTo(user.getEmail());
	}
	
	@Test
	public void shouldUpdateUserPassword() {
		// given
	    User user = persistTestUser();
	    	    	    
	    String newPassword = "0000000";
	    
	    // when
	    userRepository.updateUserPassword(user.getId(), newPassword);
	    	        
	    // then
	    entityManager.clear();
	    User updatedUser = entityManager.find(User.class, user.getId());
	    assertThat(updatedUser.getEncryptedPassword()).isEqualTo(newPassword);
	}
}
