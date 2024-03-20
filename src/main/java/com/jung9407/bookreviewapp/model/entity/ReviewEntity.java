package com.jung9407.bookreviewapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "reviews")
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int review_id;

    @Column(length = 100)
    private int book_id;

    @Column
    private String review_title;

    @Column(length = 10)
    private String review_rating;

    @Column(length = 50)
    private String reviewer;

    @Column(name = "review_date")
    private Timestamp reviewDate;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "review_site")
    private String reviewSite;

    @PreUpdate
    void modDtm() {
        this.modifiedAt = Timestamp.from(Instant.now());
    }
}
