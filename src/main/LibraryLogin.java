package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private DatabaseConnection dbConnection;
    private String loggedInUsername;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button1;

    public LibraryLogin(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;

        setTitle("Library Management Login");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(240, 240, 240));

        // Create a panel for the title and logo at the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Add the title label with spacing
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLUE);
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Load your logo image (replace with the actual path)
        ImageIcon logoImage = new ImageIcon("src/main/Halic_University_logo_(2022).svg (1).png");
        JLabel logoLabel = new JLabel(logoImage);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(logoLabel, BorderLayout.CENTER);

        contentPane.add(topPanel, BorderLayout.NORTH);

        final JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.setBackground(new Color(255, 255, 255));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));

        // Create an empty panel to push the login button to the bottom center
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setPreferredSize(new Dimension(0, 40));

        loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(200, 40));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticate(username, password)) {
                    loggedInUsername = username;
                    authenticateAndOpenPage(username, password);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(loginPanel, "Login failed. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        // Add the empty panel to center the login button at the bottom
        loginPanel.add(emptyPanel);
        loginPanel.add(loginButton);

        contentPane.add(loginPanel, BorderLayout.CENTER);

        setContentPane(contentPane);
        setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        return dbConnection.authenticateUserForTable("Users", "Username", "Password", username, password);
    }

    private void authenticateAndOpenPage(String username, String password) {
        System.out.println("Authenticating user: " + username);

        try {
            String userType = dbConnection.getUserType(username, password);
            System.out.println("User type: " + userType);

            if ("student".equalsIgnoreCase(userType)) {
                System.out.println("Opening student page...");
                openStudentPage(username);
            } else if ("patron".equalsIgnoreCase(userType)) {
                System.out.println("Opening patron page...");
                openPatronPage(username);
            } else {
                System.out.println("Unknown user type: " + userType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private void openStudentPage(String loggedInUsername) {
        StudentPage studentPage = new StudentPage(loggedInUsername);
        studentPage.setVisible(true);
        System.out.println("Student page is opening...");
    }

    private void openPatronPage(String loggedInUsername) {
        PatronPage patronPage = new PatronPage(loggedInUsername);
        patronPage.setVisible(true);
    }
}






