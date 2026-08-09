package dev.nicoli.blog.factory;

import dev.nicoli.blog.dto.comment.CommentCreateRequest;
import dev.nicoli.blog.dto.comment.CommentUpdateRequest;
import dev.nicoli.blog.dto.comment.CommentResponse;
import dev.nicoli.blog.dto.comment.CommentViewResponse;
import dev.nicoli.blog.entity.Comment;

public final class CommentFactory {

    private CommentFactory() { }

    public static Comment comment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Great post!");
        comment.setUser(UserFactory.maria());
        comment.setPost(PostFactory.post());
        return comment;
    }

    public static Comment updatedComment() {
        Comment comment = comment();
        comment.setContent("Updated comment!");
        return comment;
    }

    public static CommentCreateRequest createRequest() {
        return new CommentCreateRequest(
                "Great post!",
                1L);
    }

    public static CommentUpdateRequest updateRequest() {
        return new CommentUpdateRequest(
                "Updated comment!");
    }

    public static CommentUpdateRequest emptyUpdateRequest() {
        return new CommentUpdateRequest(null);
    }

    public static CommentResponse response() {
        return new CommentResponse(
                1L,
                "Great post!",
                UserFactory.response(),
                null,
                null);
    }

    public static CommentResponse updatedResponse() {
        return new CommentResponse(
                1L,
                "Updated comment!",
                UserFactory.response(),
                null,
                null);
    }

    public static CommentViewResponse viewResponse() {
        return new CommentViewResponse(
                1L,
                "Great post!",
                UserFactory.viewResponse(),
                null,
                null);
    }
}