package eu.kalodiodev.springjumpstart.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Implementation of email service for Development environment
 * 
 * @author Athanasios Raptodimos
 */
@Profile("development")
@Service
public class DevEmailService extends AbstractEmailService {

	/** The application logger */
	private static final Logger log = LoggerFactory.getLogger(DevEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage mail) {
		log.debug("Simulating an email service...");
		log.info(mail.toString());
		log.debug("Email sent.");
	}
}
