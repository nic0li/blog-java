package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.AbstractCrudService;
import dev.nicoli.blog.dto.category.CategoryRequest;
import dev.nicoli.blog.dto.category.CategoryResponse;
import dev.nicoli.blog.entity.Category;
import dev.nicoli.blog.mapper.CategoryMapper;
import dev.nicoli.blog.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Function;

@Service
public class CategoryService extends AbstractCrudService<Category,
        CategoryResponse,
        CategoryResponse,
        CategoryRequest,
        CategoryRequest> {

    private final CategoryRepository repository;

    private final AuthorizationService authorizationService;

    public CategoryService(CategoryRepository repository,
                           AuthorizationService authorizationService) {
        this.repository = repository;
        this.authorizationService = authorizationService;
    }

    @Override
    protected CategoryRepository repository() {
        return repository;
    }

    @Override
    protected Function<Category, CategoryResponse> mapperResponse() {
        return CategoryMapper::toResponse;
    }

    @Override
    protected void validateDeleteAuthorization(Category category) {
        authorizationService.validateAdmin();
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        authorizationService.validateAdmin();
        validateUniqueName(request.name(), null);
        Category category = CategoryMapper.toEntity(request);
        return CategoryMapper.toResponse(repository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        authorizationService.validateAdmin();
        Category category = getById(id);
        validateUniqueName(request.name(), id);
        CategoryMapper.updateEntity(category, request);
        return CategoryMapper.toResponse(repository.save(category));
    }

    public List<CategoryResponse> findAll(String name) {
        return findCategories(name).stream()
                .map(CategoryMapper::toResponse).toList();
    }

    private List<Category> findCategories(String name) {
        if (name == null || name.isBlank()) {
            return repository.findAll();
        }
        return repository.findAllByNameContainingIgnoreCase(name);
    }

    private void validateUniqueName(String name, Long categoryId) {
        if (name == null) {
            return;
        }
        var categoryByName = repository.findByNameIgnoreCase(name);
        boolean categoryAlreadyExists = categoryByName.isPresent();
        boolean isDifferent = categoryAlreadyExists
                && !categoryByName.get().getId().equals(categoryId);

        if (categoryAlreadyExists && isDifferent) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Category already exists");
        }
    }

}
