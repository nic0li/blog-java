package dev.nicoli.blog.common.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public abstract class AbstractCrudService<Entity,
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> implements CrudOperations<
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> {

    protected abstract JpaRepository<Entity, Long> repository();

    protected abstract Function<Entity, ViewResponse> mapperResponse();

    protected abstract void validateDeleteAuthorization(Entity entity);

    private String getEntityClassName() {
        var entityClass = (Class<?>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return entityClass.getSimpleName();
    }

    public Entity getById(Long id) {
        return repository().findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        getEntityClassName() + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ViewResponse findById(Long id) {
        return mapperResponse().apply(getById(id));
    }

    @Override
    public void delete(Long id) {
        Entity entity = getById(id);
        validateDeleteAuthorization(entity);
        repository().delete(entity);
    }

}
