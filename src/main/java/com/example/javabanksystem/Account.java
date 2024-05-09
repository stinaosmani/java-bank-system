package com.example.javabanksystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Account {
    private int accountId;
    private String userName;
    private double balance;
    private int bankId; // New field to associate account with a bank

    public Account(int accountId, String userName, double balance, int bankId) {
        this.accountId = accountId;
        this.userName = userName;
        this.balance = balance;
        this.bankId = bankId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }


    // 2. Create an account
    public static void createAccount(Connection connection, Scanner scanner) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        System.out.print("Enter bank ID: ");
        int bankId = scanner.nextInt();

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO accounts (user_name, balance, bank_id) VALUES ('%s', %.2f, %d)", userName, balance, bankId);
            statement.executeUpdate(sql);
            System.out.println("Account created successfully.");
        }
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit of " + amount + " successful. New balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount. Please enter a positive value.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0) {
            if (amount > balance) {
                System.out.println("Withdrawal not possible due to insufficient funds.");
            } else {
                balance -= amount;
                System.out.println("Withdrawal of " + amount + " successful. New balance: " + balance);
            }
        } else {
            System.out.println("Invalid withdrawal amount. Please enter a positive value.");
        }
    }

    // 4. Withdraw money
    public static void withdrawMoney(Connection connection, Scanner scanner) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        double balance = Transaction.getAccountBalance(connection, accountId);

        // Retrieve the bank ID associated with the account
        int bankId = Transaction.getBankIdForAccount(connection, accountId);

        // Create the Account object with bankId
        Account account = new Account(accountId, "", balance, bankId);
        account.withdraw(amount);

        Transaction.updateAccountBalance(connection, accountId, account.getBalance());
    }

    // 5. Deposit money
    public static void depositMoney(Connection connection, Scanner scanner) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();


        double balance = Transaction.getAccountBalance(connection, accountId);

        // Retrieve the bank ID associated with the account
        int bankId = Transaction.getBankIdForAccount(connection, accountId);


        Account account = new Account(accountId, "", balance, bankId);
        account.deposit(amount);


        Transaction.updateAccountBalance(connection, accountId, account.getBalance());
    }

    // 7. Check account balance for any account
    public static void checkAccountBalance(Connection connection, Scanner scanner) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();

        double balance = Transaction.getAccountBalance(connection, accountId);
        if (balance >= 0) {
            System.out.println("Account balance for account ID " + accountId + " is: " + balance);
        } else {
            System.out.println("Account not found.");
        }
    }

    // 8. See list of bank accounts
    public static void listBankAccounts(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM accounts";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    System.out.println("List of Bank Accounts:");
                    System.out.printf("%-20s%-20s%-20s%n", "Account ID", "User Name", "Balance");
                    do {
                        int accountId = resultSet.getInt("account_id");
                        String userName = resultSet.getString("user_name");
                        double balance = resultSet.getDouble("balance");
                        System.out.printf("%-20d%-20s%-20.2f%n", accountId, userName, balance);
                    } while (resultSet.next());
                } else {
                    System.out.println("No bank accounts found.");
                }
            }
        }
    }
}