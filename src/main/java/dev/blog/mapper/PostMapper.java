package dev.blog.mapper;

import dev.blog.dto.comment.CommentResponse;
import dev.blog.dto.post.*;
import dev.blog.entity.Post;
import org.springframework.util.StringUtils;

import java.util.List;

public final class PostMapper {

    private PostMapper() { }

    public static Post createEntity(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setContent(request.content());
        return post;
    }

    public static void updateEntity(Post post, PostRequest request) {
        if (StringUtils.hasText(request.title())) {
            post.setTitle(request.title());
        }
        if (StringUtils.hasText(request.content())) {
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
