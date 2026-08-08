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

    public User getAuthenticatedUser() {
        return authenticationService.getAuthenticatedUser();
    }

    public boolean isOwner(User resourceOwner) {
        return isOwner(resourceOwner, getAuthenticatedUser());
    }

    public boolean isAdmin() {
        return isAdmin(getAuthenticatedUser());
    }

    public void validateOwner(User resourceOwner) {
        if (!isOwner(resourceOwner)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this resource");
        }
    }

    public void validateAdmin() {
        if (!isAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Administrator privileges required");
        }
    }

    public void validateOwnerOrAdmin(User resourceOwner) {
        User authenticatedUser = getAuthenticatedUser();
        if (!isOwner(resourceOwner, authenticatedUser) && !isAdmin(authenticatedUser)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this resource");
        }
    }

    private boolean isOwner(User resourceOwner, User authenticatedUser) {
        return resourceOwner.getId().equals(authenticatedUser.getId());
    }

    private boolean isAdmin(User authenticatedUser) {
        return authenticatedUser.getRole() == UserRole.ADMIN;
    }

}