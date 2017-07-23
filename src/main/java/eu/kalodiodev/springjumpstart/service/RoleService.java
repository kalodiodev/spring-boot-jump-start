package eu.kalodiodev.springjumpstart.service;

import eu.kalodiodev.springjumpstart.domain.security.Role;

/**
 * Service contract for {@link Role}
 * 
 * @author Athanasios Raptodimos
 */
public interface RoleService extends CrudService<Role> {
	
	/**
	 * Find role by role name
	 * 
	 * @param name Role name
	 * @return role
	 */
	Role findByName(String name);
}
