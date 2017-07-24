package eu.kalodiodev.springjumpstart.exception;

/**
 * Email exists exception
 * 
 * @author Athanasios Raptodimos
 */
public class EmailExistsException extends RuntimeException {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 4465251831332041466L;
	
	public EmailExistsException(final String message) {
		super(message);
	}

}