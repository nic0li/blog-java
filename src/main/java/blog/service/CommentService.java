package blog.service;

import java.util.function.Function;

import blog.common.service.AbstractCrudService;
import blog.dto.comment.*;
import blog.mapper.CommentMapper;
import blog.model.Comment;
import blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService extends AbstractCrudService<Comment,
        CommentViewResponse,
        CommentResponse,
        CommentCreateRequest,
        CommentUpdateRequest> {

  @Autowired
  private CommentRepository repository;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private PostService postService;

  @Override
  public CommentRepository repository() {
    return repository;
  }

  @Override
  public Function<Comment, CommentViewResponse> mapperResponse() {
    return CommentMapper::toResponse;
  }

  @Override
  public void validateDeleteAuthorization(Comment comment) {
    authorizationService.validateOwnerOrAdmin(comment.getUser());
  }

  @Override
  public CommentResponse create(CommentCreateRequest request) {
    Comment comment = CommentMapper.createEntity(request);
    comment.setPost(postService.getById(request.postId()));
    comment.setUser(authorizationService.currentUser());
    return CommentMapper.toEditResponse(repository.save(comment));
  }

  @Override
  public CommentResponse update(Long id, CommentUpdateRequest request) {
    Comment comment = getById(id);
    authorizationService.validateOwner(comment.getUser());
    CommentMapper.updateEntity(comment, request);
    return CommentMapper.toEditResponse(repository.save(comment));
  }

}
