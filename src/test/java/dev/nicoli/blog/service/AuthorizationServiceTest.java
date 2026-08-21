package dev.nicoli.blog.service;

import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.factory.UserFactory;
import dev.nicoli.blog.service.interfaces.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthorizationServiceImpl service;

    @Test
    void shouldReturnAuthenticatedUser() {
        // Given
        User user = UserFactory.user();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        User response = service.getAuthenticatedUser();

        // Then
        assertEquals(user, response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnTrueWhenUserIsOwner() {
        // Given
        User user = UserFactory.user();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        boolean response = service.isOwner(user);

        // Then
        assertTrue(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnFalseWhenUserIsNotOwner() {
        // Given
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        boolean response = service.isOwner(admin);

        // Then
        assertFalse(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {
        // Given
        User admin = UserFactory.admin();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(admin);

        // When
        boolean response = service.isAdmin();

        // Then
        assertTrue(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnFalseWhenUserIsNotAdmin() {
        // Given
        User user = UserFactory.user();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        boolean response = service.isAdmin();

        // Then
        assertFalse(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateOwner() {
        // Given
        User user = UserFactory.user();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        service.validateOwner(user);

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        // Given
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.validateOwner(admin));

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateAdmin() {
        // Given
        User admin = UserFactory.admin();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(admin);

        // When
        service.validateAdmin();

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {
        // Given
        User user = UserFactory.user();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.validateAdmin());

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateOwnerOrAdminWhenUserIsOwner() {
        // Given
        User user = UserFactory.user();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When
        service.validateOwnerOrAdmin(user);

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateOwnerOrAdminWhenUserIsAdmin() {
        // Given
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(admin);

        // When
        service.validateOwnerOrAdmin(user);

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOrAdmin() {
        // Given
        User user = UserFactory.user();
        User admin = UserFactory.admin();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(user);

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.validateOwnerOrAdmin(admin));

        verify(authenticationService).getAuthenticatedUser();
    }

}