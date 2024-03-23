package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

public class AdminLogin extends JFrame {
    private AdminManager adminManager;

    public AdminLogin(RestaurantDAO restaurantDAO) {
        super("Admin Dashboard");
        
        this.adminManager = new AdminManager(restaurantDAO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        // Button for managing users
        JButton manageUsersButton = new JButton("Manage Users");
        manageUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the manage users interface
                // Implement this based on your requirements
                new ManageUsers(restaurantDAO);
                dispose();
                //JOptionPane.showMessageDialog(AdminLogin.this, "Manage Users Button Clicked");
            }
        });
        panel.add(manageUsersButton);

        // Button for managing the menu
        JButton manageMenuButton = new JButton("Manage Menu");
        manageMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the manage menu interface
                // Implement this based on your requirements
                new ManageMenu(restaurantDAO);
                //JOptionPane.showMessageDialog(AdminLogin.this, "Manage Menu Button Clicked");
            }
        });
        panel.add(manageMenuButton);

        // Button for viewing orders between dates
        JButton viewOrdersButton = new JButton("View Orders Between Dates");
        viewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the view orders interface
                // Implement this based on your requirements
                new ViewOrders(restaurantDAO);
                //JOptionPane.showMessageDialog(AdminLogin.this, "View Orders Between Dates Button Clicked");
            }
        });
        panel.add(viewOrdersButton);

        // Button for viewing most ordered items
        JButton viewMostOrderedButton = new JButton("View Most Ordered Items");
        viewMostOrderedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call the viewMostOrderedItems method
                Map<String, Integer> mostOrderedItems = adminManager.viewMostOrderedItems();

                // Display the most ordered items using a dialog
                StringBuilder message = new StringBuilder("Most Ordered Items:\n");
                for (Map.Entry<String, Integer> entry : mostOrderedItems.entrySet()) {
                    message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                JOptionPane.showMessageDialog(AdminLogin.this, message.toString());
            }
        });
        panel.add(viewMostOrderedButton);


        add(panel);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

/*    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RestaurantDAO restaurantDAO = null;
                try {
                    restaurantDAO = new RestaurantDAO("jdbc:mysql://localhost:3306/rst", "root", "Parola");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                new AdminLogin(restaurantDAO);
            }
        });
    }*/

}
