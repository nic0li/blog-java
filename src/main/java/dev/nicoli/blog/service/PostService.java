package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.AbstractCrudService;
import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;
import dev.nicoli.blog.mapper.PostMapper;
import dev.nicoli.blog.mapper.UserMapper;
import dev.nicoli.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
public class PostService extends AbstractCrudService<Post,
        PostResponse,
        PostViewResponse,
        PostCreateRequest,
        PostUpdateRequest> {

    private final PostRepository repository;

    private final AuthorizationService authorizationService;

    private final CategoryService categoryService;

    private final UserService userService;

    public PostService(PostRepository repository,
                       AuthorizationService authorizationService,
                       CategoryService categoryService,
                       UserService userService) {
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @Override
    public PostRepository repository() {
        return repository;
    }

    @Override
    public Function<Post, PostViewResponse> mapperResponse() {
        return PostMapper::toViewResponse;
    }

    @Override
    public void validateDeleteAuthorization(Post post) {
        authorizationService.validateOwnerOrAdmin(post.getUser());
    }

    @Override
    public PostResponse create(PostCreateRequest request) {
        Post post = PostMapper.createEntity(request);
        post.setCategory(categoryService.getById(request.categoryId()));
        post.setUser(authorizationService.currentUser());
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

    @Transactional(readOnly = true)
    public List<PostViewResponse> findAll(PostFiltersRequest request) {
        return findPosts(request).stream()
                .map(PostMapper::toViewResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserPostsResponse findByUser(Long id) {
        var user = userService.getById(id);
        List<UserPostResponse> userPosts =
                repository.findAllByUserId(id).stream()
                        .map(PostMapper::toUserPostResponse).toList();
        return new UserPostsResponse(
                UserMapper.toViewResponse(user), userPosts);
    }

    @Transactional(readOnly = true)
    public UserPostsResponse findAuthenticatedUserPosts() {
        var user = authorizationService.currentUser();
        List<UserPostResponse> userPosts =
                repository.findAllByUserId(user.getId()).stream()
                        .map(PostMapper::toUserPostResponse).toList();
        return new UserPostsResponse(
                UserMapper.toViewResponse(user), userPosts);
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
