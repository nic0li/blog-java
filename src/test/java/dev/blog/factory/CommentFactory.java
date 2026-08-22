package dev.blog.factory;

import dev.blog.dto.comment.*;
import dev.blog.entity.Comment;

public final class CommentFactory {

    public static Comment comment() {
        return comment("Great post!");
    }

    public static Comment updatedComment() {
        return comment("Updated comment!");
    }

    public static CommentRequest request() {
        return new CommentRequest("Great post!");
    }

    public static CommentRequest updateRequest() {
        return new CommentRequest("Updated comment!");
    }

    public static CommentResponse response() {
        return response("Great post!");
    }

    public static CommentResponse updatedResponse() {
        return response("Updated comment!");
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
                null,
                null,
                UserFactory.profileResponse(),
                PostFactory.response());
    }
}