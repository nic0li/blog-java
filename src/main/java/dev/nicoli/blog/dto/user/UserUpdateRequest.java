package dev.nicoli.blog.dto.user;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(

        @Email(message = "E-mail inválido")
        String email,

        String name,

        String photo

) {
}
