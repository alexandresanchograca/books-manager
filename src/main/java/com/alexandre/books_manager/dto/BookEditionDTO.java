package com.alexandre.books_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookEditionDTO(
        @NotBlank @Schema(example = "1-23-456789-2") String isbn,
        @NotBlank @Schema(example = "Java, Beginner's Guide 2") String title,
        @NotBlank @Schema(example = "Kellsie Havock") String authorName,
        @NotNull @Positive @Schema(example = "2") Integer number
) {}
