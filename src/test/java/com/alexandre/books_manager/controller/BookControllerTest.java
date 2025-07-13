package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookConverter;
import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.exception.NotFoundException;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookConverter bookConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private Book createTestBook(Long id, String batchNumber, String publisher, int year, BookEdition edition) {
        Book book = new Book();
        book.setId(id);
        book.setBatchNumber(batchNumber);
        book.setPublisher(publisher);
        book.setPublishedYear(year);
        book.setEdition(edition);
        return book;
    }

    private BookEdition createTestEdition(String isbn, String title, String author, int number) {
        BookEdition edition = new BookEdition();
        edition.setIsbn(isbn);
        edition.setTitle(title);
        edition.setAuthorName(author);
        edition.setNumber(number);
        return edition;
    }

    private BookDTO createTestBookDTO(String publisher, int year, String batchNumber, BookEditionDTO editionDTO) {
        return new BookDTO(publisher, year, batchNumber, editionDTO);
    }

    private UpdateBookDTO createUpdateBookDTO(String publisher, int year, String batchNumber, UpdateBookEditionDTO editionDTO) {
        return new UpdateBookDTO(publisher, year, batchNumber, editionDTO);
    }

    private BookEditionDTO createBookEditionDTO(String isbn, String title, String author, int number) {
        return new BookEditionDTO(isbn, title, author, number);
    }

    private UpdateBookEditionDTO createUpdateBookEditionDTO(String isbn, String title, String author, int number) {
        return new UpdateBookEditionDTO(isbn, title, author, number);
    }

    @Test
    @DisplayName("Create a new book successfully")
    void shouldCreateBook() throws Exception {
        // Given
        BookEdition edition = createTestEdition("978-3-16-148410-0", "Test Title", "Test Author", 1);
        Book book = createTestBook(null, "BATCH001", "Test Publisher", 2023, edition);
        BookEditionDTO editionDTO = createBookEditionDTO("978-3-16-148410-0", "Test Title", "Test Author", 1);
        BookDTO bookDTO = createTestBookDTO("Test Publisher", 2023, "BATCH001", editionDTO);

        when(bookConverter.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookService.save(any(Book.class))).thenReturn(book);
        when(bookConverter.toDto(any(Book.class))).thenReturn(bookDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.batchNumber").value("BATCH001"))
                .andExpect(jsonPath("$.edition.isbn").value("978-3-16-148410-0"));

        verify(bookConverter).toEntity(eq(bookDTO));
        verify(bookService).save(eq(book));
        verify(bookConverter).toDto(eq(book));
    }

    @Test
    @DisplayName("Return all books")
    void shouldReturnAllBooks() throws Exception {
        // Given
        BookEdition edition = createTestEdition("978-3-16-148410-0", "Test Title", "Test Author", 1);
        Book book1 = createTestBook(1L, "BATCH001", "Publisher1", 2023, edition);
        Book book2 = createTestBook(2L, "BATCH002", "Publisher2", 2024, edition);
        List<Book> books = Arrays.asList(book1, book2);

        BookEditionDTO editionDTO = createBookEditionDTO("978-3-16-148410-0", "Test Title", "Test Author", 1);
        BookDTO dto1 = createTestBookDTO("Publisher1", 2023, "BATCH001", editionDTO);
        BookDTO dto2 = createTestBookDTO("Publisher2", 2024, "BATCH002", editionDTO);
        List<BookDTO> dtos = Arrays.asList(dto1, dto2);

        when(bookService.findAll()).thenReturn(books);
        when(bookConverter.toDtoList(books)).thenReturn(dtos);

        // When & Then
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].publisher").value("Publisher1"))
                .andExpect(jsonPath("$[1].publisher").value("Publisher2"));

        verify(bookService).findAll();
        verify(bookConverter).toDtoList(books);
    }

    @Test
    @DisplayName("Find book by batch number and ISBN")
    void shouldFindBookByBatchNumberAndIsbn() throws Exception {
        // Given
        BookEdition edition = createTestEdition("978-3-16-148410-0", "Test Title", "Test Author", 1);
        Book book = createTestBook(1L, "BATCH001", "Test Publisher", 2023, edition);
        BookEditionDTO editionDTO = createBookEditionDTO("978-3-16-148410-0", "Test Title", "Test Author", 1);
        BookDTO bookDTO = createTestBookDTO("Test Publisher", 2023, "BATCH001", editionDTO);

        when(bookService.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0")).thenReturn(Optional.of(book));
        when(bookConverter.toDto(book)).thenReturn(bookDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/books/BATCH001/978-3-16-148410-0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.edition.isbn").value("978-3-16-148410-0"));

        verify(bookService).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        verify(bookConverter).toDto(book);
    }

    @Test
    @DisplayName("Return not found when book does not exist")
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        // Given
        when(bookService.findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/books/NONEXISTENT/978-3-16-148410-0"))
                .andExpect(status().isNotFound());

        verify(bookService).findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0");
        verify(bookConverter, never()).toDto(any(Book.class));
    }

    @Test
    @DisplayName("Update an existing book")
    void shouldUpdateBook() throws Exception {
        // Given
        BookEdition edition = createTestEdition("978-3-16-148410-0", "Test Title", "Test Author", 1);
        Book updatedBook = createTestBook(1L, "BATCH001", "Updated Publisher", 2024, edition);
        UpdateBookEditionDTO updateEditionDTO = createUpdateBookEditionDTO("978-3-16-148410-0", "Test Title", "Test Author", 1);
        UpdateBookDTO updateDTO = createUpdateBookDTO("Updated Publisher", 2024, "BATCH001", updateEditionDTO);
        BookEditionDTO updatedEditionDTO = createBookEditionDTO("978-3-16-148410-0", "Test Title", "Test Author", 1);
        BookDTO updatedDTO = createTestBookDTO("Updated Publisher", 2024, "BATCH001", updatedEditionDTO);

        when(bookConverter.toEntity(any(UpdateBookDTO.class))).thenReturn(updatedBook);
        when(bookService.update(any(Book.class))).thenReturn(updatedBook);
        when(bookConverter.toDto(updatedBook)).thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(patch("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisher").value("Updated Publisher"))
                .andExpect(jsonPath("$.publishedYear").value(2024));

        verify(bookConverter).toEntity(eq(updateDTO));
        verify(bookService).update(eq(updatedBook));
        verify(bookConverter).toDto(updatedBook);
    }

    @Test
    @DisplayName("Return not found when updating non-existent book")
    void shouldReturnNotFoundWhenUpdatingNonExistentBook() throws Exception {
        // Given (assume service throws exception or controller handles)
        UpdateBookEditionDTO updateEditionDTO = createUpdateBookEditionDTO("NONEXISTENT", "Title", "Author", 1);
        UpdateBookDTO updateDTO = createUpdateBookDTO("Publisher", 2024, "BATCH001", updateEditionDTO);
        when(bookConverter.toEntity(any(UpdateBookDTO.class))).thenReturn(new Book()); // Dummy
        when(bookService.update(any(Book.class))).thenThrow(new NotFoundException("Book not found"));

        // When & Then
        mockMvc.perform(patch("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(bookService).update(any(Book.class));
    }

    @Test
    @DisplayName("Delete an existing book")
    void shouldDeleteBook() throws Exception {
        // Given
        doNothing().when(bookService).delete("BATCH001", "978-3-16-148410-0");

        // When & Then
        mockMvc.perform(delete("/api/v1/books/BATCH001/978-3-16-148410-0"))
                .andExpect(status().isNoContent());

        verify(bookService).delete("BATCH001", "978-3-16-148410-0");
    }

    @Test
    @DisplayName("Return bad request when creating book with invalid data")
    void shouldReturnBadRequestWhenCreatingBookWithInvalidData() throws Exception {
        // Given
        BookEditionDTO invalidEditionDTO = createBookEditionDTO("", "", "", -1);
        BookDTO invalidDTO = createTestBookDTO("", -1, "", invalidEditionDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Return bad request when updating book with invalid data")
    void shouldReturnBadRequestWhenUpdatingBookWithInvalidData() throws Exception {
        // Given
        UpdateBookEditionDTO invalidEditionDTO = createUpdateBookEditionDTO("", "", "", -1);
        UpdateBookDTO invalidDTO = createUpdateBookDTO("", -1, "", invalidEditionDTO);

        // When & Then
        mockMvc.perform(patch("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).update(any(Book.class));
    }
}
