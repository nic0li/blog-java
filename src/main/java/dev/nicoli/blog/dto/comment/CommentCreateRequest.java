package dev.nicoli.blog.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotBlank
        String content,

        @NotNull
        Long postId
) {
}
