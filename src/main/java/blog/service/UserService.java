package blog.service;

import java.util.function.Function;

import blog.common.enums.UserRole;
import blog.common.service.AbstractCrudService;
import blog.dto.user.*;
import blog.mapper.UserMapper;
import blog.model.User;
import blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService extends AbstractCrudService<User,
        UserViewResponse,
        UserResponse,
        UserCreateRequest,
        UserUpdateRequest> {

  @Autowired
  private UserRepository repository;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserRepository repository() {
    return repository;
  }

  @Override
  public Function<User, UserViewResponse> mapperResponse() {
    return UserMapper::toResponse;
  }

  @Override
  public void validateDeleteAuthorization(User user) {
    authorizationService.validateOwnerOrAdmin(user);
  }

  @Override
  public UserResponse create(UserCreateRequest request) {
    validateUniqueEmail(request.email(), null);
    User user = UserMapper.createEntity(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(UserRole.USER);
    return UserMapper.toEditResponse(repository.save(user));
  }

  @Override
  public UserResponse update(Long id, UserUpdateRequest request) {
    User user = getById(id);
    authorizationService.validateOwner(user);
    validateUniqueEmail(request.email(), user.getId());
    UserMapper.updateEntity(user, request);
    return UserMapper.toEditResponse(repository.save(user));
  }

  public UserResponse findMe() {
    User user = getById(
            authorizationService.currentUser().getId());
    return UserMapper.toEditResponse(user);
  }

  public UserResponse updateMe(UserUpdateRequest request) {
    User user = getById(
            authorizationService.currentUser().getId());
    validateUniqueEmail(request.email(), user.getId());
    UserMapper.updateEntity(user, request);
    return UserMapper.toEditResponse(repository.save(user));
  }

  public void deleteMe() {
    User user = getById(
            authorizationService.currentUser().getId());
    repository.delete(user);
  }

  private void validateUniqueEmail(String email, Long currentUserId) {
    var user = repository.findByEmail(email);
    if (user.isPresent()
            && (currentUserId == null
            || !user.get().getId().equals(currentUserId))) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Email already registered");
    }
  }

}
