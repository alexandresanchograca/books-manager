package com.alexandre.books_manager.service;

import com.alexandre.books_manager.repository.BookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookEditionService {
    private BookEditionRepository bookEditionRepository;

    @Autowired
    public void setBookEditionRepository(BookEditionRepository bookEditionRepository){
        this.bookEditionRepository = bookEditionRepository;
    }
}
