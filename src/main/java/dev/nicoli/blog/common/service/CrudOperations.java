package dev.nicoli.blog.common.service;

public interface CrudOperations<
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> {

    ViewResponse findById(Long id);

    Response create(CreateRequest request);

    Response update(Long id, UpdateRequest request);

    void delete(Long id);

}