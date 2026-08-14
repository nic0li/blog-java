package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.user.*;

import java.util.List;

public interface UserService extends CrudService<
        UserResponse, UserViewResponse, UserCreateRequest, UserUpdateRequest> {

    List<UserViewResponse> findAll();

    UserResponse findMe();

    UserResponse updateMe(UserUpdateRequest request);

    void deleteMe();
}
