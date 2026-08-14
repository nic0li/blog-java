package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;

import java.util.List;

public interface PostService extends CrudService<
        PostResponse, PostViewResponse, PostCreateRequest, PostUpdateRequest> {

    List<PostViewResponse> findAll(PostFiltersRequest request);

    List<PostViewResponse> findByUser(Long id);

    List<PostViewResponse> findByAuthenticatedUser();

    Post getById(Long id);
}
