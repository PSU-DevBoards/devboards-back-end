package com.psu.devboards.dbapi.services;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Abstract class representing a filterable CRUD service for a JPA entity. Meant to reduce the amount of redundant
 * code required for general CRUD operations and general filtering operations.
 *
 * @param <I> The ID type of the entity.
 * @param <E> The entity type.
 * @param <R> The request type.
 * @param <S> The specification type.
 */
abstract class FilterableCrudService<I, E, R, S extends Specification<E>> extends CrudService<I, E, R> {

    private final JpaSpecificationExecutor<E> specificationExecutor;

    /**
     * Creates a new instance of the FilterableCrudService with its required repository and specification executor.
     *
     * @param repository The repository and specification executor for the JPA entity.
     */
    protected <T extends JpaRepository<E, I> & JpaSpecificationExecutor<E>> FilterableCrudService(T repository) {
        super(repository);
        this.specificationExecutor = repository;
    }

    /**
     * List entities matching the provided specification.
     *
     * @param specification The specification an entity must match to be returned.
     * @return Entities matching the specification.
     */
    public List<E> list(S specification) {
        return specificationExecutor.findAll(specification);
    }
}
