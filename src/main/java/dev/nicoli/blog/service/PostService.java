package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;

import java.util.List;

public interface PostService extends CrudService<
        PostResponse, PostResponse, PostCreateRequest> {

    PostResponse update(Long id, PostUpdateRequest request);

    List<PostResponse> findAll(PostFiltersRequest request);

    List<PostResponse> findByUser(Long id);

    List<PostResponse> findByAuthenticatedUser();

    Post getById(Long id);
}
