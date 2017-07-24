package eu.kalodiodev.springjumpstart.command.validator;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.kalodiodev.springjumpstart.command.UserForm;

/**
 * Password Matcher Validator Test
 *
 * @author Athanasios Raptodimos
 */
public class PasswordMatchesValidatorTest {
	
	private static Validator validator;
	private UserForm userForm;
	
	private static final String passwordMismatchMessageTemplate = "{validation.password.mismatch.message}";
	
	@BeforeClass
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Before
	public void before() {
		userForm = new UserForm();
	}
	
	@Test
	public void testPasswordsMatch() {
		userForm.setPassword("1234");
		userForm.setMatchingPassword("1234");
		Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
		
		// Filter violations by message template, only password mismatch
		violations = violations.stream()
				.filter(e -> e.getMessageTemplate().equals(passwordMismatchMessageTemplate))
				.collect(Collectors.toSet());
		
		Assert.assertEquals("Expecting no violations", 0, violations.size());
	}
	
	@Test
	public void testPasswordsNoMatch() {
		userForm.setPassword("12345");
		userForm.setMatchingPassword("1234");
		
		Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
		
		// Filter violations by message template, only password mismatch
		violations = violations.stream()
				.filter(e -> e.getMessageTemplate().equals(passwordMismatchMessageTemplate))
				.collect(Collectors.toSet());
		
		Assert.assertEquals("Expecting 2 violations for password and password confirmation",
				2, violations.size());
	}
}