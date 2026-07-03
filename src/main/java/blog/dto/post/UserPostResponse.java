package blog.dto.post;

import blog.dto.category.CategoryResponse;
import blog.dto.comment.CommentViewResponse;

import java.time.LocalDateTime;
import java.util.List;

public record UserPostResponse(

    Long id,

    String title,

    String content,

    CategoryResponse category,

    List<CommentViewResponse> comments,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {}
