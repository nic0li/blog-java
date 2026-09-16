package dev.blog.controller;

import dev.blog.dto.authentication.*;
import dev.blog.dto.user.*;
import dev.blog.service.interfaces.AuthenticationService;
import dev.blog.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    private final UserService userService;

    public AuthenticationController(AuthenticationService service,
                                    UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

}
