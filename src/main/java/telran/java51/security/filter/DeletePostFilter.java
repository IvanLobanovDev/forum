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
import telran.java51.post.dao.PostRepository;
import telran.java51.post.model.Post;

@Component
@Order(15)
@RequiredArgsConstructor
public class DeletePostFilter implements Filter {

	final PostRepository postRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			telran.java51.security.model.User user = (telran.java51.security.model.User) request.getUserPrincipal();
			String[] arr = request.getServletPath().split("/");
			String id = arr[arr.length - 1];
			Post post = postRepository.findById(id).orElse(null);
			if(post == null) {
				response.sendError(404);
				return;
			}
			if (!(user.getRoles().contains("MODERATOR")
					|| user.getName().equalsIgnoreCase(post.getAuthor()))) {
				response.sendError(403, "Permition denied");
				return;
			}
		}
		chain.doFilter(request, response);

	}

	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.DELETE.matches(method) && path.matches("/forum/post/\\w+");
	}

}
