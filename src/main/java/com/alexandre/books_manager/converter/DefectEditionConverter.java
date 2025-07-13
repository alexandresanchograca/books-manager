package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.DefectEditionDTO;
import com.alexandre.books_manager.model.BookEdition;
import com.alexandre.books_manager.model.DefectEdition;
import org.springframework.stereotype.Component;

@Component
public class DefectEditionConverter implements GenericConverter<DefectEdition, DefectEditionDTO> {
    private final BookEditionConverter bookEditionConverter;

    public DefectEditionConverter(BookEditionConverter bookEditionConverter) {
        this.bookEditionConverter = bookEditionConverter;
    }

    public DefectEditionDTO toDto(DefectEdition defectEdition) {
        if (defectEdition == null || defectEdition.getEdition() == null) {
            return null;
        }

        BookEdition bookEdition = defectEdition.getEdition();

        return new DefectEditionDTO(
                bookEditionConverter.toDto(bookEdition),
                defectEdition.getDefectCode(),
                defectEdition.getAffectedBatches()
        );
    }

    public DefectEdition toEntity(DefectEditionDTO defectEditionDto) {
        if (defectEditionDto == null) {
            return null;
        }

        BookEdition bookEdition = bookEditionConverter.toEntity(defectEditionDto.edition());

        DefectEdition defectEdition = new DefectEdition();
        defectEdition.setDefectCode(defectEditionDto.defectCode());
        defectEdition.setEdition(bookEdition);
        defectEdition.setAffectedBatches(defectEditionDto.affectedBatches());

        return defectEdition;
    }
}
