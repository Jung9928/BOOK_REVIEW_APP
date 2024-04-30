package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.entity.BookEntity;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseDTO {

    private int book_id;

    private String isbn;

    private String title;

    private String author;

    private String publisher;

    private Timestamp publishDate;

    private String rating;

    private String regular_price;

    private String selling_price;

    private String main_category;

    private String sub_category;

    private String imgPath;

    private Timestamp modifiedAt;

    private String site;

    private String detailInfoPath;

//    public BookResponseDTO(int book_id, String title, String author, String publisher, Timestamp publishDate, String rating, String origin_price, String sale_price, String imgPath, Timestamp modifiedAt, String site) {
//        this.book_id = book_id;
//        this.title = title;
//        this.author = author;
//        this.publisher = publisher;
//        this.publishDate = publishDate;
//        this.rating = rating;
//        this.origin_price = origin_price;
//        this.sale_price = sale_price;
//        this.imgPath = imgPath;
//        this.modifiedAt = modifiedAt;
//        this.site = site;
//    }
//
//    public static BookResponseDTO entityToBookResponseDTO(BookEntity bookEntity) {
//        return new BookResponseDTO(
//                bookEntity.getBook_id(),
//                bookEntity.getTitle(),
//                bookEntity.getAuthor(),
//                bookEntity.getPublisher(),
//                bookEntity.getPublishDate(),
//                bookEntity.getRating(),
//                bookEntity.getOrigin_price(),
//                bookEntity.getSale_price(),
//                bookEntity.getImgPath(),
//                bookEntity.getModifiedAt(),
//                bookEntity.getSite()
//        );
//    }
}
