package dev.blog.repository;

import dev.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(Long id);

    List<Post> findAllByTitleContainingIgnoreCase(String title);

    List<Post> findAllByCategoryNameContainingIgnoreCase(String category);

    List<Post> findAllByTitleContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(String title, String category);

}
