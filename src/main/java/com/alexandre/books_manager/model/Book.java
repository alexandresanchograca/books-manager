package com.alexandre.books_manager.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "books",
        uniqueConstraints = @UniqueConstraint(columnNames = {"batch_number", "edition_isbn"})
)
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String publisher;

    private Integer publishedYear;

    @Column(nullable = false, updatable = false)
    private String batchNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_isbn", nullable = false, updatable = false)
    private BookEdition edition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookEdition getEdition() {
        return edition;
    }

    public void setEdition(BookEdition edition) {
        this.edition = edition;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
