package com.example.javabanksystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {
    private static final String DB_URL = "jdbc:sqlite:src/main/java/com/example/javabanksystem/MyDatabase.db";

    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);
        System.out.println("Connection to SQLite database established");
        return connection;
    }
}

