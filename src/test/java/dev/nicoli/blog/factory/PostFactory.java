package dev.nicoli.blog.factory;

import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;

import java.util.List;

public final class PostFactory {

    private PostFactory() { }

    public static Post post() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("I like drama");
        post.setContent("Content");
        post.setCategory(CategoryFactory.movies());
        post.setUser(UserFactory.maria());
        post.setComments(List.of());
        return post;
    }

    public static Post updatedPost() {
        Post post = post();
        post.setTitle("I love drama");
        post.setContent("Updated content");
        post.setCategory(CategoryFactory.movies());
        return post;
    }

    public static PostCreateRequest createRequest() {
        return new PostCreateRequest(
                "I like drama",
                "Content",
                1L);
    }

    public static PostUpdateRequest updateRequest() {
        return new PostUpdateRequest(
                "I love drama",
                "Updated content",
                1L);
    }

    public static PostResponse response() {
        return new PostResponse(
                1L,
                "I like drama",
                "Content",
                CategoryFactory.response(),
                UserFactory.response(),
                null,
                null);
    }

    public static PostResponse updatedResponse() {
        return new PostResponse(
                1L,
                "I love drama",
                "Updated content",
                CategoryFactory.response(),
                UserFactory.response(),
                null,
                null);
    }
}