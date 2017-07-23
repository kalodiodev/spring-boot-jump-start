package eu.kalodiodev.springjumpstart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Index Controller
 * 
 * @author Athanasios Raptodimos
 */
@Controller
public class IndexController {
	
	/* Home view name */
	private static final String HOME_VIEW_NAME = "index";
	
	/* Login view name */
	private static final String LOGIN_VIEW_NAME = "login";
	
	@RequestMapping("/")
	public String home() {
		return HOME_VIEW_NAME;
	}
	
	@RequestMapping("/login")
	public String login() {
		return LOGIN_VIEW_NAME;
	}
}
