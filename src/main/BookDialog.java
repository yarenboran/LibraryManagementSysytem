package main;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDialog extends JDialog {
    public BookDialog(Connection connection, String bookTitle) {
        // Set up the dialog properties, such as title, size, etc.

        // Create components for book details
        JLabel titleLabel = new JLabel("Title:");
        JLabel authorLabel = new JLabel("Author:");
        JLabel isbnLabel = new JLabel("ISBN:");
        JLabel pageNumberLabel = new JLabel("Page Number:");

        // Create labels to display book details
        JLabel titleValueLabel = new JLabel();
        JLabel authorValueLabel = new JLabel();
        JLabel isbnValueLabel = new JLabel();
        JLabel pageNumberValueLabel = new JLabel();

        // Fetch book details from the database using the provided bookTitle
        String query = "SELECT Author, ISBN, PageNumber FROM Books WHERE Title = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bookTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String author = resultSet.getString("Author");
                String isbn = resultSet.getString("ISBN");
                int pageNumber = resultSet.getInt("PageNumber");

                titleValueLabel.setText(bookTitle);
                authorValueLabel.setText(author);
                isbnValueLabel.setText(isbn);
                pageNumberValueLabel.setText(String.valueOf(pageNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database error
        }

        // Set the layout manager to GridBagLayout
        setLayout(new GridBagLayout());

        // Create constraints for labels and values
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(5, 5, 5, 5); // Add padding here

        GridBagConstraints valueConstraints = new GridBagConstraints();
        valueConstraints.anchor = GridBagConstraints.WEST;

        // Add components with their constraints
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        add(titleLabel, labelConstraints);
        valueConstraints.gridx = 1;
        valueConstraints.gridy = 0;
        add(titleValueLabel, valueConstraints);

        labelConstraints.gridx = 0;
        labelConstraints.gridy = 1;
        add(authorLabel, labelConstraints);
        valueConstraints.gridx = 1;
        valueConstraints.gridy = 1;
        add(authorValueLabel, valueConstraints);

        labelConstraints.gridx = 0;
        labelConstraints.gridy = 2;
        add(isbnLabel, labelConstraints);
        valueConstraints.gridx = 1;
        valueConstraints.gridy = 2;
        add(isbnValueLabel, valueConstraints);

        labelConstraints.gridx = 0;
        labelConstraints.gridy = 3;
        add(pageNumberLabel, labelConstraints);
        valueConstraints.gridx = 1;
        valueConstraints.gridy = 3;
        add(pageNumberValueLabel, valueConstraints);

        // Set other properties of the dialog

        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}


