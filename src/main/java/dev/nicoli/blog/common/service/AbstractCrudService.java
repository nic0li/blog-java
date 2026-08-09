package dev.nicoli.blog.common.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractCrudService<Entity,
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> implements CrudOperations<
        Response,
        ViewResponse,
        CreateRequest,
        UpdateRequest> {

    private final JpaRepository<Entity, Long> repository;
    private final Class<Entity> entityClass;

    protected AbstractCrudService(
            JpaRepository<Entity, Long> repository,
            Class<Entity> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    public Entity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        entityClass.getSimpleName() + " not found"));
    }

}
