package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookConverter;
import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/books")
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
    public ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO) {
        Book book = bookService.save(bookConverter.toEntity(bookDTO));
        BookDTO savedBookDTO = bookConverter.toDto(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(savedBookDTO.batchNumber())
                .toUri();

        return ResponseEntity.created(location).body(savedBookDTO);
    }

    @GetMapping
    public @ResponseBody Iterable<BookDTO> findAllBook() {
        Iterable<Book> bookList = bookService.findAll();
        return bookConverter.toDtoList(bookList);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<BookDTO> findBookByNumber(@PathVariable Long id) {
        Optional<Book> bookFound = bookService.findById(id);

        if (bookFound.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        BookDTO bookDTO = bookConverter.toDto(bookFound.get());

        return ResponseEntity.ok(bookDTO);
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
}
