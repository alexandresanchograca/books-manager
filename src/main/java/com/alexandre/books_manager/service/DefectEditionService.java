package com.alexandre.books_manager.service;

import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.model.DefectEdition;
import com.alexandre.books_manager.repository.DefectEditionRepository;
import com.alexandre.books_manager.repository.DefectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DefectEditionService {
    private DefectRepository defectRepository;
    private DefectEditionRepository defectEditionRepository;

    @Autowired
    public void setDefectRepository(DefectRepository defectRepository) {
        this.defectRepository = defectRepository;
    }

    @Autowired
    public void setDefectBookRepository(DefectEditionRepository defectEditionRepository) {
        this.defectEditionRepository = defectEditionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<DefectEdition> findById(String id) {
        return defectEditionRepository.findById(id);
    }

    @Transactional
    public DefectEdition save(DefectEdition defectEdition) {
        return defectEditionRepository.save(defectEdition);
    }

    @Transactional(readOnly = true)
    public Iterable<DefectEdition> findAll() {
        return defectEditionRepository.findAll();
    }
}
