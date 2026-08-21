package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;

import java.util.List;

public interface UserService extends EntityService<User> {

    UserResponse create(UserCreateRequest request);

    UserProfileResponse findById(Long id);

    List<UserProfileResponse> findAll();

    UserResponse toggleUserRole(Long id);

    void deleteUser(Long id);

    UserResponse findAuthenticated();

    UserResponse updateAuthenticated(UserUpdateRequest request);

    void updateAuthenticatedPassword(UserPasswordUpdateRequest request);

    void deleteAuthenticated();

}
