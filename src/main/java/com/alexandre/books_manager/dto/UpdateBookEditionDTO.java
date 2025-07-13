package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateBookEditionDTO(
        @NotBlank String isbn,
        String title,
        String authorName,
        @Positive Integer number
) {}
