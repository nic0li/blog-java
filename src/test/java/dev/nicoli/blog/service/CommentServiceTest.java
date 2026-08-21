package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.entity.Comment;
import dev.nicoli.blog.entity.Post;
import dev.nicoli.blog.factory.*;
import dev.nicoli.blog.repository.CommentRepository;
import dev.nicoli.blog.service.interfaces.AuthorizationService;
import dev.nicoli.blog.service.interfaces.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentServiceImpl service;

    @Test
    void shouldCreateCommentSuccessfully() {
        // Given
        CommentRequest request = CommentFactory.request();
        Comment comment = CommentFactory.comment();
        Post post = PostFactory.post();

        when(postService.findEntityById(1L))
                .thenReturn(post);
        when(authorizationService.getAuthenticatedUser())
                .thenReturn(UserFactory.user());
        when(repository.save(any(Comment.class)))
                .thenReturn(comment);

        // When
        CommentResponse response = service.create(post.getId(), request);

        // Then
        CommentResponse expected = CommentFactory.response();
        assertEquals(expected, response);

        verify(postService).findEntityById(1L);
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).save(any(Comment.class));
    }

    @Test
    void shouldUpdateCommentSuccessfully() {
        // Given
        CommentRequest request = CommentFactory.updateRequest();
        Comment comment = CommentFactory.comment();
        Comment updatedComment = CommentFactory.updatedComment();

        when(repository.findById(1L))
                .thenReturn(Optional.of(comment));
        when(repository.save(comment))
                .thenReturn(updatedComment);

        // When
        CommentResponse response = service.update(1L, request);

        // Then
        CommentResponse expected = CommentFactory.updatedResponse();
        assertEquals(expected, response);

        verify(repository).findById(1L);
        verify(authorizationService).validateOwner(comment.getUser());
        verify(repository).save(comment);
    }

    @Test
    void shouldReturnAllComments() {
        // Given
        Comment comment = CommentFactory.comment();

        when(repository.findAll())
                .thenReturn(List.of(comment));

        // When
        List<CommentResponse> response = service.findAll();

        // Then
        List<CommentResponse> expected = List.of(CommentFactory.response());
        assertEquals(expected, List.of(response.getFirst()));

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCommentsExist() {
        // Given
        when(repository.findAll())
                .thenReturn(List.of());

        // When
        List<CommentResponse> response = service.findAll();

        // Then
        assertTrue(response.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnCommentById() {
        // Given
        Comment comment = CommentFactory.comment();

        when(repository.findById(1L))
                .thenReturn(Optional.of(comment));

        // When
        CommentResponse response = service.findById(1L);

        // Then
        CommentResponse expected = CommentFactory.response();
        assertEquals(expected, response);

        verify(repository).findById(1L);
    }

    @Test
    void shouldDeleteComment() {
        // Given
        Comment comment = CommentFactory.comment();

        when(repository.findById(1L))
                .thenReturn(Optional.of(comment));

        // When
        service.delete(1L);

        // Then
        verify(repository).findById(1L);
        verify(authorizationService)
                .validateOwnerOrAdmin(comment.getUser());
        verify(repository).delete(comment);
    }

}