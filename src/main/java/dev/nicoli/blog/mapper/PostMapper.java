package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.comment.CommentResponse;
import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;

import java.util.List;

public final class PostMapper {

    private PostMapper() { }

    public static Post createEntity(PostCreateRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setContent(request.content());
        return post;
    }

    public static void updateEntity(Post post, PostUpdateRequest request) {
        if (request.title() != null) {
            post.setTitle(request.title());
        }
        if (request.content() != null) {
            post.setContent(request.content());
        }
    }

    public static PostResponse toResponse(Post post) {
        return toResponse(post, CommentMapper.toListResponseWithoutPost(post.getComments()));
    }

    public static PostResponse toResponseWithoutComments(Post post) {
        return toResponse(post, List.of());
    }

    private static PostResponse toResponse(Post post, List<CommentResponse> comments) {
        return new PostResponse(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                CategoryMapper.toResponse(post.getCategory()),
                UserMapper.toProfileResponse(post.getUser()),
                comments);
    }

}
