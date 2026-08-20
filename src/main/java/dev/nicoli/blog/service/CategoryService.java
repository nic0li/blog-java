package dev.nicoli.blog.service;

import dev.nicoli.blog.common.service.CrudService;
import dev.nicoli.blog.dto.category.*;
import dev.nicoli.blog.entity.Category;

import java.util.List;

public interface CategoryService extends CrudService<
        CategoryResponse, CategoryResponse, CategoryRequest> {

    CategoryResponse update(Long id, CategoryRequest request);

    List<CategoryResponse> findAll(String name);

    Category getById(Long id);
}
