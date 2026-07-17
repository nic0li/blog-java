package dev.nicoli.blog.dto.post;

import dev.nicoli.blog.dto.category.CategoryResponse;
import dev.nicoli.blog.dto.comment.CommentViewResponse;
import dev.nicoli.blog.dto.user.UserViewResponse;

import java.time.Instant;
import java.util.List;

public record PostViewResponse(

        Long id,

        String title,

        String content,

        CategoryResponse category,

        UserViewResponse user,

        List<CommentViewResponse> comments,

        Instant createdAt,

        Instant updatedAt

) {
}
