package com.alexandre.books_manager.service;

/**
 * A generic service interface defining basic CRUD operations.
 *
 * @param <T> the type of entity this service handles
 */
public interface GenericService<T> {

    /**
     * Persists a new entity or updates an existing one.
     *
     * @param entity the entity to save
     * @return the saved entity instance
     */
    T save(T entity);

    /**
     * Retrieves all instances of the entity.
     *
     * @return an iterable containing all entities
     */
    Iterable<T> findAll();
}
