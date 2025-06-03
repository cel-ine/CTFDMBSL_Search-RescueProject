import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Connection connection = null;

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/FinalSearchAndRescueDB?useSSL=false&serverTimezone=Asia/Manila";
    private static final String USER = "root";
    private static final String PASSWORD = "ilovecompsci"; 

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
        String query = "SELECT b.barangayName, b.barangayDistance, COUNT(e.incidentNumber) AS emergencyCount " +
                   "FROM Barangay b " +
                   "LEFT JOIN Emergency e ON b.barangayID = e.barangayID " +
                   "GROUP BY b.barangayID, b.barangayName, b.barangayDistance";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                String barangayName = result.getString("barangayName");  
                Double barangayDistance = result.getDouble("barangayDistance");  
                Integer barangayRescueCount = result.getInt("emergencyCount");  
       
                barangayList.add(new BarangayTable(barangayName, barangayDistance, barangayRescueCount));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
            e.printStackTrace();
        }
        return barangayList;
    }
 
    //BARANGAY DESCRIPTION 
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

    //COUNTER
    public static int getActiveIncidentCountByStatusAndDate(String status, LocalDate date) {
    int count = 0;
    String query = "SELECT COUNT(*) FROM Emergency WHERE emergencyStatus = ? AND DATE(dateIssued) = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, status);
        stmt.setString(2, date.toString()); // yyyy-MM-dd
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            count = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return count;
}

    //🚨🚨🚨 ACTIVE INCIDENTS TABLE 
    public static ObservableList<ActiveIncidentsTable> displayAllActiveIncidents() {
        ObservableList<ActiveIncidentsTable> incidentList = FXCollections.observableArrayList();
        
        String query = """
            SELECT e.incidentNumber, e.emergencyType, e.emergencyStatus, 
            e.dateIssued, b.barangayName, 
            p.address, p.contactNum, officerInCharge,
            (p.numOfChildren + p.numOfAdults + p.numOfSeniors) AS totalRescuees,
            p.numOfChildren, p.numOfAdults, p.numOfSeniors,
            e.emergencySeverity,
            bd.barangayCaptain AS personInCharge
            FROM Emergency e
            JOIN PeopleDetails p ON e.peopleID = p.peopleID
            JOIN Barangay b ON e.barangayID = b.barangayID
            JOIN BarangayDescription bd ON b.barangayID = bd.barangayID
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
                LocalDateTime dateIssued = rs.getTimestamp("dateIssued").toLocalDateTime();
                String barangayLocation = rs.getString("barangayName");
                String personInCharge = rs.getString ("personInCharge");
                int children = rs.getInt("numOfChildren");
                int adults = rs.getInt("numOfAdults");
                int seniors = rs.getInt("numOfSeniors");
                int totalRescuees = rs.getInt("totalRescuees");
                String address = rs.getString ("address");
                String contactNum = rs.getString ("contactNum");
                String officerInCharge = rs.getString ("officerInCharge");

    
                // Create the ActiveIncidentsTable object
                ActiveIncidentsTable incident = new ActiveIncidentsTable(
                    emergencyType, emergencyStatus, emergencySeverity, incidentNumber, dateIssued,
                    barangayLocation, personInCharge, totalRescuees, children, adults, seniors, address, contactNum, officerInCharge
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
    public static String getBarangayCaptainByBarangayName(String barangayName) {
    String sql = """
        SELECT bd.barangayCaptain
        FROM BarangayDescription bd
        JOIN Barangay b ON bd.barangayID = b.barangayID
        WHERE b.barangayName = ?
    """;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, barangayName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("barangayCaptain");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}

    //➕➕➕ ADD RESCUE - this part is to add it sa database
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
                pstmt.setTimestamp(7, java.sql.Timestamp.valueOf(emergency.getDateIssued()));                
                pstmt.setInt(8, emergency.getPeopleID());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int insertPeople(PeopleCountTable person) {
        String query = "INSERT INTO PeopleDetails (address, contactNum, numOfChildren, numOfAdults, numOfSeniors, officerInCharge) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString (1, person.getAddress());
            pstmt.setString(2, person.getContactNum());
            pstmt.setInt(3, person.getNumOfChildren());
            pstmt.setInt(4, person.getNumOfAdults());
            pstmt.setInt(5, person.getNumOfSeniors());
            pstmt.setString(6, person.getofficerInCharge());


    
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); 
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

    public static ObservableList<String> getBusyOfficers() { //FOR GETTING THE OFFICERS IN CHARGE THAT IS CURRENTLY BUSY (status is not yet 'completed')
        ObservableList<String> busyList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT officerInCharge FROM PeopleDetails pd " +
                    "JOIN Emergency e ON pd.peopleID = e.peopleID " +
                    "WHERE e.emergencyStatus != 'Completed' AND officerInCharge IS NOT NULL";

        try (Connection conn = DatabaseHandler.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                busyList.add(rs.getString("officerInCharge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return busyList;
    }

    // ⚔️⚔️⚔️ UPDATE ACTIVE INCIDENT
    public static void updateActiveIncident(
        String incidentNumber,
        String emergencyType,
        String emergencyStatus,
        String emergencySeverity,
        String barangayLocation,
        int numOfRescuee,
        int children, int adults, int seniors
    ) throws SQLException {
        String getBarangayIDQuery = "SELECT barangayID FROM Barangay WHERE barangayName = ?";
        
        String updateQuery = """
            UPDATE Emergency e
            JOIN PeopleDetails p ON e.peopleID = p.peopleID
            SET e.emergencyType = ?, 
                e.emergencyStatus = ?, 
                e.emergencySeverity = ?, 
                e.barangayID = ?, 
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
                pstmt.setInt(5, children);  
                pstmt.setInt(6, adults);  
                pstmt.setInt(7, seniors);  
                pstmt.setInt(8, children + adults + seniors);  
                pstmt.setString(9, incidentNumber); 
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
            JOIN PeopleDetails p ON e.peopleID = p.peopleID
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

    //🍒🍒🍒 HISTORY TABLE
    public static ObservableList<HistoryTable> loadHistory() {
    ObservableList<HistoryTable> historyList = FXCollections.observableArrayList();
    String query = """
        SELECT h.historyID, er.reportRemarks AS incidentReport, e.emergencyType, e.emergencySeverity,
               e.incidentNumber, h.dispatchTimestamp, b.barangayName,
               bd.barangayCaptain AS personInCharge,
               (p.numOfChildren + p.numOfAdults + p.numOfSeniors) AS totalRescuees,
               p.address, p.contactNum, p.officerInCharge
        FROM History h
        JOIN Emergency e ON h.incidentNumber = e.incidentNumber
        JOIN PeopleDetails p ON e.peopleID = p.peopleID
        JOIN Barangay b ON h.barangayID = b.barangayID
        JOIN BarangayDescription bd ON b.barangayID = bd.barangayID
        LEFT JOIN EmergencyReport er ON h.historyID = er.historyID
        ORDER BY h.dispatchTimestamp DESC
        """;

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet result = pstmt.executeQuery()) {

        while (result.next()) {
            int historyID = result.getInt("historyID");
            String incidentReport = result.getString("incidentReport");
            String emType = result.getString("emergencyType");
            String emSeverity = result.getString("emergencySeverity");
            String incidentNum = result.getString("incidentNumber");
            LocalDateTime dispatchedTime = result.getTimestamp("dispatchTimestamp").toLocalDateTime();
            String barangayName = result.getString("barangayName");
            String personInCharge = result.getString("personInCharge");
            String totalRescuees = String.valueOf(result.getInt("totalRescuees"));
            String address = result.getString("address");
            String contactNum = result.getString("contactNum");
            String officerInCharge = result.getString("officerInCharge");

            historyList.add(new HistoryTable(
                historyID, incidentReport, emType, emSeverity, incidentNum,
                dispatchedTime, barangayName, personInCharge, totalRescuees, address, contactNum, officerInCharge
            ));
        }

    } catch (SQLException e) {
        System.err.println("Error loading history: " + e.getMessage());
    }

    return historyList;
}

    public static Integer insertToHistory(String incidentNumber, int barangayID) {
    String updateStatusQuery = "UPDATE Emergency SET emergencyStatus = 'COMPLETED' WHERE incidentNumber = ?";
    String insertHistoryQuery = "INSERT INTO History (incidentNumber, barangayID, dispatchTimestamp) VALUES (?, ?, NOW())";

    try (Connection conn = getConnection();
         PreparedStatement pstmtUpdate = conn.prepareStatement(updateStatusQuery);
         PreparedStatement pstmtInsert = conn.prepareStatement(insertHistoryQuery, Statement.RETURN_GENERATED_KEYS)) {

        pstmtUpdate.setString(1, incidentNumber);
        pstmtUpdate.executeUpdate();

        pstmtInsert.setString(1, incidentNumber);
        pstmtInsert.setInt(2, barangayID);
        int rowsInserted = pstmtInsert.executeUpdate();

        if (rowsInserted > 0) {
            ResultSet rs = pstmtInsert.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // This is your historyID
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    // VALIDATION TO AVOID DISPATCHING THE SAME INCIDENT
    public static boolean isAlreadyCompleted(String incidentNumber) {
    String query = "SELECT emergencyStatus FROM Emergency WHERE incidentNumber = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, incidentNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String status = rs.getString("emergencyStatus");
            return "COMPLETED".equalsIgnoreCase(status);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    //WHEN THE TABLE (emergency status col) IS UPDATED TO DISPATCHED, IT WILL BE ADDED TO HISTORY TABLE
    public static boolean isAlreadyInHistory(String incidentNumber) {
        String query = "SELECT COUNT(*) FROM History WHERE incidentNumber = ?";
        try (Connection conn = getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, incidentNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //REMOVE FROM HISTORY IF IT'S UPDATED IN THE ACTIVE INCIDENTS AND DOESNT HAVE REPORT YET
    public static boolean removeFromHistory(String incidentNumber) { 
    String query = "DELETE FROM History WHERE incidentNumber = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, incidentNumber);
        int affectedRows = stmt.executeUpdate();  
        
        return affectedRows > 0;  
    
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    //PERMANENTLY DELETE IN HISTORY
    public static boolean deleteFromHistory (String incidentNumber) {
    String getHistoryIdQuery = "SELECT historyID FROM History WHERE incidentNumber = ?";
    String deleteReportQuery = "DELETE FROM EmergencyReport WHERE historyID = ?";
    String deleteHistoryQuery = "DELETE FROM History WHERE incidentNumber = ?";

    try (Connection conn = getConnection();
         PreparedStatement getIdStmt = conn.prepareStatement(getHistoryIdQuery)) {

        getIdStmt.setString(1, incidentNumber);
        ResultSet rs = getIdStmt.executeQuery();
        if (rs.next()) {
            int historyID = rs.getInt("historyID");

            try (PreparedStatement deleteReportStmt = conn.prepareStatement(deleteReportQuery)) {
                deleteReportStmt.setInt(1, historyID);
                deleteReportStmt.executeUpdate();
            }
        }

        try (PreparedStatement deleteHistoryStmt = conn.prepareStatement(deleteHistoryQuery)) {
            deleteHistoryStmt.setString(1, incidentNumber);
            int affectedRows = deleteHistoryStmt.executeUpdate();
            return affectedRows > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public static EmergencyReport getReportByHistoryID(int historyID) {
        String sql = "SELECT reportWriter, reportRemarks, emergencyActionTaken, headRescue FROM EmergencyReport WHERE historyID = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, historyID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EmergencyReport(
                    rs.getString("reportWriter"),
                    rs.getString("reportRemarks"),
                    rs.getString("emergencyActionTaken"),
                    rs.getString ("headRescue")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //FOR ADDING RESCUE REPORT 
    public static void submitReport(int historyID, String writer, String remarks, String action) {
    String sql = """
        INSERT INTO EmergencyReport (historyID, reportWriter, reportRemarks, emergencyActionTaken)
        VALUES (?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            reportWriter = VALUES(reportWriter),
            reportRemarks = VALUES(reportRemarks),
            emergencyActionTaken = VALUES(emergencyActionTaken)
        """;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, historyID);
        stmt.setString(2, writer);
        stmt.setString(3, remarks);
        stmt.setString(4, action);

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public static boolean historyIDExists(int historyID) {
        String sql = "SELECT 1 FROM History WHERE historyID = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, historyID);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getHistoryIDByIncidentNumber(String incidentNumber) {
        String sql = "SELECT historyID FROM History WHERE incidentNumber = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, incidentNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("historyID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //VALIDATION FOR TRYING TO ADD AGAIN A REPORT FOR AN EMERGENCY WHO ALREADY HAVE ONE
    public static boolean isDuplicateRescue (String FirstName, String LastName, int barangayID) {
        String sql = "SELECT COUNT(*) FROM PeopleDetails p " +
                 "JOIN Emergency e ON p.peopleID = e.peopleID " +
                 "WHERE p.firstName = ? AND p.lastName = ? AND e.barangayID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, FirstName);
            pstmt.setString(2, LastName);
            pstmt.setInt(3, barangayID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return false; 
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
        if (parts.length > 1) {
            try {
                lastNumber = Integer.parseInt(parts[1]); 
            } catch (NumberFormatException e) {
                System.err.println("Error parsing number from incident: " + latest);
            }
        }
    }

    int newNumber = lastNumber + 1;
    String formatted = String.format("%05d", newNumber);

    return prefix + formatted;
}
}