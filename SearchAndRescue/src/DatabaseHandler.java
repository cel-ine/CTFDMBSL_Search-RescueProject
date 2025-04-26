import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static boolean validateLogin(String username, Integer pin) { 
        Connection conn = getConnection();
        if (conn == null) {
            System.err.println("No database connection. Cannot validate login.");
            return false;
        } 
        String query = "SELECT * FROM AdminAccount WHERE username = ? AND adminPin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, pin);
            try (ResultSet result = pstmt.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
        }
        return false;
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


    public static ObservableList<ActiveIncidentsTable> displayAllActiveIncidents() {
        ObservableList<ActiveIncidentsTable> incidentList = FXCollections.observableArrayList();
        
        String query = """
            SELECT e.incidentNumber, e.emergencyType, e.emergencyStatus, 
                   e.dateIssued, b.barangayName, 
                   p.firstName, p.lastName,
                   (p.numOfChildren + p.numOfAdults + p.numOfSeniors) AS totalRescuees,
                   e.emergencySeverity  -- Fetch the emergencySeverity directly from the database
            FROM Emergency e
            JOIN PeopleCount p ON e.peopleID = p.peopleID
            JOIN Barangay b ON e.barangayID = b.barangayID
            ORDER BY e.dateIssued DESC
        """;
    
        try (Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query)) {
            
            while (rs.next()) {
                String emergencyType = rs.getString("emergencyType");
                String emergencyStatus = rs.getString("emergencyStatus");
                String emergencySeverity = rs.getString("emergencySeverity"); 
                String incidentNumber = rs.getString("incidentNumber");
                LocalDate dateIssued = rs.getDate("dateIssued").toLocalDate();
                String barangayLocation = rs.getString("barangayName");
                String rescueeName = rs.getString("firstName") + " " + rs.getString("lastName");
                int totalRescuees = rs.getInt("totalRescuees");
    
                // Create the ActiveIncidentsTable object
                ActiveIncidentsTable incident = new ActiveIncidentsTable(
                    emergencyType, emergencyStatus, emergencySeverity, incidentNumber, dateIssued,
                    barangayLocation, rescueeName, totalRescuees
                );
    
                // Add to the list of incidents
                incidentList.add(incident);
            }
    
        } catch (SQLException e) {
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

    //ADD RESCUE - this part is to add it sa database
    public static boolean insertEmergency(Emergency emergency) {
        String query = "INSERT INTO Emergency (incidentNumber, barangayID, emergencyType, emergencySeverity, emergencyRescueCount, emergencyStatus, dateIssued, peopleID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, emergency.getIncidentNumber());
                pstmt.setInt(2, emergency.getBarangayID());
                pstmt.setString(3, emergency.getEmergencyType());
                pstmt.setString(4, emergency.getSeverity());
                pstmt.setInt(5, emergency.getRescueCount());
                pstmt.setString(6, emergency.getStatus());
                pstmt.setDate(7, java.sql.Date.valueOf(emergency.getDateIssued()));
                pstmt.setInt(8, emergency.getPeopleID());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int insertPeople(PeopleCount person) {
        String query = "INSERT INTO PeopleCount (firstName, lastName, numOfChildren, numOfAdults, numOfSeniors) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, person.getFirstName());
            pstmt.setString(2, person.getLastName());
            pstmt.setInt(3, person.getNumOfChildren());
            pstmt.setInt(4, person.getNumOfAdults());
            pstmt.setInt(5, person.getNumOfSeniors());
    
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // peopleID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    

    public static int getBarangayIDFromName(String name) {
        String query = "SELECT barangayID FROM Barangay WHERE barangayName = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("barangayID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ⚔️⚔️⚔️ UPDATE ACTIVE INCIDENT
    public static void updateActiveIncident(
        String incidentNumber,
        String emergencyType,
        String emergencyStatus,
        String emergencySeverity,
        String barangayLocation,
        String firstName,
        String lastName,
        int numOfRescuee,
        int children, int adults, int seniors
    ) throws SQLException {
        String getBarangayIDQuery = "SELECT barangayID FROM Barangay WHERE barangayName = ?";
        
        String updateQuery = """
            UPDATE Emergency e
            JOIN PeopleCount p ON e.peopleID = p.peopleID
            SET e.emergencyType = ?, 
                e.emergencyStatus = ?, 
                e.emergencySeverity = ?, 
                e.barangayID = ?, 
                p.firstName = ?, 
                p.lastName = ?, 
                p.numOfChildren = ?, 
                p.numOfAdults = ?, 
                p.numOfSeniors = ?, 
                e.emergencyRescueCount = ?  -- Set the total rescue count (sum of children, adults, and seniors)
            WHERE e.incidentNumber = ?;
        """;

        try (Connection conn = getConnection()) {
            int barangayID = -1;

            try (PreparedStatement getBarangayIDStmt = conn.prepareStatement(getBarangayIDQuery)) {
                getBarangayIDStmt.setString(1, barangayLocation);
                try (ResultSet rs = getBarangayIDStmt.executeQuery()) {
                    if (rs.next()) {
                        barangayID = rs.getInt("barangayID");  
                    } else {
                        System.err.println("Barangay not found: " + barangayLocation);
                        return;  
                    }
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, emergencyType);  
                pstmt.setString(2, emergencyStatus);  
                pstmt.setString(3, emergencySeverity);  
                pstmt.setInt(4, barangayID);  
                pstmt.setString(5, firstName); 
                pstmt.setString(6, lastName);  
                pstmt.setInt(7, children);  
                pstmt.setInt(8, adults);  
                pstmt.setInt(9, seniors);  
                pstmt.setInt(10, children + adults + seniors);  
                pstmt.setString(11, incidentNumber); 
                int affectedRows = pstmt.executeUpdate();  
                if (affectedRows > 0) {
                    System.out.println("Database updated successfully!");
                } else {
                    System.err.println("No matching incident found with incidentNumber: " + incidentNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating database.");
            throw e;  
        }
    }

    public static int[] getPeopleCountsByIncidentNumber(String incidentNumber) throws SQLException {
        String query = """
            SELECT p.numOfChildren, p.numOfAdults, p.numOfSeniors
            FROM Emergency e
            JOIN PeopleCount p ON e.peopleID = p.peopleID
            WHERE e.incidentNumber = ?
        """;

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, incidentNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int children = rs.getInt("numOfChildren");
                    int adults = rs.getInt("numOfAdults");
                    int seniors = rs.getInt("numOfSeniors");
                    return new int[]{children, adults, seniors};
                }
            }
        }

        return new int[]{0, 0, 0}; 
    }

    //incident number generator
    public static String generateIncidentNumberFromDB() {
        String date = java.time.LocalDate.now().toString().replaceAll("-", "");
        String prefix = "Incident#" + date + "-";
        String latest = ""; // will store the last incident number like Incident#20250415-00004
    
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT incidentNumber FROM Emergency WHERE incidentNumber LIKE ? ORDER BY incidentNumber DESC LIMIT 1")) {
            
            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                latest = rs.getString("incidentNumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    
        int lastNumber = 0;
        if (!latest.isEmpty()) {
            String[] parts = latest.split("-");
            lastNumber = Integer.parseInt(parts[1]);
        }
    
        int newNumber = lastNumber + 1;
        String formatted = String.format("%05d", newNumber);
    
        return prefix + formatted;
    }

}