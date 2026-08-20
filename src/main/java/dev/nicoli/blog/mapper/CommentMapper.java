package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.dto.post.PostResponse;
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
