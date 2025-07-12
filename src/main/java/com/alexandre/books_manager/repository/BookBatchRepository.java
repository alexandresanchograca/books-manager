package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.BookBatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookBatchRepository extends CrudRepository<BookBatch, Long> {
}
