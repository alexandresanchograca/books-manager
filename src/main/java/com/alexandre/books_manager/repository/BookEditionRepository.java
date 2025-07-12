package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.BookEdition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookEditionRepository extends CrudRepository<BookEdition, Long> {
}
