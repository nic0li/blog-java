package dev.nicoli.blog.dto.user;

public record UserResponse(

        Long id,

        String name,

        String email,

        String photo

) {
}
