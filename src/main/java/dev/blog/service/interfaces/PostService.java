package dev.blog.service.interfaces;

import dev.blog.dto.post.*;
import dev.blog.entity.Post;

import java.util.List;

public interface PostService extends EntityService<Post> {

    PostResponse create(PostRequest request);

    PostResponse update(Long id, PostRequest request);

    void delete(Long id);

    PostResponse findById(Long id);

    List<PostResponse> findAll(PostFiltersRequest request);

    List<PostResponse> findByUser(Long id);

    List<PostResponse> findByAuthenticatedUser();

}
