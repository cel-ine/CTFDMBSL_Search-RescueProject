//GETTERS AND SETTERS FOR BARANGAY TABLE
public class BarangayTable{
    private String barangayName;
    private Double barangayDistance;
    private Integer barangayRescueCount;

    public BarangayTable(String barangayName, Double barangayDistance, Integer barangayRescueCount) {
        this.barangayName = barangayName;
        this.barangayDistance = barangayDistance;
        this.barangayRescueCount = barangayRescueCount;
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
}