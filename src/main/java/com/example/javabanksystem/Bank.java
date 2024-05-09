package com.example.javabanksystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {
    private int bankId; // New field to identify each bank uniquely
    private String bankName;
    private List<Account> accounts;
    private double totalTransactionFeeAmount;
    private double totalTransferAmount;
    private double transactionFlatFeeAmount;
    private double transactionPercentFeeValue;

    public Bank(int bankId, String bankName, double totalTransactionFeeAmount, double totalTransferAmount,
                double transactionFlatFeeAmount, double transactionPercentFeeValue) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.transactionFlatFeeAmount = transactionFlatFeeAmount;
        this.transactionPercentFeeValue = transactionPercentFeeValue;
        this.accounts = new ArrayList<>();
    }


    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public double getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public void setTotalTransactionFeeAmount(double totalTransactionFeeAmount) {
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
    }

    public double getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(double totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public double getTransactionFlatFeeAmount() {
        return transactionFlatFeeAmount;
    }

    public void setTransactionFlatFeeAmount(double transactionFlatFeeAmount) {
        this.transactionFlatFeeAmount = transactionFlatFeeAmount;
    }

    public double getTransactionPercentFeeValue() {
        return transactionPercentFeeValue;
    }

    public void setTransactionPercentFeeValue(double transactionPercentFeeValue) {
        this.transactionPercentFeeValue = transactionPercentFeeValue;
    }

    public Account getAccountById(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }


    // 1. Create a bank with all required values

    public static void createBank(Connection connection, Scanner scanner) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();
        System.out.print("Enter flat fee: ");
        double flatFee = 0.0;
        try {
            flatFee = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid flat fee value.");
        }
        System.out.print("Enter percent fee: ");
        double percentFee = 0.0;
        try {
            percentFee = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid percent fee value.");
        }

        double initialTotalTransactionFeeAmount = 0.0;
        double initialTotalTransferAmount = 0.0;

        // Insert new bank into the database
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO bank (bank_name, total_transaction_fee_amount, total_transfer_amount, transaction_flat_fee_amount, transaction_percent_fee_value) VALUES ('%s', %.2f, %.2f, %.2f, %.2f)",
                    bankName, initialTotalTransactionFeeAmount, initialTotalTransferAmount, flatFee, percentFee);
            statement.executeUpdate(sql);
            System.out.println("Bank created successfully.");
        }
    }


    // 9. Check bank total transaction fee amount
    public static double getTotalTransactionFeeAmount(Connection connection, int bankId) throws SQLException {
        double totalTransactionFeeAmount = 0.0;
        String sql = "SELECT total_transaction_fee_amount FROM bank WHERE bank_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bankId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalTransactionFeeAmount = resultSet.getDouble("total_transaction_fee_amount");
                }
            }
        }
        return totalTransactionFeeAmount;
    }


    // 10. Check bank total transfer amount
    public static double getTotalTransferAmount(Connection connection, int bankId) throws SQLException {
        double totalTransferAmount = 0.0;
        String sql = "SELECT total_transfer_amount FROM bank WHERE bank_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bankId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalTransferAmount = resultSet.getDouble("total_transfer_amount");
                }
            }
        }
        return totalTransferAmount;
    }

}
