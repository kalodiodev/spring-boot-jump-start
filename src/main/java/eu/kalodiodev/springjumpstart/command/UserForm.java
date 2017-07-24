package eu.kalodiodev.springjumpstart.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import eu.kalodiodev.springjumpstart.command.validator.PasswordMatches;

/**
 * Command object for {@link User}
 * 
 * @author Athanasios Raptodimos
 */
@PasswordMatches
public class UserForm {

	@NotNull
	@NotEmpty
	private String firstName;
	
	@NotNull
	@NotEmpty
	private String lastName;
	
	@NotNull
	@NotEmpty
	@Size(min = 6)
	private String password;
	
	@NotNull
	@NotEmpty
	@Size(min = 6)
	private String matchingPassword;
	
	@NotNull
	@NotEmpty
	@Email
	private String email;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMatchingPassword() {
		return matchingPassword;
	}
	
	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "UserForm [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
}