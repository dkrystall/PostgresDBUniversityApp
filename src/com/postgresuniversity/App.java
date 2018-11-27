package com.postgresuniversity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    private final String url = "jdbc:postgresql://cs1.calstatela.edu:5432/cs4222s18";
    private final String user = "cs4222s18";
    private final String password = "fHypPC8C"; //add this before running

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
	// write your code here
        System.out.println("Yo dawg");
        App app = new App();
        app.connect();
    }
}
