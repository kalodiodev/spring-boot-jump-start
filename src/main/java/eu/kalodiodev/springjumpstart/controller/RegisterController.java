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

import eu.kalodiodev.springjumpstart.command.UserForm;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.exception.EmailExistsException;
import eu.kalodiodev.springjumpstart.service.UserService;

/**
 * Register controller
 * 
 * @author Athanasios Raptodimos
 */
@Controller
public class RegisterController {
	
	private Logger log = LoggerFactory.getLogger(RegisterController.class);
	
	/* The register view name */
	private static final String REGISTER_VIEW_NAME = "register";
	
	/** The key which identifies the User Form. */
	public static final String USER_MODEL_KEY = "userForm";
	
	private UserService userService;

	@Autowired	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String show(Model model) {
		model.addAttribute(USER_MODEL_KEY, new UserForm());
		
		return REGISTER_VIEW_NAME;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute(USER_MODEL_KEY) @Valid UserForm userForm,
			BindingResult bindingResult) {

		log.debug("Registering user: {}", userForm);
		
		if(bindingResult.hasErrors()) {
			return REGISTER_VIEW_NAME;
		}
		
		// Register user
		try {
			User user = userService.register(userForm);
			log.debug("User registered: {}", user);
		} catch (EmailExistsException e) {
			bindingResult.rejectValue("email",
					"register.email.exists.message",
					"There is already an account with this email address");
			
			return REGISTER_VIEW_NAME;
		}
		
		return "redirect:/";
	}
}