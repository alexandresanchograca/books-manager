package com.alexandre.books_manager.dto;

import java.util.List;

public record DefectEditionCreateDTO(
        String defectCode,
        String editionIsbn,
        List<String> affectedBatches
){}
