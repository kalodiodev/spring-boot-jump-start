package eu.kalodiodev.springjumpstart.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.service.EmailService;
import eu.kalodiodev.springjumpstart.service.PasswordResetTokenService;

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
	private MessageSource msgSourceMock;

	@InjectMocks
	private ForgotPasswordController forgotPasswordController;
	
	@Before
	public void setup() {
		forgotPasswordController = new ForgotPasswordController(prTokenServiceMock, emailServiceMock);
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

}
