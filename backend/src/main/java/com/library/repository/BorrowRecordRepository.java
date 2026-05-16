package com.library.repository;

import com.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord> findByBookId(Long bookId);
    Long countByStatus(Integer status);

    @Modifying
    @Transactional
    @Query("UPDATE BorrowRecord br SET br.status = 2 WHERE br.status = 0 AND br.dueTime < :now")
    void updateOverdueRecords(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE BorrowRecord br SET br.status = 2 WHERE br.status = 0 AND br.dueTime < CURRENT_TIMESTAMP")
    void updateOverdueRecords();
}
