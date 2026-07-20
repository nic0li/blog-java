package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.auth.LoginRequest;
import dev.nicoli.blog.dto.auth.LoginResponse;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.mapper.UserMapper;
import dev.nicoli.blog.repository.UserRepository;
import dev.nicoli.blog.security.JwtService;
import dev.nicoli.blog.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository repository;

    private final JwtService jwtService;
    
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository repository,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse authenticate(LoginRequest request) {
        Authentication credentials =
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.password());
        authenticationManager.authenticate(credentials);

        User user = repository.findByEmail(request.login())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String token = jwtService.generateToken(user.getId());

        return new LoginResponse(UserMapper.toResponse(user), token);
    }

    protected User getAuthenticatedUser() {
        UserDetailsImpl principal =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        return repository.findById(principal.user().getId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "User not authenticated"));
    }

}
