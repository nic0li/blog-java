package dev.blog.factory;

import dev.blog.enums.UserRole;
import dev.blog.dto.user.*;
import dev.blog.entity.User;

public final class UserFactory {

    public static User user() {
        return user(1L, "maria@email.com", "Maria", UserRole.USER);
    }

    public static User admin() {
        return user(2L, "ana@email.com", "Ana", UserRole.ADMIN);
    }

    public static UserCreateRequest createRequest() {
        return new UserCreateRequest("maria@email.com", "123456", "Maria");
    }

    public static UserUpdateRequest updateRequest() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Maria Silva");
        request.setPhoto("photo.jpg");
        request.setBio("dev");
        return request;
    }

    public static UserUpdateRequest updateRequest(String email) {
        UserUpdateRequest request = updateRequest();
        request.setEmail(email);
        return request;
    }

    public static UserUpdateRequest updateRequestWithBlankFields() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("   ");
        request.setPhoto("   ");
        request.setBio("   ");
        return request;
    }

    public static UserUpdateRequest updateRequestWithNullFields() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("mariasilva@email.com");
        return request;
    }

    public static UserResponse response() {
        return userResponse("maria@email.com", "Maria", null, null);
    }

    public static UserResponse updatedResponse() {
        return userResponse("mariasilva@email.com", "Maria Silva", "photo.jpg", "dev");
    }

    public static UserResponse updatedResponseSameEmail() {
        return userResponse("maria@email.com", "Maria Silva", "photo.jpg", "dev");
    }

    public static UserResponse updatedResponseWithBlankFields() {
        return userResponse("maria@email.com", null, null, null);
    }

    public static UserResponse updatedResponseWithNullFields() {
        return userResponse("mariasilva@email.com", "Maria", null, null);
    }

    public static UserProfileResponse profileResponse() {
        return new UserProfileResponse(1L, "Maria", null, null);
    }

    private static User user(Long id, String email, String name, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword("123456");
        user.setName(name);
        user.setRole(role);
        return user;
    }

    private static UserResponse userResponse(String email, String name, String photo, String bio) {
        return new UserResponse(1L, email, name, photo, bio, UserRole.USER);
    }
}