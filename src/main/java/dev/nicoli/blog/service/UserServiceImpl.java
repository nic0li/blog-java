package dev.nicoli.blog.service;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.common.service.CrudServiceImpl;
import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.mapper.UserMapper;
import dev.nicoli.blog.repository.UserRepository;
import dev.nicoli.blog.service.interfaces.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl extends CrudServiceImpl<User> implements UserService {

    private final UserRepository repository;

    private final AuthorizationService authorizationService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository,
                           AuthorizationService authorizationService,
                           PasswordEncoder passwordEncoder) {
        super(repository, User.class);
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        validateEmailAvailability(request.email(), null);
        User user = UserMapper.createEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = getById(id);
        authorizationService.validateOwner(user);
        return updateUserResponse(request, user);
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        authorizationService.validateOwnerOrAdmin(user);
        repository.delete(user);
    }

    @Override
    public UserViewResponse findById(Long id) {
        return UserMapper.toViewResponse(getById(id));
    }

    @Override
    public List<UserViewResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toViewResponse)
                .toList();
    }

    @Override
    public UserResponse findMe() {
        User user = authorizationService.getAuthenticatedUser();
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse updateMe(UserUpdateRequest request) {
        User user = authorizationService.getAuthenticatedUser();
        return updateUserResponse(request, user);
    }

    @Override
    public void deleteMe() {
        User user = authorizationService.getAuthenticatedUser();
        repository.delete(user);
    }

    private UserResponse updateUserResponse(UserUpdateRequest request, User user) {
        if (request.isEmailProvided()
                && request.getEmail() != null && !request.getEmail().isBlank()) {
            validateEmailAvailability(request.getEmail(), user.getId());
        }
        UserMapper.updateEntity(user, request);
        return UserMapper.toResponse(repository.save(user));
    }

    private void validateEmailAvailability(String email, Long userId) {
        var userByEmail = repository.findByEmail(email);
        boolean emailAlreadyExists = userByEmail.isPresent();
        boolean emailBelongsToAnotherUser = emailAlreadyExists
                        && !userByEmail.get().getId().equals(userId);

        if (emailAlreadyExists && emailBelongsToAnotherUser) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already registered");
        }
    }

}
