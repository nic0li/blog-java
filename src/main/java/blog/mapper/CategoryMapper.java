package blog.mapper;

import blog.dto.category.*;
import blog.model.Category;

public abstract class CategoryMapper {

  public static Category toEntity(CategoryRequest request) {
    Category category = new Category();
    category.setName(request.name());
    return category;
  }

  public static CategoryResponse toResponse(Category category) {
    return new CategoryResponse(category.getId(),
      category.getName());
  }

}
