package com.alexandre.books_manager.converter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A generic interface for converting between two types: source type {@code S} and target type {@code T}.
 *
 * <p>This interface is commonly used in applications to separate domain (entity) models from
 * data transfer objects (DTOs), enabling a clean mapping between them.</p>
 *
 * @param <S> the source type (typically an entity)
 * @param <T> the target type (typically a DTO)
 */
public interface GenericConverter<S, T> {

    /**
     * Converts a source object of type {@code S} to a target object of type {@code T}.
     *
     * @param object the source object to convert
     * @return the corresponding target object
     */
    T toDto(S object);

    /**
     * Converts a target object of type {@code T} back to a source object of type {@code S}.
     *
     * @param object the target object to convert
     * @return the corresponding source object
     */
    S toEntity(T object);

    /**
     * Converts an {@link Iterable} of source objects to a {@link List} of target objects.
     * <p>This is a default implementation that uses Java Streams to convert each element
     * from {@code S} to {@code T} by calling the {@link #toDto(S)} method.</p>
     *
     * @param iterable the iterable containing source objects
     * @return a list of converted target objects
     */
    default List<T> toDtoList(Iterable<S> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
