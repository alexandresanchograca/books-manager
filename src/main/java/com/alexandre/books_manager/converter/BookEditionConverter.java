package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.BookEditionDTO;
import com.alexandre.books_manager.dto.UpdateBookEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BookEditionConverter {
    public BookEditionDTO toDto(BookEdition edition) {
        if (edition == null) {
            return null;
        }

        return new BookEditionDTO(
                edition.getIsbn(),
                edition.getTitle(),
                edition.getAuthorName(),
                edition.getNumber()
        );
    }

    public BookEdition toEntity(BookEditionDTO bookEditionDTO) {
        if (bookEditionDTO == null) {
            return null;
        }

        BookEdition edition = new BookEdition();
        edition.setTitle(bookEditionDTO.title());
        edition.setAuthorName(bookEditionDTO.authorName());
        edition.setIsbn(bookEditionDTO.isbn());
        edition.setNumber(bookEditionDTO.number());

        return edition;
    }

    public BookEdition toEntity(UpdateBookEditionDTO bookEditionDTO) {
        if (bookEditionDTO == null) {
            return null;
        }

        BookEdition edition = new BookEdition();
        edition.setTitle(bookEditionDTO.title());
        edition.setAuthorName(bookEditionDTO.authorName());
        edition.setIsbn(bookEditionDTO.isbn());
        edition.setNumber(bookEditionDTO.number());

        return edition;
    }

    public List<BookEditionDTO> toDtoList(Iterable<BookEdition> editions) {
        return StreamSupport.stream(editions.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
