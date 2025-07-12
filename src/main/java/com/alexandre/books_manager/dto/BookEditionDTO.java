package com.alexandre.books_manager.dto;

public record BookEditionDTO(
        Long id,
        String isbn,
        String title,
        String authorName,
        Integer number
) {}
