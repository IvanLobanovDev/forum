package telran.java51.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserRepository;
import telran.java51.accounting.dto.CreateUserDto;
import telran.java51.accounting.dto.RoleDto;
import telran.java51.accounting.dto.UpdateUserDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.exceptions.UserNotFoundException;
import telran.java51.accounting.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	final UserRepository userRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto registerUser(CreateUserDto createUserDto) {
		if(userRepository.existsById(createUserDto.getLogin())) {
			return null;
		} else {
			User user = modelMapper.map(createUserDto, User.class);
			String password = BCrypt.hashpw(createUserDto.getPassword(), BCrypt.gensalt());
			user.setPassword(password);
			userRepository.save(user);
			return modelMapper.map(user, UserDto.class);
		}
	}

	@Override
	public UserDto login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto deleteUser(String user) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userRepository.deleteById(user);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(UpdateUserDto updateUserDto, String user) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userAccount.setFirstName(updateUserDto.getFirstName());
		userAccount.setLastName(updateUserDto.getLastName());
		userRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RoleDto addUserRole(String user, String role) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userAccount.addRole(role);
		userRepository.save(userAccount);
		return modelMapper.map(userAccount, RoleDto.class);
	}

	@Override
	public RoleDto deleteUserRole(String user, String role) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userAccount.removeRole(role);
		userRepository.save(userAccount);
		return modelMapper.map(userAccount, RoleDto.class);
	}

	@Override
	public boolean changePassword(String user, String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDto getUser(String user) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(userAccount, UserDto.class);
	}

}
