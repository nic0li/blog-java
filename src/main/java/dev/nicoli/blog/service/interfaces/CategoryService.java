package dev.nicoli.blog.service.interfaces;

import dev.nicoli.blog.dto.category.*;
import dev.nicoli.blog.entity.Category;

import java.util.List;

public interface CategoryService extends EntityService<Category> {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    CategoryResponse findById(Long id);

    List<CategoryResponse> findAll(String name);

}
