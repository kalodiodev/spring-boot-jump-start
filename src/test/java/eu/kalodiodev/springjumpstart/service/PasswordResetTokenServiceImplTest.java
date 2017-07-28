package eu.kalodiodev.springjumpstart.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.repository.PasswordResetTokenRepository;
import eu.kalodiodev.springjumpstart.service.impl.PasswordResetTokenServiceImpl;

/**
 * Unit test for {@link PasswordResetTokenServiceImpl}
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(JUnit4.class)
public class PasswordResetTokenServiceImplTest {
	
	@Mock
	private PasswordResetTokenRepository prtRepositoryMock;
	
	@Mock
	private UserService userServiceMock;
	
	@InjectMocks
	private PasswordResetTokenService passwordResetTokenService;
	
	@Before
	public void setup() {
		passwordResetTokenService = new PasswordResetTokenServiceImpl(prtRepositoryMock, userServiceMock);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldFindPasswordResetTokenByToken() {
		
		when(prtRepositoryMock.findByToken(anyString())).thenReturn(new PasswordResetToken());
		
		PasswordResetToken prt = passwordResetTokenService.findByToken("token");
		
		Mockito.verify(prtRepositoryMock, times(1)).findByToken(anyString());
		
		assertNotNull("Password Reset Token must not be null", prt);
	}
	
	@Test
	public void shouldCreateResetToken() {
		User user = new User();
		user.setEmail("john@example.com");
		
		when(userServiceMock.findByEmail(user.getEmail())).thenReturn(user);
		when(prtRepositoryMock.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());
		
		PasswordResetToken prt = passwordResetTokenService.createPasswordResetToken(user.getEmail());
		
		verify(userServiceMock, times(1)).findByEmail(anyString());
		verify(prtRepositoryMock,times(1)).save(any(PasswordResetToken.class));
				
		assertNotNull("Password reset token must not be null", prt);
	}
	
	@Test
	public void shouldReturnNullResetTokenWhenUserNotFound() {
		when(userServiceMock.findByEmail(anyString())).thenReturn(null);
		
		PasswordResetToken ptr = passwordResetTokenService.createPasswordResetToken("john@example.com");
		
		verify(userServiceMock,times(1)).findByEmail(anyString());
		verifyZeroInteractions(prtRepositoryMock);
		
		assertNull("Password reset token must be null", ptr);
	}
}
