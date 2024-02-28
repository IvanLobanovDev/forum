package telran.java51.accounting.service;

import telran.java51.accounting.dto.CreateUserDto;
import telran.java51.accounting.dto.RoleDto;
import telran.java51.accounting.dto.UpdateUserDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.model.Role;

public interface UserService {
	
	UserDto registerUser(CreateUserDto createUserDto);
	
	UserDto login();
	
	UserDto deleteUser(String user);
	
	UserDto updateUser(UpdateUserDto updateUserDto, String user);
	
	RoleDto addUserRole(String user, Role role);
	
	RoleDto deleteUserRole(String user, Role role);
	
	void changePassword(String user, String newPassword);
	
	UserDto getUser(String user);

	void run(String... args) throws Exception;

}
