package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;

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
                && request.getEmail() != null
                && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        if (request.isNameProvided()) {
            user.setName(request.getName() == null || request.getName().isBlank()
                    ? null : request.getName());
        }
        if (request.isPhotoProvided()) {
            user.setPhoto(request.getPhoto() == null || request.getPhoto().isBlank()
                    ? null : request.getPhoto());
        }
        if (request.isBioProvided()) {
            user.setBio(request.getBio() == null || request.getBio().isBlank()
                    ? null : request.getBio());
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

    public static UserViewResponse toViewResponse(User user) {
        return new UserViewResponse(user.getId(),
                user.getName(),
                user.getPhoto(),
                user.getBio());
    }

}
