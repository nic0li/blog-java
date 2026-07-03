package blog.repository;

import java.util.List;

import blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	List<Post> findAllByTitleContainingIgnoreCase (String title);

	List<Post> findAllByCategoryNameContainingIgnoreCase(String category);

	List<Post> findAllByTitleContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(
					String title,
					String category);

	List<Post> findAllByUserId(Long id);
	
}
