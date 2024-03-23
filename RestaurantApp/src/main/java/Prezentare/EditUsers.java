package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EditUsers extends JFrame {
    private AdminManager adminManager;

    public EditUsers(RestaurantDAO restaurantDAO) {
        super("Edit User");
        AdminManager adminManager = new AdminManager(restaurantDAO);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel newPasswordLabel = new JLabel("New Password:");
        JTextField newPasswordField = new JTextField();

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String newPassword = newPasswordField.getText();
                adminManager.editUser(username, newPassword);
                JOptionPane.showMessageDialog(EditUsers.this, "User edited successfully!");
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(newPasswordLabel);
        panel.add(newPasswordField);
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(editButton);

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

//    public static void main(String[] args) {
//        // Example usage
//        // You need to provide an instance of AdminManager here
//        RestaurantDAO restaurantDAO = null;
//        try {
//            restaurantDAO = new RestaurantDAO("jdbc:mysql://localhost:3306/rst", "root", "Parola");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        new EditUsers(restaurantDAO);
//    }
}
