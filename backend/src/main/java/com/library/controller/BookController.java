package com.library.controller;

import com.library.dto.Result;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Result<Map<String, Object>> getAllBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<Book> bookPage;
        
        if (keyword.isEmpty()) {
            bookPage = bookRepository.findAll(pageable);
        } else {
            bookPage = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword, pageable);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", bookPage.getContent());
        result.put("totalElements", bookPage.getTotalElements());
        result.put("totalPages", bookPage.getTotalPages());
        result.put("currentPage", bookPage.getNumber());
        result.put("size", bookPage.getSize());
        
        return Result.success(result);
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
        if (book.getAvailableStock() != null && book.getTotalStock() != null 
                && book.getAvailableStock() > book.getTotalStock()) {
            return Result.error("可借库存不能大于总库存");
        }
        
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
                return Result.error("ISBN已存在，不能重复添加图书");
            }
        }
        
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
        
        if (book.getAvailableStock() != null && book.getAvailableStock() == 0) {
            book.setStatus(1);
        }
        
        return Result.success(bookRepository.save(book));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        java.util.Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return Result.error("图书不存在");
        }
        
        Book book = optionalBook.get();
        
        Integer newTotalStock = bookDetails.getTotalStock() != null ? bookDetails.getTotalStock() : book.getTotalStock();
        Integer newAvailableStock = bookDetails.getAvailableStock() != null ? bookDetails.getAvailableStock() : book.getAvailableStock();
        
        if (newAvailableStock > newTotalStock) {
            return Result.error("可借库存不能大于总库存");
        }
        
        if (bookDetails.getIsbn() != null && !bookDetails.getIsbn().isEmpty()) {
            java.util.Optional<Book> existingBook = bookRepository.findByIsbn(bookDetails.getIsbn());
            if (existingBook.isPresent() && !existingBook.get().getId().equals(id)) {
                return Result.error("ISBN已存在");
            }
        }
        
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
        
        if (book.getAvailableStock() == 0) {
            book.setStatus(1);
        }
        
        book.setUpdatedTime(LocalDateTime.now());
        return Result.success(bookRepository.save(book));
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
