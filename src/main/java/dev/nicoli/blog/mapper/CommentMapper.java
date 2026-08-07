package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.entity.Comment;

import java.util.List;

public final class CommentMapper {

    private CommentMapper() { }

    public static Comment createEntity(CommentCreateRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.content());
        return comment;
    }

    public static void updateEntity(Comment comment, CommentUpdateRequest request) {
        if (request.content() != null) {
            comment.setContent(request.content());
        }
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.getId(),
                comment.getContent(),
                UserMapper.toResponse(comment.getUser()),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }

    public static CommentViewResponse toViewResponse(Comment comment) {
        return new CommentViewResponse(comment.getId(),
                comment.getContent(),
                UserMapper.toViewResponse(comment.getUser()),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }

    public static List<CommentViewResponse> toViewResponse(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toViewResponse)
                .toList();
    }

}
