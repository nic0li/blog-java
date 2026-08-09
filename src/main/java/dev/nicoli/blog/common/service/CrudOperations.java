package dev.nicoli.blog.common.service;

public interface CrudOperations<
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> {

    Response create(CreateRequest request);

    Response update(Long id, UpdateRequest request);

    void delete(Long id);

    ViewResponse findById(Long id);

}