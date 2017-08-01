package eu.kalodiodev.springjumpstart.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.kalodiodev.springjumpstart.command.UserForm;
import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.exception.EmailExistsException;
import eu.kalodiodev.springjumpstart.repository.UserRepository;
import eu.kalodiodev.springjumpstart.service.RoleService;
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
	private RoleService roleService;
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
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

	@Transactional
	@Override
	public User register(UserForm userForm) throws EmailExistsException {
		
		if(emailExist(userForm.getEmail())) {
			throw new EmailExistsException(
					"There is already an account with this email address: " + userForm.getEmail());
		}
		
		User user = new User();
		user.setFirstName(userForm.getFirstName());
		user.setLastName(userForm.getLastName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setRoles(Arrays.asList(roleService.findByName("ROLE_USER")));
		
		return saveOrUpdate(user);
	}
	
	@Override
	public void updatePassword(Long userId, String newPassword) {
		userRepository.updateUserPassword(userId, passwordEncoder.encode(newPassword));
	}
	
	//----------------> Private Methods
	
	private boolean emailExist(String email) {
		User user = findByEmail(email);
		
		return user != null;
	}
}