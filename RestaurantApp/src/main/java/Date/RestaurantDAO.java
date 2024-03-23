package Date;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RestaurantDAO {
    private Connection connection;

    public RestaurantDAO(String jdbcUrl, String username, String password) throws SQLException {
        this.connection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void createAngajatiTable() throws SQLException {
        String query = "CREATE TABLE angajati ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nume VARCHAR(255),"
                + "username VARCHAR(255),"
                + "parola VARCHAR(255)"
                + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
    public void adaugaAngajat(String nume, String username, String parola) throws SQLException {
        if (!doesTableExist("angajati")) {
            createAngajatiTable();
        }

        if (!doesAngajatExist(username)) {
            String parolaCriptata = BCrypt.hashpw(parola, BCrypt.gensalt());

            String query = "INSERT INTO angajati (nume, username, parola) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nume);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, parolaCriptata);

                preparedStatement.executeUpdate();
            }
        } else {
            System.out.println("Angajatul cu username-ul '" + username + "' există deja.");
        }
    }

    private boolean doesAngajatExist(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM angajati WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

    public void createTableMeniu(String tableName) throws SQLException {
        String query = "CREATE TABLE " + tableName + " ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nume VARCHAR(255),"
                + "pret DOUBLE,"
                + "stoc INT"
                + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    public void adaugaPreparat(String nume, double pret, int stoc, String tableName) throws SQLException {

        if (!doesTableExist(tableName)) {
            createTableMeniu(tableName);
        }

        if (!doesPreparatExist(nume, tableName)) {
            String query = "INSERT INTO " + tableName + " (nume, pret, stoc) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nume);
                preparedStatement.setDouble(2, pret);
                preparedStatement.setInt(3, stoc);

                preparedStatement.executeUpdate();
            }
        } else {
            updatePreparat(nume,pret,stoc,tableName);
            System.out.println("Preparatul cu numele '" + nume + "' există deja în meniu. I-am facut update");
        }
    }

    private boolean doesTableExist(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next();
    }

    private boolean doesPreparatExist(String nume, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE nume = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nume);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

    public ResultSet getMeniu() throws SQLException {
        String query = "SELECT * FROM meniu";
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public ResultSet getAngajati() throws SQLException {
        String query = "SELECT * FROM angajati";
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }


    public void updatePreparat(String nume, double pret, int stoc, String tableName ) throws SQLException {
        if (doesPreparatExist(nume, tableName)) {
            String query = "UPDATE " + tableName + " SET pret = ?, stoc = ? WHERE nume = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, pret);
                preparedStatement.setInt(2, stoc);
                preparedStatement.setString(3, nume);

                preparedStatement.executeUpdate();
            }
        } else {
            System.out.println("Preparatul cu numele '" + nume + "' nu există în meniu.");
        }
    }

    public void deletePreparat(String nume, String tableName) throws SQLException {
        updatePreparat(nume,getPretPreparat(getIdPreparat(nume)),-1,tableName);
    }

    public void updateParolaAngajat(String username, String newParola) throws SQLException {
        if (doesAngajatExist(username)) {
            String parolaCriptata = BCrypt.hashpw(newParola, BCrypt.gensalt());
            String query = "UPDATE angajati SET parola = ? WHERE username = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, parolaCriptata);
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();
            }
        } else {
            System.out.println("Angajatul cu username-ul '" + username + "' nu există.");
        }
    }

    public void deleteAngajat(String username) throws SQLException {
        if (doesAngajatExist(username)) {
            String query = "DELETE FROM angajati WHERE username = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                preparedStatement.executeUpdate();
            }
        } else {
            System.out.println("Angajatul cu username-ul '" + username + "' nu există.");
        }
    }

    public void createComenziTable() {
        // Query pentru crearea tabelului
        String createComenziTableSQL = "CREATE TABLE IF NOT EXISTS comenzi ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "data_ora DATETIME,"
                + "status VARCHAR(255),"
                + "cost_total DOUBLE"
                + ")";

        try (Statement statement = connection.createStatement()) {
            // Executarea query-ului de creare a tabelului
            statement.executeUpdate(createComenziTableSQL);
            System.out.println("Tabelul 'comenzi' a fost creat cu succes!");
        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelului 'comenzi': " + e.getMessage());
        }
    }

    public int createComanda() throws SQLException {
        // Data și ora curentă
        Date currentDate = new Date();
        createComenziTable();

        // Query pentru inserarea unei noi comenzi
        String query = "INSERT INTO comenzi (data_ora, status, cost_total) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Setăm parametrii
            preparedStatement.setTimestamp(1, new Timestamp(currentDate.getTime())); // Data și ora curentă
            preparedStatement.setString(2, "comanda noua"); // Statusul comenzii (în acest caz, comanda nouă)
            preparedStatement.setDouble(3, 0.0); // Costul total inițial, 0.0 pentru că nu avem încă preparate în comandă

            // Executăm query-ul de inserare
            int affectedRows = preparedStatement.executeUpdate();

            // Verificăm dacă s-au inserat înregistrări
            if (affectedRows > 0) {
                // Obținem cheile generate (în cazul nostru, ID-ul comenzii)
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Obținem valoarea ID-ului comenzii și o returnăm
                    int orderId = generatedKeys.getInt(1);
                    System.out.println("Comanda a fost creată cu succes! ID-ul comenzii: " + orderId);
                    return orderId;
                } else {
                    System.out.println("Nu s-au putut obține cheile generate pentru comandă.");
                }
            } else {
                System.out.println("Eroare la crearea comenzii.");
            }
        }
        return -1; // Return -1 in case of failure
    }

    public int getStocPreparat(String numePreparat) throws SQLException {
        String query = "SELECT stoc FROM meniu WHERE nume = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, numePreparat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("stoc");
            } else {
                // Dacă nu există niciun preparat cu acest nume, returnăm -1 sau o altă valoare semnificativă
                return -1;
            }
        }
    }

    public int adaugaPreparatLaComanda(int idComanda, String numePreparat, int cantitate) throws SQLException {
        // Verificăm mai întâi dacă preparatul există în meniu
        if (!doesPreparatExist(numePreparat, "meniu")) {
            System.out.println("Preparatul cu numele '" + numePreparat + "' nu există în meniu.");
            return -1;
        }

        // Verificăm dacă comanda există
        if (!doesComandaExist(idComanda)) {
            System.out.println("Comanda cu id-ul '" + idComanda + "' nu există.");
            return -1;
        }

        // Obținem ID-ul preparatului din meniu
        int idPreparat = getIdPreparat(numePreparat);

        // Verificăm dacă preparatul este disponibil în stoc suficient pentru comanda curentă
        if (!isPreparatDisponibil(idPreparat, cantitate)) {
            System.out.println("Preparatul '" + numePreparat + "' nu este disponibil în stoc suficient.");
            return -1;
        }

        // Obținem costul preparatului din meniu
        double pretPreparat = getPretPreparat(idPreparat);

        // Calculăm costul total pentru acest preparat
        double costTotal = pretPreparat * cantitate;

        // Actualizăm costul total al comenzii
        updateCostTotalComanda(idComanda, costTotal);

        // Adăugăm preparatul la comandă în tabelul dedicat
        String query = "INSERT INTO preparate_comanda (id_comanda, id_preparat, cantitate) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idComanda);
            preparedStatement.setInt(2, idPreparat);
            preparedStatement.setInt(3, cantitate);

            preparedStatement.executeUpdate();
        }
        updatePreparat(numePreparat,pretPreparat,getStocPreparat(numePreparat)-cantitate,"meniu");
        return 0;
    }

    private void updateCostTotalComanda(int idComanda, double costTotal) throws SQLException {
        // Obținem costul total existent pentru comanda cu id-ul dat
        double costTotalExistent = getCostTotalComanda(idComanda);

        // Adunăm costul total dat la cel existent
        double costNou = costTotalExistent + costTotal;

        String query = "UPDATE comenzi SET cost_total = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, costNou);
            preparedStatement.setInt(2, idComanda);

            preparedStatement.executeUpdate();
        }
    }

    private double getCostTotalComanda(int idComanda) throws SQLException {
        String query = "SELECT cost_total FROM comenzi WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idComanda);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("cost_total");
            } else {
                // Dacă nu există nicio comandă cu acest ID, putem considera costul total ca fiind 0
                return 0.0;
            }
        }
    }



    private double getPretPreparat(int idPreparat) throws SQLException {
        String query = "SELECT pret FROM meniu WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idPreparat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("pret");
            } else {
                // Dacă nu există niciun preparat cu acest ID, putem considera prețul ca fiind 0 sau o valoare default
                return 0.0;
            }
        }
    }


    private boolean isPreparatDisponibil(int idPreparat, int cantitate) throws SQLException {
        String query = "SELECT stoc FROM meniu WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idPreparat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int stoc = resultSet.getInt("stoc");
                return stoc >= cantitate;
            } else {
                // Dacă nu există niciun preparat cu acest ID, considerăm că nu este disponibil
                return false;
            }
        }
    }
    

    private int getIdPreparat(String numePreparat) throws SQLException {
        String query = "SELECT id FROM meniu WHERE nume = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, numePreparat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                // Dacă nu există niciun preparat cu acest nume, putem returna un ID invalid, de exemplu -1
                return -1;
            }
        }
    }


    private boolean doesComandaExist(int idComanda) throws SQLException {
        String query = "SELECT COUNT(*) FROM comenzi WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idComanda);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

    public ResultSet getComenzi() throws SQLException {
        String query = "SELECT * FROM comenzi";
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public void updateStatusComanda(int idComanda, String newStatus) throws SQLException {
        String query = "UPDATE comenzi SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, idComanda);

            preparedStatement.executeUpdate();
        }
    }

    public void createPreparateComandaTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS preparate_comanda ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "id_comanda INT,"
                + "id_preparat INT,"
                + "cantitate INT,"
                + "FOREIGN KEY (id_comanda) REFERENCES comenzi(id),"
                + "FOREIGN KEY (id_preparat) REFERENCES meniu(id)"
                + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    public String afiseazaPreparateComanda(int idComanda) {
        StringBuilder orderItems = new StringBuilder();
        try {
            ResultSet resultSetPreparate = getPreparateComanda(idComanda);
            while (resultSetPreparate.next()) {
                String numePreparat = resultSetPreparate.getString("nume_preparat");
                int cantitate = resultSetPreparate.getInt("cantitate");
                orderItems.append("Preparat: ").append(numePreparat).append(", Cantitate: ").append(cantitate).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems.toString();
    }


    private ResultSet getPreparateComanda(int idComanda) throws SQLException {
        String query = "SELECT meniu.nume AS nume_preparat, preparate_comanda.cantitate " +
                "FROM preparate_comanda " +
                "JOIN meniu ON preparate_comanda.id_preparat = meniu.id " +
                "WHERE preparate_comanda.id_comanda = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idComanda);
        return preparedStatement.executeQuery();
    }

    public void deleteComanda(int idComanda) {
        try {
            if (!doesComandaExist(idComanda)) {
                System.out.println("Comanda cu id-ul '" + idComanda + "' nu există.");
                return;
            }

            // Ștergere preparate asociate comenzii
            String deletePreparateQuery = "DELETE FROM preparate_comanda WHERE id_comanda = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deletePreparateQuery)) {
                preparedStatement.setInt(1, idComanda);
                preparedStatement.executeUpdate();
            }

            // Ștergere comandă
            String deleteComandaQuery = "DELETE FROM comenzi WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteComandaQuery)) {
                preparedStatement.setInt(1, idComanda);
                preparedStatement.executeUpdate();
                System.out.println("Comanda cu id-ul '" + idComanda + "' a fost ștearsă cu succes.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getComenziIntreDate(Timestamp dataInceput, Timestamp dataSfarsit) {
        try {
            String query = "SELECT * FROM comenzi WHERE data_ora BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, dataInceput);
            preparedStatement.setTimestamp(2, dataSfarsit);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Integer> getStatisticiCeleMaiComandateProduse() {
        Map<String, Integer> statistici = new HashMap<>();
        try {
            String query = "SELECT meniu.nume, SUM(preparate_comanda.cantitate) AS total_cantitate " +
                    "FROM preparate_comanda " +
                    "JOIN meniu ON preparate_comanda.id_preparat = meniu.id " +
                    "GROUP BY meniu.nume " +
                    "ORDER BY total_cantitate DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String numePreparat = resultSet.getString("nume");
                int totalCantitate = resultSet.getInt("total_cantitate");
                statistici.put(numePreparat, totalCantitate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistici;
    }



/*    public static void main(String[] args) {
        try {
            RestaurantDAO restaurantDAO = new RestaurantDAO("jdbc:mysql://localhost:3306/rst", "root", "Parola");
            restaurantDAO.createPreparateComandaTable();

            // Adăugarea și afișarea preparatelor și angajaților
            restaurantDAO.adaugaPreparat("Pizza Margherita", 25.0, 10, "meniu");
            restaurantDAO.adaugaPreparat("Pizza Diavola", 25.0, 10, "meniu");
            restaurantDAO.adaugaPreparat("Mix de branzeturi", 15, 303, "meniu");
            restaurantDAO.deletePreparat("Supa Crema De legume", "meniu");

            // Utilizarea try-with-resources pentru a asigura închiderea corectă a ResultSet-ului
            try (ResultSet resultSet = restaurantDAO.getMeniu()) {
                while (resultSet.next()) {
                    System.out.println("Preparat: " + resultSet.getString("nume") + ", Pret: " + resultSet.getDouble("pret") + ", Stoc: " + resultSet.getInt("stoc"));
                }
            }

            restaurantDAO.deleteAngajat("john_doe");
            restaurantDAO.deleteAngajat("jane_smith");
            restaurantDAO.adaugaAngajat("John Doe", "john_doe", "pass123");
            restaurantDAO.adaugaAngajat("Jane Smith", "jane_smith", "secret");

            // Utilizarea try-with-resources pentru a asigura închiderea corectă a ResultSet-ului
            try (ResultSet resultSetAngajati = restaurantDAO.getAngajati()) {
                while (resultSetAngajati.next()) {
                    System.out.println("Angajat: " + resultSetAngajati.getString("nume") + ", Username: " + resultSetAngajati.getString("username") + ", Password: " + resultSetAngajati.getString("parola"));
                }
            }

            // Afișarea comenzilor
            try (ResultSet resultSetComenzi = restaurantDAO.getComenzi()) {
                while (resultSetComenzi.next()) {
                    System.out.println("Comanda ID: " + resultSetComenzi.getInt("id") + ", Data și Ora: " + resultSetComenzi.getTimestamp("data_ora") + ", Status: " + resultSetComenzi.getString("status") + ", Cost Total: " + resultSetComenzi.getDouble("cost_total"));
                }
            }

            //restaurantDAO.adaugaPreparatLaComanda(3,"Pizza Diavola", 3);
            // Actualizarea statusului unei comenzi
            restaurantDAO.updateStatusComanda(3, "finalizata");

            // Utilizarea try-with-resources pentru a asigura închiderea corectă a ResultSet-ului
            try (ResultSet resultSet = restaurantDAO.getMeniu()) {
                while (resultSet.next()) {
                    System.out.println("Preparat: " + resultSet.getString("nume") + ", Pret: " + resultSet.getDouble("pret") + ", Stoc: " + resultSet.getInt("stoc"));
                }
            }

            // Utilizarea try-with-resources pentru a asigura închiderea corectă a ResultSet-ului
            try (ResultSet resultSetComenzi = restaurantDAO.getComenzi()) {
                while (resultSetComenzi.next()) {
                    System.out.println("Comanda ID: " + resultSetComenzi.getInt("id") + ", Data și Ora: " + resultSetComenzi.getTimestamp("data_ora") + ", Status: " + resultSetComenzi.getString("status") + ", Cost Total: " + resultSetComenzi.getDouble("cost_total"));
                }
            }

            Timestamp dataInceput = Timestamp.valueOf("2024-03-01 00:00:00");
            Timestamp dataSfarsit = Timestamp.valueOf("2024-03-16 23:59:59");
            ResultSet comenziIntreDate = restaurantDAO.getComenziIntreDate(dataInceput, dataSfarsit);
            // Folosirea ResultSet-ului pentru a afișa sau procesa comenzile
            System.out.println("Comenzi intre "+dataInceput + " si " + dataSfarsit + ":");
            while (comenziIntreDate.next()) {
                System.out.println("Comanda ID: " + comenziIntreDate.getInt("id") + ", Data și Ora: " + comenziIntreDate.getTimestamp("data_ora") + ", Status: " + comenziIntreDate.getString("status") + ", Cost Total: " + comenziIntreDate.getDouble("cost_total"));
            }

            // Obținerea statisticii cu cele mai comandate produse
            Map<String, Integer> statisticiCeleMaiComandateProduse = restaurantDAO.getStatisticiCeleMaiComandateProduse();
            for (Map.Entry<String, Integer> entry : statisticiCeleMaiComandateProduse.entrySet()) {
                System.out.println("Preparat: " + entry.getKey() + ", Cantitate totală comandată: " + entry.getValue());
            }

            restaurantDAO.afiseazaPreparateComanda(3);

            restaurantDAO.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/


}
