package com.psu.devboards.dbapi.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Abstract class representing a CRUD service for a JPA entity. Meant to reduce the amount of redundant code required
 * for general CRUD operations.
 *
 * @param <I> The ID type of the entity.
 * @param <E> The entity type.
 * @param <R> The request type.
 */
public abstract class CrudService<I, E, R> {
    protected final JpaRepository<E, I> repository;

    /**
     * Creates a new instance of the CrudService with its required repository.
     *
     * @param repository The repository for the JPA entity.
     */
    protected CrudService(JpaRepository<E, I> repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a listing of the requested resource.
     *
     * @return A list of the requested resource.
     */
    public List<E> list() {
        return repository.findAll();
    }

    /**
     * Retrieves the requested resource by its id.
     *
     * @param id The id the retrieve the resource by.
     * @return The resource if located.
     * @throws ResponseStatusException Throws not found if the resource is not found.
     */
    public E getById(I id) throws ResponseStatusException {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested resource not found."));
    }

    /**
     * Updates the requested resource by its id.
     *
     * @param id      The id to update the resource by.
     * @param request The request to update the resource with.
     * @throws ResponseStatusException Throws not found if the resource is not found.
     */
    public void updateById(I id, R request) throws ResponseStatusException {
        E entity = getById(id);
        repository.save(updateEntityFromRequest(request, entity));
    }

    /**
     * Deletes the requested resource by its id.
     *
     * @param id The id to delete the resource by.
     * @throws ResponseStatusException Throws not found if the resource is not found.
     */
    public void deleteById(I id) throws ResponseStatusException {
        E entity = getById(id);
        repository.delete(entity);
    }

    /**
     * Creates a new persisted instance of the resource.
     *
     * @param request The request to create the instance with.
     * @return The created instance.
     */
    public E create(R request) {
        return repository.save(createEntityFromRequest(request));
    }

    /**
     * Maps a request to an entity instance.
     *
     * @param request The request to get update attributes from.
     * @param entity  The entity to update with the request attributes.
     * @return The updated entity.
     */
    protected abstract E updateEntityFromRequest(R request, E entity);

    /**
     * Creates a new non-persisted entity instance from the supplied request.
     *
     * @param request The request to create the entity from.
     * @return The new entity instance.
     */
    protected abstract E createEntityFromRequest(R request);
}
