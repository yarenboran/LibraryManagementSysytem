package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBookDialog extends JDialog {
    private JTextField bookIDField;
    private JButton deleteButton;

    public DeleteBookDialog(final Connection connection, final BooksInfoDialog parentDialog) {
        setTitle("Delete Book");
        setSize(300, 150);
        setLocationRelativeTo(parentDialog);
        setModal(true);

        bookIDField = new JTextField(20);
        deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookID = Integer.parseInt(bookIDField.getText());

                // Delete the book from the database
                deleteBook(connection, bookID);

                // Close the dialog
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("BookID:"));
        panel.add(bookIDField);
        panel.add(new JLabel("")); // Empty label for spacing
        panel.add(deleteButton);

        getContentPane().add(panel);
    }

    private void deleteBook(Connection connection, int bookID) {
        String query = "DELETE FROM Books WHERE BookID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


