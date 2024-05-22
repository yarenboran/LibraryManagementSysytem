package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronPage extends JFrame {
    private String loggedInUsername;
    private Connection connection; // Define a Connection variable
    private JTextField textField1;

    public PatronPage(String username){
        this.loggedInUsername = username;

        try {
            // Initialize the database connection here
            DatabaseConnection dbConnection = new DatabaseConnection();
            connection = dbConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("PatronPage constructor called.");
        setTitle("Patron Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a profile menu
        final JMenu profileMenu = new JMenu("MENU");
        Font menuFont = new Font("Arial", Font.BOLD, 16);
        profileMenu.setFont(menuFont);

// Set the foreground (text) color to blue
        profileMenu.setForeground(Color.BLUE);

        // Create a profile image icon and label
        ImageIcon profileIcon = new ImageIcon("src/main/Halic_University_logo_(2022).svg.png"); // Replace with your profile image file
        JLabel profileImageLabel = new JLabel(profileIcon);

        // Create a content panel for the main layout
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Create a library access panel
        JPanel libraryAccessPanel = new JPanel(new GridLayout(3, 1));
        // Create a JLabel with the title text
        JLabel titleLabel = new JLabel("LIBRARY SYSTEM PATRON PAGE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set the font for the title
        titleLabel.setHorizontalAlignment(JLabel.CENTER); // Center-align the text
        // Set the text color to blue
        titleLabel.setForeground(Color.BLUE);
        // Add an empty border to push the text downward
        titleLabel.setBorder(BorderFactory.createEmptyBorder(130, 0, 0, 0)); // Adjust the top padding (20) as needed

// Add the title label to the library access panel
        libraryAccessPanel.add(titleLabel);




        // Add components to the content panel
        contentPanel.add(libraryAccessPanel);

        // Create menu items for "My Profile" and "Log Out"
        JMenuItem myProfileItem = new JMenuItem("My Profile");
        JMenuItem logOutItem = new JMenuItem("Log Out");;
        JMenuItem showUserInfoItem = new JMenuItem("Users Information");
        JMenuItem showBooksInfoItem = new JMenuItem("Books Access");
        JMenuItem eventAccessItem = new JMenuItem("Event Access");
        myProfileItem.setForeground(Color.BLUE);
        logOutItem.setForeground(Color.BLUE);
        showBooksInfoItem.setForeground(Color.BLUE);
        eventAccessItem.setForeground(Color.BLUE);
        showUserInfoItem.setForeground(Color.BLUE);



        // Add action listeners for menu items
        myProfileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "My Profile" action

                // Ensure you have access to the DatabaseConnection instance
                DatabaseConnection dbConnection = new DatabaseConnection();

                // Fetch user data from the database
                String username = loggedInUsername; // Use the stored username

                String[] userData = new String[3];
                try {
                    Connection connection = dbConnection.getConnection();
                    String query = "SELECT P.PatronName, P.Phone, U.Email " +
                            "FROM Patrons AS P " +
                            "JOIN Users AS U ON P.UserID = U.UserID " +
                            "WHERE U.Username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        userData[0] = resultSet.getString("PatronName");
                        userData[1] = resultSet.getString("Phone");
                        userData[2] = resultSet.getString("Email");
                    }

                    dbConnection.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                if (userData[0] != null && userData[1] != null && userData[2] != null) {
                    String name = userData[0];
                    String phone = userData[1];
                    String email = userData[2];
                    String profileMessage = "Patron Name: " + name + "\nPhone: " + phone + "\nEmail: " + email;
                    JOptionPane.showMessageDialog(PatronPage.this, profileMessage, "My Profile", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(PatronPage.this, "User data not found.", "My Profile", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        showUserInfoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsersInfoDialog usersInfoDialog = new UsersInfoDialog(PatronPage.this, connection);
                usersInfoDialog.setVisible(true);
            }
        });

        showBooksInfoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BooksInfoDialog booksInfoDialog = new BooksInfoDialog(PatronPage.this, connection);
                booksInfoDialog.setVisible(true);
            }
        });

        eventAccessItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowEventsTableDialog eventsTableDialog = new ShowEventsTableDialog(connection);
                eventsTableDialog.setVisible(true);
            }
        });

        logOutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Log Out" action
                int choice = JOptionPane.showConfirmDialog(PatronPage.this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Close the Patron Page window
                    dispose();
                    // Return to the login page
                    new LibraryLogin(new DatabaseConnection());
                }
            }
        });

        // Add menu items to the profile menu
        profileMenu.add(myProfileItem);
        profileMenu.add(showUserInfoItem);
        profileMenu.add(showBooksInfoItem);
        profileMenu.add(eventAccessItem);
        profileMenu.add(logOutItem);

        // Add the profile image and profile menu to the menu bar
        profileImageLabel.setHorizontalAlignment(JLabel.CENTER); // Center the profile image
        menuBar.add(profileImageLabel);
        menuBar.add(profileMenu);

        // Add a mouse listener to the profile image to open the profile menu
        profileImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Check for a left mouse button click
                    profileMenu.doClick(0); // Simulate a click on the profile menu
                }
            }
        });

        // Set the menu bar for the frame
        setJMenuBar(menuBar);


        // Add the content panel to the frame
        getContentPane().add(contentPanel);

        setVisible(true);
    }
}
