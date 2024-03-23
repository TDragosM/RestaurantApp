package Aplicatie;

import java.sql.*;

import Date.RestaurantDAO;

public class OrderManager {
    private RestaurantDAO restaurantDAO;

    public OrderManager(RestaurantDAO restaurantDAO) {
        this.restaurantDAO = restaurantDAO;
    }

    public int createOrder() {
        try {
            int id = restaurantDAO.createComanda();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        try {
            restaurantDAO.updateStatusComanda(orderId, newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addItemsToOrder(int orderId, String itemName, int quantity) {
        try {
            int success = restaurantDAO.adaugaPreparatLaComanda(orderId, itemName, quantity);
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void deleteOrder(int orderId) {
        restaurantDAO.deleteComanda(orderId);
    }

    public String displayOrderItems(int orderId) {
        System.out.println("Preparatele din comanda cu ID-ul " + orderId + ":");
        String rez = restaurantDAO.afiseazaPreparateComanda(orderId);
        return rez;
    }

    public String displayOrders() {
        StringBuilder ordersString = new StringBuilder();
        try {
            ordersString.append("Lista comenzilor:\n");
            ResultSet ordersResultSet = restaurantDAO.getComenzi();
            while (ordersResultSet.next()) {
                int orderId = ordersResultSet.getInt("id");
                String dateTime = ordersResultSet.getTimestamp("data_ora").toString();
                String status = ordersResultSet.getString("status");
                double totalCost = ordersResultSet.getDouble("cost_total");
                ordersString.append("ID: ").append(orderId)
                        .append(", Data și Ora: ").append(dateTime)
                        .append(", Status: ").append(status)
                        .append(", Cost Total: ").append(totalCost).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersString.toString();
    }


}

