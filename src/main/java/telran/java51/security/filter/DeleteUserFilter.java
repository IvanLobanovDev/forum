package telran.java51.security.filter;

import java.io.IOException;
import java.security.Principal;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserRepository;
import telran.java51.accounting.model.User;

@Component
@RequiredArgsConstructor
@Order(40)
public class DeleteUserFilter implements Filter {

	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		User userAccount = userRepository.findById(request.getUserPrincipal().getName()).get();
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
//			проверяем, что у пользователя есть либо роль админ, либо принципал соответствует последнему элементу эндпоинта
			Principal principal = request.getUserPrincipal();
			String[] arr = request.getServletPath().split("/");
			String user = arr[arr.length - 1];

			if (!(userAccount.getRoles().contains("ADMINISTRATOR") || principal.getName().equalsIgnoreCase(user))) {
				response.sendError(403, "Permition denied");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.DELETE.matches(method) && path.matches("/account/user/\\w+");
	}

}
