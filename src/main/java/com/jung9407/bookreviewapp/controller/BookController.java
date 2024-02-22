package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.entity.Book;
import com.jung9407.bookreviewapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/api/v1/books")
    public List<Book> getBooks() {
        List<Book> books = bookRepository.findAll();

        return books;
    }
}
