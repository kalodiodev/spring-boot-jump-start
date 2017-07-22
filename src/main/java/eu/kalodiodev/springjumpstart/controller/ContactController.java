package eu.kalodiodev.springjumpstart.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.kalodiodev.springjumpstart.command.FeedbackForm;
import eu.kalodiodev.springjumpstart.service.EmailService;

/**
 * Contact Controller
 * 
 * @author Athanasios Raptodimos
 */
@Controller
public class ContactController {
	
	/* The application LOGGER */
	private Logger log = LoggerFactory.getLogger(ContactController.class);
	
	/* The Contact us view name */
	private static final String CONTACT_VIEW_NAME = "contact";
	
	/** The key which identifies the feedback form Model. */
	public static final String FEEDBACK_MODEL_KEY = "feedbackForm";
	
	private EmailService emailService;
	
	@Autowired
	public ContactController(EmailService emailService) {
		this.emailService = emailService;
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Model model) {
		model.addAttribute(FEEDBACK_MODEL_KEY, new FeedbackForm());
		
		return CONTACT_VIEW_NAME;
	}
	
	@RequestMapping(value="/contact", method=RequestMethod.POST)
	public String contactPost(@Valid @ModelAttribute() FeedbackForm feedback, BindingResult bindingResult) {	
		
		if (! bindingResult.hasErrors()) {
			log.debug("Contact Feedback send: {}", feedback);
			emailService.sendFeedback(feedback);
		}
		
		return CONTACT_VIEW_NAME;
	}

}
