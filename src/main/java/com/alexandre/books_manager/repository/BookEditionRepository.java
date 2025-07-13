package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.BookEdition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookEditionRepository extends CrudRepository<BookEdition, String> {
    Optional<BookEdition> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
