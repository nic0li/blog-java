package blog.service;

import java.util.List;
import java.util.function.Function;

import blog.common.service.AbstractCrudService;
import blog.dto.category.*;
import blog.mapper.CategoryMapper;
import blog.model.Category;
import blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService extends AbstractCrudService<Category,
        CategoryResponse,
        CategoryResponse,
        CategoryRequest,
        CategoryRequest> {

  @Autowired
  private CategoryRepository repository;

  @Autowired
  private AuthorizationService authorizationService;

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
