package dev.nicoli.blog.factory;

import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;

import java.util.List;

public final class PostFactory {

    public static Post post() {
        return post("I like drama", "Content");
    }

    public static Post updatedPost() {
        return post("I love drama", "Updated content");
    }

    public static PostCreateRequest createRequest() {
        return new PostCreateRequest(
                "I like drama", "Content", 1L);
    }

    public static PostUpdateRequest updateRequest() {
        return new PostUpdateRequest(
                "I love drama", "Updated content", 1L);
    }

    public static PostResponse response() {
        return response("I like drama", "Content");
    }

    public static PostResponse updatedResponse() {
        return response("I love drama", "Updated content");
    }

    private static Post post(
            String title, String content) {
        Post post = new Post();
        post.setId(1L);
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(CategoryFactory.movies());
        post.setUser(UserFactory.user());
        post.setComments(List.of());
        return post;
    }

    private static PostResponse response(
            String title, String content) {
        return new PostResponse(1L,
                title,
                content,
                null,
                null,
                CategoryFactory.response(),
                UserFactory.profileResponse(),
                List.of());
    }
}