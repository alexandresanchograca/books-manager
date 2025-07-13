package com.alexandre.books_manager.dto;

import com.alexandre.books_manager.model.BookEdition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DefectEditionDTO(
        @NotNull BookEditionDTO edition,
        @NotBlank String defectCode,
        @NotEmpty List<String> affectedBatches
){}
