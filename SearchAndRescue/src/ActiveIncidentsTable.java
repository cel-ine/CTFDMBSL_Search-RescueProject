import javafx.beans.property.*;
import java.time.LocalDate;

public class ActiveIncidentsTable {

    private StringProperty emergencyType;
    private StringProperty emergencyStatus;
    private IntegerProperty incidentNumber;
    private ObjectProperty<LocalDate> timeCreated;
    private StringProperty barangayLocation;
    private StringProperty rescueeName;  // Rescuee name (first + last)
    private IntegerProperty numOfRescuee;

    // Constructor
    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, Integer incidentNumber,
                                LocalDate timeCreated, String barangayLocation, String rescueeName, 
                                Integer numOfRescuee) {
        this.emergencyType = new SimpleStringProperty(emergencyType);
        this.emergencyStatus = new SimpleStringProperty(emergencyStatus);
        this.incidentNumber = new SimpleIntegerProperty(incidentNumber);
        this.timeCreated = new SimpleObjectProperty<>(timeCreated);
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

    public int getIncidentNumber() {
        return incidentNumber.get();
    }

    public void setIncidentNumber(int incidentNumber) {
        this.incidentNumber.set(incidentNumber);
    }

    public IntegerProperty incidentNumberProperty() {
        return incidentNumber;
    }

    public LocalDate getTimeCreated() {
        return timeCreated.get();
    }

    public void setTimeCreated(LocalDate timeCreated) {
        this.timeCreated.set(timeCreated);
    }

    public ObjectProperty<LocalDate> timeCreatedProperty() {
        return timeCreated;
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
