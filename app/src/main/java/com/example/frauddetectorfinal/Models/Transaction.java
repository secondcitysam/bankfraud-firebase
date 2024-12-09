package com.example.frauddetectorfinal.Models;

public class Transaction {
    private String transactionID;
    private String fromAccount;
    private String toAccount;
    private double amount;
    private String timestamp;

    public Transaction() {
    }

    public Transaction(String transactionID, String fromAccount, String toAccount, double amount, String timestamp) {
        this.transactionID = transactionID;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionID + "\nFrom: " + fromAccount + "\nTo: " + toAccount + "\nAmount: " + amount + "\nTimestamp: " + timestamp;
    }
}
