package eu.kalodiodev.springjumpstart.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.kalodiodev.springjumpstart.command.ResetPasswordForm;
import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.service.EmailService;
import eu.kalodiodev.springjumpstart.service.PasswordResetTokenService;
import eu.kalodiodev.springjumpstart.service.UserService;
import eu.kalodiodev.springjumpstart.service.security.UserDetailsImpl;

/**
 * Test for {@link ForgotPasswordController}
 * 
 * @author Athanasios Raptodimos
 */
public class ForgotPasswordControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private PasswordResetTokenService prTokenServiceMock;
	
	@Mock
	private EmailService emailServiceMock;
	
	@Mock
	private UserDetailsService userDetailsServiceMock;
	
	@Mock
	private UserService userServiceMock;
	
	@Mock
	private MessageSource msgSourceMock;

	@InjectMocks
	private ForgotPasswordController forgotPasswordController;
	
	@Before
	public void setup() {
		forgotPasswordController = new ForgotPasswordController(
				prTokenServiceMock, emailServiceMock, userDetailsServiceMock, userServiceMock);
		mockMvc = MockMvcBuilders.standaloneSetup(forgotPasswordController).build();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldShowEmailForm() throws Exception {
		mockMvc.perform(get("/reset"))
			.andExpect(status().isOk())
			.andExpect(view().name("resetpassword/emailForm"));
	}
	
	@Test
	public void shouldPostEmail() throws Exception {	
		User user = new User();
		user.setId(1L);
		user.setEmail("test@example.com");
		
		PasswordResetToken prToken = new PasswordResetToken();
		prToken.setUser(user);
		
		when(prTokenServiceMock.createPasswordResetToken(user.getEmail())).thenReturn(prToken);

		mockMvc.perform(post("/reset")
				.param("email", user.getEmail()))
			.andExpect(status().isOk())
			.andExpect(view().name("resetpassword/emailForm"));
		
		verify(prTokenServiceMock).createPasswordResetToken(user.getEmail());
		verify(emailServiceMock).sendEmail(any(SimpleMailMessage.class));
	}
	
	@Test
	public void shouldShowChangePasswordForm() throws Exception {
		String token = "mytoken";
		User user = new User();
		user.setId(1L);
		
		PasswordResetToken prToken = new PasswordResetToken(token, user, ZonedDateTime.now(), 120);
		
		when(prTokenServiceMock.findByToken(token)).thenReturn(prToken);
		when(userDetailsServiceMock.loadUserByUsername(user.getEmail())).thenReturn(new UserDetailsImpl(user));
		
		mockMvc.perform(get("/changepassword")
				.param("id", "1")
				.param("token", token))
			.andExpect(status().isOk())
			.andExpect(view().name("resetpassword/setNewPassword"))
			.andExpect(model().attribute("passwordForm", instanceOf(ResetPasswordForm.class)));
	}
	
	@Test
	public void shouldRedirectWhenNullParamsWhileChangePassword() throws Exception {
		mockMvc.perform(get("/changepassword"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void shouldRedirectWhenInvalidTokenWhileChangePassword() throws Exception {
		String token = "mytoken";
		when(prTokenServiceMock.findByToken(token)).thenReturn(null);
		
		mockMvc.perform(get("/changepassword")
				.param("id", "1")
				.param("token", token))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void shouldRedirectWhenTokenExpired() throws Exception {
		String token = "mytoken";
		User user = new User();
		user.setId(1L);
		
		// expired token
		PasswordResetToken prToken = new PasswordResetToken(
				token, user, ZonedDateTime.now().minusMinutes(200), 120);
		
		when(prTokenServiceMock.findByToken(token)).thenReturn(prToken);
		when(userDetailsServiceMock.loadUserByUsername(user.getEmail())).thenReturn(new UserDetailsImpl(user));
		
		mockMvc.perform(get("/changepassword")
				.param("id", "1")
				.param("token", token))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void shouldRedirectWhenUserDoesNotMatchAuthenticatedWhileChangePassword() throws Exception {
		String token = "mytoken";
		User user = new User();
		user.setId(2L);
		
		// expired token
		PasswordResetToken prToken = new PasswordResetToken(token, user, ZonedDateTime.now(), 120);
		
		when(prTokenServiceMock.findByToken(token)).thenReturn(prToken);
		when(userDetailsServiceMock.loadUserByUsername(user.getEmail())).thenReturn(new UserDetailsImpl(user));
		
		mockMvc.perform(get("/changepassword")
				.param("id", "1")
				.param("token", token))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void shouldPostPasswordChange() throws Exception {		
		String password = "123456789";
		User user = new User();
		user.setEmail("test@example.com");
		user.setId(1L);
		
		// Set authentication
		SecurityContext testSecurityContext = createTestAuthentication(user);
		
		// Perform post
		mockMvc.perform(post("/changepassword/").with(securityContext(testSecurityContext))
				.param("userId", "1")
				.param("password", password)
				.param("matchingPassword", password))
			.andExpect(status().isOk())
			.andExpect(view().name("resetpassword/setNewPassword"))
			.andExpect(model().attribute("passwordChanged", true));
		
		verify(userServiceMock).updatePassword(1L, password);
	}
	
	@Test
	public void shouldNotPostMismatchingPasswords() throws Exception {
		String password = "123456789";
		User user = new User();
		user.setEmail("test@example.com");
		user.setId(1L);
		
		// Set authentication
		SecurityContext testSecurityContext = createTestAuthentication(user);
		
		// Perform post
		mockMvc.perform(post("/changepassword/").with(securityContext(testSecurityContext))
				.param("userId", "1")
				.param("password", password)
				.param("matchingPassword", "1234567890"))
			.andExpect(status().isOk())
			.andExpect(view().name("resetpassword/setNewPassword"));
		
		verifyZeroInteractions(userServiceMock);
	}
	
	@Test
	public void shouldNotPostEmptyPasswords() throws Exception {
		String password = "";
		User user = new User();
		user.setEmail("test@example.com");
		user.setId(1L);
		
		// Set authentication		
		SecurityContext testSecurityContext = createTestAuthentication(user);
		
		// Perform post
		mockMvc.perform(post("/changepassword/").with(securityContext(testSecurityContext))
				.param("userId", "1")
				.param("password", password)
				.param("matchingPassword", password))
			.andExpect(status().isOk())
			.andExpect(view().name("resetpassword/setNewPassword"));
		
		verifyZeroInteractions(userServiceMock);
	}
	
	//----------------> Private methods
	
	private SecurityContext createTestAuthentication(User user) {
		Authentication auth = new UsernamePasswordAuthenticationToken(
				user, null, new UserDetailsImpl(user).getAuthorities());
		
		SecurityContext testSecurityContext = SecurityContextHolder.getContext();
		testSecurityContext.setAuthentication(auth);
		
		return testSecurityContext;
	}

}
