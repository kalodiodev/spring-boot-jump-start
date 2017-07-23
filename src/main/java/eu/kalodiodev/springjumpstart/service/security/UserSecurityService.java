package eu.kalodiodev.springjumpstart.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.service.UserService;

/**
 * User Details Service
 *
 * @author Athanasios Raptodimos
 */
@Service("userDetailsService")
public class UserSecurityService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserDetailsImpl(user);
    }
}
