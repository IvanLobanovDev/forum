package telran.java51.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserRepository;
import telran.java51.accounting.model.User;
import telran.java51.security.context.SecurityContext;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

	final UserRepository userRepository;
	final SecurityContext securityContext;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String sessionId = request.getSession().getId();
			telran.java51.security.model.User user = securityContext.getUserBySessionId(sessionId);
			if (user == null) {
				try {
					String[] credentials = getCredentials(request.getHeader("Authorization"));
					User userAccount = userRepository.findById(credentials[0]).orElseThrow(RuntimeException::new);
					if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
						throw new RuntimeException();
					}
//					После первого логина создаем userPrincipal и кладем в securityContext
//					Далее проверка пользователя будет выполняться, как securityContext.getUserBySessionId(sessionId)
					user = new telran.java51.security.model.User(userAccount.getLogin(), userAccount.getRoles());
					securityContext.addUserSession(sessionId, user);
				} catch (Exception e) {
					// в случае ошибки, важно прервать работу фильтра, чтобы программа не добралась
					// до chain.doFilter(request, response);
					response.sendError(401);
					return;
				} 
			}
			request = new WrappedRequest(request, user.getName(), user.getRoles());
		}
		chain.doFilter(request, response);

	}

	private boolean checkEndPoint(String method, String path) {
		return !(HttpMethod.POST.matches(method) && (path.matches("/account/register") || 
				                                     path.matches("/forum/posts/tags") || 
				                                     path.matches("/forum/posts/period")) || 
				HttpMethod.GET.matches(method) && path.matches("/forum/posts/author/\\S+"));
	}

//	Логин будет 0 элемент, пароль 1 элемент
	private String[] getCredentials(String header) {
		String token = header.split(" ")[1];
		String decode = new String(Base64.getDecoder().decode(token));
		return decode.split(":");
	}

	private class WrappedRequest extends HttpServletRequestWrapper {

		private String login;
		private Set<String> roles;

		public WrappedRequest(HttpServletRequest request, String login, Set<String> roles) {
			super(request);
			this.login = login;
			this.roles = roles;
		}

//		Principal это интерфейс, который используется для того, чтобы после прохождения авторизации оставлять только имя авторизованного и отбрасывать пароль
		@Override
		public Principal getUserPrincipal() {
			return new telran.java51.security.model.User(login, roles);
		}

	}

}
