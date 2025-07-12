package com.alexandre.books_manager.service;

import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookEditionService {
    private BookEditionRepository bookEditionRepository;

    @Autowired
    public void setBookEditionRepository(BookEditionRepository bookEditionRepository) {
        this.bookEditionRepository = bookEditionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<BookEdition> findByIsbn(String isbn) {
        return bookEditionRepository.findByIsbn(isbn);
    }

    @Transactional(readOnly = true)
    public Iterable<BookEdition> findAll() {
        return bookEditionRepository.findAll();
    }
}
