package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserDialog extends JDialog {
    private JTextField userIDField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JComboBox userTypeComboBox;
    private JTextField nameField;
    private JTextField phoneField;
    private JButton addButton;

    public AddUserDialog(final Connection connection) {
        setTitle("Add User");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setModal(true);

        userIDField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        String[] userTypes = {"Student", "Patron"}; // Modify as needed
        userTypeComboBox = new JComboBox(userTypes);
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        addButton = new JButton("Add User (Register)");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userID = Integer.parseInt(userIDField.getText());
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String userType = (String) userTypeComboBox.getSelectedItem();
                String name = nameField.getText();
                String phone = phoneField.getText();

                // Insert the user into the database
                insertUser(connection, userID, username, password, email, userType, name, phone);

                // Close the dialog
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("User ID:"));
        panel.add(userIDField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("User Type:"));
        panel.add(userTypeComboBox);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("")); // Empty label for spacing
        panel.add(addButton);

        getContentPane().add(panel);
    }

    private void insertUser(Connection connection, int userID, String username, String password, String email, String userType, String name, String phone) {
        String query = "INSERT INTO Users (UserID, Username, Password, Email, UserType) VALUES (?, ?, ?, ?, ?)";
        String studentQuery = "INSERT INTO Students (UserID, StudentName, Phone) VALUES (?, ?, ?)";
        String patronQuery = "INSERT INTO Patrons (UserID, PatronName, Phone) VALUES (?, ?, ?)";

        try {
            PreparedStatement userStatement = connection.prepareStatement(query);
            userStatement.setInt(1, userID);
            userStatement.setString(2, username);
            userStatement.setString(3, password);
            userStatement.setString(4, email);
            userStatement.setString(5, userType);

            // Execute the user insertion
            userStatement.executeUpdate();

            if ("Student".equals(userType)) {
                PreparedStatement studentStatement = connection.prepareStatement(studentQuery);
                studentStatement.setInt(1, userID);
                studentStatement.setString(2, name);
                studentStatement.setString(3, phone);
                studentStatement.executeUpdate();
            } else if ("Patron".equals(userType)) {
                PreparedStatement patronStatement = connection.prepareStatement(patronQuery);
                patronStatement.setInt(1, userID);
                patronStatement.setString(2, name);
                patronStatement.setString(3, phone);
                patronStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


