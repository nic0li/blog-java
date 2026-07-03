package blog.common.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractCrudService<Entity,
  Response,
  EditResponse,
  CreateRequest,
  UpdateRequest> implements CrudOperations<
      Response,
      EditResponse,
      CreateRequest,
      UpdateRequest> {

  protected abstract JpaRepository<Entity, Long> repository();

  protected abstract Function<Entity, Response> mapperResponse();

  protected abstract void validateDeleteAuthorization(Entity entity);

  protected Entity findEntityById(Long id) {
    return repository().findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    entityName() + " not found"));
  }

  protected String entityName() {
    var type =
            (ParameterizedType) getClass().getGenericSuperclass();
    var entityClass =
            (Class<?>) type.getActualTypeArguments()[0];
    return entityClass.getSimpleName();
  }

  public Entity getById(Long id) {
    return findEntityById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> findAll() {
    return repository().findAll()
            .stream()
            .map(mapperResponse())
            .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Response findById(Long id) {
    return mapperResponse().apply(findEntityById(id));
  }

  @Override
  public void delete(Long id) {
    Entity entity = findEntityById(id);
    validateDeleteAuthorization(entity);
    repository().delete(entity);
  }

}
