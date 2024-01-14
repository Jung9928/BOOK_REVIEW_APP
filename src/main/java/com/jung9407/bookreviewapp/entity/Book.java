package com.jung9407.bookreviewapp.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_id;

    @Column(length = 100, nullable = false)
    private String book_title;

    @Column(length = 1000)
    private String imgSrc;

    @Column
    private int regular_price;

    @Column
    private int selling_price;
}
