package eu.kalodiodev.springjumpstart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.kalodiodev.springjumpstart.domain.security.Role;

/**
 * Domain repository for {@link Role}
 * 
 * @author Athanasios Raptodimos
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	/**
	 * Find Role by role name
	 * 
	 * @param roleName Role name
	 * @return role
	 */
	Role findByRole(String roleName);
}
