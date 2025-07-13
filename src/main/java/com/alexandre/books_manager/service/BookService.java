package com.alexandre.books_manager.service;

import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookRepository;
import com.alexandre.books_manager.repository.BookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    private BookEditionRepository bookEditionRepository;

    @Autowired
    public void setBookBatchRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    public void setBookEditionRepository(BookEditionRepository bookEditionRepository) {
        this.bookEditionRepository = bookEditionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Optional<Book> findByBatchNumberAndEditionIsbn(String batchNumber, String editionIsbn) {
        return bookRepository.findByBatchNumberAndEditionIsbn(batchNumber, editionIsbn);
    }

    @Transactional
    public Optional<Book> update(Book book) {
        BookEdition edition = book.getEdition();
        Optional<Book> existingBook = bookRepository.findByBatchNumberAndEditionIsbn(book.getBatchNumber(), edition.getIsbn());

        if (existingBook.isEmpty()) {
            return Optional.empty();
        }

        Book updatedBook = existingBook.get();

        if (book.getPublishedYear() != null) {
            updatedBook.setPublishedYear(book.getPublishedYear());
        }

        if(book.getPublisher() != null) {
            updatedBook.setPublisher(book.getPublisher());
        }

        return Optional.of(bookRepository.save(updatedBook));
    }

    @Transactional
    public Book save(Book book) {
        BookEdition edition = book.getEdition();

        Optional<BookEdition> existingEdition = bookEditionRepository.findByIsbn(edition.getIsbn());

        if (existingEdition.isPresent()) {
            book.setEdition(existingEdition.get());
        } else {
            BookEdition savedEdition = bookEditionRepository.save(edition);
            book.setEdition(savedEdition);
        }

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }
}
