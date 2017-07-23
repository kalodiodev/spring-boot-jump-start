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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.repository.UserRepository;
import eu.kalodiodev.springjumpstart.service.impl.UserServiceImpl;

/**
 * Unit test for {@link UserService}
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(JUnit4.class)
public class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepositoryMock;

	@Mock
	private PasswordEncoder passwordEncoderMock;
	
	@InjectMocks
	private UserService userService;
	
	@Before
	public void setup() {
		userService = new UserServiceImpl();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldRetrieveAllUsersList() {
		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());
		
		when(userRepositoryMock.findAll()).thenReturn(users);
		
		List<User> retreivedUsers = userService.all();
		
		verify(userRepositoryMock, times(1)).findAll();		
		assertEquals("Users list must have size of 2", 2, retreivedUsers.size());
	}
	
	@Test
	public void shouldFindUserById() {
		User user = testUser();
		
		when(userRepositoryMock.findOne(user.getId())).thenReturn(user);
		
		User retreivedUser = userService.find(user.getId());
		
		verify(userRepositoryMock,times(1)).findOne(user.getId());		
		assertEquals("Retreived user's id must be 2", user.getId(), retreivedUser.getId());	
	}
	
	@Test
	public void shouldSaveUser() {
		User user = testUser();
		
		when(userRepositoryMock.save(user)).thenReturn(user);
		when(passwordEncoderMock.encode(user.getPassword())).thenReturn("Encrypted Password");
		
		User savedUser = userService.saveOrUpdate(user);
		
		verify(userRepositoryMock, times(1)).save(user);
		assertNotNull("Saved user must not be null", savedUser);
		assertNotNull("Encrypted password must not be null", savedUser.getEncryptedPassword());
	}
	
	@Test
	public void shouldFindUserByEmail() {
		User user = testUser();
		
		when(userRepositoryMock.findByEmail(user.getEmail())).thenReturn(user);
		
		User retreivedUser = userService.findByEmail(user.getEmail());
		
		verify(userRepositoryMock, times(1)).findByEmail(user.getEmail());
		assertEquals("Retreived User's email should match the requested one", user.getEmail(), retreivedUser.getEmail());
	}
	
	@Test
	public void shouldDeleteUserById() {
		Long id = 1L;
		
		userService.delete(id);
		
		verify(userRepositoryMock, times(1)).delete(id);
	}
	
	
	//--------------------> Private methods
	
	private User testUser() {
		User user = new User();
		user.setId(2L);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("johndoe@example.com");
		user.setPassword("123456");
		
		return user;
	}

}
