package com.library.dto;

import lombok.Data;

@Data
public class StatisticsDTO {
    private Long totalUsers;
    private Long totalBooks;
    private Long totalBorrows;
    private Long availableBooks;
    private Long borrowedBooks;
    private Long returnedBooks;
    private Long overdueBooks;
}
