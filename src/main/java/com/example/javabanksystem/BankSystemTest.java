package com.example.javabanksystem;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.*;
import java.util.Scanner;

public class BankSystemTest {

    // Helper method to establish a connection to an in-memory database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:src/main/java/com/example/javabanksystem/MyDatabase.db");
    }

    //Test creating a new bank
    @Test
    public void testCreateBankWithRequiredValues() throws SQLException {
        try (Connection connection = getConnection()) {
            Bank.createBank(connection, new Scanner("1\nBank 1\n4\n3\n"));
            assertNotNull("Bank should exist", getBank(connection));
        }
    }

    // Helper method to retrieve bank information
    private Bank getBank(Connection connection) throws SQLException {
        String query = "SELECT * FROM bank LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int bankId = resultSet.getInt("bank_id");
                    String bankName = resultSet.getString("bank_name");
                    double totalTransactionFeeAmount = resultSet.getDouble("total_transaction_fee_amount");
                    double totalTransferAmount = resultSet.getDouble("total_transfer_amount");
                    double transactionFlatFeeAmount = resultSet.getDouble("transaction_flat_fee_amount");
                    double transactionPercentFeeValue = resultSet.getDouble("transaction_percent_fee_value");
                    return new Bank(bankId, bankName, totalTransactionFeeAmount, totalTransferAmount,
                            transactionFlatFeeAmount, transactionPercentFeeValue);
                } else {
                    return null;
                }
            }
        }
    }

    // Test creating a new account
    @Test
    public void testAddAccountToBank() throws SQLException {
        try (Connection connection = getConnection()) {
            // Create a bank
            Bank.createBank(connection, new Scanner("1\nTest Bank\n10\n5\n"));

            // Get the ID of the bank that was just created
            int bankId = getBankId(connection, "Test Bank");

            // Create am account
            String accountInput = String.format("2\nJohn Doe\n1000\n%d\n", bankId);
            Account.createAccount(connection, new Scanner(accountInput));

            // Check if the account was created successfully
            assertNotNull("Account exists", getAccount(connection));
        }
    }


    // Helper method to retrieve account information
    private Account getAccount(Connection connection) throws SQLException {
        String query = "SELECT * FROM accounts LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int accountId = resultSet.getInt("account_id");
                    String userName = resultSet.getString("user_name");
                    double balance = resultSet.getDouble("balance");
                    int bankId = resultSet.getInt("bank_id");
                    return new Account(accountId, userName, balance, bankId);
                } else {
                    return null;
                }
            }
        }
    }

    @Test
    public void testCheckTotalTransactionFeeAmountForSpecificBank() throws SQLException {
        try (Connection connection = getConnection()) {
            Bank.createBank(connection, new Scanner("1\nTest Bank 6\n10\n5\n"));

            int bankId = getBankId(connection, "Test Bank 6");


            double totalTransactionFeeAmount = Bank.getTotalTransactionFeeAmount(connection, bankId);

            // Assert that the total transaction fee amount is not negative
            assertTrue("Total transaction fee amount should be non-negative", totalTransactionFeeAmount >= 0.0);
        }
    }

    private int getBankId(Connection connection, String bankName) throws SQLException {
        int bankId = -1;
        String sql = "SELECT bank_id FROM bank WHERE bank_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bankName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bankId = resultSet.getInt("bank_id");
                }
            }
        }
        return bankId;
    }

    private double getAccountBalance(Connection connection, int accountId) throws SQLException {
        double balance = 0.0;
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getDouble("balance");
                }
            }
        }
        return balance;
    }

    // Test withdrawing from account with id 9 and then deposit on that same account
    // For the test to pass you should calculate the expected amount after withdrawl and deposit accordingly
    @Test
    public void testWithdrawAndDepositMoney() throws SQLException {
        try (Connection connection = getConnection()) {

            // Create an account with an initial balance
            String accountInput = "2\nJohn 1\n1000\n1\n";
            Account.createAccount(connection, new Scanner(accountInput));

            Account.withdrawMoney(connection, new Scanner("4\n1\n100\n"));

            // Check the current balance after withdrawal
            double currentBalance = getAccountBalance(connection, 1);

            Account.depositMoney(connection, new Scanner("5\n1\n300\n"));

            // Calculate the expected balance after deposit
            double expectedBalance = currentBalance + 300.0;

            // Check that the balance has been updated correctly
            assertEquals(expectedBalance, getAccountBalance(connection, 1), 0.01);
        }
    }

    // Test to view all transition list for one account
    @Test
    public void testListTransactionsForAccount() throws SQLException {
        try (Connection connection = getConnection()) {
            Bank.createBank(connection, new Scanner("1\nBank 3\n10\n5\n"));

            String accountInput = "2\nStine Osmani\n1000\n1\n";
            Account.createAccount(connection, new Scanner(accountInput));

            // Perform some transactions using flat fee
            Transaction.performFlatFeeTransaction(connection, new Scanner("3\n2\n1\n10\nPayment\n"));
            Transaction.performFlatFeeTransaction(connection, new Scanner("3\n2\n1\n5\nPayment\n"));

            // Display the list of transactions for the account
            Transaction.listTransactionsForAccount(connection, new Scanner("6\n2\n"));
        }
    }

    //Check Account Balance
    @Test
    public void testCheckAccountBalance() throws SQLException {
        try (Connection connection = getConnection()) {
            Bank.createBank(connection, new Scanner("1\nTest Bank 4\n10\n5\n"));

            // Add an account
            Account.createAccount(connection, new Scanner("2\nStine\n1000\n3\n"));

            // Check the balance of the account
            Account.checkAccountBalance(connection, new Scanner("7\n1\n"));

        }
    }

    // Test listing all the bank accounts
    @Test
    public void testListBankAccounts() throws SQLException {
        try (Connection connection = getConnection()) {

            Bank.createBank(connection, new Scanner("1\nTest Bank 5\n10\n5\n"));

            // Add some accounts
            Account.createAccount(connection, new Scanner("2\nStina\n1000\n1\n"));
            Account.createAccount(connection, new Scanner("2\nOsmani\n2000\n1\n"));

            // Display the list of bank accounts
            Account.listBankAccounts(connection);

        }
    }
}
