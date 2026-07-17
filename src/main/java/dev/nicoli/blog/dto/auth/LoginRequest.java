package dev.nicoli.blog.dto.auth;

public record LoginRequest(

        String login,

        String password

) {
}
