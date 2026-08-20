package dev.nicoli.blog.service;

import dev.nicoli.blog.common.enums.UserRole;
import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.factory.UserFactory;
import dev.nicoli.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        UserCreateRequest request = UserFactory.createRequest();
        User user = UserFactory.user();

        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");
        when(repository.save(any(User.class)))
                .thenReturn(user);

        // When
        UserResponse response = service.create(request);

        // Then
        UserResponse expected = UserFactory.response();
        assertEquals(expected, response);

        verify(repository).findByEmail("maria@email.com");
        verify(passwordEncoder).encode("123456");
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingUserWithExistingEmail() {
        // Given
        UserCreateRequest request = UserFactory.createRequest();
        User user = UserFactory.user();

        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(user));

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.create(request));

        verify(repository).findByEmail("maria@email.com");
        verifyNoInteractions(passwordEncoder);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateAuthenticatedUserSuccessfully() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequest();
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.empty());
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.updateMe(request);

        // Then
        UserResponse expected = UserFactory.updatedResponse();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateAuthenticatedUserWithNullEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestNullEmail();
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.updateMe(request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
        verify(repository, never()).findByEmail(any());
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateAuthenticatedUserWithSameEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestSameEmail();
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);
        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(user));
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.updateMe(request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findByEmail("maria@email.com");
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateAuthenticatedUserWithEmailEmpty() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestEmailEmpty();
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.updateMe(request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
        verify(repository, never()).findByEmail(any());
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateAuthenticatedUserWithoutEmailField() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestWithoutEmail();
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.updateMe(request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
        verify(repository, never()).findByEmail(any());
        verify(repository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAuthenticatedUserWithExistingEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequest();
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.of(admin));

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.updateMe(request));

        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnAllUsers() {
        // Given
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(repository.findAll())
                .thenReturn(List.of(user, admin));

        // When
        List<UserViewResponse> response = service.findAll();

        // Then
        assertEquals(2, response.size());
        assertEquals("Maria", response.getFirst().name());
        assertEquals("Ana", response.getLast().name());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        // Given
        when(repository.findAll())
                .thenReturn(List.of());

        // When
        List<UserViewResponse> response = service.findAll();

        // Then
        assertTrue(response.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnUserById() {
        // Given
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        // When
        UserViewResponse response = service.findById(1L);

        // Then
        UserViewResponse expected = UserFactory.viewResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.findById(1L));

        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnAuthenticatedUser() {
        // Given
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        UserResponse response = service.findMe();

        // Then
        UserResponse expected = UserFactory.response();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
    }

    @Test
    void shouldDeleteUser() {
        // Given
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        // When
        service.delete(1L);

        // Then
        verify(repository).findById(1L);
        verify(authorizationService).validateOwnerOrAdmin(user);
        verify(repository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        // Given
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.delete(1L));

        verify(repository).findById(1L);
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldDeleteAuthenticatedUser() {
        // Given
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        service.deleteMe();

        // Then
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).delete(user);
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        // Given
        UserPasswordUpdateRequest request =
                new UserPasswordUpdateRequest("123456", "654321");
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);

        when(passwordEncoder.matches("123456", user.getPassword()))
                .thenReturn(true);

        when(passwordEncoder.encode("654321"))
                .thenReturn("new-hashed-password");

        when(repository.save(user))
                .thenReturn(user);

        // When
        service.updatePassword(request);

        // Then
        assertEquals("new-hashed-password", user.getPassword());

        verify(authorizationService).getAuthenticatedUser();
        verify(passwordEncoder).matches("123456", "123456");
        verify(passwordEncoder).encode("654321");
        verify(repository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsInvalid() {
        // Given
        UserPasswordUpdateRequest request =
                new UserPasswordUpdateRequest("wrong-password", "654321");
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(user);

        when(passwordEncoder.matches("wrong-password", user.getPassword()))
                .thenReturn(false);

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.updatePassword(request));

        verify(authorizationService).getAuthenticatedUser();
        verify(passwordEncoder).matches("wrong-password", "123456");
        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldPromoteUserToAdmin() {
        // Given
        User admin = UserFactory.admin();
        User user = UserFactory.user();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(admin);

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.toggleRole(1L);

        // Then
        assertEquals(UserRole.ADMIN, user.getRole());
        assertEquals(UserRole.ADMIN, response.role());

        verify(authorizationService).validateAdmin();
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findById(1L);
        verify(repository).save(user);
    }

    @Test
    void shouldDemoteAdminToUser() {
        // Given
        User authenticatedAdmin = UserFactory.admin();

        User targetAdmin = UserFactory.admin();
        targetAdmin.setId(3L);

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(authenticatedAdmin);

        when(repository.findById(3L))
                .thenReturn(Optional.of(targetAdmin));

        when(repository.save(targetAdmin))
                .thenReturn(targetAdmin);

        // When
        UserResponse response = service.toggleRole(3L);

        // Then
        assertEquals(UserRole.USER, targetAdmin.getRole());
        assertEquals(UserRole.USER, response.role());

        verify(authorizationService).validateAdmin();
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findById(3L);
        verify(repository).save(targetAdmin);
    }

    @Test
    void shouldThrowExceptionWhenAdminChangesOwnRole() {
        // Given
        User admin = UserFactory.admin();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(admin);

        when(repository.findById(admin.getId()))
                .thenReturn(Optional.of(admin));

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.toggleRole(admin.getId()));

        verify(authorizationService).validateAdmin();
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findById(admin.getId());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserToChangeRoleIsNotFound() {
        // Given
        User admin = UserFactory.admin();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(admin);

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.toggleRole(999L));
        verify(authorizationService).validateAdmin();
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findById(999L);
        verify(repository, never()).save(any());
    }

}