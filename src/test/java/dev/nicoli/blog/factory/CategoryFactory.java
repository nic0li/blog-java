package dev.nicoli.blog.factory;

import dev.nicoli.blog.dto.category.*;
import dev.nicoli.blog.entity.Category;

public final class CategoryFactory {

    public static Category movies() {
        return category(1L, "Movies");
    }

    public static Category books() {
        return category(2L, "Books");
    }

    public static CategoryRequest request() {
        return new CategoryRequest("Movies");
    }

    public static CategoryResponse response() {
        return new CategoryResponse(1L, "Movies");
    }

    private static Category category(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

}