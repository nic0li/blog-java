package blog.dto.post;

import blog.dto.category.*;
import blog.dto.user.*;

import java.time.LocalDateTime;

public record PostResponse(

    Long id,

    String title,

    String content,

    CategoryResponse category,

    UserResponse user,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {}
