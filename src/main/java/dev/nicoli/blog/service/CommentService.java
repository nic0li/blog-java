package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.comment.*;

import java.util.List;

public interface CommentService extends CrudService<
        CommentResponse, CommentResponse, CommentCreateRequest> {

    CommentResponse update(Long id, CommentUpdateRequest request);

    List<CommentResponse> findAll();
}
