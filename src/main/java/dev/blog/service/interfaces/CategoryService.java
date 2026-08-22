package dev.blog.service.interfaces;

import dev.blog.dto.category.*;
import dev.blog.entity.Category;

import java.util.List;

public interface CategoryService extends EntityService<Category> {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    CategoryResponse findById(Long id);

    List<CategoryResponse> findAll(String name);

}
