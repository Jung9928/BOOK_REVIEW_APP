package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findAll(Pageable pageable);

    @Query("SELECT b FROM BookEntity b WHERE b.title LIKE CONCAT('%', :title, '%')")
    Page<BookEntity> findAllByTitle(@Param("title") String title, Pageable pageable);
}
