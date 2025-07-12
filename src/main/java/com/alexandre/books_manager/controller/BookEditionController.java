package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.BookEditionConverter;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.service.BookEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/book-editions")
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
    public @ResponseBody Iterable<BookEditionDTO> findAllBookEditions() {
        Iterable<BookEdition> bookEditionList = bookEditionService.findAll();
        return bookEditionConverter.toDtoList(bookEditionList);
    }
}
