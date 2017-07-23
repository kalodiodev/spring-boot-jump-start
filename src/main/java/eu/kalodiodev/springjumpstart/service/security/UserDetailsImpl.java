package eu.kalodiodev.springjumpstart.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import eu.kalodiodev.springjumpstart.domain.User;
import eu.kalodiodev.springjumpstart.domain.security.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * User Details Implementation
 *
 * @author Athanasios Raptodimos
 */
public class UserDetailsImpl implements UserDetails {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3008845978120182442L;
	
	private User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        List<Role> roles = user.getRoles();
        for( Role role : roles ) {
            authorities.add( new SimpleGrantedAuthority(role.getRole()) );
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getEncryptedPassword();
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
