package com.library.controller;

import com.library.dto.Result;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.service.BorrowService;
import com.library.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private JwtUtil jwtUtil;

    // 从 Token 中获取用户 ID
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }

    // 获取可借图书列表
    @GetMapping("/available-books")
    public Result<List<Book>> getAvailableBooks() {
        try {
            List<Book> books = borrowService.getAvailableBooks();
            return Result.success(books, "获取成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 借书
    @PostMapping("/borrow/{bookId}")
    public Result<BorrowRecord> borrowBook(@PathVariable Long bookId, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error("请先登录");
            }
            BorrowRecord record = borrowService.borrowBook(userId, bookId);
            return Result.success(record, "借阅成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 还书
    @PostMapping("/return/{recordId}")
    public Result<BorrowRecord> returnBook(@PathVariable Long recordId, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error("请先登录");
            }
            BorrowRecord record = borrowService.returnBook(userId, recordId);
            return Result.success(record, "归还成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 获取我的借阅记录
    @GetMapping("/my-records")
    public Result<List<BorrowRecord>> getMyRecords(HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error("请先登录");
            }
            // 检查并更新逾期记录
            borrowService.checkAndUpdateOverdueRecords(userId);
            List<BorrowRecord> records = borrowService.getUserBorrowRecords(userId);
            return Result.success(records, "获取成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
