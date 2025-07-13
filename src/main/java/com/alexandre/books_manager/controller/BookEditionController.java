package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookEditionConverter;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.service.BookEditionService;
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

    @GetMapping
    public @ResponseBody ResponseEntity<Iterable<BookEditionDTO>> findAllBookEditions() {
        Iterable<BookEdition> bookEditionList = bookEditionService.findAll();
        return ResponseEntity.ok(bookEditionConverter.toDtoList(bookEditionList));
    }

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

    @PatchMapping
    public @ResponseBody ResponseEntity<BookEditionDTO> editBookEdition(
            @RequestBody @Valid UpdateBookEditionDTO bookEditionDTO) {
        BookEdition updatedBookEditionEntity = bookEditionConverter.toEntity(bookEditionDTO);
        BookEdition updatedBookEdition = bookEditionService.update(updatedBookEditionEntity);

        BookEditionDTO updatedBookEditionDTO = bookEditionConverter.toDto(updatedBookEdition);
        return ResponseEntity.ok(updatedBookEditionDTO);
    }

    @DeleteMapping(path = "/{isbn}")
    public @ResponseBody ResponseEntity<BookEditionDTO> deleteBookEdition(
            @PathVariable String isbn
    ) {
        bookEditionService.deleteByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }
}
