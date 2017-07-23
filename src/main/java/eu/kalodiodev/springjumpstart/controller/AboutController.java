package eu.kalodiodev.springjumpstart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * About Controller
 * 
 * @author Athanasios Raptodimos
 */
@Controller
public class AboutController {
	
	/* The about view name */
	private static final String ABOUT_VIEW_NAME = "about";
	
	@RequestMapping("/about")
	public String about() {
		return ABOUT_VIEW_NAME;
	}
}
