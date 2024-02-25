package telran.java51.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.java51.accounting.model.Role;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
	
    String login;
    Set<Role> roles;

}
