package dev.blog.service;

import dev.blog.dto.comment.CommentResponse;
import dev.blog.dto.post.*;
import dev.blog.entity.Comment;
import dev.blog.entity.Post;
import dev.blog.factory.*;
import dev.blog.repository.PostRepository;
import dev.blog.service.interfaces.AuthorizationService;
import dev.blog.service.interfaces.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private PostServiceImpl service;

    @Test
    void shouldCreatePostSuccessfully() {
        // Given
        PostRequest request = PostFactory.request();
        Post post = PostFactory.post();

        when(categoryService.findEntityById(1L))
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

        verify(categoryService).findEntityById(1L);
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).save(any(Post.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingPostWithoutTitle() {
        // Given
        PostRequest request = PostFactory.request(null, "Content", 1L);

        // When / Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.create(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("All fields are required", exception.getReason());

        verifyNoInteractions(repository);
        verifyNoInteractions(categoryService);
        verifyNoInteractions(authorizationService);
    }

    @Test
    void shouldThrowExceptionWhenCreatingPostWithoutContent() {
        // Given
        PostRequest request = PostFactory.request("I like drama", null, 1L);

        // When / Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.create(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("All fields are required", exception.getReason());

        verifyNoInteractions(repository);
        verifyNoInteractions(categoryService);
        verifyNoInteractions(authorizationService);
    }

    @Test
    void shouldThrowExceptionWhenCreatingPostWithoutCategory() {
        // Given
        PostRequest request = PostFactory.request("I like drama", "Content", null);

        // When / Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.create(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("All fields are required", exception.getReason());

        verifyNoInteractions(repository);
        verifyNoInteractions(categoryService);
        verifyNoInteractions(authorizationService);
    }

    @Test
    void shouldUpdatePostSuccessfully() {
        // Given
        PostRequest request = PostFactory.request("I love drama", "Updated content", 1L);
        Post post = PostFactory.post();

        when(repository.findById(1L))
                .thenReturn(Optional.of(post));
        when(categoryService.findEntityById(1L))
                .thenReturn(CategoryFactory.movies());
        when(repository.save(post))
                .thenReturn(post);

        // When
        PostResponse response = service.update(1L, request);

        // Then
        PostResponse expected = PostFactory.response("I love drama", "Updated content");
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(post.getUser());
        verify(categoryService).findEntityById(1L);
        verify(repository).save(post);
    }

    @Test
    void shouldUpdatePostWithoutChangingCategory() {
        // Given
        PostRequest request = new PostRequest(null, null, null);
        Post post = PostFactory.post();

        when(repository.findById(1L))
                .thenReturn(Optional.of(post));
        when(repository.save(post))
                .thenReturn(post);

        // When
        PostResponse response = service.update(1L, request);

        // Then
        PostResponse expected = PostFactory.response();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(post.getUser());
        verify(categoryService, never()).findEntityById(any());
        verify(repository).save(post);
    }

    @Test
    void shouldReturnAllPosts() {
        // Given
        Post post = PostFactory.post();

        when(repository.findAll())
                .thenReturn(List.of(post));

        // When
        List<PostResponse> response =
                service.findAll(new PostFiltersRequest(null, null));

        // Then
        List<PostResponse> expected = List.of(PostFactory.response());
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
        List<PostResponse> response =
                service.findAll(new PostFiltersRequest("Like", null));

        // Then
        List<PostResponse> expected = List.of(PostFactory.response());
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
        List<PostResponse> response =
                service.findAll(new PostFiltersRequest(null, "Movies"));

        // Then
        List<PostResponse> expected = List.of(PostFactory.response());
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
        List<PostResponse> response =
                service.findAll(new PostFiltersRequest("Like", "Movies"));

        // Then
        List<PostResponse> expected = List.of(PostFactory.response());
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
        Comment comment = CommentFactory.comment();
        post.setComments(new ArrayList<>(List.of(comment)));

        when(repository.findAllByUserId(1L))
                .thenReturn(List.of(post));

        // When
        List<PostResponse> response = service.findByUser(1L);

        // Then
        PostResponse itemResponse = response.getFirst();
        CommentResponse commentWithoutPost = itemResponse.comments().getFirst();

        assertEquals(1, response.size());
        assertEquals(1, itemResponse.comments().size());
        assertNull(commentWithoutPost.post());

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
        List<PostResponse> response =
                service.findByAuthenticatedUser();

        // Then
        List<PostResponse> expected = List.of(PostFactory.response());
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
        PostResponse response = service.findById(1L);

        // Then
        PostResponse expected = PostFactory.response();
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