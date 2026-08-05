package dev.nicoli.blog.service;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorizationService {

    private final AuthenticationService authenticationService;

    public AuthorizationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public User currentUser() {
        return authenticationService.getAuthenticatedUser();
    }

    public boolean isNotOwner(User user) {
        return !user.getId().equals(currentUser().getId());
    }

    public boolean isNotAdmin() {
        return currentUser().getRole() != UserRole.ADMIN;
    }

    public void validateOwner(User user) {
        if (isNotOwner(user)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this resource");
        }
    }

    public void validateAdmin() {
        if (isNotAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Administrator privileges required");
        }
    }

    public void validateOwnerOrAdmin(User user) {
        if (isNotOwner(user) && isNotAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this resource");
        }
    }

}