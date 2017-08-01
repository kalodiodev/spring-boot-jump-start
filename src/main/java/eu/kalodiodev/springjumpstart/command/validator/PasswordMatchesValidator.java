package eu.kalodiodev.springjumpstart.command.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.kalodiodev.springjumpstart.command.AbstractPasswordForm;

/**
 * Password Matches Validator
 * 
 * Validate password and password confirmation match in User Form
 *
 * @author Athanasios Raptodimos
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AbstractPasswordForm> {
	
	private String message;
	private String firstField;
	private String secondField;
	
	@Override
	public void initialize(PasswordMatches passwordMatches) {
		this.message = passwordMatches.message();
		this.firstField = passwordMatches.firstField();
		this.secondField = passwordMatches.secondField();
	}
	
	@Override
	public boolean isValid(AbstractPasswordForm userForm, ConstraintValidatorContext constraintValidatorContext) {
		boolean isValid = userForm.getPassword().equals(userForm.getMatchingPassword());
		
		if( !isValid ) {
			constraintValidatorContext.disableDefaultConstraintViolation();
			
			// First password field
			constraintValidatorContext.buildConstraintViolationWithTemplate(message)
				.addPropertyNode(this.firstField).addConstraintViolation();
			
			// Second - confirmation password field
			constraintValidatorContext.buildConstraintViolationWithTemplate(message)
				.addPropertyNode(this.secondField).addConstraintViolation();
		}
		
		return isValid;
	}
}