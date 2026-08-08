package dev.nicoli.blog.service;

import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.factory.UserFactory;
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
    private AuthorizationService service;

    @Test
    void shouldReturnAuthenticatedUser() {
        // Given
        User maria = UserFactory.maria();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        User response = service.getAuthenticatedUser();

        // Then
        assertEquals(maria, response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnTrueWhenUserIsOwner() {
        // Given
        User maria = UserFactory.maria();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        boolean response = service.isOwner(maria);

        // Then
        assertTrue(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnFalseWhenUserIsNotOwner() {
        // Given
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        boolean response = service.isOwner(mariaSilva);

        // Then
        assertFalse(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {
        // Given
        User mariaSilva = UserFactory.mariaSilva();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(mariaSilva);

        // When
        boolean response = service.isAdmin();

        // Then
        assertTrue(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldReturnFalseWhenUserIsNotAdmin() {
        // Given
        User maria = UserFactory.maria();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        boolean response = service.isAdmin();

        // Then
        assertFalse(response);

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateOwner() {
        // Given
        User maria = UserFactory.maria();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        service.validateOwner(maria);

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        // Given
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.validateOwner(mariaSilva));

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateAdmin() {
        // Given
        User mariaSilva = UserFactory.mariaSilva();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(mariaSilva);

        // When
        service.validateAdmin();

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {
        // Given
        User maria = UserFactory.maria();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.validateAdmin());

        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateOwnerOrAdminWhenUserIsOwner() {
        // Given
        User maria = UserFactory.maria();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When
        service.validateOwnerOrAdmin(maria);

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldValidateOwnerOrAdminWhenUserIsAdmin() {
        // Given
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(mariaSilva);

        // When
        service.validateOwnerOrAdmin(maria);

        // Then
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOrAdmin() {
        // Given
        User maria = UserFactory.maria();
        User mariaSilva = UserFactory.mariaSilva();

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(maria);

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.validateOwnerOrAdmin(mariaSilva));

        verify(authenticationService).getAuthenticatedUser();
    }

}