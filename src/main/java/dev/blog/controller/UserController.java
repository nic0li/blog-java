package dev.blog.controller;

import dev.blog.dto.post.*;
import dev.blog.dto.user.*;
import dev.blog.service.interfaces.PostService;
import dev.blog.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> toggleUserRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.toggleUserRole(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostResponse>> findPostsByUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                postService.findByUser(id));
    }

}
