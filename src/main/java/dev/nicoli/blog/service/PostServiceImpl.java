package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.CrudServiceImpl;
import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;
import dev.nicoli.blog.mapper.PostMapper;
import dev.nicoli.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl extends CrudServiceImpl<Post> implements PostService {

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

    @Transactional(readOnly = true)
    @Override
    public PostResponse findById(Long id) {
        return PostMapper.toResponse(getById(id));
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
