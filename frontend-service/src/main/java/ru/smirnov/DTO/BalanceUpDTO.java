package ru.smirnov.DTO;

public class BalanceUpDTO {
    private String userId;
    private int balance;

    public BalanceUpDTO() {}

    public BalanceUpDTO(String userId, int balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}


