package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByBatchNumberAndEditionIsbn(String batchNumber, String isbn);
    void deleteByBatchNumberAndEditionIsbn(String batchNumber, String isbn);
}
