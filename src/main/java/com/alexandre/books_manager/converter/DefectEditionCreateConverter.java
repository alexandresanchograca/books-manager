package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.DefectEditionCreateDTO;
import com.alexandre.books_manager.model.DefectEdition;
import org.springframework.stereotype.Component;

@Component
public class DefectEditionCreateConverter implements GenericConverter<DefectEdition, DefectEditionCreateDTO> {
    public DefectEditionCreateDTO toDto(DefectEdition defectEdition) {
        if (defectEdition == null || defectEdition.getEdition() == null) {
            return null;
        }

        return new DefectEditionCreateDTO(
                defectEdition.getDefectCode(),
                defectEdition.getEdition().getIsbn(),
                defectEdition.getAffectedBatches()
        );
    }

    public DefectEdition toEntity(DefectEditionCreateDTO defectEditionDto) {
        if (defectEditionDto == null) {
            return null;
        }

        DefectEdition defectEdition = new DefectEdition();
        defectEdition.setDefectCode(defectEditionDto.defectCode());
        defectEdition.setAffectedBatches(defectEditionDto.affectedBatches());

        return defectEdition;
    }
}
