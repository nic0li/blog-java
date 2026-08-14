package dev.nicoli.blog.controller;

import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.dto.user.*;
import dev.nicoli.blog.service.interfaces.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users")
public class UserController {

    private final UserService service;

    private final PostService postService;

    public UserController(UserService service,
                          PostService postService) {
        this.service = service;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<UserViewResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserViewResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findMe() {
        return ResponseEntity.ok(service.findMe());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.updateMe(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        service.deleteMe();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostViewResponse>> findPostsByUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                postService.findByUser(id));
    }

    @GetMapping("/me/posts")
    public ResponseEntity<List<PostViewResponse>> findAuthenticatedUserPosts() {
        return ResponseEntity.ok(
                postService.findByAuthenticatedUser());
    }

}
