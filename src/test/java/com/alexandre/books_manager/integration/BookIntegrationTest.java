package com.alexandre.books_manager.integration;

import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookEditionRepository;
import com.alexandre.books_manager.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class BookIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookEditionRepository bookEditionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;
    private BookEdition testEdition;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/books";
        
        // Create a test BookEdition
        testEdition = new BookEdition();
        testEdition.setIsbn("978-3-16-148410-0");
        testEdition.setTitle("Test Book Title");
        testEdition.setAuthorName("Test Author");
        testEdition.setNumber(1);
        testEdition = bookEditionRepository.save(testEdition);
    }

    @Test
    void shouldCreateAndRetrieveBook() {
        // Given
        BookEditionDTO editionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        BookDTO bookDTO = new BookDTO(
            "Test Publisher",
            2023,
            "BATCH001",
            editionDTO
        );

        // When - Create book
        ResponseEntity<BookDTO> createResponse = restTemplate.postForEntity(
                baseUrl, bookDTO, BookDTO.class);

        // Then - Verify creation
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().publisher()).isEqualTo("Test Publisher");
        assertThat(createResponse.getBody().batchNumber()).isEqualTo("BATCH001");

        // When - Retrieve book
        ResponseEntity<BookDTO> getResponse = restTemplate.getForEntity(
                baseUrl + "/BATCH001/978-3-16-148410-0", BookDTO.class);

        // Then - Verify retrieval
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().publisher()).isEqualTo("Test Publisher");
        assertThat(getResponse.getBody().batchNumber()).isEqualTo("BATCH001");
    }

    @Test
    void shouldReturnAllBooks() {
        // Given
        BookEditionDTO editionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        BookDTO bookDTO1 = new BookDTO(
            "Publisher 1",
            2023,
            "BATCH001",
            editionDTO
        );

        BookDTO bookDTO2 = new BookDTO(
            "Publisher 2",
            2024,
            "BATCH002",
            editionDTO
        );

        // Create two books
        restTemplate.postForEntity(baseUrl, bookDTO1, BookDTO.class);
        restTemplate.postForEntity(baseUrl, bookDTO2, BookDTO.class);

        // When
        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity(baseUrl, BookDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void shouldUpdateBook() {
        // Given
        BookEditionDTO editionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        BookDTO bookDTO = new BookDTO(
            "Original Publisher",
            2023,
            "BATCH001",
            editionDTO
        );

        // Create book
        restTemplate.postForEntity(baseUrl, bookDTO, BookDTO.class);

        // Update book
        UpdateBookEditionDTO updateEditionDTO = new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        UpdateBookDTO updateBookDTO = new UpdateBookDTO(
            "Updated Publisher",
            2024,
            "BATCH001",
            updateEditionDTO
        );

        // When
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PATCH,
                new HttpEntity<>(updateBookDTO),
                BookDTO.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().publisher()).isEqualTo("Updated Publisher");
        assertThat(response.getBody().publishedYear()).isEqualTo(2024);
    }

    @Test
    void shouldDeleteBook() {
        // Given
        BookEditionDTO editionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        BookDTO bookDTO = new BookDTO(
            "Test Publisher",
            2023,
            "BATCH001",
            editionDTO
        );

        // Create book
        restTemplate.postForEntity(baseUrl, bookDTO, BookDTO.class);

        // Verify book exists
        ResponseEntity<BookDTO> getResponse = restTemplate.getForEntity(
                baseUrl + "/BATCH001/978-3-16-148410-0", BookDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // When - Delete book
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/BATCH001/978-3-16-148410-0",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Then
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify book is deleted
        ResponseEntity<BookDTO> getAfterDeleteResponse = restTemplate.getForEntity(
                baseUrl + "/BATCH001/978-3-16-148410-0", BookDTO.class);
        assertThat(getAfterDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnNotFoundForNonExistentBook() {
        // When
        ResponseEntity<BookDTO> response = restTemplate.getForEntity(
                baseUrl + "/NONEXISTENT/978-3-16-148410-0", BookDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequestForInvalidBookData() {
        // Given
        BookDTO invalidBookDTO = new BookDTO(
            "", // invalid empty publisher
            null, // invalid null publishedYear
            "", // invalid empty batchNumber
            new BookEditionDTO("", "", "", null) // invalid edition
        );

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, invalidBookDTO, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
} 