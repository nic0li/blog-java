package dev.nicoli.blog.service;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthenticationService authenticationService;

    public AuthorizationServiceImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public User getAuthenticatedUser() {
        return authenticationService.getAuthenticatedUser();
    }

    @Override
    public boolean isOwner(User resourceOwner) {
        return isOwner(resourceOwner, getAuthenticatedUser());
    }

    @Override
    public boolean isAdmin() {
        return isAdmin(getAuthenticatedUser());
    }

    @Override
    public void validateOwner(User resourceOwner) {
        if (!isOwner(resourceOwner)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this resource");
        }
    }

    @Override
    public void validateAdmin() {
        if (!isAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Administrator privileges required");
        }
    }

    @Override
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