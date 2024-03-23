package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteUser extends JFrame {
    private JTextField usernameField;
    private AdminManager adminManager;

    public DeleteUser(RestaurantDAO restaurantDAO) {
        super("Delete User");
        AdminManager adminManager = new AdminManager(restaurantDAO);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);

        JPanel panel = new JPanel(new GridLayout(2, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (!username.isEmpty()) {
                    int choice = JOptionPane.showConfirmDialog(DeleteUser.this, "Are you sure you want to delete this user?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        adminManager.deleteUser(username);
                        JOptionPane.showMessageDialog(DeleteUser.this, "User deleted successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(DeleteUser.this, "Please enter a username", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(deleteButton);

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
}
