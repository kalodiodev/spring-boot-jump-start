package eu.kalodiodev.springjumpstart.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.service.EmailService;
import eu.kalodiodev.springjumpstart.service.PasswordResetTokenService;
import eu.kalodiodev.springjumpstart.util.UserUtils;

/**
 * Forgot password controller 
 * 
 * @author Athanasios Raptodimos
 */
@Controller
public class ForgotPasswordController {
	
	/* Logger */
	private static final Logger log = LoggerFactory.getLogger(ForgotPasswordController.class);
	
	@Autowired
	private MessageSource msgSource;
	
	@Value("${webmaster.email")
	private String webMasterEmail;
	
	/** Email Form View name */
	public static final String EMAIL_FORM_VIEW_NAME = "resetpassword/emailForm";
	
	/** Forgot password url mapping */
	public static final String FORGOT_PASSWORD_URL = "/reset";
	
	/** Change password url mapping */
	public static final String CHANGE_PASSWORD_URL = "/changepassword";

	public static final String MAIL_SENT_KEY = "mailSent";
	
	private PasswordResetTokenService passwordResetTokenService;
	private EmailService emailService;
	
	@Autowired	
	public ForgotPasswordController(PasswordResetTokenService passwordResetTokenService, EmailService emailService) {
		this.passwordResetTokenService = passwordResetTokenService;
		this.emailService = emailService;
	}

	@RequestMapping(value = FORGOT_PASSWORD_URL, method = RequestMethod.GET)
	public String emailForm() {
		return EMAIL_FORM_VIEW_NAME;
	}
	
	@RequestMapping(value = FORGOT_PASSWORD_URL, method = RequestMethod.POST)
	public String postEmail(HttpServletRequest request, @RequestParam("email") String email, Model model) {
		
		// create token
		log.debug("Requested reset password token for email {}", email);	
		PasswordResetToken prToken = passwordResetTokenService.createPasswordResetToken(email);
		
		if(prToken != null) {
			// create Reset url
			String resetUrl = UserUtils.passwordResetUrl(request, prToken.getUser().getId(), prToken.getToken());
			log.debug("Reset password URL {}", resetUrl);
			
			// Send email
			emailService.sendEmail(constructResetTokenEmail(prToken.getUser(), resetUrl, request));
		} else {
			log.warn("Could not create password reset token for email {}", email);
		}
		
		// attribute used to show alert in view
		model.addAttribute(MAIL_SENT_KEY, true);
		
		return EMAIL_FORM_VIEW_NAME;
	}
	
	
	// -------------> Private methods
		
	private SimpleMailMessage constructResetTokenEmail(User user, String resetUrl, HttpServletRequest request) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setSubject(msgSource.getMessage("mail.password.reset.subject", null, request.getLocale()));
		mail.setText(msgSource.getMessage("mail.password.reset.body", null, request.getLocale()) + "\r\n" + resetUrl);
		mail.setFrom(webMasterEmail);
		
		return mail;
	}
}
