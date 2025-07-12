package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookBatchConverter;
import com.alexandre.books_manager.dto.BookBatchDTO;
import com.alexandre.books_manager.model.BookBatch;
import com.alexandre.books_manager.service.BookBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/v1/book-batch")
public class BookBatchController {

    private BookBatchService bookBatchService;
    private BookBatchConverter bookBatchConverter;

    @Autowired
    public void setBookBatchService(BookBatchService bookBatchService) {
        this.bookBatchService = bookBatchService;
    }

    @Autowired
    public void setBookBatchConverter(BookBatchConverter bookBatchConverter) {
        this.bookBatchConverter = bookBatchConverter;
    }

    @PostMapping
    public ResponseEntity<Object> saveBookBatch(@RequestBody BookBatchDTO bookBatchDTO) {
        bookBatchService.save(bookBatchConverter.toEntity(bookBatchDTO));
        return ResponseEntity.status(201).build();
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<BookBatchDTO> findAllBookBatches() {
        Iterable<BookBatch> bookBatchList = bookBatchService.findAll();
        return bookBatchConverter.toDtoList(bookBatchList);
    }
}
