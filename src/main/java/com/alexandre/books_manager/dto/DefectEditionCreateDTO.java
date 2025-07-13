package com.alexandre.books_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DefectEditionCreateDTO(
        @NotBlank @Schema(example = "67038-100") String defectCode,
        @NotBlank @Schema(example = "1-23-456789-2") String editionIsbn,
        @NotEmpty @Schema(example = "[ \"34-820-4567\" ]") List<String> affectedBatches
){}
