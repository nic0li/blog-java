package dev.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.blog.dto.post.PostResponse;
import dev.blog.dto.user.UserProfileResponse;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String content,
        Instant createdAt,
        Instant updatedAt,
        UserProfileResponse user,

        @JsonIgnoreProperties("comments")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        PostResponse post
) {
}
