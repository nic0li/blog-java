package blog.controller;

import java.util.List;

import blog.dto.category.*;
import blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categories")
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> findAll(
					@RequestParam(required = false) String name) {
		if (name == null || name.isBlank()) {
			return ResponseEntity.ok(service.findAll());
		}
		return ResponseEntity.ok(service.findByName(name));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<CategoryResponse> create(
					@RequestBody CategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
						.body(service.create(request));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(
					@PathVariable Long id,
					@RequestBody CategoryRequest request) {
		return ResponseEntity.status(HttpStatus.OK)
						.body(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
