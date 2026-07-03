package blog.dto.comment;

import blog.dto.user.*;

import java.time.LocalDateTime;

public record CommentViewResponse(

    Long id,

    String content,

    UserViewResponse user,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {}
