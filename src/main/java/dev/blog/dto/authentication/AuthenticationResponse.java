package dev.blog.dto.authentication;

import dev.blog.dto.user.UserResponse;

public record AuthenticationResponse(
        UserResponse user,
        String token
) {
}
