package com.example.javabanksystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BankSystem {

    public static void main(String[] args) {
        try (Connection connection = SQLiteConnector.connect()) {
            initializeDatabase(connection);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Bank System!");

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Create a bank");
                System.out.println("2. Create an account");
                System.out.println("3. Perform transactions");
                System.out.println("4. Withdraw money");
                System.out.println("5. Deposit money");
                System.out.println("6. See list of transactions for any account");
                System.out.println("7. Check account balance for any account");
                System.out.println("8. See list of bank accounts");
                System.out.println("9. Check bank total transaction fee amount");
                System.out.println("10. Check bank total transfer amount");
                System.out.println("11. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        Bank.createBank(connection, scanner);
                        break;
                    case 2:
                        Account.createAccount(connection, scanner);
                        break;
                    case 3:
                        System.out.println("Choose transaction type:");
                        System.out.println("1. Flat Fee Transaction");
                        System.out.println("2. Percent Fee Transaction");
                        System.out.print("Enter your choice: ");
                        int transactionTypeChoice = scanner.nextInt();
                        switch (transactionTypeChoice) {
                            case 1:
                                Transaction.performFlatFeeTransaction(connection, scanner);
                                break;
                            case 2:
                                Transaction.performPercentFeeTransaction(connection, scanner);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        break;

                    case 4:
                        Account.withdrawMoney(connection, scanner);
                        break;
                    case 5:
                        Account.depositMoney(connection, scanner);
                        break;
                    case 6:
                        Transaction.listTransactionsForAccount(connection, scanner);
                        break;
                    case 7:
                        Account.checkAccountBalance(connection, scanner);
                        break;
                    case 8:
                        Account.listBankAccounts(connection);
                        break;
                    case 9:
                        System.out.print("Enter bank ID: ");
                        int bankIdForFee = scanner.nextInt();
                        double totalTransactionFeeAmount = Bank.getTotalTransactionFeeAmount(connection, bankIdForFee);
                        System.out.println("Total transaction fee amount for Bank ID " + bankIdForFee + ": " + totalTransactionFeeAmount);
                        break;
                    case 10:
                        System.out.print("Enter bank ID: ");
                        int bankId = scanner.nextInt();
                        double totalTransferAmount = Bank.getTotalTransferAmount(connection, bankId);
                        System.out.println("Total transfer amount for Bank ID " + bankId + ": " + totalTransferAmount);
                        break;
                    case 11:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException, IOException {
        String schemaFilePath = "src/main/resources/schema.sql";

        // Read the schema file
        String schema = new String(Files.readAllBytes(Paths.get(schemaFilePath)));

        // Split the schema into individual SQL statements
        String[] sqlStatements = schema.split(";");

        // Execute each SQL statement
        try (Statement statement = connection.createStatement()) {
            System.out.println("Executing SQL queries to create tables...");
            for (String sqlStatement : sqlStatements) {

                String trimmedSql = sqlStatement.trim();
                if (!trimmedSql.isEmpty()) {
                    boolean success = statement.execute(trimmedSql);
                    if (success) {
                        System.out.println("Table created successfully");
                    } else {
                        System.out.println("Error creating table");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}
