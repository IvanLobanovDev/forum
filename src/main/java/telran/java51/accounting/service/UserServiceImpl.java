package telran.java51.accounting.service;

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
		userAccount.getRoles().add(role);
		userRepository.save(userAccount);
		return modelMapper.map(userAccount, RoleDto.class);
	}

	@Override
	public RoleDto deleteUserRole(String user, String role) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userAccount.getRoles().remove(role);
		userRepository.save(userAccount);
		return modelMapper.map(userAccount, RoleDto.class);
	}

	@Override
	public boolean changePassword() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDto getUser(String user) {
		User userAccount = userRepository.findById(user).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(userAccount, UserDto.class);
	}

}
