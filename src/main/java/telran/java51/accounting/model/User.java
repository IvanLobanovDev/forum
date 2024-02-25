package telran.java51.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")
public class User {
	
	@Id
    String login;
	@Setter
    String password;
    @Setter
    String firstName;
    @Setter
    String lastName;
    Set<Role> roles;
    
    public User() {
    	roles = new HashSet<>();
    	roles.add(Role.USER);
    }
    
	public User(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
	public boolean addRole(Role role) {
		return roles.add(role);
	}
    
	public boolean removeRole(Role role) {
		return roles.remove(role);
	}

}
