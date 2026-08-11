package dev.nicoli.blog.factory;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;

public final class UserFactory {

    private UserFactory() { }

    public static User maria() {
        return user(
                1L,
                "maria@email.com",
                "Maria",
                UserRole.USER);
    }

    public static User mariaSilva() {
        return user(
                2L,
                "mariasilva@email.com",
                "Maria Silva",
                UserRole.ADMIN);
    }

    public static UserCreateRequest createRequest() {
        return new UserCreateRequest(
                "maria@email.com",
                "123456",
                "Maria");
    }

    public static UserUpdateRequest updateRequest() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("mariasilva@email.com");
        request.setName("Maria Silva");
        request.setPhoto(null);
        request.setBio("dev");
        return request;
    }

    public static UserResponse response() {
        return new UserResponse(
                1L,
                "maria@email.com",
                "Maria",
                null,
                null,
                UserRole.USER);
    }

    public static UserViewResponse viewResponse() {
        return new UserViewResponse(
                1L,
                "Maria",
                null,
                null);
    }

    public static UserResponse updatedResponse() {
        return new UserResponse(
                1L,
                "mariasilva@email.com",
                "Maria Silva",
                null,
                "dev",
                UserRole.USER);
    }

    public static UserResponse updatedResponseWithSameEmail() {
        return new UserResponse(
                1L,
                "maria@email.com",
                "Maria Silva",
                null,
                "dev",
                UserRole.USER);
    }

    private static User user(
            Long id,
            String email,
            String name,
            UserRole role) {

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword("123456");
        user.setName(name);
        user.setRole(role);

        return user;
    }

}