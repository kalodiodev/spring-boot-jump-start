package eu.kalodiodev.springjumpstart.controller;

import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eu.kalodiodev.springjumpstart.command.ResetPasswordForm;
import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.service.EmailService;
import eu.kalodiodev.springjumpstart.service.PasswordResetTokenService;
import eu.kalodiodev.springjumpstart.service.UserService;
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
	
	/** Reset password View name */
	public static final String RESET_PASSWORD_VIEW_NAME = "resetpassword/setNewPassword";
	
	/** Forgot password url mapping */
	public static final String FORGOT_PASSWORD_URL = "/reset";
	
	/** Change password url mapping */
	public static final String CHANGE_PASSWORD_URL = "/changepassword";
	
	/** The key which identifies the reset password form Model. */
	public static final String RESET_PASSWORD_MODEL_KEY = "passwordForm";

	public static final String MAIL_SENT_KEY = "mailSent";
	public static final String PASSWORD_CHANGED_KEY = "passwordChanged";
	
	private PasswordResetTokenService passwordResetTokenService;
	private EmailService emailService;
	private UserDetailsService userDetailsService;
	private UserService userService;
	
	@Autowired	
	public ForgotPasswordController(PasswordResetTokenService passwordResetTokenService, 
			EmailService emailService, UserDetailsService userDetailsService, UserService userService) {
		
		this.passwordResetTokenService = passwordResetTokenService;
		this.emailService = emailService;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
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
	
	@RequestMapping(value = CHANGE_PASSWORD_URL, method = RequestMethod.GET)
	public String resetPassword(@RequestParam(name = "id", defaultValue = "0", required = false) Long id, 
			@RequestParam(name = "token", required = false) String token, Model model) {
		
		if(id == 0 || StringUtils.isEmpty(token)) {
			log.error("Invalid user id {} or token value {}", id, token);
			return "redirect:/";
		}
		
		PasswordResetToken prToken = passwordResetTokenService.findByToken(token);
		
		if(prToken == null) {
			log.warn("Token could not be found {}", token);
			return "redirect:/";
		}
		
		User user = prToken.getUser();
		
		if(user.getId() != id) {
			log.error("The user id {} provided does not match the user id {} associated with the token {}", 
					id, user.getId(), token);
			
			return "redirect:/";
		}
		
		if(ZonedDateTime.now().isAfter(prToken.getExpiryDate())) {
			log.error("The token {} has expired", token);
			
			return "redirect:/";
		}
		
		ResetPasswordForm resetPasswordForm = new ResetPasswordForm();
		resetPasswordForm.setUserId(user.getId());
		model.addAttribute(RESET_PASSWORD_MODEL_KEY, resetPasswordForm);
		
		// Authenticating user
		Authentication auth = new UsernamePasswordAuthenticationToken(
				user, null, userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
				
		return RESET_PASSWORD_VIEW_NAME;
	}
	
	@RequestMapping(value = CHANGE_PASSWORD_URL, method = RequestMethod.POST)
	public String changePassword(@ModelAttribute(RESET_PASSWORD_MODEL_KEY) @Valid ResetPasswordForm passwordForm, 
			BindingResult bindingResult, Model model) {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null) {
			log.error("Unathenticated user tried to POST reset password");	
			return "redirect:/";
		}
		
		if(bindingResult.hasErrors()) {
			log.debug("Password reset form has errors");
			return RESET_PASSWORD_VIEW_NAME;
		}
		
		User user = (User) authentication.getPrincipal();
		if(user.getId() != passwordForm.getUserId()) {
			log.error("Security breach! User {} is trying to make password reset to user with id {}",
					user.getId(), passwordForm.getUserId());
			
			return "redirect:/";
		}
		
		userService.updatePassword(user.getId(), passwordForm.getPassword());
		log.info("Succesfully updated password of user {}", user.getEmail());
		
		model.addAttribute(PASSWORD_CHANGED_KEY, true);
		
		return RESET_PASSWORD_VIEW_NAME;
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
