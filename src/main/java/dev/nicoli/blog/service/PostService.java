package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.AbstractCrudService;
import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;
import dev.nicoli.blog.mapper.PostMapper;
import dev.nicoli.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService extends AbstractCrudService<Post,
        PostResponse,
        PostViewResponse,
        PostCreateRequest,
        PostUpdateRequest> {

    private final PostRepository repository;

    private final AuthorizationService authorizationService;

    private final CategoryService categoryService;

    public PostService(PostRepository repository,
                       AuthorizationService authorizationService,
                       CategoryService categoryService) {
        super(repository, Post.class);
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.categoryService = categoryService;
    }

    @Override
    public PostResponse create(PostCreateRequest request) {
        Post post = PostMapper.createEntity(request);
        post.setCategory(categoryService.getById(request.categoryId()));
        post.setUser(authorizationService.getAuthenticatedUser());
        return PostMapper.toResponse(repository.save(post));
    }

    @Override
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = getById(id);
        authorizationService.validateOwner(post.getUser());
        PostMapper.updateEntity(post, request);
        if (request.categoryId() != null) {
            post.setCategory(categoryService.getById(request.categoryId()));
        }
        return PostMapper.toResponse(repository.save(post));
    }

    @Override
    public void delete(Long id) {
        Post post = getById(id);
        authorizationService.validateOwnerOrAdmin(post.getUser());
        repository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostViewResponse findById(Long id) {
        return PostMapper.toViewResponse(getById(id));
    }

    @Transactional(readOnly = true)
    public List<PostViewResponse> findAll(PostFiltersRequest request) {
        return findPosts(request).stream()
                .map(PostMapper::toViewResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PostViewResponse> findByUser(Long id) {
        return repository.findAllByUserId(id).stream()
                .map(PostMapper::toViewResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PostViewResponse> findByAuthenticatedUser() {
        var user = authorizationService.getAuthenticatedUser();
        return repository.findAllByUserId(user.getId()).stream()
                .map(PostMapper::toViewResponse).toList();
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
