import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Connection connection = null;

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/SearchAndRescue?useSSL=false&serverTimezone=Asia/Manila";
    private static final String USER = "root";
    private static final String PASSWORD = "ilovecompsci"; // Change if needed

    private DatabaseHandler() {
        connectToDB();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private static void connectToDB() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL Driver
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                System.out.println("Connected to database!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectToDB(); // Reconnect if closed
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
        return connection;
    }
    

    private static boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    //🏠🏠🏠 HOME PAGE - BARANGAY TABLE
    public static ObservableList<BarangayTable> displayBarangay() {
        ObservableList<BarangayTable> barangayList = FXCollections.observableArrayList();
        String query = "SELECT barangayName, barangayDistance, barangayRescueCount FROM Barangay";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                String barangayName = result.getString("barangayName");  
                Double barangayDistance = result.getDouble("barangayDistance");  
                Integer barangayRescueCount = result.getInt("barangayRescueCount");  
       
                barangayList.add(new BarangayTable(barangayName, barangayDistance, barangayRescueCount));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
            e.printStackTrace();
        }
        return barangayList;
    }
 
    // 🏠🏠🏠 HOME PAGE - BARANGAY DESCRIPTION (Simplified version)
    public static ObservableList<BarangayDescTable> displayBarangayDesc() {
        ObservableList<BarangayDescTable> barangayDescList = FXCollections.observableArrayList();
        String query = "SELECT " +
                    "Barangay.barangayName, " +
                    "BarangayDescription.barangayCaptain, " +
                    "BarangayDescription.barangayHazardProne, " +
                    "BarangayDescription.barangayPopulation " +
                    "FROM Barangay " +
                    "JOIN BarangayDescription ON Barangay.barangayID = BarangayDescription.barangayID";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                String barangayName = result.getString("barangayName");
                String barangayCaptain = result.getString("barangayCaptain");
                String hazardProne = result.getString("barangayHazardProne");
                int population = result.getInt("barangayPopulation");

                barangayDescList.add(new BarangayDescTable(barangayName, barangayCaptain, hazardProne, population));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching barangay descriptions: " + e.getMessage());
            e.printStackTrace();
        }

        return barangayDescList;
    }


    //➕➕➕ ADD RESCUE FXML 
    public static ObservableList<String> loadBarangays() {
        ObservableList<String> barangayList = FXCollections.observableArrayList();
        String query = "SELECT barangayName FROM Barangay";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery()) {


            while (result.next()) {
                String barangayName = result.getString("barangayName");
                barangayList.add(barangayName);
            }
        } catch (SQLException e) {
            System.err.println("Error loading account data: " + e.getMessage());
        }
        return barangayList;
    }

    public static boolean addUser(AdminUser newUser) { 
        String query = "INSERT INTO WazeAccounts (email, username, passwords, birthdate, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newUser.getEmail());
            pstmt.setString(2, newUser.getUsername());
            pstmt.setString(3, newUser.getPassword());
            pstmt.setString(4, newUser.getBirthDate().toString());
            pstmt.setString(5, newUser.getFirstName());
            pstmt.setString(6, newUser.getLastName());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    
    public static String generateIncidentNumber() {
        String date = java.time.LocalDate.now().toString().replaceAll("-", "");
        int random = (int)(Math.random() * 90000) + 10000; // 5-digit random number
        return "Incident#" + date + "-" + random;
    }
    public static int getBarangayIDByName(String barangayName) {
        int barangayID = -1;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT barangayID FROM Barangay WHERE barangayName = ?")) {
            stmt.setString(1, barangayName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                barangayID = rs.getInt("barangayID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barangayID;
    }
        


}