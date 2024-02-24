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
@Order(25)
@RequiredArgsConstructor
public class AddPostOrCommentFilter implements Filter {

	final UserRepository userRepository;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		User userAccount = userRepository.findById(request.getUserPrincipal().getName()).get();
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String[] arr = request.getServletPath().split("/");
			String author = arr[arr.length - 1];
			if (!userAccount.getLogin().equalsIgnoreCase(author)) {
				response.sendError(403, "Permition denied");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return (HttpMethod.POST.matches(method) && path.matches("/forum/post/\\w+"))
				|| (HttpMethod.PUT.matches(method) && path.matches("/forum/post/\\w+/comment/\\w+"));
	}

}
