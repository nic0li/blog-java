package dev.blog.dto.post;

import dev.blog.dto.category.CategoryResponse;
import dev.blog.dto.comment.CommentResponse;
import dev.blog.dto.user.UserProfileResponse;

import java.time.Instant;
import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String content,
        Instant createdAt,
        Instant updatedAt,
        CategoryResponse category,
        UserProfileResponse user,
        List<CommentResponse> comments
) {
}
