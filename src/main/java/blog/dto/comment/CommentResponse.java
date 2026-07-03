package blog.dto.comment;

import blog.dto.user.*;

import java.time.LocalDateTime;

public record CommentResponse(

    Long id,

    String content,

    UserResponse user,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {}
