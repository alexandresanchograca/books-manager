package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDTO(
        @NotBlank  String publisher,
        @NotBlank Integer publishedYear,
        @NotBlank String batchNumber,
        @NotNull BookEditionDTO edition
) {}
