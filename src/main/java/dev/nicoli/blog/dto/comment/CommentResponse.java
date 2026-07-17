package dev.nicoli.blog.dto.comment;

import dev.nicoli.blog.dto.user.UserResponse;

import java.time.Instant;

public record CommentResponse(

        Long id,

        String content,

        UserResponse user,

        Instant createdAt,

        Instant updatedAt

) {
}
