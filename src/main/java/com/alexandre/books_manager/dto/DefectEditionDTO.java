package com.alexandre.books_manager.dto;

import com.alexandre.books_manager.model.BookEdition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DefectEditionDTO(
        @NotNull BookEditionDTO edition,
        @NotBlank @Schema(example = "67038-100") String defectCode,
        @NotEmpty @Schema(example = "[ \"34-820-4567\" ]") List<String> affectedBatches

){}
