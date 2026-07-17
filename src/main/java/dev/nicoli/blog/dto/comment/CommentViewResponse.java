package dev.nicoli.blog.dto.comment;

import dev.nicoli.blog.dto.user.UserViewResponse;

import java.time.Instant;

public record CommentViewResponse(

        Long id,

        String content,

        UserViewResponse user,

        Instant createdAt,

        Instant updatedAt

) {
}
