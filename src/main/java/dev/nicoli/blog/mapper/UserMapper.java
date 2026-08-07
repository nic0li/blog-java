package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.user.UserCreateRequest;
import dev.nicoli.blog.dto.user.UserResponse;
import dev.nicoli.blog.dto.user.UserUpdateRequest;
import dev.nicoli.blog.dto.user.UserViewResponse;
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
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.photo() != null) {
            user.setPhoto(request.photo());
        }
        if (request.bio() != null) {
            user.setBio(request.bio());
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
