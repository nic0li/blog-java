package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.user.*;

import java.util.List;

public interface UserService extends CrudService<
        UserResponse, UserProfileResponse, UserCreateRequest> {

    List<UserProfileResponse> findAll();

    UserResponse findMe();

    UserResponse updateMe(UserUpdateRequest request);

    void deleteMe();

    void updatePassword(UserPasswordUpdateRequest request);

    UserResponse toggleRole(Long id);
}
