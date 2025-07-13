package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookEditionDTO(
        @NotBlank  String isbn,
        @NotBlank String title,
        @NotBlank String authorName,
        @NotNull @Positive Integer number
) {}
