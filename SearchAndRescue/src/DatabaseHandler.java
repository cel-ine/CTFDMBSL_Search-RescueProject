import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.beans.property.SimpleObjectProperty;
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
 
    // 🏠🏠🏠 HOME PAGE - BARANGAY DESCRIPTION 
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

    public static ObservableList<ActiveIncidentsTable> getAllActiveIncidents() {
        ObservableList<ActiveIncidentsTable> incidentList = FXCollections.observableArrayList();
    
        String query = """
            SELECT 
                e.emergencyType,
                e.emergencyStatus,
                e.incidentNumber,
                e.dateIssued AS timeCreated,
                b.barangayName AS barangayLocation,
                CONCAT(p.firstName, ' ', p.lastName) AS rescueeName,
                (p.numOfChildren + p.numOfAdults + p.numOfSeniors) AS numberOfRescuee
            FROM Emergency e
            JOIN Barangay b ON e.barangayID = b.barangayID
            JOIN PeopleCount p ON e.incidentNumber = p.peopleID
        """;
    
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                String emergencyType = rs.getString("emergencyType");
                String emergencyStatus = rs.getString("emergencyStatus");
                Integer incidentNumber = rs.getInt("incidentNumber");
    
                LocalDate timeCreated = rs.getDate("timeCreated").toLocalDate();
                String barangayLocation = rs.getString("barangayLocation");
                String rescueeName = rs.getString("rescueeName");
                Integer numberOfRescuee = rs.getInt("numberOfRescuee");
    
                // Create object without reportedBy
                ActiveIncidentsTable incident = new ActiveIncidentsTable(
                        emergencyType,
                        emergencyStatus,
                        incidentNumber,
                        timeCreated,
                        barangayLocation,
                        rescueeName,
                        numberOfRescuee
                );
    
                incidentList.add(incident);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching active incidents: " + e.getMessage());
            e.printStackTrace();
        }
    
        return incidentList;
    }
    
    
    
    

    //➕➕➕ ADD RESCUE FXML 
    public static ObservableList<BarangayTable> loadBarangays() {
    ObservableList<BarangayTable> barangayList = FXCollections.observableArrayList();
    String query = "SELECT barangayID, barangayName FROM Barangay";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet result = pstmt.executeQuery()) {

        while (result.next()) {
            int barangayID = result.getInt("barangayID");
            String barangayName = result.getString("barangayName");
            barangayList.add(new BarangayTable(barangayID, barangayName)); 
        }
    } catch (SQLException e) {
        System.err.println("Error loading barangays: " + e.getMessage());
    }
    return barangayList;  
}

    //  public static boolean addRoutes(AdminRoutes newRoute, List<String> locationList) {
    //     try (Connection conn = DatabaseHandler.getNewConnection()) { 
    //         if (conn == null || conn.isClosed()) {
    //             System.err.println("Database connection failed!");
    //             return false;
    //         }
    //         conn.setAutoCommit(false); 
    //         System.out.println("Using connection: " + conn);
    
    //         // Insert into WazeRoutes
    //         try (PreparedStatement pstmtRoutes = conn.prepareStatement(
    //                 "INSERT INTO WazeRoutes (route_id, account_id, route_startpoint, route_endpoint) VALUES (?, ?, ?, ?)")) {
    //             pstmtRoutes.setString(1, newRoute.getRouteID());
    //             pstmtRoutes.setInt(2, newRoute.getAccountID());
    //             pstmtRoutes.setString(3, newRoute.getRoute_startpoint());
    //             pstmtRoutes.setString(4, newRoute.getRoute_endpoint());
    //             pstmtRoutes.executeUpdate();
    //             System.out.println("Route inserted successfully.");
    //         }
    
    //         String altRouteID = RouteIDGenerator.generateAltRouteID();
    //         String alternativeRoute = RouteIDGenerator.generateRandomAlternativeRoute(
    //                 newRoute.getRoute_startpoint(), newRoute.getRoute_endpoint(), locationList);
 
    //         String stopover = (newRoute.getStopOver() == null || newRoute.getStopOver().isEmpty()) 
    //                         ? "No Stopover" : newRoute.getStopOver();

    //         try (PreparedStatement pstmtAltRoutes = conn.prepareStatement(
    //                 "INSERT INTO WazeAltRoutes (alt_route_id, route_id, alt_routes, stop_overloc) VALUES (?, ?, ?, ?)")) {
    //             pstmtAltRoutes.setString(1, altRouteID);
    //             pstmtAltRoutes.setString(2, newRoute.getRouteID());
    //             pstmtAltRoutes.setString(3, alternativeRoute);
    //             pstmtAltRoutes.setString(4, stopover);
    //             pstmtAltRoutes.executeUpdate();
    //             System.out.println("Alternative route inserted successfully.");
    //         }

    //         try (PreparedStatement pstmtTravelTime = conn.prepareStatement(
    //                 "INSERT INTO WazeTravelTime (traveltime_id, route_id, est_time) VALUES (?, ?, ?)")) {
    //             pstmtTravelTime.setString(1, "T_T-" + String.format("%03d", new Random().nextInt(999)));
    //             pstmtTravelTime.setString(2, newRoute.getRouteID());
    //             pstmtTravelTime.setString(3, RouteIDGenerator.generateRandomEstTime());
    //             pstmtTravelTime.executeUpdate();
    //             System.out.println("Travel time inserted successfully.");
    //         }
    
    //         conn.commit(); 
    //         System.out.println("Transaction committed successfully.");
    //         return true;
    
    //     } catch (SQLException e) {
    //         System.err.println("Error adding route: " + e.getMessage());
    //         return false;
    //     }
    // }


    
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