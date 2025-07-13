package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookEditionRepository bookEditionRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        // Create a test BookEdition first
        BookEdition testEdition = new BookEdition();
        testEdition.setIsbn("978-3-16-148410-0");
        testEdition.setTitle("Test Book Title");
        testEdition.setAuthorName("Test Author");
        testEdition.setNumber(1);
        testEdition = bookEditionRepository.save(testEdition);

        // Create a test Book
        testBook = new Book();
        testBook.setPublisher("Test Publisher");
        testBook.setPublishedYear(2023);
        testBook.setBatchNumber("BATCH001");
        testBook.setEdition(testEdition);
    }

    @Test
    void shouldSaveBook() {
        // When
        Book savedBook = bookRepository.save(testBook);

        // Then
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getBatchNumber()).isEqualTo("BATCH001");
        assertThat(savedBook.getPublisher()).isEqualTo("Test Publisher");
        assertThat(savedBook.getEdition().getIsbn()).isEqualTo("978-3-16-148410-0");
    }

    @Test
    void shouldFindBookByBatchNumberAndEditionIsbn() {
        // Given
        bookRepository.save(testBook);

        // When
        Optional<Book> foundBook = bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");

        // Then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getBatchNumber()).isEqualTo("BATCH001");
        assertThat(foundBook.get().getEdition().getIsbn()).isEqualTo("978-3-16-148410-0");
    }

    @Test
    void shouldReturnEmptyWhenBookNotFound() {
        // When
        Optional<Book> foundBook = bookRepository.findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0");

        // Then
        assertThat(foundBook).isEmpty();
    }

    @Test
    void shouldDeleteBookByBatchNumberAndEditionIsbn() {
        // Given
        bookRepository.save(testBook);

        // When
        bookRepository.deleteByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");

        // Then
        Optional<Book> foundBook = bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        assertThat(foundBook).isEmpty();
    }

    @Test
    void shouldFindAllBooks() {
        // Given
        bookRepository.save(testBook);

        BookEdition secondEdition = new BookEdition();
        secondEdition.setIsbn("978-3-16-148410-1");
        secondEdition.setTitle("Second Book Title");
        secondEdition.setAuthorName("Second Author");
        secondEdition.setNumber(2);
        secondEdition = bookEditionRepository.save(secondEdition);

        Book secondBook = new Book();
        secondBook.setPublisher("Second Publisher");
        secondBook.setPublishedYear(2024);
        secondBook.setBatchNumber("BATCH002");
        secondBook.setEdition(secondEdition);
        bookRepository.save(secondBook);

        // When
        Iterable<Book> allBooks = bookRepository.findAll();

        // Then
        assertThat(allBooks).hasSize(2);
    }
} 