package dev.nicoli.blog.factory;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;

public final class UserFactory {

    private UserFactory() { }

    public static User user() {
        return user(1L,
                "maria@email.com", "Maria", UserRole.USER);
    }

    public static User admin() {
        return user(2L,
                "ana@email.com", "Ana", UserRole.ADMIN);
    }

    public static UserCreateRequest createRequest() {
        return new UserCreateRequest(
                "maria@email.com", "123456", "Maria");
    }

    public static UserUpdateRequest updateRequest() {
        return userUpdateRequest("mariasilva@email.com");
    }

    public static UserUpdateRequest updateRequestSameEmail() {
        return userUpdateRequest("maria@email.com");
    }

    public static UserUpdateRequest updateRequestNullEmail() {
        return userUpdateRequest(null);
    }

    public static UserUpdateRequest updateRequestEmailEmpty() {
        return userUpdateRequest("");
    }

    public static UserUpdateRequest updateRequestWithoutEmail() {
        return userUpdateRequest();
    }

    private static UserUpdateRequest userUpdateRequest(String email) {
        UserUpdateRequest request = userUpdateRequest();
        request.setEmail(email);
        return request;
    }

    private static UserUpdateRequest userUpdateRequest() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Maria Silva");
        request.setPhoto(null);
        request.setBio("dev");
        return request;
    }

    public static UserResponse response() {
        return userResponse("maria@email.com", "Maria", null);
    }

    public static UserResponse updatedResponse() {
        return userResponse("mariasilva@email.com", "Maria Silva", "dev");
    }

    public static UserResponse updatedResponseSameEmail() {
        return userResponse("maria@email.com", "Maria Silva", "dev");
    }

    public static UserViewResponse viewResponse() {
        return new UserViewResponse(1L, "Maria", null, null);
    }

    private static User user(
            Long id, String email, String name, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword("123456");
        user.setName(name);
        user.setRole(role);
        return user;
    }

    private static UserResponse userResponse(
            String email, String name, String bio) {
        return new UserResponse(1L, email, name, null, bio, UserRole.USER);
    }

}