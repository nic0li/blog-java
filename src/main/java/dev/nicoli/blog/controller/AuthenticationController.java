package dev.nicoli.blog.controller;

import dev.nicoli.blog.dto.authentication.LoginRequest;
import dev.nicoli.blog.dto.authentication.LoginResponse;
import dev.nicoli.blog.dto.user.UserCreateRequest;
import dev.nicoli.blog.dto.user.UserResponse;
import dev.nicoli.blog.service.AuthenticationService;
import dev.nicoli.blog.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> create(
            @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

}
