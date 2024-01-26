package org.example;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String Connector_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(Connector_URL, USERNAME, PASSWORD);
        return connection;
    }

    private void testConnection() {
        try (Connection connection = DriverManager.getConnection(Connector_URL, USERNAME, PASSWORD)) {
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.err.println("Connection failed. Error message:  " + e.getMessage());
        }
    }


}
