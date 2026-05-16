package com.library.config;

import com.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueTaskScheduler {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOverdueRecords() {
        borrowRecordRepository.updateOverdueRecords();
    }
}
