package dev.nicoli.blog.service.interfaces;

public interface EntityService<Entity> {

    Entity findEntityById(Long id);

}
