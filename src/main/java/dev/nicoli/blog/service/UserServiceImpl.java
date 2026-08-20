package dev.nicoli.blog.service;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.common.service.CrudServiceImpl;
import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.mapper.UserMapper;
import dev.nicoli.blog.repository.UserRepository;
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
    public void delete(Long id) {
        User user = getById(id);
        authorizationService.validateOwnerOrAdmin(user);
        repository.delete(user);
    }

    @Override
    public UserProfileResponse findById(Long id) {
        return UserMapper.toProfileResponse(getById(id));
    }

    @Override
    public List<UserProfileResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toProfileResponse)
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

    @Override
    public void updatePassword(UserPasswordUpdateRequest request) {
        User user = authorizationService.getAuthenticatedUser();
        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid current password");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }

    @Override
    public UserResponse toggleRole(Long id) {
        authorizationService.validateAdmin();
        User authenticatedUser = authorizationService.getAuthenticatedUser();
        User user = getById(id);
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
