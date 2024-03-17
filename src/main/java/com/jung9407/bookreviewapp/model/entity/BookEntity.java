package com.jung9407.bookreviewapp.model.entity;

import com.jung9407.bookreviewapp.util.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "books")
@NoArgsConstructor
public class BookEntity {

    @Id
    private int book_id;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(name = "publish_date")
    private Timestamp publishDate;

    @Column(length = 20)
    private String rating;

    @Column
    private String regular_price;

    @Column
    private String selling_price;

    @Column(name = "main_ctg_num")
    private String main_category;

    @Column(name = "sub_stg_num")
    private String sub_category;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(length = 30)
    private String site;

    @PreUpdate
    void modDtm() {
        this.modifiedAt = Timestamp.from(Instant.now());
    }

//    public static BookEntity createBookEntity() {
//        BookEntity bookEntity = new BookEntity();
//
//        bookEntity.setBook_id();
//
//        return bookEntity;
//    }
}