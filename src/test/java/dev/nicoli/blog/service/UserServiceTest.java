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
        User maria = UserFactory.maria();

        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");
        when(repository.save(any(User.class)))
                .thenReturn(maria);

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
        User maria = UserFactory.maria();

        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(maria));

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
        User maria = UserFactory.maria();

        when(repository.findById(1L))
                .thenReturn(Optional.of(maria));
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.empty());
        when(repository.save(maria))
                .thenReturn(maria);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(maria);
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository).save(maria);
    }

    @Test
    void shouldUpdateUserWithoutChangingEmail() {
        // Given
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail(null);
        request.setName("Maria Silva");
        request.setPhoto(null);
        request.setBio("dev");
        User maria = UserFactory.maria();

        when(repository.findById(1L))
                .thenReturn(Optional.of(maria));
        when(repository.save(maria))
                .thenReturn(maria);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponseWithSameEmail();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(maria);
        verify(repository, never()).findByEmail(any());
        verify(repository).save(maria);
    }

    @Test
    void shouldUpdateUserKeepingSameEmail() {
        // Given
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("maria@email.com");
        request.setName("Maria Silva");
        request.setPhoto(null);
        request.setBio("dev");
        User maria = UserFactory.maria();

        when(repository.findById(1L))
                .thenReturn(Optional.of(maria));
        when(repository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(maria));
        when(repository.save(maria))
                .thenReturn(maria);

        // When
        UserResponse response = service.update(1L, request);

        // Then
        UserResponse expected = UserFactory.updatedResponseWithSameEmail();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(maria);
        verify(repository).findByEmail("maria@email.com");
        verify(repository).save(maria);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequest();
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(repository.findById(1L))
                .thenReturn(Optional.of(maria));
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.of(mariaSilva));

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.update(1L, request));

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(maria);
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnAllUsers() {
        // Given
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(repository.findAll())
                .thenReturn(List.of(maria, mariaSilva));

        // When
        List<UserViewResponse> response = service.findAll();

        // Then
        assertEquals(2, response.size());
        assertEquals("Maria", response.getFirst().name());
        assertEquals("Maria Silva", response.getLast().name());

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
        User maria = UserFactory.maria();

        when(repository.findById(1L))
                .thenReturn(Optional.of(maria));

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
        User maria = UserFactory.maria();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(maria);

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
        User maria = UserFactory.maria();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(maria);
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.empty());
        when(repository.save(maria))
                .thenReturn(maria);

        // When
        UserResponse response = service.updateMe(request);

        // Then
        UserResponse expected = UserFactory.updatedResponse();
        assertEquals(expected, response);

        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findByEmail("mariasilva@email.com");
        verify(repository).save(maria);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAuthenticatedUserWithExistingEmail() {
        // Given
        UserUpdateRequest request = UserFactory.updateRequest();
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(maria);
        when(repository.findByEmail("mariasilva@email.com"))
                .thenReturn(Optional.of(mariaSilva));

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
        User maria = UserFactory.maria();

        when(repository.findById(1L))
                .thenReturn(Optional.of(maria));

        // When
        service.delete(1L);

        // Then
        verify(repository).findById(1L);
        verify(authorizationService).validateOwnerOrAdmin(maria);
        verify(repository).delete(maria);
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
        User maria = UserFactory.maria();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        service.deleteMe();

        // Then
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).delete(maria);
    }

}