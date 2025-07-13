package com.alexandre.books_manager.service;

import com.alexandre.books_manager.exception.BadRequestException;
import com.alexandre.books_manager.exception.NotFoundException;
import com.alexandre.books_manager.model.BookEdition;
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
class BookEditionServiceTest {

    @Mock
    private BookEditionRepository bookEditionRepository;

    @InjectMocks
    private BookEditionService bookEditionService;

    private BookEdition testBookEdition;
    private BookEdition secondBookEdition;

    @BeforeEach
    void setUp() {
        testBookEdition = new BookEdition();
        testBookEdition.setIsbn("978-3-16-148410-0");
        testBookEdition.setTitle("Test Book Title");
        testBookEdition.setAuthorName("Test Author");
        testBookEdition.setNumber(1);

        secondBookEdition = new BookEdition();
        secondBookEdition.setIsbn("978-3-16-148410-1");
        secondBookEdition.setTitle("Second Book Title");
        secondBookEdition.setAuthorName("Second Author");
        secondBookEdition.setNumber(2);
    }

    @Test
    void shouldSaveBookEdition() {
        // Given
        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.empty());
        when(bookEditionRepository.save(any(BookEdition.class))).thenReturn(testBookEdition);

        // When
        BookEdition savedBookEdition = bookEditionService.save(testBookEdition);

        // Then
        assertThat(savedBookEdition).isNotNull();
        assertThat(savedBookEdition.getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(savedBookEdition.getTitle()).isEqualTo("Test Book Title");
        assertThat(savedBookEdition.getAuthorName()).isEqualTo("Test Author");
        assertThat(savedBookEdition.getNumber()).isEqualTo(1);
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).save(testBookEdition);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenSavingExistingBookEdition() {
        // Given
        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(testBookEdition));

