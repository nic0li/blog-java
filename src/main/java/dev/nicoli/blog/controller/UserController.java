package dev.nicoli.blog.controller;

import dev.nicoli.blog.dto.post.UserPostsResponse;
import dev.nicoli.blog.dto.user.UserResponse;
import dev.nicoli.blog.dto.user.UserUpdateRequest;
import dev.nicoli.blog.dto.user.UserViewResponse;
import dev.nicoli.blog.service.PostService;
import dev.nicoli.blog.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findById() {
        return ResponseEntity.ok(service.findMe());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> update(
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.updateMe(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete() {
        service.deleteMe();
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/posts")
    public ResponseEntity<UserPostsResponse> findPostsByUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                postService.findByUser(id));
    }

    @GetMapping("/me/posts")
    public ResponseEntity<UserPostsResponse> findAuthenticatedUserPosts() {
        return ResponseEntity.ok(
                postService.findAuthenticatedUserPosts());
    }

}
