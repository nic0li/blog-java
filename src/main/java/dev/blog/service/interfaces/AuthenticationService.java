package dev.blog.service.interfaces;

import dev.blog.dto.authentication.*;
import dev.blog.entity.User;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    User getAuthenticatedUser();
}
