package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.comment.*;
import dev.nicoli.blog.entity.Comment;
import dev.nicoli.blog.factory.CommentFactory;
import dev.nicoli.blog.factory.PostFactory;
import dev.nicoli.blog.factory.UserFactory;
import dev.nicoli.blog.repository.CommentRepository;
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
    private CommentService service;

    @Test
    void shouldCreateCommentSuccessfully() {
        // Given
        CommentCreateRequest request = CommentFactory.createRequest();
        Comment comment = CommentFactory.comment();

        when(postService.getById(1L))
                .thenReturn(PostFactory.post());
        when(authorizationService.getAuthenticatedUser())
                .thenReturn(UserFactory.maria());
        when(repository.save(any(Comment.class)))
                .thenReturn(comment);

        // When
        CommentResponse response = service.create(request);

        // Then
        CommentResponse expected = CommentFactory.response();
        assertEquals(expected, response);

        verify(postService).getById(1L);
        verify(authorizationService).getAuthenticatedUser();
        verify(repository).save(any(Comment.class));
    }

    @Test
    void shouldUpdateCommentSuccessfully() {
        // Given
        CommentUpdateRequest request = CommentFactory.updateRequest();
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
        List<CommentViewResponse> response = service.findAll();

        // Then
        assertEquals(1, response.size());
        assertEquals(comment.getId(), response.getFirst().id());
        assertEquals(comment.getContent(), response.getFirst().content());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCommentsExist() {
        // Given
        when(repository.findAll())
                .thenReturn(List.of());

        // When
        List<CommentViewResponse> response = service.findAll();

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
        CommentViewResponse response = service.findById(1L);

        // Then
        CommentViewResponse expected = CommentFactory.viewResponse();

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