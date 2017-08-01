package eu.kalodiodev.springjumpstart.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.kalodiodev.springjumpstart.command.validator.PasswordMatches;

/**
 * Abstract command object for passwords
 * 
 * <p>Command objects provide password change must extend this class</p>
 * 
 * @author Athanasios Raptodimos
 */
@PasswordMatches
public abstract class AbstractPasswordForm {

	@NotNull
	@NotEmpty
	@Size(min = 6)
	protected String password;
	
	@NotNull
	@NotEmpty
	@Size(min = 6)
	protected String matchingPassword;
	
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
}
