package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.DefectEdition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefectEditionRepository extends CrudRepository<DefectEdition, String> {
}
