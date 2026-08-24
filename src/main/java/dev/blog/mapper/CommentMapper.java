package dev.blog.mapper;

import dev.blog.dto.comment.*;
import dev.blog.entity.Comment;
import org.springframework.util.StringUtils;

import java.util.List;

public final class CommentMapper {

    private CommentMapper() { }

    public static Comment createEntity(CommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.content());
        return comment;
    }

    public static void updateEntity(Comment comment, CommentRequest request) {
        if (StringUtils.hasText(request.content())) {
            comment.setContent(request.content());
        }
    }

    public static CommentResponse toResponse(Comment comment) {
        return toResponse(comment, true);
    }

    public static List<CommentResponse> toListResponse(List<Comment> comments) {
        return comments.stream()
                .map(comment -> toResponse(comment, false))
                .toList();
    }

    private static CommentResponse toResponse(Comment comment, boolean includePost) {
        return new CommentResponse(comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                UserMapper.toProfileResponse(comment.getUser()),
                includePost
                        ? PostMapper.toResponseWithoutComments(comment.getPost())
                        : null);
    }

}
