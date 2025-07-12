package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.BookBatchDTO;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.model.BookBatch;
import com.alexandre.books_manager.model.BookEdition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BookBatchConverter {

    private final BookEditionConverter bookEditionConverter;

    public BookBatchConverter(BookEditionConverter bookEditionConverter) {
        this.bookEditionConverter = bookEditionConverter;
    }

    public BookBatchDTO toDto(BookBatch bookBatch) {
        if (bookBatch == null) {
            return null;
        }

        return new BookBatchDTO(
                bookBatch.getId(),
                bookBatch.getPublisher(),
                bookBatch.getPublishedYear(),
                bookBatch.getBatchNumber(),
                bookEditionConverter.toDto(bookBatch.getEdition())
        );
    }

    public BookBatch toEntity(BookBatchDTO bookBatchDTO) {
        if (bookBatchDTO == null) {
            return null;
        }

        BookBatch bookBatch = new BookBatch();
        bookBatch.setBatchNumber(bookBatchDTO.batchNumber());
        bookBatch.setPublisher(bookBatchDTO.publisher());
        bookBatch.setPublishedYear(bookBatchDTO.publishedYear());
        bookBatch.setEdition(bookEditionConverter.toEntity(bookBatchDTO.edition()));

        return bookBatch;
    }

    /**
     * Helper method to convert a list of entities to a list of DTOs.
     *
     * @param bookBatches The list of entities.
     * @return A list of DTOs.
     */
    public List<BookBatchDTO> toDtoList(Iterable<BookBatch> bookBatches) {
        return StreamSupport.stream(bookBatches.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
