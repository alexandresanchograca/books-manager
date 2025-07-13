package com.alexandre.books_manager.service;

import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookRepository;
import com.alexandre.books_manager.repository.BookEditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookEditionRepository bookEditionRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookEdition testEdition;

    @BeforeEach
    void setUp() {
        testEdition = new BookEdition();
        testEdition.setIsbn("978-3-16-148410-0");
        testEdition.setTitle("Test Book Title");
        testEdition.setAuthorName("Test Author");
        testEdition.setNumber(1);

        testBook = new Book();
        testBook.setId(1L);
        testBook.setPublisher("Test Publisher");
        testBook.setPublishedYear(2023);
        testBook.setBatchNumber("BATCH001");
        testBook.setEdition(testEdition);
    }

    @Test
    void shouldSaveBook() {
        // Given
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        Book savedBook = bookService.save(testBook);

        // Then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isEqualTo(1L);
        assertThat(savedBook.getBatchNumber()).isEqualTo("BATCH001");
        verify(bookRepository, times(1)).save(testBook);
    }

    @Test
    void shouldFindAllBooks() {
        // Given
        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setBatchNumber("BATCH002");
        secondBook.setEdition(testEdition);

        List<Book> books = Arrays.asList(testBook, secondBook);
        when(bookRepository.findAll()).thenReturn(books);

        // When
        Iterable<Book> foundBooks = bookService.findAll();

        // Then
        assertThat(foundBooks).hasSize(2);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void shouldFindBookByBatchNumberAndEditionIsbn() {
        // Given
        when(bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.of(testBook));

        // When
        Optional<Book> foundBook = bookService.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");

        // Then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getBatchNumber()).isEqualTo("BATCH001");
        verify(bookRepository, times(1)).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
    }

    @Test
    void shouldReturnEmptyWhenBookNotFound() {
        // Given
        when(bookRepository.findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0"))
                .thenReturn(Optional.empty());

        // When
        Optional<Book> foundBook = bookService.findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0");

        // Then
        assertThat(foundBook).isEmpty();
        verify(bookRepository, times(1)).findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0");
    }

    @Test
    void shouldUpdateBook() {
        // Given
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setPublisher("Updated Publisher");
        updatedBook.setPublishedYear(2024);
        updatedBook.setBatchNumber("BATCH001");
        updatedBook.setEdition(testEdition);

        when(bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // When
        Book result = bookService.update(updatedBook);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPublisher()).isEqualTo("Updated Publisher");
        assertThat(result.getPublishedYear()).isEqualTo(2024);
        verify(bookRepository, times(1)).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentBook() {
        // Given
        when(bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.update(testBook))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book not found");

        verify(bookRepository, times(1)).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldDeleteBook() {
        // Given
        when(bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).deleteByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");

        // When
        bookService.delete("BATCH001", "978-3-16-148410-0");

        // Then
        verify(bookRepository, times(1)).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        verify(bookRepository, times(1)).deleteByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        // Given
        when(bookRepository.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.delete("BATCH001", "978-3-16-148410-0"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book not found");

        verify(bookRepository, times(1)).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        verify(bookRepository, never()).deleteByBatchNumberAndEditionIsbn(anyString(), anyString());
    }
} 