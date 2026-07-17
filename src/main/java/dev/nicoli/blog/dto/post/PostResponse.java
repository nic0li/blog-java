package dev.nicoli.blog.dto.post;

import dev.nicoli.blog.dto.category.CategoryResponse;
import dev.nicoli.blog.dto.user.UserResponse;

import java.time.Instant;

public record PostResponse(

        Long id,

        String title,

        String content,

        CategoryResponse category,

        UserResponse user,

        Instant createdAt,

        Instant updatedAt

) {
}
