package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter implements GenericConverter<Book, BookDTO> {

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
}
