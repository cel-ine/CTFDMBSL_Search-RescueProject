public class ActiveIncidentsTable {
    private String emergencyType;
    private String emergencyStatus;
    private String incidentNumber;
    private String timeCreated;
    private String barangayLocation;
    private String nameOfRescuee;
    private String numberOfRescuee;

    public ActiveIncidentsTable(String emergencyType, String emergencyStatus, String incidentNumber, String timeCreated, String barangayLocation, String nameOfRescuee, String numberOfRescuee) {
        this.emergencyType = emergencyType;
        this.emergencyStatus = emergencyStatus;
        this.incidentNumber = incidentNumber;
        this.timeCreated = timeCreated;
        this.barangayLocation = barangayLocation;
        this.nameOfRescuee = nameOfRescuee;
        this.numberOfRescuee = numberOfRescuee;
    }
}