        // When & Then
        assertThatThrownBy(() -> bookEditionService.save(testBookEdition))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Book Edition already exists");

        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, never()).save(any(BookEdition.class));
    }

    @Test
    void shouldFindAllBookEditions() {
        // Given
        List<BookEdition> bookEditions = Arrays.asList(testBookEdition, secondBookEdition);
        when(bookEditionRepository.findAll()).thenReturn(bookEditions);

        // When
        Iterable<BookEdition> foundBookEditions = bookEditionService.findAll();

        // Then
        assertThat(foundBookEditions).hasSize(2);
        assertThat(foundBookEditions).contains(testBookEdition, secondBookEdition);
        verify(bookEditionRepository, times(1)).findAll();
    }

    @Test
    void shouldFindBookEditionByIsbn() {
        // Given
        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(testBookEdition));

        // When
        Optional<BookEdition> foundBookEdition = bookEditionService.findByIsbn("978-3-16-148410-0");

        // Then
        assertThat(foundBookEdition).isPresent();
        assertThat(foundBookEdition.get().getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(foundBookEdition.get().getTitle()).isEqualTo("Test Book Title");
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
    }

    @Test
    void shouldReturnEmptyWhenBookEditionNotFound() {
        // Given
        when(bookEditionRepository.findByIsbn("NONEXISTENT-ISBN")).thenReturn(Optional.empty());

        // When
        Optional<BookEdition> foundBookEdition = bookEditionService.findByIsbn("NONEXISTENT-ISBN");

        // Then
        assertThat(foundBookEdition).isEmpty();
        verify(bookEditionRepository, times(1)).findByIsbn("NONEXISTENT-ISBN");
    }

    @Test
    void shouldUpdateBookEdition() {
        // Given
        BookEdition updatedBookEdition = new BookEdition();
        updatedBookEdition.setIsbn("978-3-16-148410-0");
        updatedBookEdition.setTitle("Updated Book Title");
        updatedBookEdition.setAuthorName("Updated Author");
        updatedBookEdition.setNumber(2);

        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(testBookEdition));
        when(bookEditionRepository.save(any(BookEdition.class))).thenReturn(updatedBookEdition);

        // When
        BookEdition result = bookEditionService.update(updatedBookEdition);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Book Title");
        assertThat(result.getAuthorName()).isEqualTo("Updated Author");
        assertThat(result.getNumber()).isEqualTo(2);
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).save(any(BookEdition.class));
    }

    @Test
    void shouldUpdateBookEditionWithPartialData() {
        // Given
        BookEdition partialUpdate = new BookEdition();
        partialUpdate.setIsbn("978-3-16-148410-0");
        partialUpdate.setTitle("Updated Title Only");
        // authorName and number are null

        BookEdition existingBookEdition = new BookEdition();
        existingBookEdition.setIsbn("978-3-16-148410-0");
        existingBookEdition.setTitle("Original Title");
        existingBookEdition.setAuthorName("Original Author");
        existingBookEdition.setNumber(1);

        BookEdition expectedUpdated = new BookEdition();
        expectedUpdated.setIsbn("978-3-16-148410-0");
        expectedUpdated.setTitle("Updated Title Only");
        expectedUpdated.setAuthorName("Original Author"); // Should remain unchanged
        expectedUpdated.setNumber(1); // Should remain unchanged

        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(existingBookEdition));
        when(bookEditionRepository.save(any(BookEdition.class))).thenReturn(expectedUpdated);

        // When
        BookEdition result = bookEditionService.update(partialUpdate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title Only");
        assertThat(result.getAuthorName()).isEqualTo("Original Author");
        assertThat(result.getNumber()).isEqualTo(1);
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).save(any(BookEdition.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentBookEdition() {
        // Given
        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookEditionService.update(testBookEdition))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book Edition not found");

        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, never()).save(any(BookEdition.class));
    }

    @Test
    void shouldDeleteBookEditionByIsbn() {
        // Given
        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(testBookEdition));
        doNothing().when(bookEditionRepository).deleteByIsbn("978-3-16-148410-0");

        // When
        bookEditionService.deleteByIsbn("978-3-16-148410-0");

        // Then
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).deleteByIsbn("978-3-16-148410-0");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentBookEdition() {
        // Given
        when(bookEditionRepository.findByIsbn("NONEXISTENT-ISBN")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookEditionService.deleteByIsbn("NONEXISTENT-ISBN"))
                .isInstanceOf(NotFoundException.class);

        verify(bookEditionRepository, times(1)).findByIsbn("NONEXISTENT-ISBN");
        verify(bookEditionRepository, never()).deleteByIsbn(anyString());
    }

    @Test
    void shouldUpdateOnlyTitleWhenOtherFieldsAreNull() {
        // Given
        BookEdition updateWithOnlyTitle = new BookEdition();
        updateWithOnlyTitle.setIsbn("978-3-16-148410-0");
        updateWithOnlyTitle.setTitle("New Title");
        // authorName and number are null

        BookEdition existingBookEdition = new BookEdition();
        existingBookEdition.setIsbn("978-3-16-148410-0");
        existingBookEdition.setTitle("Old Title");
        existingBookEdition.setAuthorName("Old Author");
        existingBookEdition.setNumber(5);

        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(existingBookEdition));
        when(bookEditionRepository.save(any(BookEdition.class))).thenReturn(existingBookEdition);

        // When
        BookEdition result = bookEditionService.update(updateWithOnlyTitle);

        // Then
        assertThat(result).isNotNull();
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).save(any(BookEdition.class));
    }

    @Test
    void shouldUpdateOnlyAuthorNameWhenOtherFieldsAreNull() {
        // Given
        BookEdition updateWithOnlyAuthor = new BookEdition();
        updateWithOnlyAuthor.setIsbn("978-3-16-148410-0");
        updateWithOnlyAuthor.setAuthorName("New Author");
        // title and number are null

        BookEdition existingBookEdition = new BookEdition();
        existingBookEdition.setIsbn("978-3-16-148410-0");
        existingBookEdition.setTitle("Old Title");
        existingBookEdition.setAuthorName("Old Author");
        existingBookEdition.setNumber(5);

        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(existingBookEdition));
        when(bookEditionRepository.save(any(BookEdition.class))).thenReturn(existingBookEdition);

        // When
        BookEdition result = bookEditionService.update(updateWithOnlyAuthor);

        // Then
        assertThat(result).isNotNull();
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).save(any(BookEdition.class));
    }

    @Test
    void shouldUpdateOnlyNumberWhenOtherFieldsAreNull() {
        // Given
        BookEdition updateWithOnlyNumber = new BookEdition();
        updateWithOnlyNumber.setIsbn("978-3-16-148410-0");
        updateWithOnlyNumber.setNumber(10);
        // title and authorName are null

        BookEdition existingBookEdition = new BookEdition();
        existingBookEdition.setIsbn("978-3-16-148410-0");
        existingBookEdition.setTitle("Old Title");
        existingBookEdition.setAuthorName("Old Author");
        existingBookEdition.setNumber(5);

        when(bookEditionRepository.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(existingBookEdition));
        when(bookEditionRepository.save(any(BookEdition.class))).thenReturn(existingBookEdition);

        // When
        BookEdition result = bookEditionService.update(updateWithOnlyNumber);

        // Then
        assertThat(result).isNotNull();
        verify(bookEditionRepository, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionRepository, times(1)).save(any(BookEdition.class));
    }
}
