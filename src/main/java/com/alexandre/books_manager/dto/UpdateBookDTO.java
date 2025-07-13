package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBookDTO(
        String publisher,
        Integer publishedYear,
        @NotBlank String batchNumber,
        @NotNull UpdateBookEditionDTO edition
) {}
