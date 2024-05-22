package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AddBookDialog extends JDialog {
    private JPanel bookListPanel;
    private JButton addButton;
    private JTextField searchField;

    public AddBookDialog(final Connection connection) {
        setTitle("Add Books");
        setSize(530, 530);
        setLocationRelativeTo(null);
        setModal(true);

        // Create a panel for the book list
        bookListPanel = new JPanel();
        bookListPanel.setLayout(new BoxLayout(bookListPanel, BoxLayout.Y_AXIS));

        // Fetch the list of available books from your database
        String query = "SELECT BookID, Title, Available FROM Books WHERE Available = true";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int bookID = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");

                JCheckBox checkBox = new JCheckBox(title);
                checkBox.setActionCommand(Integer.toString(bookID));
                bookListPanel.add(checkBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a button to add the selected books
        addButton = new JButton("Add Selected Books");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component[] components = bookListPanel.getComponents();

                for (Component component : components) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            int bookID = Integer.parseInt(checkBox.getActionCommand());

                            // Update the availability of the selected book in the database
                            updateBookAvailability(connection, bookID, false);

                            // You can add code to associate the book with the student here
                            // e.g., update another table that tracks the association
                        }
                    }
                }

                // Close the dialog
                dispose();
            }
        });

        // Create a search field
        searchField = new JTextField(20);

        // Add an action listener to the search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                filterBooks(searchTerm);
            }
        });

        // Create a panel for search components
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search Books:"));
        searchPanel.add(searchField);

        // Add the search panel to the dialog
        add(searchPanel, BorderLayout.NORTH);

        // Add the book list and add button to the dialog
        add(new JScrollPane(bookListPanel));
        add(addButton, BorderLayout.SOUTH);
    }

    private void filterBooks(String searchTerm) {
        // Filter the existing list based on the search term
        Component[] components = bookListPanel.getComponents();

        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                String title = checkBox.getText();
                if (title.toLowerCase().contains(searchTerm.toLowerCase())) {
                    checkBox.setVisible(true);
                } else {
                    checkBox.setVisible(false);
                }
            }
        }
    }

    private void updateBookAvailability(Connection connection, int bookID, boolean available) {
        // Update the availability of the book in the database
        String updateQuery = "UPDATE Books SET Available = ? WHERE BookID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1, available);
            preparedStatement.setInt(2, bookID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}







