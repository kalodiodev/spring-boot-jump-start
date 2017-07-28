package eu.kalodiodev.springjumpstart.repository;

import java.time.ZonedDateTime;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;

/**
 * Abstract Repository Test Class
 * <p>Repository tests must extend this class</p>
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class AbstractRepositoryTest {
	
	@Autowired
	protected TestEntityManager entityManager;
		
	protected User persistTestUser() {
	    User user = new User();
	    user.setFirstName("John");
	    user.setLastName("Doe");
	    user.setEmail("johndoe@examle.com");
	    user.setPassword("12345678");
	    user.setEncryptedPassword("dfsdfsdfsdf");
	    
	    return entityManager.persist(user);	    
	}
	
	protected PasswordResetToken persistTestPasswordResetToken(User user) {
		ZonedDateTime now = ZonedDateTime.now();		
		PasswordResetToken prToken = new PasswordResetToken(user.getEmail(), user, now, 120);

		return entityManager.persist(prToken);		
	}
}
