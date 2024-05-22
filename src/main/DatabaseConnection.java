package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class DatabaseConnection {
    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/library_managementdb";
    String username = "root";
    String password = "";
    Connection connection = null;

    public DatabaseConnection() {
        // Constructor to initialize the database connection
        try {
            // Establish the database connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUserForTable(String tableName, String usernameColumn, String passwordColumn, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // Build the SQL query dynamically
            String query = "SELECT " + passwordColumn + " FROM " + tableName + " WHERE " + usernameColumn + " = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String dbPassword = resultSet.getString(1);

                // In a real application, you should implement password hashing and salting here
                // Then, compare the hashed password
                if (password.equals(dbPassword)) {
                    return true; // Authentication successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //closeConnection(connection, preparedStatement, resultSet);
        }
        return false; // Authentication failed
    }

    public String getUserType(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String userType = null;

        try {
            connection = getConnection();

            String query = "SELECT UserType FROM Users WHERE Username = ? AND Password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userType = resultSet.getString("UserType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, preparedStatement, resultSet);
        }
        return userType; // Returns "student" or "patron" or null if not found
    }




}




