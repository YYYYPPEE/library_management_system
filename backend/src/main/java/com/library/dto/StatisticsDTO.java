package com.library.dto;

public class StatisticsDTO {
    private Long totalUsers;
    private Long totalBooks;
    private Long totalBorrows;
    private Long availableBooks;
    private Long borrowedBooks;
    private Long returnedBooks;
    private Long overdueBooks;

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(Long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public Long getTotalBorrows() {
        return totalBorrows;
    }

    public void setTotalBorrows(Long totalBorrows) {
        this.totalBorrows = totalBorrows;
    }

    public Long getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(Long availableBooks) {
        this.availableBooks = availableBooks;
    }

    public Long getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Long borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public Long getReturnedBooks() {
        return returnedBooks;
    }

    public void setReturnedBooks(Long returnedBooks) {
        this.returnedBooks = returnedBooks;
    }

    public Long getOverdueBooks() {
        return overdueBooks;
    }

    public void setOverdueBooks(Long overdueBooks) {
        this.overdueBooks = overdueBooks;
    }
}
