package com.alexandre.books_manager.util;

import com.alexandre.books_manager.dto.BookDTO;
import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.Book;
import com.alexandre.books_manager.model.BookEdition;

public class TestDataBuilder {

    public static BookEdition createTestBookEdition() {
        BookEdition edition = new BookEdition();
        edition.setIsbn("978-3-16-148410-0");
        edition.setTitle("Test Book Title");
        edition.setAuthorName("Test Author");
        edition.setNumber(1);
        return edition;
    }

    public static BookEdition createTestBookEdition(String isbn) {
        BookEdition edition = createTestBookEdition();
        edition.setIsbn(isbn);
        return edition;
    }

    public static Book createTestBook() {
        Book book = new Book();
        book.setPublisher("Test Publisher");
        book.setPublishedYear(2023);
        book.setBatchNumber("BATCH001");
        book.setEdition(createTestBookEdition());
        return book;
    }

    public static Book createTestBook(String batchNumber, String isbn) {
        Book book = createTestBook();
        book.setBatchNumber(batchNumber);
        book.setEdition(createTestBookEdition(isbn));
        return book;
    }

    public static BookEditionDTO createTestBookEditionDTO() {
        return new BookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );
    }

    public static BookEditionDTO createTestBookEditionDTO(String isbn) {
        return new BookEditionDTO(
            isbn,
            "Test Book Title",
            "Test Author",
            1
        );
    }

    public static BookDTO createTestBookDTO() {
        return new BookDTO(
            "Test Publisher",
            2023,
            "BATCH001",
            createTestBookEditionDTO()
        );
    }

    public static BookDTO createTestBookDTO(String batchNumber, String isbn) {
        return new BookDTO(
            "Test Publisher",
            2023,
            batchNumber,
            createTestBookEditionDTO(isbn)
        );
    }

    public static UpdateBookEditionDTO createTestUpdateBookEditionDTO() {
        return new UpdateBookEditionDTO(
            "978-3-16-148410-0",
            "Test Book Title",
            "Test Author",
            1
        );
    }

    public static UpdateBookEditionDTO createTestUpdateBookEditionDTO(String isbn) {
        return new UpdateBookEditionDTO(
            isbn,
            "Test Book Title",
            "Test Author",
            1
        );
    }

    public static UpdateBookDTO createTestUpdateBookDTO() {
        return new UpdateBookDTO(
            "Updated Publisher",
            2024,
            "BATCH001",
            createTestUpdateBookEditionDTO()
        );
    }

    public static UpdateBookDTO createTestUpdateBookDTO(String batchNumber, String isbn) {
        return new UpdateBookDTO(
            "Updated Publisher",
            2024,
            batchNumber,
            createTestUpdateBookEditionDTO(isbn)
        );
    }
} 