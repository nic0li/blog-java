package dev.blog.service.interfaces;

import dev.blog.dto.comment.*;
import dev.blog.entity.Comment;

import java.util.List;

public interface CommentService extends EntityService<Comment> {

    CommentResponse create(Long postId, CommentRequest request);

    CommentResponse update(Long id, CommentRequest request);

    void delete(Long id);

    CommentResponse findById(Long id);

    List<CommentResponse> findAll();

}
