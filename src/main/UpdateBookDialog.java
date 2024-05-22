package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UpdateBookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField pageNumberField;
    private JCheckBox availableCheckBox;
    private JButton saveButton;
    private int bookID;
    private Connection connection;

    public UpdateBookDialog(Connection connection, final int bookID) {
        this.connection = connection;
        this.bookID = bookID;

        setTitle("Update Book");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);

        titleField = new JTextField(20);
        authorField = new JTextField(20);
        isbnField = new JTextField(20);
        pageNumberField = new JTextField(20);
        availableCheckBox = new JCheckBox("Available");
        saveButton = new JButton("Save Changes");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                int pageNumber = Integer.parseInt(pageNumberField.getText());
                boolean isAvailable = availableCheckBox.isSelected();

                // Update the book in the database
                updateBook(bookID, title, author, isbn, pageNumber, isAvailable);

                // Close the dialog
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Page Number:"));
        panel.add(pageNumberField);
        panel.add(new JLabel("Available:"));
        panel.add(availableCheckBox);
        panel.add(new JLabel("")); // Empty label for spacing
        panel.add(saveButton);

        getContentPane().add(panel);

        // Load book details to populate the fields
        loadBookDetails();
    }

    private void loadBookDetails() {
        String query = "SELECT Title, Author, ISBN, PageNumber, Available FROM Books WHERE BookID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                titleField.setText(resultSet.getString("Title"));
                authorField.setText(resultSet.getString("Author"));
                isbnField.setText(resultSet.getString("ISBN"));
                pageNumberField.setText(resultSet.getString("PageNumber"));
                availableCheckBox.setSelected(resultSet.getBoolean("Available"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateBook(int bookID, String title, String author, String isbn, int pageNumber, boolean available) {
        String query = "UPDATE Books SET Title = ?, Author = ?, ISBN = ?, PageNumber = ?, Available = ? WHERE BookID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, isbn);
            preparedStatement.setInt(4, pageNumber);
            preparedStatement.setBoolean(5, available);
            preparedStatement.setInt(6, bookID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

