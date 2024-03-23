package Prezentare;

import Aplicatie.AdminManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ManageUsers extends JFrame {
    private AdminManager adminManager;

    public ManageUsers(RestaurantDAO restaurantDAO) {
        super("Manage Users");

        AdminManager adminManager = new AdminManager(restaurantDAO);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton createUserButton = new JButton("Create User");
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a window for creating a new user
                new CreateUsers(restaurantDAO);
            }
        });
        panel.add(createUserButton);

        JButton editUserButton = new JButton("Edit User");
        editUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a window for editing an existing user
                new EditUsers(restaurantDAO);
            }
        });
        panel.add(editUserButton);

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a window for deleting an existing user
                new DeleteUser(restaurantDAO);
            }
        });
        panel.add(deleteUserButton);

        add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

/*    public static void main(String[] args) {

        RestaurantDAO restaurantDAO = null;
        try {
            restaurantDAO = new RestaurantDAO("jdbc:mysql://localhost:3306/rst", "root", "Parola");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new ManageUsers(restaurantDAO);
    }*/
}
