package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ManageMenu extends JFrame {
    private JTextField nameField, priceField, stockField;
    private AdminManager adminManager;

    public ManageMenu(RestaurantDAO restaurantDAO) {
        super("Manage Menu");
        adminManager = new AdminManager(restaurantDAO);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250); // Increased height to accommodate the new button

        JPanel panel = new JPanel(new GridLayout(8, 2)); // Increased rows for better layout

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();
        JLabel stockLabel = new JLabel("Stock:");
        stockField = new JTextField();

        // Add empty labels for spacing
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(stockLabel);
        panel.add(stockField);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton showAllButton = new JButton("Show All Items");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                adminManager.addMenuItem(name, price, stock);
                JOptionPane.showMessageDialog(ManageMenu.this, "Menu item added successfully!");
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newStock = Integer.parseInt(stockField.getText());
                adminManager.editMenuItem(name, newPrice, newStock);
                JOptionPane.showMessageDialog(ManageMenu.this, "Menu item edited successfully!");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                adminManager.deleteMenuItem(name);
                JOptionPane.showMessageDialog(ManageMenu.this, "Menu item deleted successfully!");
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fetch all items from the menu
                Map<String, Integer> menuItems = adminManager.getAllMenuItems();

                // Display all menu items using a dialog
                StringBuilder message = new StringBuilder("All Menu Items:\n");
                for (Map.Entry<String, Integer> entry : menuItems.entrySet()) {
                    message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                JOptionPane.showMessageDialog(ManageMenu.this, message.toString());
            }
        });

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(showAllButton); // Add the new button

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
}
