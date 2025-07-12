package com.alexandre.books_manager.dto;

public record BookDTO(
        String publisher,
        Integer publishedYear,
        String batchNumber,
        BookEditionDTO edition
) {}
