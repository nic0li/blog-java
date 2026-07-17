package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;

public abstract class PostMapper {

    public static Post createEntity(
            PostCreateRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setContent(request.content());
        return post;
    }

    public static void updateEntity(Post post,
                                    PostUpdateRequest request) {
        if (request.title() != null) {
            post.setTitle(request.title());
        }
        if (request.content() != null) {
            post.setContent(request.content());
        }
    }

    public static PostResponse toEditResponse(Post post) {
        return new PostResponse(post.getId(),
                post.getTitle(),
                post.getContent(),
                CategoryMapper.toResponse(post.getCategory()),
                UserMapper.toEditResponse(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    public static PostViewResponse toResponse(Post post) {
        return new PostViewResponse(post.getId(),
                post.getTitle(),
                post.getContent(),
                CategoryMapper.toResponse(post.getCategory()),
                UserMapper.toResponse(post.getUser()),
                CommentMapper.toResponse(post.getComments()),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    public static UserPostResponse toUserPostResponse(Post post) {
        return new UserPostResponse(post.getId(),
                post.getTitle(),
                post.getContent(),
                CategoryMapper.toResponse(post.getCategory()),
                CommentMapper.toResponse(post.getComments()),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

}
