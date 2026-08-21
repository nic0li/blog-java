package dev.nicoli.blog.mapper;

import dev.nicoli.blog.dto.category.*;
import dev.nicoli.blog.entity.Category;

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
        if (request.name() != null) {
            category.setName(request.name());
        }
    }

}
