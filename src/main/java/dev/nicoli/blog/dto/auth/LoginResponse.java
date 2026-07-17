package dev.nicoli.blog.dto.auth;

import dev.nicoli.blog.dto.user.UserResponse;

public record LoginResponse(

        UserResponse user,

        String token

) {
}
