package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.CrudServiceImpl;
import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.entity.Comment;
import dev.nicoli.blog.mapper.CommentMapper;
import dev.nicoli.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends CrudServiceImpl<Comment> implements CommentService {

    private final CommentRepository repository;

    private final AuthorizationService authorizationService;

    private final PostService postService;

    public CommentServiceImpl(CommentRepository repository,
                              AuthorizationService authorizationService,
                              PostService postService) {
        super(repository, Comment.class);
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.postService = postService;
    }

    @Override
    public CommentResponse create(CommentCreateRequest request) {
        Comment comment = CommentMapper.createEntity(request);
        comment.setPost(postService.getById(request.postId()));
        comment.setUser(authorizationService.getAuthenticatedUser());
        return CommentMapper.toResponse(repository.save(comment));
    }

    @Override
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment comment = getById(id);
        authorizationService.validateOwner(comment.getUser());
        CommentMapper.updateEntity(comment, request);
        return CommentMapper.toResponse(repository.save(comment));
    }

    @Override
    public void delete(Long id) {
        Comment comment = getById(id);
        authorizationService.validateOwnerOrAdmin(comment.getUser());
        repository.delete(comment);
    }

    @Override
    public CommentViewResponse findById(Long id) {
        return CommentMapper.toViewResponse(getById(id));
    }

    @Override
    public List<CommentViewResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(CommentMapper::toViewResponse)
                .toList();
    }

}
