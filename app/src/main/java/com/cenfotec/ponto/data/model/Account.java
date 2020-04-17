package com.cenfotec.ponto.data.model;

public class Account {

    private String accountNumber;
    private Float balance;

    public Account() {
    }

    public Account(String accountNumber, Float balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
}
