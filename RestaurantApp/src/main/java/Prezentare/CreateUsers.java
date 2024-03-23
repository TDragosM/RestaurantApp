package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreateUsers extends JFrame {
    private AdminManager adminManager;

    private JTextField nameField;
    private JTextField usernameField;
    private JTextField passwordField;

    public CreateUsers(RestaurantDAO restaurantDAO) {
        super("Manage Users");

        AdminManager adminManager = new AdminManager(restaurantDAO);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField();

        JButton createUserButton = new JButton("Create User");
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                adminManager.createUser(name, username, password);
                JOptionPane.showMessageDialog(CreateUsers.this, "User created successfully!");
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(createUserButton);

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

//    public static void main(String[] args) {
//        // Example usage
//        RestaurantDAO restaurantDAO = null;
//        try {
//            restaurantDAO = new RestaurantDAO("jdbc:mysql://localhost:3306/rst", "root", "Parola");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        new CreateUsers(restaurantDAO);
//    }
}
