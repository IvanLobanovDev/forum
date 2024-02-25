package telran.java51.security.filter;

import java.io.IOException;

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
@Order(30)
public class UpdateUserOrAddPostOrAddCommentFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
//			проверяем, что принципал соответствует последнему элементу эндпоинта
			if (!request.getUserPrincipal().getName().equalsIgnoreCase(
					request.getServletPath().split("/")[request.getServletPath().split("/").length - 1])) {
				response.sendError(403, "Permition denied");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.PUT.matches(method) && path.matches("/account/user/\\w+")
				|| (HttpMethod.POST.matches(method) && path.matches("/forum/post/\\w+"))
				|| (HttpMethod.PUT.matches(method) && path.matches("/forum/post/\\w+/comment/\\w+"));
	}

}
