package Aplicatie;

import Date.RestaurantDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthManager {
    private RestaurantDAO restaurantDAO;

    public AuthManager(RestaurantDAO restaurantDAO) {
        this.restaurantDAO = restaurantDAO;
    }

    public boolean auth(String username, String password) {
        try {
            ResultSet resultSet = restaurantDAO.getAngajati();
            while (resultSet.next()) {
                String dbUsername = resultSet.getString("username");
                String dbPassword = resultSet.getString("parola");
                if (username.equals(dbUsername) && BCrypt.checkpw(password, dbPassword)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
