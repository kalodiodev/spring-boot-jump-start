package eu.kalodiodev.springjumpstart.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eu.kalodiodev.springjumpstart.domain.security.Role;
import eu.kalodiodev.springjumpstart.repository.RoleRepository;
import eu.kalodiodev.springjumpstart.service.impl.RoleServiceImpl;

/**
 * Unit test for {@link RoleServiceImpl}
 * 
 * @author Athanasios Raptodimos
 */
public class RoleServiceImplTest {
	
	@Mock
	private RoleRepository roleRepositoryMock;
	
	@InjectMocks
	private RoleService roleService;
	
	@Before
	public void setup() {
		this.roleService = new RoleServiceImpl();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldRetrieveAllRolesList() {
		List<Role> roles = new ArrayList<>();
		roles.add(new Role());
		roles.add(new Role());
		
		when(roleRepositoryMock.findAll()).thenReturn(roles);
		
		List<Role> retrievedRoles = roleService.all();
		
		verify(roleRepositoryMock, times(1)).findAll();
		assertEquals("Roles list size must be 2", 2, retrievedRoles.size());
	}
	
	@Test
	public void shouldFindRoleById() {
		Role role = testRole();
		
		when(roleRepositoryMock.findOne(role.getId())).thenReturn(role);
		
		Role retrievedRole = roleService.find(role.getId());
		
		verify(roleRepositoryMock, times(1)).findOne(role.getId());
		assertNotNull("Retrieved Role must not be null", retrievedRole);
		assertEquals("Retrieved Role's id must be equal with the requested one.", role.getId(), retrievedRole.getId());
	}
	
	@Test
	public void shouldFindRoleByRoleName() {
		Role role = testRole();
		
		when(roleRepositoryMock.findByRole(role.getRole())).thenReturn(role);
		
		Role retrievedRole = roleService.findByName(role.getRole());
		
		verify(roleRepositoryMock, times(1)).findByRole(role.getRole());
		assertNotNull("Retrieved Role must not be null", retrievedRole);
		assertEquals("Retrieved Role's name must be equal with the requested one.", role.getRole(), retrievedRole.getRole());
	}
	
	@Test
	public void shouldSaveRole() {
		Role role = testRole();
		
		when(roleRepositoryMock.save(role)).thenReturn(role);
		
		Role savedRole = roleService.saveOrUpdate(role);
		
		verify(roleRepositoryMock, times(1)).save(role);
		assertNotNull("Saved role must not be null", savedRole);
	}
	
	@Test
	public void shouldDeleteRoleById() {
		Long id = 1L;
		
		roleService.delete(id);
		
		verify(roleRepositoryMock, times(1)).delete(id);
	}
	
	//-----------------> Private methods
	
	private Role testRole() {
		Role role = new Role();
		role.setId(3L);
		role.setRole("Admin");
		
		return role;
	}
}