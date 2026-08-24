package dev.blog.factory;

import dev.blog.dto.post.*;
import dev.blog.entity.Category;
import dev.blog.entity.Post;
import dev.blog.entity.User;

import java.util.List;

public final class PostFactory {

    public static Post post() {
        return post("I like drama", "Content");
    }

    public static Post post(String title, String content) {
        return entityPost(title, content);
    }

    public static PostRequest request() {
        return request("I like drama", "Content", 1L);
    }

    public static PostRequest request(String title, String content, Long categoryId) {
        return postRequest(title, content, categoryId);
    }

    public static PostResponse response() {
        return response("I like drama", "Content");
    }

    public static PostResponse response(String title, String content) {
        return postResponse(title, content);
    }

    private static Post entityPost(String title, String content) {
        Post post = new Post();
        post.setId(1L);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(null);
        post.setUpdatedAt(null);
        post.setCategory(CategoryFactory.movies());
        post.setUser(UserFactory.user());
        post.setComments(List.of());
        return post;
    }

    private static PostRequest postRequest(String title, String content, Long categoryId) {
        return new PostRequest(title, content, categoryId);
    }

    private static PostResponse postResponse(String title, String content) {
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