package com.mysite.springtraining.projectdemo.controller;

import com.mysite.springtraining.projectdemo.model.Book;
import com.mysite.springtraining.projectdemo.model.Person;
import com.mysite.springtraining.projectdemo.service.BooksService;
import com.mysite.springtraining.projectdemo.service.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "books_per_page", required = false) Integer booksPerPage, @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear, Model model) {

        model.addAttribute("books", booksService.index(page, booksPerPage, sortByYear));

        return "books/index";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = booksService.show(id);

        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.index());

        if (book.getOwner() != null) {
            model.addAttribute("person", peopleService.show(book.getOwner().getId()));
        } else {
            model.addAttribute("person", new Person());
        }

        return "books/show";
    }

    @PatchMapping("/{id}/give")
    public String giveToPerson(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.giveToPerson(id, person);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/take")
    public String takeFromPerson(@PathVariable("id") int id) {
        booksService.takeFromPerson(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBook() {
        return "books/search";
    }

    @PostMapping("/search")
    public String searchBookResult(@RequestParam("nameContains") String nameContains, Model model) {
        model.addAttribute("nameContains", nameContains);
        model.addAttribute("foundBook", null);
        model.addAttribute("owner", null);

        Book foundBook = booksService.searchBookByName(nameContains);

        if (nameContains != null && !nameContains.isEmpty()) {
            if (foundBook != null) {
                model.addAttribute("foundBook", foundBook);
                if (foundBook.getOwner() != null) {
                    model.addAttribute("owner",foundBook.getOwner());
                }
            }
        }

        return "books/search";
    }
}
