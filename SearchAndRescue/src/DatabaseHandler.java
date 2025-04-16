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


    //🚨🚨🚨 ACTIVE INCIDENTS TAB 
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

                ActiveIncidentsTable incident = new ActiveIncidentsTable(
                    emergencyType, emergencyStatus, emergencySeverity, incidentNumber, dateIssued,
                    barangayLocation, rescueeName, totalRescuees
                );

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
        String sql = "INSERT INTO Emergency (incidentNumber, barangayID, emergencyType, emergencySeverity, emergencyRescueCount, emergencyStatus, dateIssued, peopleID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, emergency.getIncidentNumber());
                stmt.setInt(2, emergency.getBarangayID());
                stmt.setString(3, emergency.getEmergencyType());
                stmt.setString(4, emergency.getSeverity());
                stmt.setInt(5, emergency.getRescueCount());
                stmt.setString(6, emergency.getStatus());
                stmt.setDate(7, java.sql.Date.valueOf(emergency.getDateIssued()));
                stmt.setInt(8, emergency.getPeopleID());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int insertPeople(PeopleCount person) {
        String sql = "INSERT INTO PeopleCount (peopleMemberCount, firstName, lastName, numOfChildren, numOfAdults, numOfSeniors) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, person.getMemberCount());
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());
            stmt.setInt(4, person.getNumOfChildren());
            stmt.setInt(5, person.getNumOfAdults());
            stmt.setInt(6, person.getNumOfSeniors());
    
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // peopleID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    

    public static int getBarangayIDFromName(String name) {
        String sql = "SELECT barangayID FROM Barangay WHERE barangayName = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("barangayID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // UPDATE ACTIVE INCIDENT
    public static void updateActiveIncident(
        String incidentNumber,
        String emergencyType,
        String emergencyStatus,
        String emergencySeverity,
        String barangayLocation, // This is barangayName
        String firstName,
        String lastName,
        int totalRescuees
    ) {
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
                p.numOfSeniors = ?
            WHERE e.incidentNumber = ?
        """;

        try (Connection conn = getConnection()) {
            int barangayID = -1;

            // Get the correct barangayID from the barangay name
            try (PreparedStatement getBarangayIDStmt = conn.prepareStatement(getBarangayIDQuery)) {
                getBarangayIDStmt.setString(1, barangayLocation);
                ResultSet rs = getBarangayIDStmt.executeQuery();
                if (rs.next()) {
                    barangayID = rs.getInt("barangayID");
                } else {
                    System.err.println("Barangay not found: " + barangayLocation);
                    return;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, emergencyType);
                stmt.setString(2, emergencyStatus);
                stmt.setString(3, emergencySeverity);
                stmt.setInt(4, barangayID);
                stmt.setString(5, firstName);
                stmt.setString(6, lastName);
                stmt.setInt(7, totalRescuees); // still placeholder; adjust later
                stmt.setInt(8, totalRescuees);
                stmt.setInt(9, totalRescuees);
                stmt.setString(10, incidentNumber);

                stmt.executeUpdate();
                System.out.println("Database updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }








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
    
        // Get last number or start from 0
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