package dev.blog.mapper;

import dev.blog.dto.category.*;
import dev.blog.entity.Category;
import org.springframework.util.StringUtils;

public final class CategoryMapper {

    private CategoryMapper() { }

    public static Category createEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return category;
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(),
                category.getName());
    }

    public static void updateEntity(Category category,
                                    CategoryRequest request) {
        if (StringUtils.hasText(request.name())) {
            category.setName(request.name());
        }
    }

}
