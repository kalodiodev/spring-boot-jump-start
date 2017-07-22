package eu.kalodiodev.springjumpstart.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import eu.kalodiodev.springjumpstart.command.FeedbackForm;
import eu.kalodiodev.springjumpstart.service.impl.SmtpEmailService;

/**
 * Email Service Implementation Test
 * 
 * @author Athanasios Raptodimos
 */
@RunWith(JUnit4.class)
public class SmtpEmailServiceTest {
		
	@Mock
	private MailSender mailSender;
	
	@InjectMocks
	private SmtpEmailService smtpEmailService;
	
	@Before
	public void setup() {
		smtpEmailService = new SmtpEmailService();
		MockitoAnnotations.initMocks(this);
	}
		
	@Test
	public void shouldSendEmail() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		smtpEmailService.sendEmail(simpleMailMessage);
		
		verify(mailSender, times(1)).send(simpleMailMessage);
	}
	
	@Test
	public void shouldSendFeedbackEmail() {
		FeedbackForm feedback = new FeedbackForm();
		
		smtpEmailService.sendFeedback(feedback);
		
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
}
