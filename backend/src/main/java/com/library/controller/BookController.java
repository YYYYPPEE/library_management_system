package com.library.controller;

import com.library.dto.Result;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Result<List<Book>> getAllBooks() {
        return Result.success(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public Result<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("图书不存在"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Book> createBook(@RequestBody Book book) {
        book.setCreatedTime(LocalDateTime.now());
        book.setUpdatedTime(LocalDateTime.now());
        if (book.getTotalStock() == null) {
            book.setTotalStock(0);
        }
        if (book.getAvailableStock() == null) {
            book.setAvailableStock(book.getTotalStock());
        }
        if (book.getStatus() == null) {
            book.setStatus(0);
        }
        return Result.success(bookRepository.save(book));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    if (bookDetails.getTotalStock() != null) {
                        int diff = bookDetails.getTotalStock() - book.getTotalStock();
                        book.setTotalStock(bookDetails.getTotalStock());
                        book.setAvailableStock(Math.max(0, book.getAvailableStock() + diff));
                    }
                    if (bookDetails.getAvailableStock() != null) {
                        book.setAvailableStock(Math.min(bookDetails.getAvailableStock(), book.getTotalStock()));
                    }
                    if (bookDetails.getStatus() != null) {
                        book.setStatus(bookDetails.getStatus());
                    }
                    book.setUpdatedTime(LocalDateTime.now());
                    return Result.success(bookRepository.save(book));
                })
                .orElse(Result.error("图书不存在"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return Result.success("删除成功");
        }
        return Result.error("图书不存在");
    }
}
