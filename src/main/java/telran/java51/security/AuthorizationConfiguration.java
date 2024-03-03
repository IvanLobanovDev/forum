package telran.java51.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import telran.java51.accounting.model.Role;
import telran.java51.post.dao.PostRepository;
import telran.java51.post.dto.exceptions.PostNotFoundException;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {

	final CustomWebSecurity customWebSecurity;

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());
//		включение куки. По умолчанию выключено из-за современных мультиинстансовых сервисов
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/account/register", "/forum/posts/**")
				.permitAll().requestMatchers("/account/user/{login}/role/{role}").hasRole(Role.ADMINISTRATOR.name())
				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
//				Проверка что логин и объект authentication совпадают
				.access(new WebExpressionAuthorizationManager("#login == authentication.name"))
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager(
						"#login == authentication.name or hasRole('ADMINISTRATOR')"))
				.requestMatchers(HttpMethod.POST, "/forum/post/{author}")
				.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
				.requestMatchers(HttpMethod.PUT, "/forum/post/*/comment/{author}")
				.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
//					Кастомный метод сверяющий автора поста и имя  authentication совпадают

//				======== Not actual solution =======
//				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
//					.access(new WebExpressionAuthorizationManager("@customWebSecurity.checkPostAuthor(#id, authentication.name)"))

//				======== NEW SOLUTION =======

				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
				.access((authentication,
						context) -> new AuthorizationDecision(customWebSecurity
								.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName())))

				.requestMatchers(HttpMethod.DELETE, "/forum/post/{id}")
				.access((authentication,
						context) -> new AuthorizationDecision(customWebSecurity
								.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName())
								|| context.getRequest().isUserInRole(Role.MODERATOR.name())))
				.anyRequest().authenticated()

		);
		return http.build();
	}

}

//Реализовать методы UpdatePost и DeletePost
//поправить чтобы роли можно было не только капсом писать
