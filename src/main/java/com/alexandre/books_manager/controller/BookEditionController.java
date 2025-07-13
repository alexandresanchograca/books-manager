package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookEditionConverter;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.ErrorResponseDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.service.BookEditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/book-editions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Book Edition Management", description = "APIs for managing book editions")
public class BookEditionController {

    private BookEditionService bookEditionService;
    private BookEditionConverter bookEditionConverter;

    @Autowired
    public void setBookEditionService(BookEditionService bookEditionService) {
        this.bookEditionService = bookEditionService;
    }

    @Autowired
    public void setBookEditionConverter(BookEditionConverter bookEditionConverter) {
        this.bookEditionConverter = bookEditionConverter;
    }

    @Operation(summary = "Get all book editions",
            description = "Retrieve a list of all book editions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book editions retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookEditionDTO.class)))),
    })
    @GetMapping
    public @ResponseBody ResponseEntity<Iterable<BookEditionDTO>> findAllBookEditions() {
        Iterable<BookEdition> bookEditionList = bookEditionService.findAll();
        return ResponseEntity.ok(bookEditionConverter.toDtoList(bookEditionList));
    }

    @Operation(summary = "Get book edition by ISBN",
            description = "Retrieve a specific book edition by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book edition found successfully"),
            @ApiResponse(responseCode = "404", description = "Book edition not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping(path = "/{isbn}")
    public @ResponseBody ResponseEntity<BookEditionDTO> findEdition(
            @PathVariable String isbn
    ) {
        Optional<BookEdition> bookEdition = bookEditionService.findByIsbn(isbn);

        if (bookEdition.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BookEditionDTO bookEditionDTO = bookEditionConverter.toDto(bookEdition.get());
        return ResponseEntity.ok(bookEditionDTO);
    }

    @Operation(summary = "Create a new book edition",
            description = "Save a new book edition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book edition deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book edition not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<BookEditionDTO> saveBookEdition(@RequestBody @Valid BookEditionDTO bookEditionDTO) {
        BookEdition savedBookEdition = bookEditionService.save(bookEditionConverter.toEntity(bookEditionDTO));
        BookEditionDTO savedBookEditionDTO = bookEditionConverter.toDto(savedBookEdition);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(savedBookEditionDTO);
    }

    @Operation(summary = "Update book edition information",
            description = "Update book edition information by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book edition updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book edition not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping
    public @ResponseBody ResponseEntity<BookEditionDTO> editBookEdition(
            @RequestBody @Valid UpdateBookEditionDTO bookEditionDTO) {
        BookEdition updatedBookEditionEntity = bookEditionConverter.toEntity(bookEditionDTO);
        BookEdition updatedBookEdition = bookEditionService.update(updatedBookEditionEntity);

        BookEditionDTO updatedBookEditionDTO = bookEditionConverter.toDto(updatedBookEdition);
        return ResponseEntity.ok(updatedBookEditionDTO);
    }

    @Operation(summary = "Delete book edition by ISBN",
            description = "Delete a specific book edition by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book edition deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book edition not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping(path = "/{isbn}")
    public @ResponseBody ResponseEntity<BookEditionDTO> deleteBookEdition(
            @PathVariable String isbn
    ) {
        bookEditionService.deleteByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }
}
