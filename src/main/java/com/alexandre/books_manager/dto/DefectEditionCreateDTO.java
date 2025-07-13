package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DefectEditionCreateDTO(
        @NotBlank  String defectCode,
        @NotBlank String editionIsbn,
        @NotEmpty List<String> affectedBatches
){}
