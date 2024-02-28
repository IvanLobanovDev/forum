package telran.java51.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserRepository;
import telran.java51.accounting.model.User;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userAccount = userRepository.findById(username)
//				эксепшен который бросается, если пытаться злогиниться под именем пользователя, которого нет
				.orElseThrow(() -> new UsernameNotFoundException(username));
		Collection<String> authorities = userAccount.getRoles()
						.stream()
						.map(r -> "ROLE_" + r.name())
						.collect(Collectors.toList());
//		У фреймворка все объекты свои, поэтому мы создаем объект User спринга и кладем в него имя, пароль и роли и дальше по цепочке идет уже этот объект
		return new org.springframework.security.core.userdetails.User(username, userAccount.getPassword(), 
				AuthorityUtils.createAuthorityList(authorities));
	}

}
