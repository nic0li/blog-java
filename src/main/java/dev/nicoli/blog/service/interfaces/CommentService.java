package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.entity.Comment;

import java.util.List;

public interface CommentService extends EntityService<Comment> {

    CommentResponse create(Long postId, CommentRequest request);

    CommentResponse update(Long id, CommentRequest request);

    void delete(Long id);

    CommentResponse findById(Long id);

    List<CommentResponse> findAll();

}
