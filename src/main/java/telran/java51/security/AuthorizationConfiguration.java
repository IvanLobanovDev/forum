package telran.java51.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthoritiesAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import telran.java51.accounting.model.Role;

@Configuration
public class AuthorizationConfiguration {
	
	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/account/register", "/forum/posts/**")
					.permitAll()
				.requestMatchers("/account/user/{login}/role/{role}")
					.hasRole(Role.ADMINISTRATOR.name())
				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
//				Проверка что логин и объект authentication совпадают
					.access(new WebExpressionAuthorizationManager("#login == authentication.name"))
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
					.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')"))
				.requestMatchers(HttpMethod.POST, "/forum/post/{author}")
					.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
				.requestMatchers(HttpMethod.PUT, "/forum/post/*/comment/{author}")
					.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
//					Кастомный метод сверяющий автора поста и имя  authentication совпадают
				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
					.access(new WebExpressionAuthorizationManager("@customSecurity.checkPostAuthor(#id, authentication.name)"))
//				.access(new AuthoritiesAuthorizationManager<T>() {
//					
//					@Override
//					public AuthorizationDecision check(Supplier<Authrntication> )
//				})
				.anyRequest()
					.authenticated()
				
				);
		return http.build();
	}

}

//Реализовать методы UpdatePost и DeletePost
//поправить чтобы роли можно было не только капсом писать
