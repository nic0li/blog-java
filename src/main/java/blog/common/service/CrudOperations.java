package blog.common.service;

import java.util.List;

public interface CrudOperations<
        Response,
        EditResponse,
        CreateRequest,
        UpdateRequest> {

  List<Response> findAll();

  Response findById(Long id);

  EditResponse create(CreateRequest request);

  EditResponse update(Long id, UpdateRequest request);

  void delete(Long id);

}