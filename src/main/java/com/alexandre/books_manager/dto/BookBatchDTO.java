package com.alexandre.books_manager.dto;

public record BookBatchDTO(
        Long id,
        String publisher,
        Integer publishedYear,
        String batchNumber,
        BookEditionDTO edition
) {}
