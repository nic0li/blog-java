package blog.controller;

import java.util.List;

import blog.dto.post.PostFiltersRequest;
import blog.dto.post.*;
import blog.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Posts")
public class PostController {
	
	@Autowired
	private PostService service;

	@GetMapping
	public ResponseEntity<List<PostViewResponse>> findAll(
					@ModelAttribute
					@ParameterObject
					PostFiltersRequest request) {

		return ResponseEntity.ok(service.findAll(request));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostViewResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<PostResponse> create(
					@RequestBody PostCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
						.body(service.create(request));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<PostResponse> update(
					@PathVariable Long id,
					@RequestBody PostUpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK)
						.body(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
