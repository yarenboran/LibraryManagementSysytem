package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddEventDialog extends JDialog {
    private JTextField eventNameField;
    private JTextField placeField;
    private JTextField dateField;

    public AddEventDialog(final Connection connection, final ShowEventsTableDialog parentDialog) {
        setTitle("Add Event");
        setSize(300, 200);
        setLocationRelativeTo(parentDialog);
        setModal(true);

        eventNameField = new JTextField(20);
        placeField = new JTextField(20);
        dateField = new JTextField(20);

        JButton addButton = new JButton("Add Event");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String eventName = eventNameField.getText();
                String place = placeField.getText();
                String date = dateField.getText();

                // Insert the new event into the database
                insertEvent(connection, eventName, place, date);

                // Refresh the event table in the parent dialog
                parentDialog.loadEventTable(connection);

                // Close the dialog
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Event Name:"));
        panel.add(eventNameField);
        panel.add(new JLabel("Place:"));
        panel.add(placeField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        getContentPane().add(panel);
    }

    private void insertEvent(Connection connection, String eventName, String place, String date) {
        String query = "INSERT INTO Events (EventName, Place, EventDate) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, eventName);
            preparedStatement.setString(2, place);
            preparedStatement.setString(3, date);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

