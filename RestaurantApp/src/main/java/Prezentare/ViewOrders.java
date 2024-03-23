package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ViewOrders extends JFrame {
    private JTextField startDateField, endDateField;
    private AdminManager adminManager;

    public ViewOrders(RestaurantDAO restaurantDAO) {
        super("View Orders Between Dates");
        this.adminManager = new AdminManager(restaurantDAO);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2)); // 3 rows, 2 columns for layout

        JLabel startDateLabel = new JLabel("Start Date:");
        startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("End Date:");
        endDateField = new JTextField();

        panel.add(startDateLabel);
        panel.add(startDateField);
        panel.add(endDateLabel);
        panel.add(endDateField);

        JButton viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                viewOrdersBetweenDates(startDate, endDate);
            }
        });

        panel.add(new JLabel()); // Empty label for spacing
        panel.add(viewOrdersButton);

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void viewOrdersBetweenDates(String startDate, String endDate) {
        try {
            adminManager.viewOrdersBetweenDates(startDate, endDate);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ViewOrders.this, "Error: Unable to fetch orders between dates.");
        }
    }

}
