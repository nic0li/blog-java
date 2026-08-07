package dev.nicoli.blog.dto.user;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(

        @Email(message = "Invalid email")
        String email,

        String name,

        String photo,

        String bio

) {
}
