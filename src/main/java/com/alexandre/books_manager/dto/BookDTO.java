package com.alexandre.books_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookDTO(
        @NotBlank @Schema(example = "Schamberger-Huels")  String publisher,
        @NotNull @Positive @Schema(example = "2002") Integer publishedYear,
        @NotBlank @Schema(example = "34-820-4567") String batchNumber,
        @NotNull BookEditionDTO edition
) {}
