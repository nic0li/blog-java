package dev.blog.service.interfaces;

import dev.blog.entity.User;

public interface AuthorizationService {

    User getAuthenticatedUser();

    boolean isOwner(User resourceOwner);

    boolean isAdmin();

    void validateOwner(User resourceOwner);

    void validateAdmin();

    void validateOwnerOrAdmin(User resourceOwner);
}
