package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateBookDTO(
        String publisher,
        @Positive Integer publishedYear,
        @NotBlank String batchNumber,
        @NotNull BookEditionDTO edition
) {}
