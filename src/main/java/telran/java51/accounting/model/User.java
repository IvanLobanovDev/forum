package telran.java51.accounting.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")
@NoArgsConstructor
public class User {
	
	@Id
    String login;
	@Setter
    String password;
    @Setter
    String firstName;
    @Setter
    String lastName;
    Set<String> roles = new HashSet<>(Arrays.asList("USER"));
    
	public User(String login, String password, String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
	public boolean addRole(String role) {
		return roles.add(role);
	}
    
	public boolean removeRole(String role) {
		return roles.add(role);
	}

}
