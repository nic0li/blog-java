package blog.dto.post;

import blog.dto.category.*;
import blog.dto.comment.*;
import blog.dto.user.*;

import java.time.LocalDateTime;
import java.util.List;

public record PostViewResponse(

    Long id,

    String title,

    String content,

    CategoryResponse category,

    UserViewResponse user,

    List<CommentViewResponse> comments,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {}
