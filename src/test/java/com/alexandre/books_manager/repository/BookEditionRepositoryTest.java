package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.BookEdition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookEditionRepositoryTest {

    @Autowired
    private BookEditionRepository bookEditionRepository;

    @Test
    void shouldSaveBookEdition() {
        // Given
        BookEdition bookEdition = new BookEdition();
        bookEdition.setIsbn("978-3-16-148410-0");
        bookEdition.setTitle("Test Book Title");
        bookEdition.setAuthorName("Test Author");
        bookEdition.setNumber(1);

        // When
        BookEdition savedEdition = bookEditionRepository.save(bookEdition);

        // Then
        assertThat(savedEdition.getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(savedEdition.getTitle()).isEqualTo("Test Book Title");
        assertThat(savedEdition.getAuthorName()).isEqualTo("Test Author");
    }

    @Test
    void shouldFindBookEditionByIsbn() {
        // Given
        BookEdition bookEdition = new BookEdition();
        bookEdition.setIsbn("978-3-16-148410-0");
        bookEdition.setTitle("Test Book Title");
        bookEdition.setAuthorName("Test Author");
        bookEdition.setNumber(1);
        bookEditionRepository.save(bookEdition);

        // When
        Optional<BookEdition> foundEdition = bookEditionRepository.findByIsbn("978-3-16-148410-0");

        // Then
        assertThat(foundEdition).isPresent();
        assertThat(foundEdition.get().getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(foundEdition.get().getTitle()).isEqualTo("Test Book Title");
    }

    @Test
    void shouldReturnEmptyWhenBookEditionNotFound() {
        // When
        Optional<BookEdition> foundEdition = bookEditionRepository.findByIsbn("NONEXISTENT-ISBN");

        // Then
        assertThat(foundEdition).isEmpty();
    }

    @Test
    void shouldFindAllBookEditions() {
        // Given
        BookEdition firstEdition = new BookEdition();
        firstEdition.setIsbn("978-3-16-148410-0");
        firstEdition.setTitle("First Book Title");
        firstEdition.setAuthorName("First Author");
        firstEdition.setNumber(1);
        bookEditionRepository.save(firstEdition);

        BookEdition secondEdition = new BookEdition();
        secondEdition.setIsbn("978-3-16-148410-1");
        secondEdition.setTitle("Second Book Title");
        secondEdition.setAuthorName("Second Author");
        secondEdition.setNumber(2);
        bookEditionRepository.save(secondEdition);

        // When
        Iterable<BookEdition> allEditions = bookEditionRepository.findAll();

        // Then
        assertThat(allEditions).hasSize(2);
    }

    @Test
    void shouldDeleteBookEditionByIsbn() {
        // Given
        BookEdition bookEdition = new BookEdition();
        bookEdition.setIsbn("978-3-16-148410-0");
        bookEdition.setTitle("Test Book Title");
        bookEdition.setAuthorName("Test Author");
        bookEdition.setNumber(1);
        bookEditionRepository.save(bookEdition);

        // When
        bookEditionRepository.deleteByIsbn("978-3-16-148410-0");

        // Then
        Optional<BookEdition> foundEdition = bookEditionRepository.findByIsbn("978-3-16-148410-0");
        assertThat(foundEdition).isEmpty();
    }
} 