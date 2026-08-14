package dev.nicoli.blog.service;

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
    private UserService service;

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
        assertThrows(
                ResponseStatusException.class,
                () -> service.create(request));

        verify(repository).findByEmail("maria@email.com");
        verifyNoInteractions(passwordEncoder);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequest();
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.empty());
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(user);
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateUserWithNullEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestNullEmail();
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(user);
        verify(repository, never()).findByEmail(any());
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateUserWithSameEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestSameEmail();
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));
        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(user));
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(user);
        verify(repository).findByEmail("maria@email.com");
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateUserWithEmailEmpty() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestEmailEmpty();
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(user);
        verify(repository, never()).findByEmail(any());
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateUserWithoutEmailField() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequestWithoutEmail();
        User user = UserFactory.user();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));
        when(repository.save(user))
                .thenReturn(user);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponseSameEmail();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(user);
        verify(repository, never()).findByEmail(any());
        verify(repository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequest();
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.of(admin));

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.update(1L, request));

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(user);
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
        assertThrows(
                ResponseStatusException.class,
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
        assertThrows(
                ResponseStatusException.class,
                () -> service.updateMe(request));

        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository, never()).save(any());
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
        assertThrows(
                ResponseStatusException.class,
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

}