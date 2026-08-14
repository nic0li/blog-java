package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.authentication.*;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.factory.UserFactory;
import dev.nicoli.blog.repository.UserRepository;
import dev.nicoli.blog.security.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        // Given
        LoginRequest request =
                new LoginRequest("nicoli@email.com", "123456");

        User user = UserFactory.user();

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(user));

        when(jwtService.generateToken(1L))
                .thenReturn("jwt-token");

        // When
        LoginResponse response = service.authenticate(request);

        // Then
        assertEquals(1L, response.user().id());
        assertEquals("maria@email.com", response.user().email());
        assertEquals("jwt-token", response.token());

        verify(authenticationManager)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class));
        verify(authentication).getPrincipal();
        verify(jwtService)
                .generateToken(1L);
        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationFails() {
        // Given
        LoginRequest request =
                new LoginRequest("maria@email.com", "123456");

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When / Then
        assertThrows(
                BadCredentialsException.class,
                () -> service.authenticate(request));

        verify(authenticationManager)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class));

        verifyNoInteractions(repository);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldReturnAuthenticatedUser() {
        // Given
        User user = UserFactory.user();

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(user));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        // When
        User authenticatedUser = service.getAuthenticatedUser();

        // Then
        assertEquals(1L, authenticatedUser.getId());
        assertEquals("maria@email.com", authenticatedUser.getEmail());

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAuthenticated() {
        // Given / When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.getAuthenticatedUser());

        verifyNoInteractions(repository);
    }

}