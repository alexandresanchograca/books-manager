package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookConverter;
import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookConverter bookConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;
    private BookEdition testEdition;
    private BookDTO testBookDTO;
    private UpdateBookDTO testUpdateBookDTO;

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

        BookEditionDTO editionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        testBookDTO = new BookDTO(
            "Test Publisher",
            2023,
            "BATCH001",
            editionDTO
        );

        UpdateBookEditionDTO updateEditionDTO = new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        testUpdateBookDTO = new UpdateBookDTO(
            "Updated Publisher",
            2024,
            "BATCH001",
            updateEditionDTO
        );
    }

    @Test
    void shouldCreateBook() throws Exception {
        // Given
        when(bookConverter.toEntity(any(BookDTO.class))).thenReturn(testBook);
        when(bookService.save(any(Book.class))).thenReturn(testBook);
        when(bookConverter.toDto(any(Book.class))).thenReturn(testBookDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.batchNumber").value("BATCH001"))
                .andExpect(jsonPath("$.edition.isbn").value("978-3-16-148410-0"));

        verify(bookConverter, times(1)).toEntity(testBookDTO);
        verify(bookService, times(1)).save(testBook);
        verify(bookConverter, times(1)).toDto(testBook);
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        // Given
        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setBatchNumber("BATCH002");
        secondBook.setEdition(testEdition);

        List<Book> books = Arrays.asList(testBook, secondBook);
        List<BookDTO> bookDTOs = Arrays.asList(testBookDTO, testBookDTO);

        when(bookService.findAll()).thenReturn(books);
        when(bookConverter.toDtoList(books)).thenReturn(bookDTOs);

        // When & Then
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].publisher").value("Test Publisher"))
                .andExpect(jsonPath("$[1].publisher").value("Test Publisher"));

        verify(bookService, times(1)).findAll();
        verify(bookConverter, times(1)).toDtoList(books);
    }

    @Test
    void shouldFindBookByBatchNumberAndIsbn() throws Exception {
        // Given
        when(bookService.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.of(testBook));
        when(bookConverter.toDto(testBook)).thenReturn(testBookDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/books/BATCH001/978-3-16-148410-0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.batchNumber").value("BATCH001"))
                .andExpect(jsonPath("$.edition.isbn").value("978-3-16-148410-0"));

        verify(bookService, times(1)).findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0");
        verify(bookConverter, times(1)).toDto(testBook);
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        // Given
        when(bookService.findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0"))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/books/NONEXISTENT/978-3-16-148410-0"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findByBatchNumberAndEditionIsbn("NONEXISTENT", "978-3-16-148410-0");
        verify(bookConverter, never()).toDto(any(Book.class));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        // Given
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setPublisher("Updated Publisher");
        updatedBook.setPublishedYear(2024);
        updatedBook.setBatchNumber("BATCH001");
        updatedBook.setEdition(testEdition);

        BookEditionDTO updatedEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        BookDTO updatedBookDTO = new BookDTO(
            "Updated Publisher",
            2024,
            "BATCH001",
            updatedEditionDTO
        );

        when(bookConverter.toEntity(any(UpdateBookDTO.class))).thenReturn(updatedBook);
        when(bookService.update(any(Book.class))).thenReturn(updatedBook);
        when(bookConverter.toDto(updatedBook)).thenReturn(updatedBookDTO);

        // When & Then
        mockMvc.perform(patch("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisher").value("Updated Publisher"))
                .andExpect(jsonPath("$.publishedYear").value(2024));

        verify(bookConverter, times(1)).toEntity(testUpdateBookDTO);
        verify(bookService, times(1)).update(updatedBook);
        verify(bookConverter, times(1)).toDto(updatedBook);
    }

    @Test
    void shouldDeleteBook() throws Exception {
        // Given
        when(bookService.findByBatchNumberAndEditionIsbn("BATCH001", "978-3-16-148410-0"))
                .thenReturn(Optional.of(testBook));
        doNothing().when(bookService).delete("BATCH001", "978-3-16-148410-0");

        // When & Then
        mockMvc.perform(delete("/api/v1/books/BATCH001/978-3-16-148410-0"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).delete("BATCH001", "978-3-16-148410-0");
    }

    @Test
    void shouldReturnBadRequestWhenCreatingBookWithInvalidData() throws Exception {
        // Given
        BookDTO invalidBookDTO = new BookDTO(
            "", // invalid empty publisher
            null, // invalid null publishedYear
            "", // invalid empty batchNumber
            new BookEditionDTO("", "", "", null) // invalid edition
        );

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookDTO)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).save(any(Book.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingBookWithInvalidData() throws Exception {
        // Given
        UpdateBookDTO invalidUpdateBookDTO = new UpdateBookDTO(
            "", // invalid empty publisher
            null, // invalid null publishedYear
            "", // invalid empty batchNumber
            new UpdateBookEditionDTO("", "", "", null) // invalid edition
        );

        // When & Then
        mockMvc.perform(patch("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBookDTO)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).update(any(Book.class));
    }
} 