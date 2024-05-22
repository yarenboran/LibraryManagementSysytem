package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;



public class StudentPage extends JFrame {

    private String loggedInUsername; // Store the logged-in username
    private LibraryTree libraryTree;
    private JTextField LIBRARYSTUDENTPAGETextField;

    public StudentPage(String username) {
        this.loggedInUsername = username; // Store the username

        setTitle("LIBRARY SYSTEM STUDENT PAGE");
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


        // Create a profile image icon and label (if you have a profile image)
        ImageIcon profileIcon = new ImageIcon("src/main/Halic_University_logo_(2022).svg.png");
        JLabel profileImageLabel = new JLabel(profileIcon);

        // Create menu items for "My Profile" and "Log Out"
        JMenuItem myProfileItem = new JMenuItem("My Profile");
        myProfileItem.setForeground(Color.BLUE);
        JMenuItem logOutItem = new JMenuItem("Log Out");
        logOutItem.setForeground(Color.BLUE);

        // Create a content panel for the main layout
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Ensure you have access to the DatabaseConnection instance
        final DatabaseConnection dbConnection = new DatabaseConnection();

        // Add the LibraryTree to the left part (West) of the content panel
        libraryTree = new LibraryTree(dbConnection.getConnection());
        JScrollPane treeScrollPane = new JScrollPane(libraryTree);
        treeScrollPane.setPreferredSize(new Dimension(300, 0)); // Adjust the width as needed
        contentPanel.add(treeScrollPane, BorderLayout.WEST);



        // Create a library access panel
        JPanel libraryAccessPanel = new JPanel(new GridLayout(3, 1));
        // Create a JLabel with the title text
        JLabel titleLabel = new JLabel("LIBRARY SYSTEM STUDENT PAGE");
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




        // Add action listeners for menu items
        myProfileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "My Profile" action

                // Ensure you have access to the DatabaseConnection instance
                DatabaseConnection dbConnection = new DatabaseConnection();

                // Fetch user data from the database
                String username = loggedInUsername; // Use the stored username
                System.out.println("Attempting to retrieve profile for user: " + username);

                String[] userData = new String[3];
                try {
                    Connection connection = dbConnection.getConnection();
                    System.out.println("Connected to the database."); // Add this debug statement

                    String query = "SELECT S.StudentName, S.Phone, U.Email " +
                            "FROM Students AS S " +
                            "JOIN Users AS U ON S.UserID = U.UserID " +
                            "WHERE U.Username = ?";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        userData[0] = resultSet.getString("StudentName");
                        userData[1] = resultSet.getString("Phone");
                        userData[2] = resultSet.getString("Email");
                    }



                    System.out.println(userData[0]+ userData[1]+ userData[2]);

                    dbConnection.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                if (userData[0] != null && userData[1] != null && userData[2] != null) {
                    String name = userData[0];
                    String phone = userData[1];
                    String email = userData[2];

                    String profileMessage = "Student Name: " + name + "\nPhone: " + phone + "\nEmail: " + email;
                    JOptionPane.showMessageDialog(StudentPage.this, profileMessage, "My Profile", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(StudentPage.this, "User data not found.", "My Profile", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logOutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Log Out" action
                int choice = JOptionPane.showConfirmDialog(StudentPage.this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Close the StudentPage window
                    dispose();
                    // Return to the login page
                    new LibraryLogin(new DatabaseConnection());
                }
            }
        });

        // Add menu items to the profile menu
        profileMenu.add(myProfileItem);


        // Create menu items for "Add Book" and "See Upcoming Events"
        JMenuItem addBookMenuItem = new JMenuItem("Add Book");
        addBookMenuItem.setForeground(Color.BLUE);
        JMenuItem seeEventsMenuItem = new JMenuItem("See Upcoming Events");
        seeEventsMenuItem.setForeground(Color.BLUE);

// Add action listeners to the menu items
        addBookMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = dbConnection.getConnection(); // Create a database connection
                AddBookDialog addBookDialog = new AddBookDialog(connection); // Pass the connection to the constructor
                addBookDialog.setVisible(true); // Show the dialog
            }
        });

        seeEventsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = dbConnection.getConnection(); // Create a database connection
                ShowEventsDialog eventsDialog = new ShowEventsDialog(connection);
                eventsDialog.setVisible(true);
            }
        });

// Add the menu items to the profile menu
        profileMenu.add(addBookMenuItem);
        profileMenu.add(seeEventsMenuItem);
        profileMenu.add(logOutItem);




        // Add the profile image and profile menu to the menu bar
        profileImageLabel.setHorizontalAlignment(JLabel.CENTER); // Center the profile image
        menuBar.add(profileImageLabel);
        menuBar.add(profileMenu);


        // Add the menu to the menu bar
        menuBar.add(profileMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        // Add the content panel to the frame
        getContentPane().add(contentPanel);




        setVisible(true);
    }
}









