package dev.blog.service;

import dev.blog.dto.comment.*;
import dev.blog.entity.Comment;
import dev.blog.mapper.CommentMapper;
import dev.blog.repository.CommentRepository;
import dev.blog.service.interfaces.AuthorizationService;
import dev.blog.service.interfaces.CommentService;
import dev.blog.service.interfaces.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends EntityServiceImpl<Comment> implements CommentService {

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
    public CommentResponse create(Long postId, CommentRequest request) {
        Comment comment = CommentMapper.createEntity(request);
        comment.setPost(postService.findEntityById(postId));
        comment.setUser(authorizationService.getAuthenticatedUser());
        return CommentMapper.toResponse(repository.save(comment));
    }

    @Override
    public CommentResponse update(Long id, CommentRequest request) {
        Comment comment = findEntityById(id);
        authorizationService.validateOwner(comment.getUser());
        CommentMapper.updateEntity(comment, request);
        return CommentMapper.toResponse(repository.save(comment));
    }

    @Override
    public void delete(Long id) {
        Comment comment = findEntityById(id);
        authorizationService.validateOwnerOrAdmin(comment.getUser());
        repository.delete(comment);
    }

    @Override
    public CommentResponse findById(Long id) {
        return CommentMapper.toResponse(findEntityById(id));
    }

    @Override
    public List<CommentResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

}
