package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.entity.BookEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class BookResponseDTO {

    private int book_id;

    private String title;

    private String author;

    private String publisher;

    private Timestamp publishDate;

    private String rating;

    private String origin_price;

    private String sale_price;

    private String imgPath;

    private Timestamp modifiedAt;

    private String site;

    public BookResponseDTO(int book_id, String title, String author, String publisher, Timestamp publishDate, String rating, String origin_price, String sale_price, String imgPath, Timestamp modifiedAt, String site) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.rating = rating;
        this.origin_price = origin_price;
        this.sale_price = sale_price;
        this.imgPath = imgPath;
        this.modifiedAt = modifiedAt;
        this.site = site;
    }

    public static BookResponseDTO entityToBookResponseDTO(BookEntity bookEntity) {
        return new BookResponseDTO(
                bookEntity.getBook_id(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getPublisher(),
                bookEntity.getPublishDate(),
                bookEntity.getRating(),
                bookEntity.getOrigin_price(),
                bookEntity.getSale_price(),
                bookEntity.getImgPath(),
                bookEntity.getModifiedAt(),
                bookEntity.getSite()
        );
    }
}
