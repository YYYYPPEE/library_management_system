package com.library.service;

import com.library.dto.StatisticsDTO;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    public StatisticsDTO getStatistics() {
        StatisticsDTO stats = new StatisticsDTO();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalBooks(bookRepository.count());
        stats.setTotalBorrows(borrowRecordRepository.count());
        stats.setAvailableBooks(bookRepository.countByAvailableStockGreaterThanZero());
        stats.setBorrowedBooks(borrowRecordRepository.countByStatus(0));
        stats.setReturnedBooks(borrowRecordRepository.countByStatus(1));
        stats.setOverdueBooks(borrowRecordRepository.countByStatus(2));
        return stats;
    }
}
