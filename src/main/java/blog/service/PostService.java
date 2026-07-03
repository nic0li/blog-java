package blog.service;

import java.util.List;
import java.util.function.Function;

import blog.common.service.AbstractCrudService;
import blog.dto.post.*;
import blog.mapper.*;
import blog.model.Post;
import blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService extends AbstractCrudService<Post,
        PostViewResponse,
        PostResponse,
        PostCreateRequest,
        PostUpdateRequest> {

  @Autowired
  private PostRepository repository;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private UserService userService;

  @Override
  public PostRepository repository() {
    return repository;
  }

  @Override
  public Function<Post, PostViewResponse> mapperResponse() {
    return PostMapper::toResponse;
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
    return PostMapper.toEditResponse(repository.save(post));
  }

  @Override
  public PostResponse update(Long id, PostUpdateRequest request) {
    Post post = getById(id);
    authorizationService.validateOwner(post.getUser());
    PostMapper.updateEntity(post, request);
    if (request.categoryId() != null) {
      post.setCategory(categoryService.getById(request.categoryId()));
    }
    return PostMapper.toEditResponse(repository.save(post));
  }

  @Transactional(readOnly = true)
  public List<PostViewResponse> findAll(PostFiltersRequest request) {
    return findPosts(request).stream()
            .map(PostMapper::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public UserPostsResponse findByUser(Long id) {
    var user = userService.getById(id);
    List<UserPostResponse> userPosts =
            repository.findAllByUserId(id).stream()
            .map(PostMapper::toUserPostResponse).toList();
    return new UserPostsResponse(
            UserMapper.toResponse(user), userPosts);
  }

  @Transactional(readOnly = true)
  public UserPostsResponse findAuthenticatedUserPosts() {
    var user = authorizationService.currentUser();
    List<UserPostResponse> userPosts =
            repository.findAllByUserId(user.getId()).stream()
            .map(PostMapper::toUserPostResponse).toList();
    return new UserPostsResponse(
            UserMapper.toResponse(user), userPosts);
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
