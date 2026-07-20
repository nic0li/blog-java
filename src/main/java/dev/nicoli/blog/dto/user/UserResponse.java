package dev.nicoli.blog.dto.user;

import dev.nicoli.blog.common.enums.UserRole;

public record UserResponse(

        Long id,

        String name,

        String email,

        String photo,

        UserRole role

) {
}
