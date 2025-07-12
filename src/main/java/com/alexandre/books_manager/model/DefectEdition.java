package com.alexandre.books_manager.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class DefectEdition {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String defectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_isbn", nullable = false)
    private BookEdition edition;

    @OneToMany(mappedBy = "affectedBatch")
    private List<Defect> affectedBatches;

    public String getDefectCode() {
        return defectCode;
    }

    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    public List<Defect> getAffectedBatches() {
        return affectedBatches;
    }

    public void setAffectedBatches(List<Defect> affectedBatches) {
        this.affectedBatches = affectedBatches;
    }

    public BookEdition getEdition() {
        return edition;
    }

    public void setEdition(BookEdition edition) {
        this.edition = edition;
    }
}
