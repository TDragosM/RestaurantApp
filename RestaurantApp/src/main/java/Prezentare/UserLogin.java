package Prezentare;

import Aplicatie.OrderManager;
import Date.RestaurantDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserLogin extends JFrame {
    private OrderManager orderManager;

    public UserLogin(RestaurantDAO restaurantDAO) {
        super("User Dashboard");
        this.orderManager = new OrderManager(restaurantDAO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        // Button for creating order
        JButton createOrderButton = new JButton("Create Order");
        createOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = orderManager.createOrder();
                JOptionPane.showMessageDialog(UserLogin.this, "Order with id:"+ id  + " created successfully!");
            }
        });
        panel.add(createOrderButton);

        // Button for updating order status
        JButton updateStatusButton = new JButton("Update Order Status");
        updateStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(UserLogin.this, "Enter Order ID:");
                if (input != null) {
                    int orderId = Integer.parseInt(input);
                    String newStatus = JOptionPane.showInputDialog(UserLogin.this, "Enter New Status:");
                    if (newStatus != null) {
                        orderManager.updateOrderStatus(orderId, newStatus);
                        JOptionPane.showMessageDialog(UserLogin.this, "Order status updated successfully!");
                    }
                }
            }
        });
        panel.add(updateStatusButton);

        // Button for adding items to order
        JButton addItemsButton = new JButton("Add Items to Order");
        addItemsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String orderIdInput = JOptionPane.showInputDialog(UserLogin.this, "Enter Order ID:");
                if (orderIdInput != null) {
                    int orderId = Integer.parseInt(orderIdInput);
                    String itemName = JOptionPane.showInputDialog(UserLogin.this, "Enter Item Name:");
                    if (itemName != null) {
                        String quantityInput = JOptionPane.showInputDialog(UserLogin.this, "Enter Quantity:");
                        if (quantityInput != null) {
                            int quantity = Integer.parseInt(quantityInput);
                            int success = orderManager.addItemsToOrder(orderId, itemName, quantity);
                            if(success==0) JOptionPane.showMessageDialog(UserLogin.this, "Items added to order successfully!");
                            else JOptionPane.showMessageDialog(UserLogin.this, "Items NOT added to order!");
                        }
                    }
                }
            }
        });
        panel.add(addItemsButton);

        // Button for deleting order
        JButton deleteOrderButton = new JButton("Delete Order");
        deleteOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(UserLogin.this, "Enter Order ID to Delete:");
                if (input != null) {
                    int orderId = Integer.parseInt(input);
                    orderManager.deleteOrder(orderId);
                    JOptionPane.showMessageDialog(UserLogin.this, "Order deleted successfully!");
                }
            }
        });
        panel.add(deleteOrderButton);

        // Button for displaying order items
        JButton displayOrderItemsButton = new JButton("Display Order Items");
        displayOrderItemsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(UserLogin.this, "Enter Order ID to Display Items:");
                if (input != null) {
                    int orderId = Integer.parseInt(input);
                    String rez = orderManager.displayOrderItems(orderId);
                    JOptionPane.showMessageDialog(UserLogin.this, rez);
                }
            }
        });
        panel.add(displayOrderItemsButton);

        // Button for displaying all orders
        JButton displayAllOrdersButton = new JButton("Display All Orders");
        displayAllOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String rezultat = orderManager.displayOrders();
                JOptionPane.showMessageDialog(UserLogin.this, rezultat);
            }
        });
        panel.add(displayAllOrdersButton);

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
                new UserLogin(restaurantDAO);
            }
        });
    }*/
}
