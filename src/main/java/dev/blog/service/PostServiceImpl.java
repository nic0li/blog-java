package dev.blog.service;

import dev.blog.dto.post.*;
import dev.blog.entity.Post;
import dev.blog.mapper.PostMapper;
import dev.blog.repository.PostRepository;
import dev.blog.service.interfaces.AuthorizationService;
import dev.blog.service.interfaces.CategoryService;
import dev.blog.service.interfaces.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostServiceImpl extends EntityServiceImpl<Post> implements PostService {

    private final PostRepository repository;

    private final AuthorizationService authorizationService;

    private final CategoryService categoryService;

    public PostServiceImpl(PostRepository repository,
                           AuthorizationService authorizationService,
                           CategoryService categoryService) {
        super(repository, Post.class);
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.categoryService = categoryService;
    }

    @Override
    public PostResponse create(PostRequest request) {
        if (!request.hasTitle() || !request.hasContent() || !request.hasCategoryId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "All fields are required");
        }
        Post post = PostMapper.createEntity(request);
        post.setCategory(categoryService.findEntityById(request.categoryId()));
        post.setUser(authorizationService.getAuthenticatedUser());
        return PostMapper.toResponse(repository.save(post));
    }

    @Override
    public PostResponse update(Long id, PostRequest request) {
        Post post = findEntityById(id);
        authorizationService.validateOwner(post.getUser());
        PostMapper.updateEntity(post, request);
        if (request.categoryId() != null) {
            post.setCategory(categoryService.findEntityById(request.categoryId()));
        }
        return PostMapper.toResponse(repository.save(post));
    }

    @Override
    public void delete(Long id) {
        Post post = findEntityById(id);
        authorizationService.validateOwnerOrAdmin(post.getUser());
        repository.delete(post);
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponse findById(Long id) {
        return PostMapper.toResponse(findEntityById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> findAll(PostFiltersRequest request) {
        return findPosts(request).stream()
                .map(PostMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> findByUser(Long id) {
        return repository.findAllByUserId(id).stream()
                .map(PostMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> findByAuthenticatedUser() {
        var user = authorizationService.getAuthenticatedUser();
        return repository.findAllByUserId(user.getId()).stream()
                .map(PostMapper::toResponse).toList();
    }

    private List<Post> findPosts(PostFiltersRequest request) {
        if (request.hasTitle() && request.hasCategory()) {
            return repository
                    .findAllByTitleContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(
                            request.title(),
                            request.category());
        }
        if (request.hasTitle()) {
            return repository
                    .findAllByTitleContainingIgnoreCase(request.title());
        }
        if (request.hasCategory()) {
            return repository
                    .findAllByCategoryNameContainingIgnoreCase(request.category());
        }
        return repository.findAll();
    }

}
