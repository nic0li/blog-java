package dev.nicoli.blog.common.service;

public interface CrudService<
        Response,
        ViewResponse,
        CreateRequest> {

    Response create(CreateRequest request);

    void delete(Long id);

    ViewResponse findById(Long id);

}