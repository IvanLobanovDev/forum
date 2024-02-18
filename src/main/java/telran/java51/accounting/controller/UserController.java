package telran.java51.accounting.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dto.CreateUserDto;
import telran.java51.accounting.dto.RoleDto;
import telran.java51.accounting.dto.UpdateUserDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserController {
	
	final UserService userService;

	@PostMapping ("/register")
	public UserDto registerUser(@RequestBody CreateUserDto createUserDto) {
		return userService.registerUser(createUserDto);
	}
	

//	@PostMapping("/login")
//	public UserDto login() {
//		return userService.getUser(userId);
//	}

	@DeleteMapping("/user/{user}")
	public UserDto deleteUser(@PathVariable String user) {
		return userService.deleteUser(user);
	}

	@PutMapping("/user/{user}")
	public UserDto updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable String user) {
		return userService.updateUser(updateUserDto, user);
	}

	@PutMapping("/user/{user}/role/{role}")
	public RoleDto addUserRole(@PathVariable String user, @PathVariable String role) {
		return userService.addUserRole(user, role);
	}

	@DeleteMapping("/user/{user}/role/{role}")
	public RoleDto deleteUserRole(@PathVariable String user, @PathVariable String role) {
		return userService.deleteUserRole(user, role);
	}

	@PutMapping("/password")
	public boolean changePassword() {
		// TODO Auto-generated method stub
		return false;
	}

	@GetMapping("/user/{user}")
	public UserDto getUser(@PathVariable String user) {
		return userService.getUser(user);
	}

}
