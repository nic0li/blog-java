package dev.nicoli.blog.dto.post;

import dev.nicoli.blog.dto.category.CategoryResponse;
import dev.nicoli.blog.dto.comment.CommentViewResponse;

import java.time.Instant;
import java.util.List;

public record UserPostResponse(

        Long id,

        String title,

        String content,

        CategoryResponse category,

        List<CommentViewResponse> comments,

        Instant createdAt,

        Instant updatedAt

) {
}
