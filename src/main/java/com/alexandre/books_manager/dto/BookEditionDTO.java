package com.alexandre.books_manager.dto;

public record BookEditionDTO(
        String isbn,
        String title,
        String authorName,
        Integer number
) {}
