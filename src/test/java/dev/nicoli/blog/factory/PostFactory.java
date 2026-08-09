package dev.nicoli.blog.factory;

import dev.nicoli.blog.dto.post.PostCreateRequest;
import dev.nicoli.blog.dto.post.PostFiltersRequest;
import dev.nicoli.blog.dto.post.PostResponse;
import dev.nicoli.blog.dto.post.PostUpdateRequest;
import dev.nicoli.blog.entity.Post;

import java.util.List;

public final class PostFactory {

    private PostFactory() { }

    public static Post post() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Spring Boot");
        post.setContent("Spring Boot Content");
        post.setCategory(CategoryFactory.movies());
        post.setUser(UserFactory.maria());
        post.setComments(List.of());
        return post;
    }

    public static Post updatedPost() {
        Post post = post();
        post.setTitle("Updated Spring Boot");
        post.setContent("Updated Spring Boot Content");
        post.setCategory(CategoryFactory.movies());
        return post;
    }

    public static PostCreateRequest createRequest() {
        return new PostCreateRequest(
                "Spring Boot",
                "Spring Boot Content",
                1L);
    }

    public static PostUpdateRequest updateRequest() {
        return new PostUpdateRequest(
                "Updated Spring Boot",
                "Updated Spring Boot Content",
                1L);
    }

    public static PostUpdateRequest emptyUpdateRequest() {
        return new PostUpdateRequest(
                null,
                null,
                null);
    }

    public static PostFiltersRequest filters() {
        return new PostFiltersRequest(
                "Spring",
                "Movies");
    }

    public static PostResponse response() {
        return new PostResponse(
                1L,
                "Spring Boot",
                "Spring Boot Content",
                CategoryFactory.response(),
                UserFactory.response(),
                null,
                null);
    }

    public static PostResponse updatedResponse() {
        return new PostResponse(
                1L,
                "Updated Spring Boot",
                "Updated Spring Boot Content",
                CategoryFactory.response(),
                UserFactory.response(),
                null,
                null);
    }
}