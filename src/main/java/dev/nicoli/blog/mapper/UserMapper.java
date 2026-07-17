package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.user.UserCreateRequest;
import dev.nicoli.blog.dto.user.UserResponse;
import dev.nicoli.blog.dto.user.UserUpdateRequest;
import dev.nicoli.blog.dto.user.UserViewResponse;
import dev.nicoli.blog.entity.User;

public abstract class UserMapper {

    public static User createEntity(
            UserCreateRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPhoto(request.photo());
        return user;
    }

    public static void updateEntity(User user,
                                    UserUpdateRequest request) {
        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.photo() != null) {
            user.setPhoto(request.photo());
        }
    }

    public static UserResponse toEditResponse(User user) {
        return new UserResponse(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoto());
    }

    public static UserViewResponse toResponse(User user) {
        return new UserViewResponse(user.getId(),
                user.getName(),
                user.getPhoto());
    }

}
