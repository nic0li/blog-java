package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.post.*;
import dev.nicoli.blog.entity.Post;
import dev.nicoli.blog.factory.CategoryFactory;
import dev.nicoli.blog.factory.PostFactory;
import dev.nicoli.blog.factory.UserFactory;
import dev.nicoli.blog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private PostService service;

    @Test
    void shouldCreatePostSuccessfully() {
        // Given
        PostCreateRequest request = PostFactory.createRequest();
        Post post = PostFactory.post();

        when(categoryService.getById(1L))
                .thenReturn(CategoryFactory.movies());
        when(authorizationService.getAuthenticatedUser())
                .thenReturn(UserFactory.user());
        when(repository.save(any(Post.class)))
                .thenReturn(post);

        // When
        PostResponse response = service.create(request);

        // Then
        PostResponse expected = PostFactory.response();
        assertEquals(expected, response);

        verify(categoryService).getById(1L);
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).save(any(Post.class));
    }

    @Test
    void shouldUpdatePostSuccessfully() {
        // Given
        PostUpdateRequest request = PostFactory.updateRequest();
        Post post = PostFactory.post();
        Post updatedPost = PostFactory.updatedPost();

        when(repository.findById(1L))
                .thenReturn(Optional.of(post));
        when(categoryService.getById(1L))
                .thenReturn(CategoryFactory.movies());
        when(repository.save(post))
                .thenReturn(updatedPost);

        // When
        PostResponse response = service.update(1L, request);

        // Then
        PostResponse expected = PostFactory.updatedResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(post.getUser());
        verify(categoryService).getById(1L);
        verify(repository).save(post);
    }

    @Test
    void shouldUpdatePostWithoutChangingCategory() {
        // Given
        PostUpdateRequest request = new PostUpdateRequest(null, null, null);
        Post post = PostFactory.post();
        Post updatedPost = PostFactory.updatedPost();

        when(repository.findById(1L))
                .thenReturn(Optional.of(post));
        when(repository.save(post))
                .thenReturn(updatedPost);

        // When
        PostResponse response = service.update(1L, request);

        // Then
        PostResponse expected = PostFactory.updatedResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(post.getUser());
        verify(categoryService, never()).getById(any());
        verify(repository).save(post);
    }

    @Test
    void shouldReturnAllPosts() {
        // Given
        Post post = PostFactory.post();

        when(repository.findAll())
                .thenReturn(List.of(post));

        // When
        List<PostViewResponse> response =
                service.findAll(new PostFiltersRequest(null, null));

        // Then
        List<PostViewResponse> expected = List.of(PostFactory.viewResponse());
        assertEquals(expected, List.of(response.getFirst()));

        verify(repository).findAll();
    }

    @Test
    void shouldFilterPostsByTitle() {
        // Given
        Post post = PostFactory.post();

        when(repository.findAllByTitleContainingIgnoreCase("Like"))
                .thenReturn(List.of(post));

        // When
        List<PostViewResponse> response =
                service.findAll(new PostFiltersRequest("Like", null));

        // Then
        List<PostViewResponse> expected = List.of(PostFactory.viewResponse());
        assertEquals(expected, List.of(response.getFirst()));

        verify(repository)
                .findAllByTitleContainingIgnoreCase("Like");
    }

    @Test
    void shouldFilterPostsByCategory() {
        // Given
        Post post = PostFactory.post();

        when(repository.findAllByCategoryNameContainingIgnoreCase("Movies"))
                .thenReturn(List.of(post));

        // When
        List<PostViewResponse> response =
                service.findAll(new PostFiltersRequest(null, "Movies"));

        // Then
        List<PostViewResponse> expected = List.of(PostFactory.viewResponse());
        assertEquals(expected, List.of(response.getFirst()));

        verify(repository)
                .findAllByCategoryNameContainingIgnoreCase("Movies");
    }

    @Test
    void shouldFilterPostsByTitleAndCategory() {
        // Given
        Post post = PostFactory.post();

        when(repository
                .findAllByTitleContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(
                        "Like",
                        "Movies"))
                .thenReturn(List.of(post));

        // When
        List<PostViewResponse> response =
                service.findAll(new PostFiltersRequest("Like", "Movies"));

        // Then
        List<PostViewResponse> expected = List.of(PostFactory.viewResponse());
        assertEquals(expected, List.of(response.getFirst()));

        verify(repository)
                .findAllByTitleContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(
                        "Like",
                        "Movies");
    }

    @Test
    void shouldReturnPostsByUser() {
        // Given
        Post post = PostFactory.post();

        when(repository.findAllByUserId(1L))
                .thenReturn(List.of(post));

        // When
        List<PostViewResponse> response = service.findByUser(1L);

        // Then
        List<PostViewResponse> expected = List.of(PostFactory.viewResponse());
        assertEquals(expected, List.of(response.getFirst()));

        verify(repository).findAllByUserId(1L);
    }

    @Test
    void shouldReturnPostsByAuthenticatedUser() {
        // Given
        Post post = PostFactory.post();

        when(authorizationService.getAuthenticatedUser())
                .thenReturn(UserFactory.user());

        when(repository.findAllByUserId(1L))
                .thenReturn(List.of(post));

        // When
        List<PostViewResponse> response =
                service.findByAuthenticatedUser();

        // Then
        List<PostViewResponse> expected = List.of(PostFactory.viewResponse());
        assertEquals(expected, List.of(response.getFirst()));

        verify(authorizationService).getAuthenticatedUser();
        verify(repository).findAllByUserId(1L);
    }

    @Test
    void shouldReturnPostById() {
        // Given
        Post post = PostFactory.post();

        when(repository.findById(1L))
                .thenReturn(Optional.of(post));

        // When
        PostViewResponse response = service.findById(1L);

        // Then
        PostViewResponse expected = PostFactory.viewResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
    }

    @Test
    void shouldDeletePost() {
        // Given
        Post post = PostFactory.post();

        when(repository.findById(1L))
                .thenReturn(Optional.of(post));

        // When
        service.delete(1L);

        // Then
        verify(repository).findById(1L);
        verify(authorizationService)
                .validateOwnerOrAdmin(post.getUser());
        verify(repository).delete(post);
    }

}