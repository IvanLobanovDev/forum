package telran.java51.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

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

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			User userAccount;
			try {
				String[] credentials = getCredentials(request.getHeader("Authorization"));
				userAccount = userRepository.findById(credentials[0]).orElseThrow(RuntimeException::new);
				if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				// в случае ошибки, важно прервать работу фильтра, чтобы программа не добралась
				// до chain.doFilter(request, response);
				response.sendError(401);
				return;
			}
			request = new WrappedRequest(request, userAccount.getLogin());
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

		public WrappedRequest(HttpServletRequest request, String login) {
			super(request);
			this.login = login;
		}

//		Principal это интерфейс, который используется для того, чтобы после прохождения авторизации оставлять только имя авторизованного и отбрасывать пароль
		@Override
		public Principal getUserPrincipal() {
			return () -> login;
		}

	}

}
