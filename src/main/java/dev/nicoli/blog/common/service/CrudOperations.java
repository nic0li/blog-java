package dev.nicoli.blog.common.service;

import java.util.List;

public interface CrudOperations<
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> {

    List<ViewResponse> findAll();

    ViewResponse findById(Long id);

    Response create(CreateRequest request);

    Response update(Long id, UpdateRequest request);

    void delete(Long id);

}