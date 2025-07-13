package com.alexandre.books_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponseDTO(
        @Schema(example = "Message Error") String message,
        @Schema(example = "1752437901") long timestamp
) {}
