import javafx.beans.property.*;
import java.time.LocalDate;

public class ActiveIncidentsTable {

    private StringProperty emergencyType;
    private StringProperty emergencyStatus;
    private StringProperty incidentNumber;  // Change to StringProperty
    private ObjectProperty<LocalDate> dateIssued;
    private StringProperty barangayLocation;
    private StringProperty rescueeName;  // Rescuee name (first + last)
    private IntegerProperty numOfRescuee;

    // Constructor
    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, String incidentNumber,  // Change to String
                                LocalDate dateIssued, String barangayLocation, String rescueeName, 
                                Integer numOfRescuee) {
        this.emergencyType = new SimpleStringProperty(emergencyType);
        this.emergencyStatus = new SimpleStringProperty(emergencyStatus);
        this.incidentNumber = new SimpleStringProperty(incidentNumber);  // Store as StringProperty
        this.dateIssued = new SimpleObjectProperty<>(dateIssued);
        this.barangayLocation = new SimpleStringProperty(barangayLocation);
        this.rescueeName = new SimpleStringProperty(rescueeName);  // Use for rescuee name
        this.numOfRescuee = new SimpleIntegerProperty(numOfRescuee);
    }

    // Getters and Setters
    public String getEmergencyType() {
        return emergencyType.get();
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType.set(emergencyType);
    }

    public StringProperty emergencyTypeProperty() {
        return emergencyType;
    }

    public String getEmergencyStatus() {
        return emergencyStatus.get();
    }

    public void setEmergencyStatus(String emergencyStatus) {
        this.emergencyStatus.set(emergencyStatus);
    }

    public StringProperty emergencyStatusProperty() {
        return emergencyStatus;
    }

    public String getIncidentNumber() {  // Change to return String
        return incidentNumber.get();
    }

    public void setIncidentNumber(String incidentNumber) {  // Change to set String
        this.incidentNumber.set(incidentNumber);
    }

    public StringProperty incidentNumberProperty() {
        return incidentNumber;
    }

    public LocalDate getDateIssued() {
        return dateIssued.get();
    }
    
    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued.set(dateIssued);
    }
    
    public ObjectProperty<LocalDate> dateIssuedProperty() {
        return dateIssued;
    }    
    
    public String getBarangayLocation() {
        return barangayLocation.get();
    }

    public void setBarangayLocation(String barangayLocation) {
        this.barangayLocation.set(barangayLocation);
    }

    public StringProperty barangayLocationProperty() {
        return barangayLocation;
    }

    public String getRescueeName() {
        return rescueeName.get();
    }

    public void setRescueeName(String rescueeName) {
        this.rescueeName.set(rescueeName);
    }

    public StringProperty rescueeNameProperty() {
        return rescueeName;
    }

    public int getNumOfRescuee() {
        return numOfRescuee.get();
    }

    public void setNumOfRescuee(int numOfRescuee) {
        this.numOfRescuee.set(numOfRescuee);
    }

    public IntegerProperty numOfRescueeProperty() {
        return numOfRescuee;
    }
}
