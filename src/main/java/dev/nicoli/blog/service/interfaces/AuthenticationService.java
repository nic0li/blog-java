package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.dto.authentication.*;
import dev.nicoli.blog.entity.User;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest request);

    User getAuthenticatedUser();
}
