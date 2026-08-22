package dev.blog.service;

import dev.blog.service.interfaces.EntityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class EntityServiceImpl<Entity> implements EntityService<Entity> {

    private final JpaRepository<Entity, Long> repository;
    private final Class<Entity> entityClass;

    protected EntityServiceImpl(
            JpaRepository<Entity, Long> repository,
            Class<Entity> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    public Entity findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        entityClass.getSimpleName() + " not found"));
    }

}
