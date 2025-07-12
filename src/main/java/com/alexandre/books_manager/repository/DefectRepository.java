package com.alexandre.books_manager.repository;

import com.alexandre.books_manager.model.Defect;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefectRepository extends CrudRepository<Defect, String> {
}
