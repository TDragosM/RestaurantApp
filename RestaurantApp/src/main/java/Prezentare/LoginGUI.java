package Prezentare;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import Aplicatie.AuthManager;
import Date.RestaurantDAO;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthManager authManager;
    private JCheckBox adminCheckBox;

    public LoginGUI(RestaurantDAO restaurantDAO) {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 180); // Increased height to accommodate the new checkbox

        // Create an instance of AuthManager with the provided RestaurantDAO
        authManager = new AuthManager(restaurantDAO);

        JPanel panel = new JPanel(new GridLayout(4, 2)); // Increased rows to accommodate the new checkbox

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        adminCheckBox = new JCheckBox("Admin");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Call auth method on the AuthManager instance
                boolean loggedIn = authManager.auth(username, password);
                boolean isAdmin = adminCheckBox.isSelected(); // Check if the checkbox is selected

                if (loggedIn) {
                    if (isAdmin) {
                        JOptionPane.showMessageDialog(LoginGUI.this, "Admin Login Successful!");
                        // Perform actions specific to admin login <3
                        new AdminLogin(restaurantDAO);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginGUI.this, "User Login Successful!");
                        new UserLogin(restaurantDAO);
                        // Perform actions specific to user login
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Add components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(adminCheckBox); // Add the checkbox
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(loginButton);

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create an instance of RestaurantDAO and pass it to LoginGUI <33333333
                RestaurantDAO restaurantDAO = null;
                try {
                    restaurantDAO = new RestaurantDAO("jdbc:mysql://localhost:3306/rst", "root", "Parola");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                new LoginGUI(restaurantDAO);
            }
        });
    }
}
