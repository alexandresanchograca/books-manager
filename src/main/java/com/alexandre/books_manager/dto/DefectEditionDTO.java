package com.alexandre.books_manager.dto;

import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.model.Defect;

import java.util.List;

public record DefectEditionDTO(
        BookEdition edition,
        String defectCode,
        List<Defect> affectedBatches
){}
