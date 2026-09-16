package dev.blog.controller;

import dev.blog.dto.comment.CommentRequest;
import dev.blog.dto.comment.CommentResponse;
import dev.blog.dto.post.*;
import dev.blog.service.interfaces.CommentService;
import dev.blog.service.interfaces.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Posts")
public class PostController {

    private final PostService service;

    private final CommentService commentService;

    public PostController(PostService service,
                          CommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll(
            @ModelAttribute @ParameterObject PostFiltersRequest filters) {
        return ResponseEntity.ok(service.findAll(filters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.create(id, request));
    }

}
