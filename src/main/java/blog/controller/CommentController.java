package blog.controller;

import java.util.List;

import blog.dto.comment.*;
import blog.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comments")
public class CommentController {
	
	@Autowired
	private CommentService service;
	
	@GetMapping
	public ResponseEntity<List<CommentViewResponse>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CommentViewResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<CommentResponse> create(
					@RequestBody CommentCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
						.body(service.create(request));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<CommentResponse> update(
					@PathVariable Long id,
					@RequestBody CommentUpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK)
						.body(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
