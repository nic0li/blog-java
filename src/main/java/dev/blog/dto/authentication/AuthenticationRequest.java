package dev.blog.dto.authentication;

public record AuthenticationRequest(
        String login,
        String password
) {
}
