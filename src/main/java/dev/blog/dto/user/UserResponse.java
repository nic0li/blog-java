package dev.blog.dto.user;

import dev.blog.enums.UserRole;

public record UserResponse(
        Long id,
        String email,
        String name,
        String photo,
        String bio,
        UserRole role
) {
}
