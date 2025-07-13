package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookEditionConverter;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.service.BookEditionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookEditionController.class)
@ActiveProfiles("test")
class BookEditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookEditionService bookEditionService;

    @MockitoBean
    private BookEditionConverter bookEditionConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private BookEdition testBookEdition;
    private BookEdition secondBookEdition;
    private BookEditionDTO testBookEditionDTO;
    private BookEditionDTO secondBookEditionDTO;
    private UpdateBookEditionDTO testUpdateBookEditionDTO;

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

        testBookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        secondBookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-1",
            "Second Book Title",
            "Second Author",
            2
        );

        testUpdateBookEditionDTO = new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Updated Book Title",
            "Updated Author",
            2
        );
    }

    @Test
    void shouldCreateBookEdition() throws Exception {
        // Given
        when(bookEditionConverter.toEntity(any(BookEditionDTO.class))).thenReturn(testBookEdition);
        when(bookEditionService.save(any(BookEdition.class))).thenReturn(testBookEdition);
        when(bookEditionConverter.toDto(any(BookEdition.class))).thenReturn(testBookEditionDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookEditionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"))
                .andExpect(jsonPath("$.title").value("Test Book Title"))
                .andExpect(jsonPath("$.authorName").value("Test Author"))
                .andExpect(jsonPath("$.number").value(1));

        verify(bookEditionConverter, times(1)).toEntity(testBookEditionDTO);
        verify(bookEditionService, times(1)).save(testBookEdition);
        verify(bookEditionConverter, times(1)).toDto(testBookEdition);
    }

    @Test
    void shouldReturnAllBookEditions() throws Exception {
        // Given
        List<BookEdition> bookEditions = Arrays.asList(testBookEdition, secondBookEdition);
        List<BookEditionDTO> bookEditionDTOs = Arrays.asList(testBookEditionDTO, secondBookEditionDTO);

        when(bookEditionService.findAll()).thenReturn(bookEditions);
        when(bookEditionConverter.toDtoList(bookEditions)).thenReturn(bookEditionDTOs);

        // When & Then
        mockMvc.perform(get("/api/v1/book-editions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].isbn").value("978-3-16-148410-0"))
                .andExpect(jsonPath("$[0].title").value("Test Book Title"))
                .andExpect(jsonPath("$[1].isbn").value("978-3-16-148410-1"))
                .andExpect(jsonPath("$[1].title").value("Second Book Title"));

        verify(bookEditionService, times(1)).findAll();
        verify(bookEditionConverter, times(1)).toDtoList(bookEditions);
    }

    @Test
    void shouldFindBookEditionByIsbn() throws Exception {
        // Given
        when(bookEditionService.findByIsbn("978-3-16-148410-0")).thenReturn(Optional.of(testBookEdition));
        when(bookEditionConverter.toDto(testBookEdition)).thenReturn(testBookEditionDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/book-editions/978-3-16-148410-0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"))
                .andExpect(jsonPath("$.title").value("Test Book Title"))
                .andExpect(jsonPath("$.authorName").value("Test Author"))
                .andExpect(jsonPath("$.number").value(1));

        verify(bookEditionService, times(1)).findByIsbn("978-3-16-148410-0");
        verify(bookEditionConverter, times(1)).toDto(testBookEdition);
    }

    @Test
    void shouldReturnNotFoundWhenBookEditionDoesNotExist() throws Exception {
        // Given
        when(bookEditionService.findByIsbn("NONEXISTENT-ISBN")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/book-editions/NONEXISTENT-ISBN"))
                .andExpect(status().isNotFound());

        verify(bookEditionService, times(1)).findByIsbn("NONEXISTENT-ISBN");
        verify(bookEditionConverter, never()).toDto(any(BookEdition.class));
    }

    @Test
    void shouldUpdateBookEdition() throws Exception {
        // Given
        BookEdition updatedBookEdition = new BookEdition();
        updatedBookEdition.setIsbn("978-3-16-148410-0");
        updatedBookEdition.setTitle("Updated Book Title");
        updatedBookEdition.setAuthorName("Updated Author");
        updatedBookEdition.setNumber(2);

        BookEditionDTO updatedBookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Updated Book Title",
            "Updated Author",
            2
        );

        when(bookEditionConverter.toEntity(any(UpdateBookEditionDTO.class))).thenReturn(updatedBookEdition);
        when(bookEditionService.update(any(BookEdition.class))).thenReturn(updatedBookEdition);
        when(bookEditionConverter.toDto(updatedBookEdition)).thenReturn(updatedBookEditionDTO);

        // When & Then
        mockMvc.perform(patch("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateBookEditionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"))
                .andExpect(jsonPath("$.title").value("Updated Book Title"))
                .andExpect(jsonPath("$.authorName").value("Updated Author"))
                .andExpect(jsonPath("$.number").value(2));

        verify(bookEditionConverter, times(1)).toEntity(testUpdateBookEditionDTO);
        verify(bookEditionService, times(1)).update(updatedBookEdition);
        verify(bookEditionConverter, times(1)).toDto(updatedBookEdition);
    }

    @Test
    void shouldDeleteBookEdition() throws Exception {
        // Given
        doNothing().when(bookEditionService).deleteByIsbn("978-3-16-148410-0");

        // When & Then
        mockMvc.perform(delete("/api/v1/book-editions/978-3-16-148410-0"))
                .andExpect(status().isNoContent());

        verify(bookEditionService, times(1)).deleteByIsbn("978-3-16-148410-0");
    }

    @Test
    void shouldReturnBadRequestWhenCreatingBookEditionWithInvalidData() throws Exception {
        // Given
        BookEditionDTO invalidBookEditionDTO = new BookEditionDTO(
            "", // invalid empty isbn
            "", // invalid empty title
            "", // invalid empty authorName
            null // invalid null number
        );

        // When & Then
        mockMvc.perform(post("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookEditionDTO)))
                .andExpect(status().isBadRequest());

        verify(bookEditionService, never()).save(any(BookEdition.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingBookEditionWithInvalidData() throws Exception {
        // Given
        UpdateBookEditionDTO invalidUpdateBookEditionDTO = new UpdateBookEditionDTO(
            "", // invalid empty isbn
            "", // invalid empty title
            "", // invalid empty authorName
            null // invalid null number
        );

        // When & Then
        mockMvc.perform(patch("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBookEditionDTO)))
                .andExpect(status().isBadRequest());

        verify(bookEditionService, never()).update(any(BookEdition.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingBookEditionWithMissingRequiredFields() throws Exception {
        // Given
        BookEditionDTO incompleteBookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            null, // missing title
            "Test Author",
            1
        );

        // When & Then
        mockMvc.perform(post("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteBookEditionDTO)))
                .andExpect(status().isBadRequest());

        verify(bookEditionService, never()).save(any(BookEdition.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingBookEditionWithMissingRequiredFields() throws Exception {
        // Given
        UpdateBookEditionDTO incompleteUpdateBookEditionDTO = new UpdateBookEditionDTO(
            "", // missing isbn
            "Test Title",
            "Test Author",
            1
        );

        // When & Then
        mockMvc.perform(patch("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteUpdateBookEditionDTO)))
                .andExpect(status().isBadRequest());

        verify(bookEditionService, never()).update(any(BookEdition.class));
    }

    @Test
    void shouldHandleEmptyBookEditionsList() throws Exception {
        // Given
        when(bookEditionService.findAll()).thenReturn(Arrays.asList());
        when(bookEditionConverter.toDtoList(any())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/book-editions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookEditionService, times(1)).findAll();
        verify(bookEditionConverter, times(1)).toDtoList(any());
    }

    @Test
    void shouldHandlePartialUpdateWithNullFields() throws Exception {
        // Given
        UpdateBookEditionDTO partialUpdateDTO = new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Updated Title Only",
            null, // authorName is null
            null  // number is null
        );

        BookEdition updatedBookEdition = new BookEdition();
        updatedBookEdition.setIsbn("978-3-16-148410-0");
        updatedBookEdition.setTitle("Updated Title Only");
        updatedBookEdition.setAuthorName("Original Author");
        updatedBookEdition.setNumber(1);

        BookEditionDTO updatedBookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Updated Title Only",
            "Original Author",
            1
        );

        when(bookEditionConverter.toEntity(any(UpdateBookEditionDTO.class))).thenReturn(updatedBookEdition);
        when(bookEditionService.update(any(BookEdition.class))).thenReturn(updatedBookEdition);
        when(bookEditionConverter.toDto(updatedBookEdition)).thenReturn(updatedBookEditionDTO);

        // When & Then
        mockMvc.perform(patch("/api/v1/book-editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title Only"));

        verify(bookEditionConverter, times(1)).toEntity(partialUpdateDTO);
        verify(bookEditionService, times(1)).update(updatedBookEdition);
        verify(bookEditionConverter, times(1)).toDto(updatedBookEdition);
    }
}
