package dev.nicoli.blog.service;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.common.service.AbstractCrudService;
import dev.nicoli.blog.dto.user.UserCreateRequest;
import dev.nicoli.blog.dto.user.UserResponse;
import dev.nicoli.blog.dto.user.UserUpdateRequest;
import dev.nicoli.blog.dto.user.UserViewResponse;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.mapper.UserMapper;
import dev.nicoli.blog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Function;

@Service
public class UserService extends AbstractCrudService<User,
        UserResponse,
        UserViewResponse,
        UserCreateRequest,
        UserUpdateRequest> {

    private final UserRepository repository;

    private final AuthorizationService authorizationService;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       AuthorizationService authorizationService,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UserRepository repository() {
        return repository;
    }

    @Override
    protected Function<User, UserViewResponse> mapperResponse() {
        return UserMapper::toViewResponse;
    }

    @Override
    protected void validateDeleteAuthorization(User user) {
        authorizationService.validateOwnerOrAdmin(user);
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        validateUniqueEmail(request.email(), null);
        User user = UserMapper.createEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = getById(id);
        authorizationService.validateOwner(user);
        validateUniqueEmail(request.email(), user.getId());
        UserMapper.updateEntity(user, request);
        return UserMapper.toResponse(repository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserViewResponse> findAll() {
        return repository().findAll()
                .stream()
                .map(mapperResponse())
                .toList();
    }

    public UserResponse findMe() {
        User user = authorizationService.currentUser();
        return UserMapper.toResponse(user);
    }

    public UserResponse updateMe(UserUpdateRequest request) {
        User user = authorizationService.currentUser();
        validateUniqueEmail(request.email(), user.getId());
        UserMapper.updateEntity(user, request);
        return UserMapper.toResponse(repository.save(user));
    }

    public void deleteMe() {
        User user = authorizationService.currentUser();
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
