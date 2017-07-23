package eu.kalodiodev.springjumpstart.domain.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import eu.kalodiodev.springjumpstart.domain.AbstractDomain;
import eu.kalodiodev.springjumpstart.domain.User;

/**
 * User Role domain object
 * 
 * @author Athanasios Raptodimos
 */
@Entity
public class Role extends AbstractDomain {

    private String role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<User> users = new ArrayList<>();

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        if(!this.users.contains(user)){
            this.users.add(user);
        }

        if(!user.getRoles().contains(this)){
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user){
        this.users.remove(user);
        user.getRoles().remove(this);
    }
}
