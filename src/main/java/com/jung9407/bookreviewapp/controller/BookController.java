package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.requestDTO.BookSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.BookPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.BookResponseDTO;
import com.jung9407.bookreviewapp.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

//    @GetMapping("/list/{title}")
//    public Page<BookResponseDTO> getBookList(@PathVariable("title") String title, Pageable pageable) {
//        log.info("title : " + title);
//        return bookService.findBookList(title, pageable);
//    }

    // /bookList/paging?page=1
    @GetMapping("/list")
    public BookPagingResponseDTO<List<BookResponseDTO>> getBookList(@PageableDefault(sort = {"publishDate"}) Pageable pageable, BookSearchConditionDTO bookSearchConditionDTO) {
        log.info("searchMainCategory : " + bookSearchConditionDTO.getSearchMainCategory());
        log.info("searchSubCategory : " + bookSearchConditionDTO.getSearchSubCategory());
        log.info("searchValue : " + bookSearchConditionDTO.getSearchValue());
        return bookService.getBookList(pageable, bookSearchConditionDTO);
    }
}
