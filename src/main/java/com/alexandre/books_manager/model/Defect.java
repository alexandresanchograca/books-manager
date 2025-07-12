package com.alexandre.books_manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Defect {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String affectedBatch;


    public String getAffectedBatch() {
        return affectedBatch;
    }

    public void setAffectedBatch(String affectedBatch) {
        this.affectedBatch = affectedBatch;
    }
}
