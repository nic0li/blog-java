package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.AbstractCrudService;
import dev.nicoli.blog.dto.comment.CommentCreateRequest;
import dev.nicoli.blog.dto.comment.CommentResponse;
import dev.nicoli.blog.dto.comment.CommentUpdateRequest;
import dev.nicoli.blog.dto.comment.CommentViewResponse;
import dev.nicoli.blog.entity.Comment;
import dev.nicoli.blog.mapper.CommentMapper;
import dev.nicoli.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CommentService extends AbstractCrudService<Comment,
        CommentResponse,
        CommentViewResponse,
        CommentCreateRequest,
        CommentUpdateRequest> {

    private final CommentRepository repository;

    private final AuthorizationService authorizationService;

    private final PostService postService;

    public CommentService(CommentRepository repository,
                          AuthorizationService authorizationService,
                          PostService postService) {
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.postService = postService;
    }

    @Override
    public CommentRepository repository() {
        return repository;
    }

    @Override
    public Function<Comment, CommentViewResponse> mapperResponse() {
        return CommentMapper::toViewResponse;
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
        return CommentMapper.toResponse(repository.save(comment));
    }

    @Override
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment comment = getById(id);
        authorizationService.validateOwner(comment.getUser());
        CommentMapper.updateEntity(comment, request);
        return CommentMapper.toResponse(repository.save(comment));
    }

}
