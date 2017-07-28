package eu.kalodiodev.springjumpstart.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import eu.kalodiodev.springjumpstart.domain.util.JSR310PersistenceConverters.ZonedDateTimeConverter;

/**
 * Domain object for Password reset token
 * 
 * @author Athanasios Raptodimos
 */
@Entity
public class PasswordResetToken {
	
	/* Token is valid for a specific time length in minutes */
	private static final int EXPIRATION_LENGTH = 60 * 2;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String token;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	
	@Convert(converter = ZonedDateTimeConverter.class)
	private ZonedDateTime expiryDate;
	
	/**
	 * Default Constructor 
	 */
	public PasswordResetToken() {
		
	}
	
	/**
	 * Full Constructor
	 * 
	 * @param token User's token
	 * @param user User to create token for
	 * @param creationDateTime The date time token created
	 * @param expirationInMinutes The length, in minutes, for which token will be valid
	 * @throws IllegalArgumentException If the token, user or creation date time are null
	 */
	public PasswordResetToken(String token, User user, ZonedDateTime creationDateTime, int expirationInMinutes) {
		// Validate arguments
		if((token == null) || (user == null) || (creationDateTime == null)) {
			throw new IllegalArgumentException("token, user and creation date time can't be null");
		}
		if(expirationInMinutes == 0) {
			// use default expiration time period
			expirationInMinutes = EXPIRATION_LENGTH;
		}
		
		this.token = token;
		this.user = user;
		expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ZonedDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(ZonedDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
}
