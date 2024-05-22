package main;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ShowEventsDialog extends JDialog {
    private JList  eventList;

    public ShowEventsDialog(Connection connection) {
        setTitle("Upcoming Events");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);

        eventList = new JList();
        JScrollPane scrollPane = new JScrollPane(eventList);

        // Fetch upcoming events from the database and populate the JList
        DefaultListModel  listModel = new DefaultListModel();
        eventList.setModel(listModel);

        try {
            String query = "SELECT EventName, Place, EventDate FROM Events WHERE EventDate >= CURDATE() ORDER BY EventDate";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String eventName = resultSet.getString("EventName");
                String place = resultSet.getString("Place");
                String eventDate = resultSet.getString("EventDate");
                listModel.addElement(eventName + " - " + place + " - " + eventDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(panel);
    }
}

