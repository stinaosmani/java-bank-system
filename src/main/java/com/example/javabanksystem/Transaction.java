package com.example.javabanksystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Transaction {
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER, FLAT_FEE, PERCENT_FEE
    }

    private double amount;
    private int originatingAccountId;
    private int resultingAccountId;
    private String transactionReason;
    private TransactionType transactionType;

    public Transaction(double amount, int originatingAccountId, int resultingAccountId, String transactionReason, TransactionType transactionType) {
        this.amount = amount;
        this.originatingAccountId = originatingAccountId;
        this.resultingAccountId = resultingAccountId;
        this.transactionReason = transactionReason;
        this.transactionType = transactionType;
    }

    // 3. Perform both flat fee and percent fee transaction from one account to another
    public static void performFlatFeeTransaction(Connection connection, Scanner scanner) throws SQLException {
        performTransaction(connection, scanner, TransactionType.FLAT_FEE);
    }

    public static void performPercentFeeTransaction(Connection connection, Scanner scanner) throws SQLException {
        performTransaction(connection, scanner, TransactionType.PERCENT_FEE);
    }

    private static void performTransaction(Connection connection, Scanner scanner, TransactionType transactionType) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter originating account ID: ");
        int originatingAccountId = scanner.nextInt();
        System.out.print("Enter resulting account ID: ");
        int resultingAccountId = scanner.nextInt();
        System.out.print("Enter amount to transfer: ");
        double transferAmount = scanner.nextDouble();
        System.out.print("Enter transaction reason: ");
        scanner.nextLine();
        String transactionReason = scanner.nextLine();

        try {
            // Retrieve the bank ID associated with the originating account
            int bankId = getBankIdForAccount(connection, originatingAccountId);

            // Get the fee value from the bank table based on the transaction type
            double fee = 0.0;
            if (transactionType == TransactionType.FLAT_FEE) {
                fee = getFlatFeeForBank(connection, bankId);
            } else if (transactionType == TransactionType.PERCENT_FEE) {
                fee = getPercentFeeForBank(connection, bankId);
            }

            // Calculate the total fee
            double totalFee = calculateTotalFee(transferAmount, fee, transactionType);

            double originatingAccountBalance = getAccountBalance(connection, originatingAccountId);
            double totalAmountToDeduct = transferAmount + totalFee;

            if (originatingAccountBalance >= totalAmountToDeduct) {
                updateAccountBalance(connection, originatingAccountId, originatingAccountBalance - totalAmountToDeduct);
                updateAccountBalance(connection, resultingAccountId, getAccountBalance(connection, resultingAccountId) + transferAmount);
                insertTransactionRecord(connection, transferAmount, originatingAccountId, resultingAccountId, transactionReason, transactionType.toString());
                System.out.println("Transaction completed successfully.");
                updateTotalTransactionFeeAmount(connection, totalFee); // Update total transaction fee amount
                updateTotalTransferAmount(connection, transferAmount); // Update total transfer amount
            } else {
                System.out.println("Insufficient balance in the originating account.");
            }
        } catch (SQLException e) {
            System.out.println("Error performing transaction: " + e.getMessage());
        }
    }

    public static int getBankIdForAccount(Connection connection, int accountId) throws SQLException {
        String sql = "SELECT bank_id FROM accounts WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("bank_id");
                } else {
                    throw new SQLException("Bank ID not found for account ID: " + accountId);
                }
            }
        }
    }

    private static double getFlatFeeForBank(Connection connection, int bankId) throws SQLException {
        String sql = "SELECT transaction_flat_fee_amount FROM bank WHERE bank_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bankId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("transaction_flat_fee_amount");
                } else {
                    throw new SQLException("Flat fee not found for bank ID: " + bankId);
                }
            }
        }
    }

    private static double getPercentFeeForBank(Connection connection, int bankId) throws SQLException {
        String sql = "SELECT transaction_percent_fee_value FROM bank WHERE bank_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bankId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("transaction_percent_fee_value");
                } else {
                    throw new SQLException("Percent fee not found for bank ID: " + bankId);
                }
            }
        }
    }


    private static void updateTotalTransactionFeeAmount(Connection connection, double totalFee) throws SQLException {
        String sql = "UPDATE bank SET total_transaction_fee_amount = total_transaction_fee_amount + ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, totalFee);
            statement.executeUpdate();
            System.out.println("Total transaction fee amount updated successfully.");
        }
    }


    private static void updateTotalTransferAmount(Connection connection, double transferAmount) throws SQLException {
        String sql = "UPDATE bank SET total_transfer_amount = total_transfer_amount + ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, transferAmount);
            statement.executeUpdate();
            System.out.println("Total transfer amount updated successfully.");
        }
    }


    private static double calculateTotalFee(double transferAmount, double fee, TransactionType transactionType) {
        if (transactionType == TransactionType.FLAT_FEE) {
            return fee;
        } else if (transactionType == TransactionType.PERCENT_FEE) {
            return (fee / 100) * transferAmount;
        } else {
            return 0.0; // Handle other transaction types if needed
        }
    }


    public static double getAccountBalance(Connection connection, int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                } else {
                    throw new SQLException("Account not found with ID: " + accountId);
                }
            }
        }
    }

    public static void updateAccountBalance(Connection connection, int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, accountId);
            statement.executeUpdate();
        }
    }

    private static void insertTransactionRecord(Connection connection, double amount, int originatingAccountId, int resultingAccountId, String transactionReason, String transactionType) throws SQLException {
        String sql = "INSERT INTO transactions (amount, originating_account_id, resulting_account_id, transaction_reason, transaction_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            statement.setInt(2, originatingAccountId);
            statement.setInt(3, resultingAccountId);
            statement.setString(4, transactionReason);
            statement.setString(5, transactionType);
            statement.executeUpdate();
        }
    }

    // 6. See list of transactions for every account
    public static void listTransactionsForAccount(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE originating_account_id = ? OR resulting_account_id = ?")) {
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", "Transaction ID", "Amount", "Originating Account", "Resulting Account", "Transaction Reason");
                    do {
                        int transactionId = resultSet.getInt("transaction_id");
                        double amount = resultSet.getDouble("amount");
                        int originatingAccountId = resultSet.getInt("originating_account_id");
                        int resultingAccountId = resultSet.getInt("resulting_account_id");
                        String transactionReason = resultSet.getString("transaction_reason");

                        System.out.printf("%-20d%-20.2f%-20d%-20d%-20s%n", transactionId, amount, originatingAccountId, resultingAccountId, transactionReason);
                    } while (resultSet.next());
                } else {
                    System.out.println("No transactions found for the account.");
                }
            }
        }
    }

}
