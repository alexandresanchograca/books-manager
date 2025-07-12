package com.alexandre.books_manager.converter;

import com.alexandre.books_manager.dto.DefectEditionCreateDTO;
import com.alexandre.books_manager.model.DefectEdition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class DefectEditionConverter {
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

    public List<DefectEditionCreateDTO> toDtoList(Iterable<DefectEdition> defectEditions) {
        return StreamSupport.stream(defectEditions.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
