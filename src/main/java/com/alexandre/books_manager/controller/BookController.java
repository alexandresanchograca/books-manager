package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookConverter;
import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.service.BookService;
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

    @GetMapping
    public @ResponseBody ResponseEntity<Iterable<BookDTO>> findAllBook() {
        Iterable<Book> bookList = bookService.findAll();
        return ResponseEntity.ok(bookConverter.toDtoList(bookList));
    }

    @PatchMapping
    public @ResponseBody ResponseEntity<BookDTO> updateBookByBatchNumberAndIsbn(
            @RequestBody @Valid UpdateBookDTO bookDTO) {
        Book updatedBookEntity = bookConverter.toEntity(bookDTO);
        Book updatedBook = bookService.update(updatedBookEntity);

        BookDTO updatedBookDTO = bookConverter.toDto(updatedBook);
        return ResponseEntity.ok(updatedBookDTO);
    }

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

    @DeleteMapping(path = "/{batchNumber}/{isbn}")
    public @ResponseBody ResponseEntity<BookDTO> deleteBookByBatchNumberAndIsbn(
            @PathVariable String batchNumber,
            @PathVariable String isbn) {
        bookService.delete(batchNumber, isbn);
        return ResponseEntity.noContent().build();
    }
}
