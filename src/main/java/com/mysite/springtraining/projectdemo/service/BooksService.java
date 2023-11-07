package com.mysite.springtraining.projectdemo.service;

import com.mysite.springtraining.projectdemo.model.Book;
import com.mysite.springtraining.projectdemo.model.Person;
import com.mysite.springtraining.projectdemo.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> index(Integer page, Integer booksPerPage, Boolean sortByYear) {
        if (page != null && booksPerPage != null) {
            return pagingIndex(page, booksPerPage, sortByYear);
        } else {
            return index(sortByYear);
        }
    }

    public List<Book> pagingIndex(Integer page, Integer booksPerPage, Boolean sortByYear) {
        if (sortByYear != null && sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> index(Boolean sortByYear) {
        if (sortByYear != null && sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        }
        return booksRepository.findAll();
    }

    public Book show(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void giveToPerson(int id, Person person) {
        Book book = show(id);
        book.setTakenOn(new Date());
        book.setOwner(person);
        booksRepository.save(book);
    }

    @Transactional
    public void takeFromPerson(int id) {
        Book book = show(id);
        book.setTakenOn(null);
        book.setOwner(null);
        booksRepository.save(book);
    }

    public Book searchBookByName(String nameContains) {
        return booksRepository.findFirstByNameContainingIgnoreCase(nameContains);
    }
}
