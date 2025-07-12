package com.alexandre.books_manager.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class BookEdition {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String authorName;

    @Column(unique = true, nullable = false)
    private String isbn;

    private Integer number;

    @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookBatch> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
