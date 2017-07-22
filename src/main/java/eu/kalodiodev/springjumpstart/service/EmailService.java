package eu.kalodiodev.springjumpstart.service;

import org.springframework.mail.SimpleMailMessage;

import eu.kalodiodev.springjumpstart.command.FeedbackForm;

/**
 * Email service contract
 * 
 * @author Athanasios Raptodimos
 */
public interface EmailService {
	
	/**
	 * Send feedback email using {@link FeedbackForm}
	 * 
	 * @param feedbackForm The feedback form
	 */
	void sendFeedback(FeedbackForm feedbackForm);
	
	/**
	 * Send generic email using {@link SimpleMailMessage}
	 * 
	 * @param mail The simple mail message
	 */
	void sendEmail(SimpleMailMessage mail);

}
