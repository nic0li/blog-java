package dev.nicoli.blog.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank
        String name
) {
}
