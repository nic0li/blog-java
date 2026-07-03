package blog.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequest(

	@NotBlank
	String title,

	@NotBlank
	String content,

	@NotNull
	Long categoryId

) {}
