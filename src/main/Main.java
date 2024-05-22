package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DatabaseConnection dbConnection = new DatabaseConnection();
                LibraryLogin loginPage = new LibraryLogin(dbConnection);
                loginPage.setVisible(true);

            }
        });
    }
}



