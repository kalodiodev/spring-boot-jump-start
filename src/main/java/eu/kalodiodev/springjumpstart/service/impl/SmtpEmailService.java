package eu.kalodiodev.springjumpstart.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Implementation of Email Service for Production Environment
 * 
 * @author Athanasios Raptodimos
 */
@Profile("production")
@Service
public class SmtpEmailService extends AbstractEmailService {

	/** The application logger */
	private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);

	@Autowired
	private MailSender mailSender;

	@Override
	public void sendEmail(SimpleMailMessage mail) {
		log.debug("Sending email for: {}", mail);
		mailSender.send(mail);
		log.info("Email sent.");
	}
}
