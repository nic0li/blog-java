package dev.blog.factory;

import dev.blog.dto.comment.*;
import dev.blog.entity.Comment;

public final class CommentFactory {

    public static Comment comment() {
        return entityComment();
    }

    public static CommentRequest request(String content) {
        return commentRequest(content);
    }

    public static CommentResponse response() {
        return response("Great post!");
    }

    public static CommentResponse response(String content) {
        return commentResponse(content);
    }

    private static Comment entityComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Great post!");
        comment.setUser(UserFactory.user());
        comment.setPost(PostFactory.post());
        return comment;
    }

    private static CommentRequest commentRequest(String content) {
        return new CommentRequest(content);
    }

    private static CommentResponse commentResponse(String content) {
        return new CommentResponse(1L, content, null, null,
                UserFactory.profileResponse(), PostFactory.response());
    }
}