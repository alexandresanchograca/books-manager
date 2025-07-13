package com.alexandre.books_manager.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A generic converter to be used as a base for concrete converter implementations
 *
 * @param <S> the source type
 * @param <T> the target type
 */
public interface GenericConverter<S, T> {
    T toDto(S object);

    S toEntity(T object);

    /**
     * Converts the source iterables of type S to target type T
     *
     * @param iterable the iterable to convert
     * @return the list of converted elements
     */
    default List<T> toDtoList(Iterable<S> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
