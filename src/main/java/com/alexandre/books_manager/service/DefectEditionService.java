package com.alexandre.books_manager.service;

import com.alexandre.books_manager.exception.BadRequestException;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.DefectEdition;
import com.alexandre.books_manager.repository.BookRepository;
import com.alexandre.books_manager.repository.DefectEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DefectEditionService implements GenericService<DefectEdition> {
    private BookRepository bookRepository;
    private DefectEditionRepository defectEditionRepository;

    @Autowired
    public void setDefectBookRepository(DefectEditionRepository defectEditionRepository) {
        this.defectEditionRepository = defectEditionRepository;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Optional<DefectEdition> findById(String id) {
        return defectEditionRepository.findById(id);
    }

    @Transactional
    public DefectEdition save(DefectEdition defectEdition) {
        String currentEditionISBN = defectEdition.getEdition().getIsbn();

        defectEdition.getAffectedBatches().forEach(affectedBatch -> {
            Optional<Book> foundBook = bookRepository
                    .findByBatchNumberAndEditionIsbn(affectedBatch, currentEditionISBN);

            if (foundBook.isEmpty()) {
                throw new BadRequestException("No book found with batch number: " + affectedBatch + ", and ISBN: " + currentEditionISBN);
            }
        });

        return defectEditionRepository.save(defectEdition);
    }

    @Transactional(readOnly = true)
    public Iterable<DefectEdition> findAll() {
        return defectEditionRepository.findAll();
    }
}
