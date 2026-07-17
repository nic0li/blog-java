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
    public CategoryRepository repository() {
        return repository;
    }

    @Override
    public Function<Category, CategoryResponse> mapperResponse() {
        return CategoryMapper::toResponse;
    }

    @Override
    public void validateDeleteAuthorization(Category category) {
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
        category.setName(request.name());
        return CategoryMapper.toResponse(repository.save(category));
    }

    public List<CategoryResponse> findByName(String name) {
        return repository.findAllByNameContainingIgnoreCase(name).stream()
                .map(CategoryMapper::toResponse).toList();
    }

    private void validateUniqueName(String name, Long currentCategoryId) {
        var category = repository.findByNameIgnoreCase(name);
        if (category.isPresent()
                && (currentCategoryId == null
                || !category.get().getId().equals(currentCategoryId))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Category already exists");
        }
    }

}
