package eu.kalodiodev.springjumpstart.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import eu.kalodiodev.springjumpstart.domain.security.Role;

/**
 * User Domain Object
 * 
 * @author Athanasios Raptodimos
 */
@Entity
public class User extends AbstractDomain {
	
	private String firstName;
	private String lastName;
	
	@Column( unique=true, nullable=false )
	private String email;
	
	/* User account status */
	private Boolean enabled = true;
	
	/* Should not be stored in database, password must be encrypted */
	@Transient
	private String password;
	
	@Column( nullable=false )
	private String encryptedPassword;
	
	// User has many roles
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable
	private List<Role> roles = new ArrayList<>();
	
	@OneToMany(
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY,
		mappedBy = "user"
	)
	private Set<PasswordResetToken> passwordResetTokens = new HashSet<>();
	
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role){
		if(!this.roles.contains(role)){
			this.roles.add(role);
		}
		
		if(!role.getUsers().contains(this)){
			role.getUsers().add(this);
		}
	}
	
	public void removeRole(Role role){
		this.roles.remove(role);
		role.getUsers().remove(this);
	}
	
	public Set<PasswordResetToken> getPasswordResetTokens() {
		return passwordResetTokens;
	}
	
	public void setPasswordResetTokens(Set<PasswordResetToken> passwordResetTokens) {
		this.passwordResetTokens = passwordResetTokens;
	}
	
	@Override
	public String toString() {
		return "User{" + 
				"id=" + getId() + 
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}