package main;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class LibraryTree extends JTree {
    public LibraryTree(final Connection connection) {
        // Create the root node for the JTree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Library");

        // Create the "Books" tree and add it as a child of the root
        DefaultMutableTreeNode booksTree = createBooksTree(connection);
        root.add(booksTree);

        // Create the "Authors" tree and add it as a child of the root
        DefaultMutableTreeNode authorsTree = createAuthorsTree(connection);
        root.add(authorsTree);

        // Set the root node for the JTree
        setModel(new DefaultTreeModel(root));

        // Add a tree selection listener to open BookDialog
        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
                if (selectedNode != null) {
                    // Check if it's a book node and open BookDialog
                    if (selectedNode.getParent() != null && selectedNode.getParent().toString().equals("Books")) {
                        String bookTitle = selectedNode.toString();
                        openBookDialog(connection, bookTitle);
                    }
                }
            }
        });
    }

    private void openBookDialog(Connection connection, String bookTitle) {
        // Create a BookDialog instance to display book information
        BookDialog bookDialog = new BookDialog(connection, bookTitle);
        bookDialog.setVisible(true);
    }


    private DefaultMutableTreeNode createBooksTree(Connection connection) {
        DefaultMutableTreeNode booksTree = new DefaultMutableTreeNode("Books");

        // Fetch book data from your database and add them as sub-trees under "Books"
        String query = "SELECT Title FROM Books";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                DefaultMutableTreeNode book = new DefaultMutableTreeNode(title);
                booksTree.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            booksTree.add(new DefaultMutableTreeNode("Database Error"));
        }

        return booksTree;
    }

    private DefaultMutableTreeNode createAuthorsTree(Connection connection) {
        DefaultMutableTreeNode authorsTree = new DefaultMutableTreeNode("Authors");

        // Fetch author data from your database and organize them as sub-trees under "Authors"
        String query = "SELECT Author, Title FROM Books";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            String currentAuthor = "";
            DefaultMutableTreeNode authorNode = null;
            while (resultSet.next()) {
                String author = resultSet.getString("Author");
                String title = resultSet.getString("Title");

                if (!author.equals(currentAuthor)) {
                    authorNode = new DefaultMutableTreeNode(author);
                    authorsTree.add(authorNode);
                    currentAuthor = author;
                }

                if (authorNode != null) {
                    DefaultMutableTreeNode bookNode = new DefaultMutableTreeNode(title);
                    authorNode.add(bookNode);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            authorsTree.add(new DefaultMutableTreeNode("Database Error"));
        }

        return authorsTree;
    }


}




