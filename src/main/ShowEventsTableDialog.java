package main;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class ShowEventsTableDialog extends JDialog {
    private JTable eventTable;
    private DefaultTableModel tableModel;

    public ShowEventsTableDialog(final Connection connection) {
        setTitle("Event Information");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setModal(true);

        tableModel = new DefaultTableModel();
        eventTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(eventTable);

        tableModel.addColumn("Event ID");
        tableModel.addColumn("Event Name");
        tableModel.addColumn("Place");
        tableModel.addColumn("Event Date");

        loadEventTable(connection);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addEventButton = new JButton("Add Event");
        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEventDialog addEventDialog = new AddEventDialog(connection, ShowEventsTableDialog.this);
                addEventDialog.setVisible(true);
            }
        });

        buttonPanel.add(addEventButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadEventTable(Connection connection) {
        tableModel.setRowCount(0);

        try {
            String query = "SELECT EventID, EventName, Place, EventDate FROM Events";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int eventID = resultSet.getInt("EventID");
                String eventName = resultSet.getString("EventName");
                String place = resultSet.getString("Place");
                String eventDate = resultSet.getString("EventDate");

                tableModel.addRow(new Object[]{eventID, eventName, place, eventDate});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


