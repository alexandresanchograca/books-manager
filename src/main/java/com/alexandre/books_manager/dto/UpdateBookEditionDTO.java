package com.alexandre.books_manager.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBookEditionDTO(
        @NotBlank String isbn,
        String title,
        String authorName,
        Integer number
) {}
