package eu.kalodiodev.springjumpstart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import eu.kalodiodev.springjumpstart.domain.security.Role;

/**
 * Test for {@link RoleRepository}
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryIT {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Test
	public void whenFindByRole_thenReturnRole() {		
		// given
		Role role = new Role();
		role.setRole("ADMIN_ROLE");
		
		entityManager.persist(role);
		entityManager.flush();
		
		// when
		Role found = roleRepository.findByRole(role.getRole());
		
		// then
		assertThat(found.getRole()).isEqualTo(role.getRole());
	}
}