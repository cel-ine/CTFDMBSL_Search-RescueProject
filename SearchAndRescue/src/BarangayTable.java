//GETTERS AND SETTERS FOR BARANGAY TABLE
public class BarangayTable{
    private Integer barangayID;
    private String barangayName;
    private Double barangayDistance;
    private Integer barangayRescueCount;

    public BarangayTable (Integer barangayID, String barangayName) {
        this.barangayID = barangayID;
        this.barangayName = barangayName;
    }

    public BarangayTable(String barangayName, Double barangayDistance, Integer barangayRescueCount) {
        this.barangayName = barangayName;
        this.barangayDistance = barangayDistance;
        this.barangayRescueCount = barangayRescueCount;
    }

    public Integer getBarangayID() {
        return barangayID;
    }

    public void setBarangayID(Integer barangayID) {
        this.barangayID = barangayID;
    }

    public String getBarangayName() {
        return barangayName;
    }

    public void setBarangayName(String barangayName) {
        this.barangayName = barangayName;
    }

    public Double getBarangayDistance() {
        return barangayDistance;
    }

    public void setBarangayDistance(Double barangayDistance) {
        this.barangayDistance = barangayDistance;
    }

    public Integer getBarangayRescueCount() {
        return barangayRescueCount;
    }

    public void setBarangayRescueCount(Integer barangayRescueCount) {
        this.barangayRescueCount = barangayRescueCount;
    }

    @Override
    public String toString() {
        return barangayName;  // This ensures that only barangayName is shown in the ComboBox.
    }
}