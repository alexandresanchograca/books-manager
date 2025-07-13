package com.alexandre.books_manager.service;

import com.alexandre.books_manager.exception.BadRequestException;
import com.alexandre.books_manager.exception.NotFoundException;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookEditionService implements GenericService<BookEdition> {
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
    public BookEdition save(BookEdition bookEdition) {
        Optional<BookEdition> foundBookEdition = bookEditionRepository.findByIsbn(bookEdition.getIsbn());

        if (foundBookEdition.isPresent()) {
            throw new BadRequestException("Book Edition already exists");
        }

        return bookEditionRepository.save(bookEdition);
    }

    @Transactional
    public BookEdition update(BookEdition bookEdition) {
        Optional<BookEdition> foundBookEdition = bookEditionRepository.findByIsbn(bookEdition.getIsbn());

        if (foundBookEdition.isEmpty()) {
            throw new NotFoundException("Book Edition not found");
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

        return bookEditionRepository.save(updatedBookEdition);
    }


    @Transactional
    public void deleteByIsbn(String isbn) {
        Optional<BookEdition> foundBookEdition = bookEditionRepository.findByIsbn(isbn);

        if (foundBookEdition.isEmpty()) {
            throw new NotFoundException("Book Edition not found");
        }

        bookEditionRepository.deleteByIsbn(isbn);
    }
}
