package dev.nicoli.blog.dto.user;

public record UserProfileResponse(
        Long id,
        String name,
        String photo,
        String bio
) {
}
