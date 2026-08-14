package dev.nicoli.blog.service;

import dev.nicoli.blog.dto.category.*;
import dev.nicoli.blog.entity.Category;
import dev.nicoli.blog.factory.CategoryFactory;
import dev.nicoli.blog.repository.CategoryRepository;
import dev.nicoli.blog.service.interfaces.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    void shouldCreateCategorySuccessfully() {
        // Given
        CategoryRequest request = CategoryFactory.request();
        Category category = CategoryFactory.movies();

        when(repository.findByNameIgnoreCase("Movies"))
                .thenReturn(Optional.empty());
        when(repository.save(any(Category.class)))
                .thenReturn(category);

        // When
        CategoryResponse response = service.create(request);

        // Then
        CategoryResponse expected = CategoryFactory.response();
        assertEquals(expected, response);

        verify(authorizationService).validateAdmin();
        verify(repository).findByNameIgnoreCase("Movies");
        verify(repository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        // Given
        CategoryRequest request = CategoryFactory.request();
        Category category = CategoryFactory.movies();

        when(repository.findByNameIgnoreCase("Movies"))
                .thenReturn(Optional.of(category));

        // When / Then
        assertThrows(ResponseStatusException.class,
                () -> service.create(request));

        verify(authorizationService).validateAdmin();
        verify(repository).findByNameIgnoreCase("Movies");
        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        // Given
        CategoryRequest request = new CategoryRequest("Books");
        Category category = CategoryFactory.movies();

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));
        when(repository.findByNameIgnoreCase("Books"))
                .thenReturn(Optional.empty());
        when(repository.save(category))
                .thenReturn(category);

        // When
        CategoryResponse response = service.update(1L, request);

        // Then
        CategoryResponse expected = new CategoryResponse(1L, "Books");
        assertEquals(expected, response);

        verify(authorizationService).validateAdmin();
        verify(repository).findById(1L);
        verify(repository).findByNameIgnoreCase("Books");
        verify(repository).save(category);
    }

    @Test
    void shouldUpdateCategoryWithSameName() {
        // Given
        CategoryRequest request = CategoryFactory.request();
        Category category = CategoryFactory.movies();

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));
        when(repository.findByNameIgnoreCase("Movies"))
                .thenReturn(Optional.of(category));
        when(repository.save(category))
                .thenReturn(category);

        // When
        CategoryResponse response = service.update(1L, request);

        // Then
        CategoryResponse expected = CategoryFactory.response();
        assertEquals(expected, response);

        verify(authorizationService).validateAdmin();
        verify(repository).findById(1L);
        verify(repository).findByNameIgnoreCase("Movies");
        verify(repository).save(category);
    }

    @Test
    void shouldUpdateCategoryWithoutChangingName() {
        // Given
        CategoryRequest request = new CategoryRequest(null);
        Category category = CategoryFactory.movies();

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        when(repository.save(category))
                .thenReturn(category);

        // When
        CategoryResponse response = service.update(1L, request);

        // Then
        CategoryResponse expected = CategoryFactory.response();
        assertEquals(expected, response);

        verify(authorizationService).validateAdmin();
        verify(repository).findById(1L);
        verify(repository, never()).findByNameIgnoreCase(any());
        verify(repository).save(category);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingCategoryWithExistingName() {
        // Given
        CategoryRequest request = CategoryFactory.request();
        Category category1 = CategoryFactory.movies();
        Category category2 = CategoryFactory.books();

        when(repository.findById(2L))
                .thenReturn(Optional.of(category2));
        when(repository.findByNameIgnoreCase("Movies"))
                .thenReturn(Optional.of(category1));

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.update(2L, request));

        verify(authorizationService).validateAdmin();
        verify(repository).findById(2L);
        verify(repository).findByNameIgnoreCase("Movies");
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnAllCategoriesWhenFilterIsNull() {
        // Given
        List<Category> categories = List.of(
                CategoryFactory.movies(),
                CategoryFactory.books());

        when(repository.findAll())
                .thenReturn(categories);

        // When
        List<CategoryResponse> response = service.findAll(null);

        // Then
        assertEquals(2, response.size());
        assertEquals("Movies", response.getFirst().name());
        assertEquals("Books", response.getLast().name());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnAllCategoriesWhenFilterIsBlank() {
        // Given
        List<Category> categories = List.of(
                CategoryFactory.movies(),
                CategoryFactory.books());

        when(repository.findAll())
                .thenReturn(categories);

        // When
        List<CategoryResponse> response = service.findAll("   ");

        // Then
        assertEquals(2, response.size());
        assertEquals("Movies", response.getFirst().name());
        assertEquals("Books", response.getLast().name());

        verify(repository).findAll();
        verify(repository, never())
                .findAllByNameContainingIgnoreCase(any());
    }

    @Test
    void shouldFilterCategoriesByName() {
        // Given
        Category category = CategoryFactory.movies();

        when(repository.findAllByNameContainingIgnoreCase("mov"))
                .thenReturn(List.of(category));

        // When
        List<CategoryResponse> response = service.findAll("mov");

        // Then
        assertEquals(1, response.size());
        assertEquals("Movies", response.getFirst().name());

        verify(repository)
                .findAllByNameContainingIgnoreCase("mov");
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        // Given
        when(repository.findAll())
                .thenReturn(List.of());

        // When
        List<CategoryResponse> response = service.findAll(null);

        // Then
        assertEquals(0, response.size());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoryMatchesFilter() {
        // Given
        when(repository.findAllByNameContainingIgnoreCase("mov"))
                .thenReturn(List.of());

        // When
        List<CategoryResponse> response = service.findAll("mov");

        // Then
        assertEquals(0, response.size());

        verify(repository)
                .findAllByNameContainingIgnoreCase("mov");
    }

    @Test
    void shouldReturnCategoryById() {
        // Given
        Category category = CategoryFactory.movies();

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        // When
        CategoryResponse response = service.findById(1L);

        // Then
        CategoryResponse expected = CategoryFactory.response();
        assertEquals(expected, response);

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        // Given
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.findById(1L));

        verify(repository).findById(1L);
    }

    @Test
    void shouldDeleteCategory() {
        // Given
        Category category = CategoryFactory.movies();

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        // When
        service.delete(1L);

        // Then
        verify(authorizationService).validateAdmin();
        verify(repository).findById(1L);
        verify(repository).delete(category);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCategory() {
        // Given
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                ResponseStatusException.class,
                () -> service.delete(1L));

        verify(repository).findById(1L);
        verify(repository, never()).delete(any());
    }

}