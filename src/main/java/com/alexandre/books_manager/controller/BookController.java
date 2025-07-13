package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookConverter;
import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.ErrorResponseDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.service.BookService;
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
@RequestMapping(path = "/api/v1/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

    private BookService bookService;
    private BookConverter bookConverter;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Autowired
    public void setBookConverter(BookConverter bookConverter) {
        this.bookConverter = bookConverter;
    }

    @Operation(summary = "Create a new book",
            description = "Save a new book with its edition information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<BookDTO> saveBook(@RequestBody @Valid BookDTO bookDTO) {
        Book book = bookService.save(bookConverter.toEntity(bookDTO));
        BookDTO savedBookDTO = bookConverter.toDto(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(savedBookDTO);
    }

    @Operation(summary = "Get all books",
            description = "Retrieve a list of all books with their edition information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDTO.class)))),
    })
    @GetMapping
    public @ResponseBody ResponseEntity<Iterable<BookDTO>> findAllBook() {
        Iterable<Book> bookList = bookService.findAll();
        return ResponseEntity.ok(bookConverter.toDtoList(bookList));
    }

    @Operation(summary = "Update book information",
            description = "Update book information by batch number and ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping
    public @ResponseBody ResponseEntity<BookDTO> updateBookByBatchNumberAndIsbn(
            @RequestBody @Valid UpdateBookDTO bookDTO) {
        Book updatedBookEntity = bookConverter.toEntity(bookDTO);
        Book updatedBook = bookService.update(updatedBookEntity);

        BookDTO updatedBookDTO = bookConverter.toDto(updatedBook);
        return ResponseEntity.ok(updatedBookDTO);
    }

    @Operation(summary = "Get book by batch number and ISBN",
            description = "Retrieve a specific book by its batch number and edition ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping(path = "/{batchNumber}/{isbn}")
    public @ResponseBody ResponseEntity<BookDTO> findBookByBatchNumberAndIsbn(
            @PathVariable String batchNumber,
            @PathVariable String isbn) {
        Optional<Book> bookFound = bookService.findByBatchNumberAndEditionIsbn(batchNumber, isbn);

        if (bookFound.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BookDTO bookDTO = bookConverter.toDto(bookFound.get());
        return ResponseEntity.ok(bookDTO);
    }

    @Operation(summary = "Delete book by batch number and ISBN",
            description = "Delete a specific book by its batch number and edition ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping(path = "/{batchNumber}/{isbn}")
    public @ResponseBody ResponseEntity<BookDTO> deleteBookByBatchNumberAndIsbn(
            @PathVariable String batchNumber,
            @PathVariable String isbn) {
        bookService.delete(batchNumber, isbn);
        return ResponseEntity.noContent().build();
    }
}
