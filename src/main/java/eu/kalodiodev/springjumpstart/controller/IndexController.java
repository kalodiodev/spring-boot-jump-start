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
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}
}
