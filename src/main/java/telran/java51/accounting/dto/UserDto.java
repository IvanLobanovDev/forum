package telran.java51.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import telran.java51.accounting.model.Role;

@Getter
public class UserDto {
	
    String login;
    String firstName;
    String lastName;
    Set<Role> roles;

}
