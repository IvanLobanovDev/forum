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
@Order(40)
public class DeleteUserFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
//			проверяем, что у пользователя есть либо роль админ, либо принципал соответствует последнему элементу эндпоинта
			telran.java51.security.model.User user = (telran.java51.security.model.User) request.getUserPrincipal();
			String[] arr = request.getServletPath().split("/");
			String userFromPath = arr[arr.length - 1];

			if (!(user.getRoles().contains("ADMINISTRATOR") || user.getName().equalsIgnoreCase(userFromPath))) {
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
