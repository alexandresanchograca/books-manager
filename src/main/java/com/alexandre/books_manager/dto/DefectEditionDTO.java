package com.alexandre.books_manager.dto;

import com.alexandre.books_manager.model.BookEdition;

import java.util.List;

public record DefectEditionDTO(
        BookEditionDTO edition,
        String defectCode,
        List<String> affectedBatches
){}
