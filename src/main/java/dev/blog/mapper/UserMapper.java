package dev.blog.mapper;

import dev.blog.dto.user.*;
import dev.blog.entity.User;
import org.springframework.util.StringUtils;

public final class UserMapper {

    private UserMapper() { }

    public static User createEntity(UserCreateRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        return user;
    }

    public static void updateEntity(User user, UserUpdateRequest request) {
        if (request.isEmailProvided()
                && StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (request.isNameProvided()) {
            user.setName(StringUtils.hasText(request.getName())
                    ? request.getName() : null);
        }
        if (request.isPhotoProvided()) {
            user.setPhoto(StringUtils.hasText(request.getPhoto())
                    ? request.getPhoto() : null);
        }
        if (request.isBioProvided()) {
            user.setBio(StringUtils.hasText(request.getBio())
                    ? request.getBio() : null);
        }
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoto(),
                user.getBio(),
                user.getRole());
    }

    public static UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(user.getId(),
                user.getName(),
                user.getPhoto(),
                user.getBio());
    }

}
