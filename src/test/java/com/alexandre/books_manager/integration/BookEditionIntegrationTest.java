package com.alexandre.books_manager.integration;

import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.repository.BookEditionRepository;
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
class BookEditionIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookEditionRepository bookEditionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/book-editions";
        bookEditionRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveBookEdition() {
        // Given
        BookEditionDTO bookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        // When - Create book edition
        ResponseEntity<BookEditionDTO> createResponse = restTemplate.postForEntity(
                baseUrl, bookEditionDTO, BookEditionDTO.class);

        // Then - Verify creation
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().isbn()).isEqualTo("978-3-16-148410-0");
        assertThat(createResponse.getBody().title()).isEqualTo("Test Book Title");
        assertThat(createResponse.getBody().authorName()).isEqualTo("Test Author");
        assertThat(createResponse.getBody().number()).isEqualTo(1);

        // When - Retrieve book edition
        ResponseEntity<BookEditionDTO> getResponse = restTemplate.getForEntity(
                baseUrl + "/978-3-16-148410-0", BookEditionDTO.class);

        // Then - Verify retrieval
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().isbn()).isEqualTo("978-3-16-148410-0");
        assertThat(getResponse.getBody().title()).isEqualTo("Test Book Title");
        assertThat(getResponse.getBody().authorName()).isEqualTo("Test Author");
        assertThat(getResponse.getBody().number()).isEqualTo(1);
    }

    @Test
    void shouldReturnAllBookEditions() {
        // Given
        BookEditionDTO bookEditionDTO1 = new BookEditionDTO(
            "978-3-16-148410-0",
            "First Book Title",
            "First Author",
            1
        );

        BookEditionDTO bookEditionDTO2 = new BookEditionDTO(
            "978-3-16-148410-1",
            "Second Book Title",
            "Second Author",
            2
        );

        // Create two book editions
        restTemplate.postForEntity(baseUrl, bookEditionDTO1, BookEditionDTO.class);
        restTemplate.postForEntity(baseUrl, bookEditionDTO2, BookEditionDTO.class);

        // When
        ResponseEntity<BookEditionDTO[]> response = restTemplate.getForEntity(baseUrl, BookEditionDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        
        // Verify first book edition
        assertThat(response.getBody()[0].isbn()).isEqualTo("978-3-16-148410-0");
        assertThat(response.getBody()[0].title()).isEqualTo("First Book Title");
        assertThat(response.getBody()[0].authorName()).isEqualTo("First Author");
        assertThat(response.getBody()[0].number()).isEqualTo(1);
        
        // Verify second book edition
        assertThat(response.getBody()[1].isbn()).isEqualTo("978-3-16-148410-1");
        assertThat(response.getBody()[1].title()).isEqualTo("Second Book Title");
        assertThat(response.getBody()[1].authorName()).isEqualTo("Second Author");
        assertThat(response.getBody()[1].number()).isEqualTo(2);
    }

    @Test
    void shouldUpdateBookEdition() {
        // Given
        BookEditionDTO bookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Original Book Title",
            "Original Author",
            1
        );

        // Create book edition
        restTemplate.postForEntity(baseUrl, bookEditionDTO, BookEditionDTO.class);

        // Update book edition
        UpdateBookEditionDTO updateBookEditionDTO = new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Updated Book Title",
            "Updated Author",
            2
        );

        // When
        ResponseEntity<BookEditionDTO> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PATCH,
                new HttpEntity<>(updateBookEditionDTO),
                BookEditionDTO.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isbn()).isEqualTo("978-3-16-148410-0");
        assertThat(response.getBody().title()).isEqualTo("Updated Book Title");
        assertThat(response.getBody().authorName()).isEqualTo("Updated Author");
        assertThat(response.getBody().number()).isEqualTo(2);
    }

    @Test
    void shouldUpdateBookEditionWithPartialData() {
        // Given
        BookEditionDTO bookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Original Book Title",
            "Original Author",
            1
        );

        // Create book edition
        restTemplate.postForEntity(baseUrl, bookEditionDTO, BookEditionDTO.class);

        // Update only title
        UpdateBookEditionDTO partialUpdateDTO = new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Updated Title Only",
            null, // authorName is null
            null  // number is null
        );

        // When
        ResponseEntity<BookEditionDTO> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PATCH,
                new HttpEntity<>(partialUpdateDTO),
                BookEditionDTO.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo("Updated Title Only");
        // authorName and number should remain unchanged
        assertThat(response.getBody().authorName()).isEqualTo("Original Author");
        assertThat(response.getBody().number()).isEqualTo(1);
    }

    @Test
    void shouldDeleteBookEdition() {
        // Given
        BookEditionDTO bookEditionDTO = new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );

        // Create book edition
        restTemplate.postForEntity(baseUrl, bookEditionDTO, BookEditionDTO.class);

        // Verify book edition exists
        ResponseEntity<BookEditionDTO> getResponse = restTemplate.getForEntity(
                baseUrl + "/978-3-16-148410-0", BookEditionDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // When - Delete book edition
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/978-3-16-148410-0",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Then
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify book edition is deleted
        ResponseEntity<BookEditionDTO> getAfterDeleteResponse = restTemplate.getForEntity(
                baseUrl + "/978-3-16-148410-0", BookEditionDTO.class);
        assertThat(getAfterDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
