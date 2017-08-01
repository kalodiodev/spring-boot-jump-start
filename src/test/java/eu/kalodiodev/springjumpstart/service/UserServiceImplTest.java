package eu.kalodiodev.springjumpstart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import eu.kalodiodev.springjumpstart.command.UserForm;
import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.domain.security.Role;
import eu.kalodiodev.springjumpstart.exception.EmailExistsException;
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
	
	@Mock
	private RoleService roleServiceMock;
	
	@Mock
	private PasswordResetTokenService prTokenServiceMock;
	
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
	
	@Test
	public void shouldSaveRegisteringUser() throws EmailExistsException {		
		ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
		
		User user = testUser();				
		UserForm userForm = new UserForm();
		userForm.setEmail(user.getEmail());
		userForm.setFirstName(user.getFirstName());
		userForm.setLastName(user.getLastName());
		userForm.setPassword(user.getPassword());
		
		when(userRepositoryMock.findByEmail(anyString())).thenReturn(null);
		when(userRepositoryMock.save(any(User.class))).thenReturn(user);
		when(roleServiceMock.findByName(anyString())).thenReturn(new Role());
		
		User registered = userService.register(userForm);
		
		verify(userRepositoryMock).save(userArg.capture());
		
		assertNotNull("Registered user must not be null", registered);
		assertThat(userArg.getValue().getEmail()).isEqualTo(userForm.getEmail());
		assertThat(userArg.getValue().getFirstName()).isEqualTo(userForm.getFirstName());
		assertThat(userArg.getValue().getLastName()).isEqualTo(userForm.getLastName());
		assertThat(userArg.getValue().getPassword()).isEqualTo(userForm.getPassword());
	}
	
	@Test(expected = EmailExistsException.class)
	public void shouldThrowEmailExistsExceptionWhenRegistering() throws EmailExistsException {
		UserForm userForm = new UserForm();
		
		when(userRepositoryMock.findByEmail(anyString())).thenReturn(new User());
		
		userService.register(userForm);
	}
	
	@Test
	public void shouldUpdateUserEncryptedPassword() {
		Long userId = 1L;
		String password = "12345678";
		String encPassword = "Encrypted Password";
		
		when(passwordEncoderMock.encode(password)).thenReturn(encPassword);
		
		userService.updatePassword(userId, password);

		verify(userRepositoryMock).updateUserPassword(userId, encPassword);
	}
	
	@Test
	public void shouldDeleteUserPasswordResetTokensAfterPasswordUpdate() {
		Long userId = 1L;
		
		Set<PasswordResetToken> tokens = new HashSet<>();
		tokens.add(new PasswordResetToken());
		tokens.add(new PasswordResetToken());
		
		when(passwordEncoderMock.encode(anyString())).thenReturn("encrypted password");
		when(prTokenServiceMock.findAllByUserId(userId)).thenReturn(tokens);
		
		userService.updatePassword(userId, "12345678");
		
		verify(prTokenServiceMock).delete(tokens);
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
