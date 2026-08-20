package dev.nicoli.blog.dto.user;

import dev.nicoli.blog.common.enums.UserRole;

public record UserResponse(
        Long id,
        String email,
        String name,
        String photo,
        String bio,
        UserRole role
) {
}
