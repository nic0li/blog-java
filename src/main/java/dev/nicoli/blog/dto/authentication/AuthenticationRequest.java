package dev.nicoli.blog.dto.authentication;

public record AuthenticationRequest(

        String login,

        String password

) {
}
