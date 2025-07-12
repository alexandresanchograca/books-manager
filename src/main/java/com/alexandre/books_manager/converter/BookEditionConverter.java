package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.BookEditionDTO;
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
                edition.getId(),
                edition.getTitle(),
                edition.getAuthorName(),
                edition.getIsbn(),
                edition.getNumber()
        );
    }

    /**
     * Converts an EditionCreateDTO into a new BookEdition entity.
     * This new entity is not yet saved to the database (it has no ID).
     *
     * @param bookEditionDTO The DTO with the creation data.
     * @return A new BookEdition entity instance.
     */
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

    /**
     * Helper method to convert a list of entities to a list of DTOs.
     *
     * @param editions The list of entities.
     * @return A list of DTOs.
     */
    public List<BookEditionDTO> toDtoList(Iterable<BookEdition> editions) {
        return StreamSupport.stream(editions.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
