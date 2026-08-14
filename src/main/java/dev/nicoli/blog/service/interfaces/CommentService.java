package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.comment.*;

import java.util.List;

public interface CommentService extends CrudService<
        CommentResponse, CommentViewResponse, CommentCreateRequest, CommentUpdateRequest> {

    List<CommentViewResponse> findAll();
}
