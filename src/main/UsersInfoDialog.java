package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersInfoDialog extends JDialog {
    private Connection connection;
    private JList userList;
    private DefaultListModel userListModel;
    private final JFrame parentFrame;
    private JButton addUserButton;



    public UsersInfoDialog(JFrame parent, final Connection connection) {
        super(parent, "User List", true);
        this.connection = connection;
        this.parentFrame = parent; // Store the parent frame
        setSize(480, 400);
        setLocationRelativeTo(parent);

        userListModel = new DefaultListModel();
        userList = new JList(userListModel);
        JScrollPane userListScrollPane = new JScrollPane(userList);

        final JComboBox filterComboBox = new JComboBox(new String[]{"All", "Student", "Patron"});
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filter = filterComboBox.getSelectedItem().toString();
                updateUserList(filter);
            }
        });

        // Create Add User button
        addUserButton = new JButton("Add User (Register)");

        // Add an action listener to the Add User button
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Add User dialog when the button is clicked
                AddUserDialog addUserDialog = new AddUserDialog(connection);
                addUserDialog.setVisible(true);
            }
        });


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(filterComboBox, BorderLayout.NORTH);
        panel.add(userListScrollPane, BorderLayout.CENTER);
        panel.add(addUserButton, BorderLayout.SOUTH);

        add(panel);
        updateUserList("All");

    }

    private void updateUserList(String filter) {
        userListModel.clear();
        String query = filter.equals("All")
                ? "SELECT U.Username, U.UserType, S.StudentName, P.PatronName, U.Email, S.Phone AS StudentPhone, P.Phone AS PatronPhone " +
                "FROM Users U " +
                "LEFT JOIN Students S ON U.UserID = S.UserID " +
                "LEFT JOIN Patrons P ON U.UserID = P.UserID"
                : "SELECT U.Username, U.UserType, S.StudentName, P.PatronName, U.Email, S.Phone AS StudentPhone, P.Phone AS PatronPhone " +
                "FROM Users U " +
                "LEFT JOIN Students S ON U.UserID = S.UserID " +
                "LEFT JOIN Patrons P ON U.UserID = P.UserID " +
                "WHERE U.UserType = ?";

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if (!filter.equals("All")) {
                preparedStatement.setString(1, filter);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("Username");
                String userType = resultSet.getString("UserType");
                String studentName = resultSet.getString("StudentName");
                String patronName = resultSet.getString("PatronName");
                String email = resultSet.getString("Email");
                String studentPhone = resultSet.getString("StudentPhone");
                String patronPhone = resultSet.getString("PatronPhone");

                String userInfo = "Username: " + username + " - UserType: " + userType;
                if (userType.equals("Student")) {
                    userInfo += " - Student Name: " + studentName + " - Email: " + email + " - Phone: " + studentPhone;
                } else if (userType.equals("Patron")) {
                    userInfo += " - Patron Name: " + patronName + " - Email: " + email + " - Phone: " + patronPhone;
                }

                userListModel.addElement(userInfo);
            }

            dbConnection.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}

