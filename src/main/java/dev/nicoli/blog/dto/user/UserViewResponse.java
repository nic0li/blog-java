package dev.nicoli.blog.dto.user;

public record UserViewResponse(

        Long id,

        String name,

        String photo,

        String bio

) {
}
