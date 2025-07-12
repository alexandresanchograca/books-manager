package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.DefectEditionConverter;
import com.alexandre.books_manager.converter.DefectEditionCreateConverter;
import com.alexandre.books_manager.dto.DefectEditionCreateDTO;
import com.alexandre.books_manager.dto.DefectEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.model.DefectEdition;
import com.alexandre.books_manager.service.BookEditionService;
import com.alexandre.books_manager.service.DefectEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/book-defects")
public class DefectEditionController {
    private DefectEditionService defectEditionService;
    private DefectEditionCreateConverter defectEditionCreateConverter;
    private DefectEditionConverter defectEditionConverter;
    private BookEditionService bookEditionService;

    @Autowired
    public void setDefectEditionService(DefectEditionService defectEditionService) {
        this.defectEditionService = defectEditionService;
    }

    @Autowired
    public void setDefectEditionCreateConverter(DefectEditionCreateConverter defectEditionCreateConverter) {
        this.defectEditionCreateConverter = defectEditionCreateConverter;
    }

    @Autowired
    public void setBookEditionService(BookEditionService bookEditionService) {
        this.bookEditionService = bookEditionService;
    }

    @Autowired
    public void setDefectEditionConverter(DefectEditionConverter defectEditionConverter) {
        this.defectEditionConverter = defectEditionConverter;
    }

    @GetMapping
    public ResponseEntity<List<DefectEditionDTO>> findAll() {
        Iterable<DefectEdition> defectEditions = defectEditionService.findAll();
        return ResponseEntity.ok(defectEditionConverter.toDtoList(defectEditions));
    }

    @PostMapping
    public ResponseEntity<DefectEditionDTO> addDefect(@RequestBody DefectEditionCreateDTO defectEditionDTO) {
        DefectEdition defectEdition = defectEditionCreateConverter.toEntity(defectEditionDTO);
        Optional<BookEdition> relativeEdition = bookEditionService.findByIsbn(defectEditionDTO.editionIsbn());

        if(relativeEdition.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        defectEdition.setEdition(relativeEdition.get());

        DefectEdition savedDefectEdition = defectEditionService.save(defectEdition);
        DefectEditionDTO savedDefectEditionDTO = defectEditionConverter.toDto(savedDefectEdition);
        return ResponseEntity.ok(savedDefectEditionDTO);
    }
}
