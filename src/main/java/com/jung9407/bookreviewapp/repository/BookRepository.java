package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
