package com.alexandre.books_manager.dto;

import com.alexandre.books_manager.model.Defect;

import java.util.List;

public record DefectEditionCreateDTO(
        String defectCode,
        String editionIsbn,
        List<Defect> affectedBatches
){}
