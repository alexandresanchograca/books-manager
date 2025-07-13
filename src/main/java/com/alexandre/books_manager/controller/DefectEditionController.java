package com.alexandre.books_manager.controller;

import com.alexandre.books_manager.converter.DefectEditionConverter;
import com.alexandre.books_manager.converter.DefectEditionCreateConverter;
import com.alexandre.books_manager.dto.*;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.model.DefectEdition;
import com.alexandre.books_manager.service.BookEditionService;
import com.alexandre.books_manager.service.DefectEditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/book-defects")
@Tag(name = "Book Defect Management", description = "APIs for managing book defects")
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

    @Operation(summary = "Get all book defects",
            description = "Retrieve a list of all book defects and quality issues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Defected Books retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DefectEditionDTO.class)))),
    })
    @GetMapping
    public ResponseEntity<List<DefectEditionDTO>> findAll() {
        Iterable<DefectEdition> defectEditions = defectEditionService.findAll();
        return ResponseEntity.ok(defectEditionConverter.toDtoList(defectEditions));
    }

    @Operation(summary = "Create a new book defect",
            description = "Add a new defect record for a specific book edition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book defect created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or book edition not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<DefectEditionDTO> addDefect(@RequestBody @Valid DefectEditionCreateDTO defectEditionDTO) {
        DefectEdition defectEdition = defectEditionCreateConverter.toEntity(defectEditionDTO);
        Optional<BookEdition> relativeEdition = bookEditionService.findByIsbn(defectEditionDTO.editionIsbn());

        if (relativeEdition.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        defectEdition.setEdition(relativeEdition.get());

        DefectEdition savedDefectEdition = defectEditionService.save(defectEdition);
        DefectEditionDTO savedDefectEditionDTO = defectEditionConverter.toDto(savedDefectEdition);
        return ResponseEntity.ok(savedDefectEditionDTO);
    }
}
