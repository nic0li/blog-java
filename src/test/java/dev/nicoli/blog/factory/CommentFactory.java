package dev.nicoli.blog.factory;

import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.entity.Comment;

public final class CommentFactory {

    public static Comment comment() {
        return comment("Great post!");
    }

    public static Comment updatedComment() {
        return comment("Updated comment!");
    }

    public static CommentCreateRequest createRequest() {
        return new CommentCreateRequest("Great post!", 1L);
    }

    public static CommentUpdateRequest updateRequest() {
        return new CommentUpdateRequest("Updated comment!");
    }

    public static CommentResponse response() {
        return response("Great post!");
    }

    public static CommentResponse updatedResponse() {
        return response("Updated comment!");
    }

    public static CommentViewResponse viewResponse() {
        return new CommentViewResponse(1L,
                "Great post!",
                UserFactory.viewResponse(),
                null,
                null);
    }

    private static Comment comment(String content) {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent(content);
        comment.setUser(UserFactory.user());
        comment.setPost(PostFactory.post());
        return comment;
    }

    private static CommentResponse response(String content) {
        return new CommentResponse(1L,
                content,
                UserFactory.response(),
                null,
                null);
    }
}