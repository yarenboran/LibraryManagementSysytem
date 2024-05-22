package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;





public class BooksInfoDialog extends JDialog {
    private final JFrame parentFrame;
    private JTable bookTable;
    private JScrollPane tableScrollPane;
    private JRadioButton availableRadioButton;
    private JRadioButton nonAvailableRadioButton;
    private JButton insertBookButton;
    private JButton deleteBookButton;
    private Connection connection;


    public BooksInfoDialog(JFrame parent, final Connection connection) {
        super(parent, "Books Information", true);
        this.connection = connection;
        this.parentFrame = parent; // Store the parent frame
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Create a JTable to display book information
        bookTable = new JTable();
        tableScrollPane = new JScrollPane(bookTable);

        bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = bookTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Retrieve book information from the selected row
                        Object bookIDObject = bookTable.getValueAt(selectedRow, 0);

                        if (bookIDObject instanceof Integer) {
                            int bookID = (Integer) bookIDObject;
                            // Now you can use bookID as an int
                            openUpdateBookDialog(bookID);
                        } else {
                            // Handle the case where the value is not an Integer
                            // You can display an error message or take appropriate action.
                        }
                    }
                }
            }
        });







        // Create RadioButtons for filtering
        availableRadioButton = new JRadioButton("Available");
        nonAvailableRadioButton = new JRadioButton("Non-Available");

        // Group the RadioButtons together to ensure only one is selected at a time
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(availableRadioButton);
        radioButtonGroup.add(nonAvailableRadioButton);

        // Create a panel for the RadioButtons
        JPanel radioPanel = new JPanel();
        radioPanel.add(availableRadioButton);
        radioPanel.add(nonAvailableRadioButton);

        // Add an ActionListener to the RadioButtons to filter the table
        availableRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable(true);
            }
        });

        nonAvailableRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable(false);
            }
        });

        // Create a panel to hold the table and RadioButtons
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.add(radioPanel, BorderLayout.NORTH);

        // Create Insert Book and Delete Book buttons
        insertBookButton = new JButton("Insert Book");
        deleteBookButton = new JButton("Delete Book");

        // Add action listeners for the buttons
        insertBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsertBookDialog insertBookDialog = new InsertBookDialog(connection, BooksInfoDialog.this);
                if(connection == null){
                    System.out.println("connection is nulll");
                }
                insertBookDialog.setVisible(true);
                loadBookData();

            }
        });

        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteBookDialog deleteBookDialog = new DeleteBookDialog(connection, BooksInfoDialog.this);
                if(connection == null){
                    System.out.println("connection is nulll");
                }
                deleteBookDialog.setVisible(true);

                // After deleting the book from the database, reload the book data in the JTable
                loadBookData();
            }
        });

        // Create a panel for the Insert Book and Delete Book buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(insertBookButton);
        buttonPanel.add(deleteBookButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the table and RadioButtons panel to the dialog's content pane
        getContentPane().add(contentPanel);

        // Load all book data into the table by default
        loadBookData();
    }

    private void loadBookData() {
        String query = "SELECT * FROM Books";

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a table model with column names
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.setColumnIdentifiers(new Object[]{"BookID", "Title", "Author", "ISBN", "PageNumber", "Available"});

            // Populate the table model with book data
            while (resultSet.next()) {
                int bookID = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String isbn = resultSet.getString("ISBN");
                int pageNumber = resultSet.getInt("PageNumber");
                boolean available = resultSet.getBoolean("Available");

                tableModel.addRow(new Object[]{bookID, title, author, isbn, pageNumber, available});
            }

            // Set the table model for the JTable
            bookTable.setModel(tableModel);

            dbConnection.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private void filterTable(boolean available) {
        DefaultTableModel tableModel = (DefaultTableModel) bookTable.getModel();
        tableModel.setRowCount(0); // Clear the table

        String query = "SELECT * FROM Books WHERE Available = ?";

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, available);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Populate the table model with filtered book data
            while (resultSet.next()) {
                int bookID = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String isbn = resultSet.getString("ISBN");
                int pageNumber = resultSet.getInt("PageNumber");
                boolean bookAvailable = resultSet.getBoolean("Available");

                tableModel.addRow(new Object[]{bookID, title, author, isbn, pageNumber, bookAvailable});
            }

            dbConnection.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Create a method to open the "Update Book" dialog
    private void openUpdateBookDialog(int bookID) {
        // Create and open the "Update Book" dialog
        UpdateBookDialog updateBookDialog = new UpdateBookDialog(connection, bookID);
        updateBookDialog.setVisible(true);
    }

}


