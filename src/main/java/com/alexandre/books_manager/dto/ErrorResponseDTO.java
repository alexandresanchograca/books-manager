package com.alexandre.books_manager.dto;

public record ErrorResponseDTO(
        String message,
        long timestamp
) {}
