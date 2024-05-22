package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertBookDialog extends JDialog {
    private JTextField bookIDField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField pageNumberField;
    private JCheckBox availableCheckBox;
    private JButton addButton;

    public InsertBookDialog(final  Connection connection,final BooksInfoDialog parentDialog) {
        setTitle("Insert Book");
        setSize(400, 300);
        setLocationRelativeTo(parentDialog);
        setModal(true);

        bookIDField = new JTextField(20);
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        isbnField = new JTextField(20);
        pageNumberField = new JTextField(20);
        availableCheckBox = new JCheckBox("Available");
        addButton = new JButton("Add");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookID = Integer.parseInt(bookIDField.getText());
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                int pageNumber = Integer.parseInt(pageNumberField.getText());
                boolean isAvailable = availableCheckBox.isSelected();

                // Insert the book into the database
                insertBook(connection, bookID, title, author, isbn, pageNumber, isAvailable);

                // Close the dialog
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("BookID:"));
        panel.add(bookIDField);
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
        panel.add(addButton);

        getContentPane().add(panel);
    }

    private void insertBook(Connection connection, int bookID, String title, String author, String isbn, int pageNumber, boolean available) {
        String query = "INSERT INTO Books (BookID, Title, Author, ISBN, PageNumber, Available) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookID);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, author);
            preparedStatement.setString(4, isbn);
            preparedStatement.setInt(5, pageNumber);
            preparedStatement.setBoolean(6, available);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

