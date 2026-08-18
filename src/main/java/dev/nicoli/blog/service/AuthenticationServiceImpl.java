package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.authentication.*;
import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.mapper.UserMapper;
import dev.nicoli.blog.repository.UserRepository;
import dev.nicoli.blog.security.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;

    private final JwtService jwtService;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository repository,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.login(),
                                request.password()));

        User user = ((UserDetailsImpl) authentication.getPrincipal()).user();

        String token = jwtService.generateToken(user.getId());

        return new AuthenticationResponse(UserMapper.toResponse(user), token);
    }

    @Override
    public User getAuthenticatedUser() {
        UserDetailsImpl principal = getAuthenticatedPrincipal();

        return repository.findById(principal.user().getId())
                .orElseThrow(this::unauthenticatedException);
    }

    private UserDetailsImpl getAuthenticatedPrincipal() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw unauthenticatedException();
        }

        return (UserDetailsImpl) authentication.getPrincipal();
    }

    private ResponseStatusException unauthenticatedException() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User not authenticated");
    }

}
