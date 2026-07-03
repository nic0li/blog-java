package blog.repository;

import java.util.List;
import java.util.Optional;

import blog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category> findAllByNameContainingIgnoreCase(String name);

	Optional<Category> findByNameIgnoreCase(String name);
	
}
