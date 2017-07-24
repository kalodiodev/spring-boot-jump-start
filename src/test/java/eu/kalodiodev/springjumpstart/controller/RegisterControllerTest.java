package eu.kalodiodev.springjumpstart.controller;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.kalodiodev.springjumpstart.command.UserForm;
import eu.kalodiodev.springjumpstart.exception.EmailExistsException;
import eu.kalodiodev.springjumpstart.service.UserService;

/**
 * Test for {@link RegisterController}
 * 
 * @author Athanasios Raptodimos
 */
public class RegisterControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private RegisterController registerController;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
	}
	
	@Test
	public void shouldShowRegistrationFormPage() throws Exception {
		mockMvc.perform(get("/register/"))
			.andExpect(status().isOk())
			.andExpect(view().name("register"))
			.andExpect(model().attribute("userForm", instanceOf(UserForm.class)));
	}
	
	@Test
	public void shouldRedirectAfterSuccessfullRegistration() throws Exception {
		UserForm userForm = testUserForm();
		
		mockMvc.perform(post("/register/")
				.param("email", userForm.getEmail())
				.param("firstName", userForm.getFirstName())
				.param("lastName", userForm.getLastName())
				.param("password", userForm.getPassword())
				.param("matchingPassword", userForm.getMatchingPassword()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void shouldShowRegistrationFormWhenEmailAlreadyExists() throws Exception {
		UserForm userForm = testUserForm();
		
		when(userService.register(any(UserForm.class)))
			.thenThrow(new EmailExistsException("Email already exists"));
		
		mockMvc.perform(post("/register/")
				.param("firstName", userForm.getFirstName())
				.param("lastName", userForm.getLastName())
				.param("email", userForm.getEmail())
				.param("password", userForm.getPassword())
				.param("matchingPassword", userForm.getMatchingPassword()))
			.andExpect(status().isOk())
			.andExpect(view().name("register"));
	}
	
	//---------------> Private methods
	
	private UserForm testUserForm() {
		UserForm userForm = new UserForm();
		userForm.setEmail("johndoe@example.com");
		userForm.setFirstName("John");
		userForm.setLastName("Doe");
		userForm.setPassword("1234567890");
		userForm.setMatchingPassword("1234567890");
		
		return userForm;
	}
}