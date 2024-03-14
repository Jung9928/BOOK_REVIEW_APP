package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.responseDTO.BookResponseDTO;
import com.jung9407.bookreviewapp.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping("/list/{title}")
    public Page<BookResponseDTO> getBookList(@PathVariable("title") String title, Pageable pageable) {
        log.info("title : " + title);
        return bookService.findBookList(title, pageable);
    }

    // /bookList/paging?page=1
//    @GetMapping("/bookList/paging")
//    public String paging(@PathVariable("page") Pageable pageable) {
//        Page<BookResponseDTO> bookList = bookService.paging(pageable);
//
//        int blockLimit = 5;             // 한 화면에 보여지는 페이지 번호 갯수
//        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;       // 1 6 11 16 ~
//        int endPage = ((startPage + blockLimit - 1) < bookList.getTotalPages()) ? startPage + blockLimit - 1 : bookList.getTotalPages();
//
//        model
//    }
}
