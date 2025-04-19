import javafx.beans.property.*;
import java.time.LocalDate;

public class ActiveIncidentsTable {

    private StringProperty emergencyType;
    private StringProperty emergencyStatus;
    private StringProperty emergencySeverity;
    private StringProperty incidentNumber;
    private ObjectProperty<LocalDate> dateIssued;
    private StringProperty barangayLocation;
    private StringProperty rescueeName;
    private IntegerProperty numOfRescuee;
    private IntegerProperty children;
    private IntegerProperty adults;
    private IntegerProperty seniors;

    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, String emergencySeverity,
                                String incidentNumber, LocalDate dateIssued, String barangayLocation,
                                String rescueeName, int numOfRescuee, Integer children, Integer adults, Integer seniors) {

        this.emergencyType = new SimpleStringProperty(emergencyType);
        this.emergencyStatus = new SimpleStringProperty(emergencyStatus);
        this.emergencySeverity = new SimpleStringProperty(emergencySeverity);
        this.incidentNumber = new SimpleStringProperty(incidentNumber);
        this.dateIssued = new SimpleObjectProperty<>(dateIssued);
        this.barangayLocation = new SimpleStringProperty(barangayLocation);
        this.rescueeName = new SimpleStringProperty(rescueeName);
        this.numOfRescuee = new SimpleIntegerProperty(numOfRescuee);

        this.children = new SimpleIntegerProperty(children != null ? children : 0);
        this.adults = new SimpleIntegerProperty(adults != null ? adults : 0);
        this.seniors = new SimpleIntegerProperty(seniors != null ? seniors : 0);
    }

    
    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, String emergencySeverity,
                                String incidentNumber, LocalDate dateIssued, String barangayLocation,
                                String rescueeName, Integer numOfRescuee) {

        this.emergencyType = new SimpleStringProperty(emergencyType);
        this.emergencyStatus = new SimpleStringProperty(emergencyStatus);
        this.emergencySeverity = new SimpleStringProperty(emergencySeverity);
        this.incidentNumber = new SimpleStringProperty(incidentNumber);
        this.dateIssued = new SimpleObjectProperty<>(dateIssued);
        this.barangayLocation = new SimpleStringProperty(barangayLocation);
        this.rescueeName = new SimpleStringProperty(rescueeName);
        this.numOfRescuee = new SimpleIntegerProperty(numOfRescuee);

        this.children = new SimpleIntegerProperty(0);
        this.adults = new SimpleIntegerProperty(0);
        this.seniors = new SimpleIntegerProperty(0);
    }

    public String getEmergencyType() { return emergencyType.get(); 
    }
    public void setEmergencyType(String value) { 
        emergencyType.set(value); 
    }
    public StringProperty emergencyTypeProperty() { 
        return emergencyType; 
    }

    public String getEmergencyStatus() { 
        return emergencyStatus.get(); 
    }
    public void setEmergencyStatus(String value) { 
        emergencyStatus.set(value); 
    }
    public StringProperty emergencyStatusProperty() { 
        return emergencyStatus; 
    }

    public String getEmergencySeverity() { 
        return emergencySeverity.get(); 
    }
    public void setEmergencySeverity(String value) { 
        emergencySeverity.set(value); 
    }
    public StringProperty emergencySeverityProperty() { 
        return emergencySeverity; 
    }

    public String getIncidentNumber() { 
        return incidentNumber.get(); 
    }
    public void setIncidentNumber(String value) { 
        incidentNumber.set(value); 
    }
    public StringProperty incidentNumberProperty() { 
        return incidentNumber; 
    }

    public LocalDate getDateIssued() { 
        return dateIssued.get(); 
    }
    public void setDateIssued(LocalDate value) { 
        dateIssued.set(value); 
    }
    public ObjectProperty<LocalDate> dateIssuedProperty() { 
        return dateIssued; 
    }

    public String getBarangayLocation() { 
        return barangayLocation.get(); 
    }
    public void setBarangayLocation(String value) { 
        barangayLocation.set(value); 
    }
    public StringProperty barangayLocationProperty() { 
        return barangayLocation; 
    }

    public String getRescueeName() { 
        return rescueeName.get(); 
    }
    public void setRescueeName(String value) { 
        rescueeName.set(value); 
    }
    public StringProperty rescueeNameProperty() { 
        return rescueeName; 
    }

    public int getNumOfRescuee() { 
        return numOfRescuee.get(); 
    }
    public void setNumOfRescuee(int value) { 
        numOfRescuee.set(value); 
    }
    public IntegerProperty numOfRescueeProperty() { 
        return numOfRescuee; 
    }


    public int getChildren() { 
        return children.get(); 
    }
    public void setChildren(int value) { 
        children.set(value); 
    }


    public int getAdults() { 
        return adults.get(); 
    }
    public void setAdults(int value) { 
        adults.set(value); 
    }


    public int getSeniors() { 
        return seniors.get(); 
    }
    public void setSeniors(int value) { 
        seniors.set(value); 
    }
}