package telran.java51.post.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import telran.java51.post.dto.DatePeriodDto;
import telran.java51.post.model.Post;

public interface PostRepository extends CrudRepository<Post, String> {
	
	Stream<Post> findAllPostsByAuthorIgnoreCase(String author);
	
//	@Query("{tags: { $in: ?0}}")
//	Stream<Post> findPostsByTagsIgnoreCase(List<String> tags);
	
	Stream<Post> findPostsByTagsInIgnoreCase(List<String> tags);
	
//	@Query("{dateCreated: {$gte: ?0, $lte: ?1}}")
//	Stream<Post> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo);
	
	Stream<Post> findPostsByDateCreatedBetween(LocalDate dateFrom, LocalDate dateTo);

	//
}
