package dev.blog.service;

import dev.blog.enums.UserRole;
import dev.blog.dto.user.*;
import dev.blog.entity.User;
import dev.blog.mapper.UserMapper;
import dev.blog.repository.UserRepository;
import dev.blog.service.interfaces.AuthorizationService;
import dev.blog.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl extends EntityServiceImpl<User> implements UserService {

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
    public UserProfileResponse findById(Long id) {
        return UserMapper.toProfileResponse(findEntityById(id));
    }

    @Override
    public List<UserProfileResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toProfileResponse)
                .toList();
    }

    @Override
    public UserResponse toggleUserRole(Long id) {
        authorizationService.validateAdmin();
        User authenticatedUser = authorizationService.getAuthenticatedUser();
        User user = findEntityById(id);
        if (authenticatedUser.getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot change your own role");
        }
        UserRole role = user.getRole() == UserRole.ADMIN
                ? UserRole.USER : UserRole.ADMIN;
        user.setRole(role);
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = findEntityById(id);
        authorizationService.validateOwnerOrAdmin(user);
        repository.delete(user);
    }

    @Override
    public UserResponse findAuthenticated() {
        User user = authorizationService.getAuthenticatedUser();
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse updateAuthenticated(UserUpdateRequest request) {
        User user = authorizationService.getAuthenticatedUser();
        return updateUser(request, user);
    }

    @Override
    public void updateAuthenticatedPassword(UserPasswordUpdateRequest request) {
        User user = authorizationService.getAuthenticatedUser();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid current password");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }

    @Override
    public void deleteAuthenticated() {
        User user = authorizationService.getAuthenticatedUser();
        repository.delete(user);
    }

    private UserResponse updateUser(UserUpdateRequest request, User user) {
        if (request.isEmailProvided()
                && StringUtils.hasText(request.getEmail())) {
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Email already registered");
        }
    }

}
