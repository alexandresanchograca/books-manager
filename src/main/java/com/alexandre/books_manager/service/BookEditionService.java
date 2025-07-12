package com.alexandre.books_manager.service;

import com.alexandre.books_manager.model.BookBatch;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookEditionService {
    private BookEditionRepository bookEditionRepository;

    @Autowired
    public void setBookEditionRepository(BookEditionRepository bookEditionRepository) {
        this.bookEditionRepository = bookEditionRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<BookEdition> findAll() {
        return bookEditionRepository.findAll();
    }
}
