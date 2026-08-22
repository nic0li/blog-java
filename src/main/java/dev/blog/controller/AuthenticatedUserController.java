package dev.blog.controller;

import dev.blog.dto.post.*;
import dev.blog.dto.user.*;
import dev.blog.service.interfaces.PostService;
import dev.blog.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/me")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "AuthenticatedUser")
public class AuthenticatedUserController {

    private final UserService service;

    private final PostService postService;

    public AuthenticatedUserController(UserService service,
                                       PostService postService) {
        this.service = service;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> findAuthenticated() {
        return ResponseEntity.ok(service.findAuthenticated());
    }

    @PatchMapping
    public ResponseEntity<UserResponse> updateAuthenticated(
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.updateAuthenticated(request));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updateAuthenticatedPassword(
            @RequestBody UserPasswordUpdateRequest request) {

        service.updateAuthenticatedPassword(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAuthenticated() {
        service.deleteAuthenticated();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findAuthenticatedUserPosts() {
        return ResponseEntity.ok(postService.findByAuthenticatedUser());
    }

}
