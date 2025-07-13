package com.alexandre.books_manager.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "defect_editions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"defect_code", "edition_isbn"})
)
public class DefectEdition {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String defectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_isbn", nullable = false)
    private BookEdition edition;

    @ElementCollection
    @CollectionTable(name = "affected_batches",
            joinColumns = @JoinColumn(name = "defect_code"))
    @Column(name = "batch_number")
    private List<String> affectedBatches = new ArrayList<>();

    public String getDefectCode() {
        return defectCode;
    }

    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    public List<String> getAffectedBatches() {
        return affectedBatches;
    }

    public void setAffectedBatches(List<String> affectedBatches) {
        this.affectedBatches = affectedBatches;
    }

    public BookEdition getEdition() {
        return edition;
    }

    public void setEdition(BookEdition edition) {
        this.edition = edition;
    }
}
