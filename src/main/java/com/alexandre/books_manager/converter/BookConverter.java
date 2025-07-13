package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BookConverter {

    private final BookEditionConverter bookEditionConverter;

    public BookConverter(BookEditionConverter bookEditionConverter) {
        this.bookEditionConverter = bookEditionConverter;
    }

    public BookDTO toDto(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDTO(
                book.getPublisher(),
                book.getPublishedYear(),
                book.getBatchNumber(),
                bookEditionConverter.toDto(book.getEdition())
        );
    }

    public Book toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }

        Book book = new Book();
        book.setBatchNumber(bookDTO.batchNumber());
        book.setPublisher(bookDTO.publisher());
        book.setPublishedYear(bookDTO.publishedYear());
        book.setEdition(bookEditionConverter.toEntity(bookDTO.edition()));

        return book;
    }

    public Book toEntity(UpdateBookDTO updateBookDTO) {
        if (updateBookDTO == null) {
            return null;
        }

        Book book = new Book();
        book.setBatchNumber(updateBookDTO.batchNumber());
        book.setPublisher(updateBookDTO.publisher());
        book.setPublishedYear(updateBookDTO.publishedYear());
        book.setEdition(bookEditionConverter.toEntity(updateBookDTO.edition()));

        return book;
    }

    /**
     * Helper method to convert a list of entities to a list of DTOs.
     *
     * @param bookBatches The list of entities.
     * @return A list of DTOs.
     */
    public List<BookDTO> toDtoList(Iterable<Book> bookBatches) {
        return StreamSupport.stream(bookBatches.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
