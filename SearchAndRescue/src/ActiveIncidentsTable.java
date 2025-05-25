import javafx.beans.property.*;
import java.time.LocalDate;

public class ActiveIncidentsTable {

    private StringProperty emergencyType;
    private StringProperty emergencyStatus;
    private StringProperty emergencySeverity;
    private StringProperty incidentNumber;
    private ObjectProperty<LocalDate> dateIssued;
    private StringProperty barangayLocation;
    private IntegerProperty numOfRescuee;
    private IntegerProperty children;
    private IntegerProperty adults;
    private IntegerProperty seniors;
    private StringProperty personInCharge;
    private StringProperty address;
    private StringProperty contactNum;


    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, String emergencySeverity,
                                String incidentNumber, LocalDate dateIssued, String barangayLocation, String personInCharge, int numOfRescuee, Integer children, Integer adults, Integer seniors, 
                                String address, String contactNum) {

        this.emergencyType = new SimpleStringProperty(emergencyType);
        this.emergencyStatus = new SimpleStringProperty(emergencyStatus);
        this.emergencySeverity = new SimpleStringProperty(emergencySeverity);
        this.incidentNumber = new SimpleStringProperty(incidentNumber);
        this.dateIssued = new SimpleObjectProperty<>(dateIssued);
        this.barangayLocation = new SimpleStringProperty(barangayLocation);
        this.numOfRescuee = new SimpleIntegerProperty(numOfRescuee);

        this.children = new SimpleIntegerProperty(children != null ? children : 0);
        this.adults = new SimpleIntegerProperty(adults != null ? adults : 0);
        this.seniors = new SimpleIntegerProperty(seniors != null ? seniors : 0);
        this.personInCharge = new SimpleStringProperty (personInCharge); 
        this.address = new SimpleStringProperty(address);
        this.contactNum = new SimpleStringProperty(contactNum);


    }

    
    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, String emergencySeverity,
                                String incidentNumber, LocalDate dateIssued, String barangayLocation,
                                Integer numOfRescuee) {

        this.emergencyType = new SimpleStringProperty(emergencyType);
        this.emergencyStatus = new SimpleStringProperty(emergencyStatus);
        this.emergencySeverity = new SimpleStringProperty(emergencySeverity);
        this.incidentNumber = new SimpleStringProperty(incidentNumber);
        this.dateIssued = new SimpleObjectProperty<>(dateIssued);
        this.barangayLocation = new SimpleStringProperty(barangayLocation);
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

    public String getAddress() { 
        return address.get(); 
    }
    public void setAddress(String value) { 
        address.set(value); 
    }
    public StringProperty addressProperty() { 
        return address; 
    }


    public String getContactNum() { 
        return contactNum.get(); 
    }
    public void setContactNum(String value) { 
        contactNum.set(value); 
    }
    public StringProperty contactNumProperty() { 
        return contactNum; 
    }

    public String getPersonInCharge() { 
        return personInCharge.get(); 
    }
    public void setPersonInCharge(String value) { 
        personInCharge.set(value); 
    }
    public StringProperty personInChargeProperty() { 
        return personInCharge; 
    }
}