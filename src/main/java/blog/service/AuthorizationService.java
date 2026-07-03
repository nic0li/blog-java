package blog.service;

import blog.common.enums.UserRole;
import blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorizationService {

  @Autowired
  private AuthService authService;

  public User currentUser() {
    return authService.getAuthenticatedUser();
  }

  public boolean isOwner(User user) {
    return user.getId().equals(currentUser().getId());
  }

  public boolean isAdmin() {
    return currentUser().getRole() == UserRole.ADMIN;
  }

  public void validateOwner(User user) {
    if (!isOwner(user)) {
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

  public void validateOwnerOrAdmin(User user) {
    if (!isOwner(user) && !isAdmin()) {
      throw new ResponseStatusException(
              HttpStatus.FORBIDDEN,
              "You are not allowed to modify this resource");
    }
  }

}