package dev.nicoli.blog.dto.authentication;

import dev.nicoli.blog.dto.user.UserResponse;

public record AuthenticationResponse(
        UserResponse user,
        String token
) {
}
