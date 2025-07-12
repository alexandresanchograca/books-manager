package com.alexandre.books_manager.service;

import com.alexandre.books_manager.dto.BookBatchDTO;
import com.alexandre.books_manager.model.BookBatch;
import com.alexandre.books_manager.repository.BookBatchRepository;
import com.alexandre.books_manager.repository.BookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookBatchService {
    private BookBatchRepository bookBatchRepository;
    private BookEditionRepository bookEditionRepository;

    @Autowired
    public void setBookBatchRepository(BookBatchRepository bookBatchRepository) {
        this.bookBatchRepository = bookBatchRepository;
    }

    @Autowired
    public void setBookEditionRepository(BookEditionRepository bookEditionRepository) {
        this.bookEditionRepository = bookEditionRepository;
    }

    @Transactional
    public void save(BookBatch bookBatch) {
        bookEditionRepository.save(bookBatch.getEdition());
        bookBatchRepository.save(bookBatch);
    }
    @Transactional(readOnly = true)
    public Iterable<BookBatch> findAll() {
        return bookBatchRepository.findAll();
    }
}
