package blog.mapper;

import blog.dto.comment.*;
import blog.model.Comment;

import java.util.List;

public abstract class CommentMapper {

  public static Comment createEntity(
          CommentCreateRequest request) {
    Comment comment = new Comment();
    comment.setContent(request.content());
    return comment;
  }

  public static void updateEntity(Comment comment,
                                 CommentUpdateRequest request) {
    if (request.content() != null) {
      comment.setContent(request.content());
    }
  }

  public static CommentResponse toEditResponse(Comment comment) {
    return new CommentResponse(comment.getId(),
      comment.getContent(),
      UserMapper.toEditResponse(comment.getUser()),
      comment.getCreatedAt(),
      comment.getUpdatedAt());
  }

  public static CommentViewResponse toResponse(Comment comment) {
    return new CommentViewResponse(comment.getId(),
      comment.getContent(),
      UserMapper.toResponse(comment.getUser()),
      comment.getCreatedAt(),
      comment.getUpdatedAt());
  }

  public static List<CommentViewResponse> toResponse(List<Comment> comments) {
    return comments.stream()
      .map(CommentMapper::toResponse)
      .toList();
  }

}
