package dev.nicoli.blog.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "A password é obrigatória")
        @Size(min = 3, message = "A password deve ter no mínimo 3 caracteres")
        String password,

        String name,

        String photo

) {
}
