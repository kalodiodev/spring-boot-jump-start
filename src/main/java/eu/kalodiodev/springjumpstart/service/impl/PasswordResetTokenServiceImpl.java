package eu.kalodiodev.springjumpstart.service.impl;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.kalodiodev.springjumpstart.domain.PasswordResetToken;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.repository.PasswordResetTokenRepository;
import eu.kalodiodev.springjumpstart.service.PasswordResetTokenService;
import eu.kalodiodev.springjumpstart.service.UserService;

/**
 * Password Reset Token Service Implementation
 * 
 * @author Athanasios Raptodimos
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
	
	/* Logger */
	private Logger log = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);
	
	private PasswordResetTokenRepository prtRepository;
	private UserService userService;
	
	@Value("${reset.token.expiration.length.minutes}")
	private int tokenExpirationLength;
	
	@Autowired
	public PasswordResetTokenServiceImpl(PasswordResetTokenRepository prtRepository, UserService userService) {
		this.prtRepository = prtRepository;
		this.userService = userService;
	}

	@Override
	public PasswordResetToken findByToken(String token) {
		return prtRepository.findByToken(token);
	}

	@Transactional
	@Override
	public PasswordResetToken createPasswordResetToken(String email) {
				
		User user = userService.findByEmail(email);
		PasswordResetToken prToken = null; 
		
		if(user != null) {
			String token = UUID.randomUUID().toString();
			ZonedDateTime now = ZonedDateTime.now();
			prToken = new PasswordResetToken(token,user, now, tokenExpirationLength);
			
			prToken = prtRepository.save(prToken);		
			
			log.debug("Created token {} for user {}", token, user.getEmail());
			
		} else {
			log.warn("Could not find user with email {}", email);
		}
		
		return prToken;
	}

	@Override
	public void delete(Iterable<PasswordResetToken> tokens) {
		prtRepository.delete(tokens);
	}

	@Override
	public Set<PasswordResetToken> findAllByUserId(Long userId) {
		return prtRepository.findAllByUserId(userId);
	}
}
