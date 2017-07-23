package eu.kalodiodev.springjumpstart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Test for {@link UserRepository}
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void whenFindByEmail_thenReturnUser() {
	    // given
	    User user = new User();
	    user.setFirstName("John");
	    user.setLastName("Doe");
	    user.setEmail("johndoe@examle.com");
	    user.setPassword("12345678");
	    user.setEncryptedPassword("dfsdfsdfsdf");
	   
	    entityManager.persist(user);
	    entityManager.flush();
	 
	    // when
	    User found = userRepository.findByEmail(user.getEmail());
	 
	    // then
	    assertThat(found.getEmail()).isEqualTo(user.getEmail());
	}
}
