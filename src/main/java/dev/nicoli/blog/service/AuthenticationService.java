package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.authentication.*;
import dev.nicoli.blog.entity.User;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    User getAuthenticatedUser();
}
