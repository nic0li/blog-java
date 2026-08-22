package dev.blog.mapper;

import dev.blog.dto.comment.*;
import dev.blog.dto.post.PostResponse;
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

    public static CommentResponse toResponseWithoutPost(Comment comment) {
        return toResponse(comment, false);
    }

    public static List<CommentResponse> toListResponseWithoutPost(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toResponseWithoutPost)
                .toList();
    }

    private static CommentResponse toResponse(Comment comment, boolean includePost) {
        PostResponse post = includePost
                ? PostMapper.toResponseWithoutComments(comment.getPost())
                : null;
        return new CommentResponse(comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                UserMapper.toProfileResponse(comment.getUser()),
                post);
    }

}
