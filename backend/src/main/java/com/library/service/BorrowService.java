package com.library.service;

import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    // 借书
    @Transactional
    public BorrowRecord borrowBook(Long userId, Long bookId) {
        // 验证图书是否存在
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("图书不存在"));

        // 检查库存
        if (book.getAvailableStock() <= 0) {
            throw new RuntimeException("图书库存不足");
        }

        // 检查图书状态（是否下架）
        if (book.getStatus() == 1) {
            throw new RuntimeException("图书已下架，无法借阅");
        }

        // 检查用户是否已借过该图书且未归还
        List<BorrowRecord> existingRecords = borrowRecordRepository.findByUserId(userId);
        boolean alreadyBorrowed = existingRecords.stream()
                .anyMatch(record -> record.getBookId().equals(bookId) && record.getStatus() == 0);
        if (alreadyBorrowed) {
            throw new RuntimeException("您已借阅该图书且未归还");
        }

        // 扣减库存
        book.setAvailableStock(book.getAvailableStock() - 1);
        bookRepository.save(book);

        // 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowTime(LocalDateTime.now());
        record.setDueTime(LocalDateTime.now().plusDays(30)); // 默认借期30天
        record.setStatus(0); // 0-借阅中
        return borrowRecordRepository.save(record);
    }

    // 还书
    @Transactional
    public BorrowRecord returnBook(Long userId, Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("借阅记录不存在"));

        // 验证是否是该用户的记录
        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此借阅记录");
        }

        // 检查是否已归还
        if (record.getStatus() != 0) {
            throw new RuntimeException("该图书已归还或已逾期");
        }

        // 更新借阅记录
        record.setReturnTime(LocalDateTime.now());
        // 判断是否逾期
        if (LocalDateTime.now().isAfter(record.getDueTime())) {
            record.setStatus(2); // 已逾期
        } else {
            record.setStatus(1); // 已归还
        }
        borrowRecordRepository.save(record);

        // 增加库存
        Book book = bookRepository.findById(record.getBookId())
                .orElseThrow(() -> new RuntimeException("图书不存在"));
        book.setAvailableStock(Math.min(book.getAvailableStock() + 1, book.getTotalStock()));
        bookRepository.save(book);

        return record;
    }

    // 获取用户的借阅记录
    public List<BorrowRecord> getUserBorrowRecords(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }

    // 获取可借图书（库存>0且状态正常）
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll().stream()
                .filter(book -> book.getStatus() == 0 && book.getAvailableStock() > 0)
                .toList();
    }

    // 检查并更新用户的逾期记录
    public void checkAndUpdateOverdueRecords(Long userId) {
        List<BorrowRecord> records = borrowRecordRepository.findByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        for (BorrowRecord record : records) {
            if (record.getStatus() == 0 && now.isAfter(record.getDueTime())) {
                record.setStatus(2);
                borrowRecordRepository.save(record);
            }
        }
    }
}
