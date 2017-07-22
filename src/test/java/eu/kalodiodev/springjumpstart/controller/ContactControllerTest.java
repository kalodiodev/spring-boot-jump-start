package eu.kalodiodev.springjumpstart.controller;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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

import eu.kalodiodev.springjumpstart.command.FeedbackForm;
import eu.kalodiodev.springjumpstart.service.EmailService;

/**
 * Contact Controller Test
 *
 * @author Athanasios Raptodimos
 */
public class ContactControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private EmailService emailService;

	@InjectMocks
    private ContactController contactController;

    @Before
    public void setup(){
    	MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
    }

    @Test
    public void shouldShowContactPage() throws Exception{
        mockMvc.perform(get("/contact/"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attribute("feedbackForm", instanceOf(FeedbackForm.class)));
    }
    
    @Test
    public void shouldPostFeedback() throws Exception {
    	FeedbackForm feedback = feedback();
    	
    	mockMvc.perform(post("/contact/")
    			.param("email", feedback.getEmail())
    			.param("firstName", feedback.getFirstName())
    			.param("lastName", feedback.getLastName())
    			.param("message", feedback.getMessage()))
    		.andExpect(status().isOk())
    		.andExpect(view().name("contact"));
    	
    	verify(emailService, times(1)).sendFeedback(feedback);
    }
    
    @Test
    public void shouldNotPostInvalidFeedback() throws Exception {
    	// new empty feedback form
    	FeedbackForm feedback = new FeedbackForm();
    	
    	mockMvc.perform(post("/contact/")
    			.param("email", feedback.getEmail())
    			.param("firstName", feedback.getFirstName())
    			.param("lastName", feedback.getLastName())
    			.param("message", feedback.getMessage()))
    		.andExpect(status().isOk())
    		.andExpect(view().name("contact"));
    	
    	verifyZeroInteractions(emailService);
    }
    
    
    //----------------------> Private methods
    
    private FeedbackForm feedback() {
    	FeedbackForm feedback = new FeedbackForm();
    	feedback.setEmail("user@example.com");
    	feedback.setFirstName("John");
    	feedback.setLastName("Doe");
    	feedback.setMessage("Test Message");
    	
    	return feedback;
    }

}
