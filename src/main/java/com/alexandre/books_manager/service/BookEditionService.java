package com.alexandre.books_manager.service;

import com.alexandre.books_manager.exception.BadRequestException;
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

    @Transactional
    public Optional<BookEdition> save(BookEdition bookEdition) {
        Optional<BookEdition> foundBookEdition = bookEditionRepository.findByIsbn(bookEdition.getIsbn());

        if (foundBookEdition.isPresent()) {
            throw new BadRequestException("Book Edition already exists");
        }

        return Optional.of(bookEditionRepository.save(bookEdition));
    }

    @Transactional
    public Optional<BookEdition> update(BookEdition bookEdition) {
        Optional<BookEdition> foundBookEdition = bookEditionRepository.findByIsbn(bookEdition.getIsbn());

        if (foundBookEdition.isEmpty()) {
            return Optional.empty();
        }

        BookEdition updatedBookEdition = foundBookEdition.get();

        if (bookEdition.getAuthorName() != null) {
            updatedBookEdition.setAuthorName(bookEdition.getAuthorName());
        }

        if (bookEdition.getNumber() != null) {
            updatedBookEdition.setNumber(bookEdition.getNumber());
        }

        if (bookEdition.getTitle() != null) {
            updatedBookEdition.setTitle(bookEdition.getTitle());
        }

        return Optional.of(bookEditionRepository.save(updatedBookEdition));
    }
}
