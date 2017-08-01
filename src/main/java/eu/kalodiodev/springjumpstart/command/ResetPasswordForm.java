package eu.kalodiodev.springjumpstart.command;

import javax.validation.constraints.NotNull;

/**
 * Command object for password reset
 * 
 * @author Athanasios Raptodimos
 */
public class ResetPasswordForm extends AbstractPasswordForm {

	@NotNull
	private Long userId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}	
}
