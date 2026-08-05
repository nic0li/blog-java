package dev.nicoli.blog.dto.authentication;

public record LoginRequest(

        String login,

        String password

) {
}
