package eu.kalodiodev.springjumpstart.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import eu.kalodiodev.springjumpstart.command.FeedbackForm;
import eu.kalodiodev.springjumpstart.service.EmailService;

/**
 * Abstract Email Service provides send Feedback email functionality
 * 
 * @author Athanasios Raptodimos
 */
public abstract class AbstractEmailService implements EmailService {
	
	@Value("${email.to.address}")
	private String toAddress;
	
	 /**
     * Creates a Simple Mail Message from a {@link FeedbackForm}
     * 
     * @param feedback The Feedback Form
     * @return The simple mail message
     */
    protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(FeedbackForm feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAddress);
        message.setFrom(feedback.getEmail());
        message.setReplyTo(feedback.getEmail());
        message.setSubject("[Security GPS Tracker]: Feedback received from " + 
        		feedback.getFirstName() + " " + feedback.getLastName() + "!");
        message.setText("User with email: " + feedback.getEmail() + " left this feedback:\n" + feedback.getMessage());
        return message;
    }

	@Override
	public void sendFeedback(FeedbackForm feedbackForm) {
		sendEmail(prepareSimpleMailMessageFromFeedbackPojo(feedbackForm));		
	}
}
