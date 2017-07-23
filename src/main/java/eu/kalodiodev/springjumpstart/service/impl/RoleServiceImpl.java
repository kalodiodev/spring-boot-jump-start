package eu.kalodiodev.springjumpstart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.kalodiodev.springjumpstart.domain.security.Role;
import eu.kalodiodev.springjumpstart.repository.RoleRepository;
import eu.kalodiodev.springjumpstart.service.RoleService;

/**
 * Role service implementation
 * 
 * @author Athanasios Raptodimos
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	private RoleRepository roleRepository;
	
	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public List<Role> all() {
		List<Role> roles = new ArrayList<>();
		roleRepository.findAll().forEach(roles::add);
		
		return roles;
	}

	@Override
	public Role saveOrUpdate(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role find(Long id) {
		return roleRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		roleRepository.delete(id);		
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByRole(name);
	}
}
