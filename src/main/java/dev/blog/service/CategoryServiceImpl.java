package dev.blog.service;

import dev.blog.dto.category.*;
import dev.blog.entity.Category;
import dev.blog.mapper.CategoryMapper;
import dev.blog.repository.CategoryRepository;
import dev.blog.service.interfaces.AuthorizationService;
import dev.blog.service.interfaces.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceImpl extends EntityServiceImpl<Category> implements CategoryService {

    private final CategoryRepository repository;

    private final AuthorizationService authorizationService;

    public CategoryServiceImpl(CategoryRepository repository,
                               AuthorizationService authorizationService) {
        super(repository, Category.class);
        this.repository = repository;
        this.authorizationService = authorizationService;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        authorizationService.validateAdmin();
        validateUniqueName(request.name(), null);
        Category category = CategoryMapper.createEntity(request);
        return CategoryMapper.toResponse(repository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        authorizationService.validateAdmin();
        Category category = findEntityById(id);
        validateUniqueName(request.name(), id);
        CategoryMapper.updateEntity(category, request);
        return CategoryMapper.toResponse(repository.save(category));
    }

    @Override
    public void delete(Long id) {
        authorizationService.validateAdmin();
        Category category = findEntityById(id);
        repository.delete(category);
    }

    @Override
    public CategoryResponse findById(Long id) {
        return CategoryMapper.toResponse(findEntityById(id));
    }

    @Override
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
