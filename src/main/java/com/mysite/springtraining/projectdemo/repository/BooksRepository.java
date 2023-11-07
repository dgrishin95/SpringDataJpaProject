package com.mysite.springtraining.projectdemo.repository;

import com.mysite.springtraining.projectdemo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Book, Integer> {

    Book findFirstByNameContainingIgnoreCase(String name);
}
