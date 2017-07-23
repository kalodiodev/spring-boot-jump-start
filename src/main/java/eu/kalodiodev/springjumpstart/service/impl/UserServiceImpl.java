package eu.kalodiodev.springjumpstart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.repository.UserRepository;
import eu.kalodiodev.springjumpstart.service.UserService;

/**
 * User service implementation
 * 
 * @author Athanasios Raptodimos
 */
@Service
public class UserServiceImpl implements UserService {
	
	/** The application logger */
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public List<User> all() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		
		return users;
	}
	
	@Override
	public User find(Long id) {
		return userRepository.findOne(id);
	}
	
	@Override
	public User findByEmail(String email) {
		log.debug("Find user by email: {}", email);
		
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User saveOrUpdate(User user) {
		log.debug("Saving user: {}", user);
		
		if(user.getPassword() != null) {
			user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		return userRepository.save(user);
	}
	
	@Override
	public void delete(Long id) {
		userRepository.delete(id);
	}
}