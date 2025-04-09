//GETTERS AND SETTERS FOR BARANGAY DESCRIPTION TABLE 

public class BarangayDescTable {
    private String barangayName;
    private String barangayCaptain;
    private String barangayHazard; 
    private Integer barangayPopulation; 

    public BarangayDescTable(String barangayName, String barangayCaptain, String barangayHazard, Integer barangayPopulation) {
        this.barangayName = barangayName;
        this.barangayCaptain = barangayCaptain;
        this.barangayHazard = barangayHazard;
        this.barangayPopulation = barangayPopulation;
    }
    public String getBarangayName() {
        return barangayName;
    }   
    public void setBarangayName(String barangayName) {
        this.barangayName = barangayName;
    }
    public String getBarangayCaptain() {
        return barangayCaptain;
    }   
    public void setBarangayCaptain(String barangayCaptain) {
        this.barangayCaptain = barangayCaptain;
    }
    public String getBarangayHazard() {
        return barangayHazard;
    }
    public void setBarangayHazard(String barangayHazard) {
        this.barangayHazard = barangayHazard;
    }
    public Integer getBarangayPopulation() {
        return barangayPopulation;
    }
    public void setBarangayPopulation(Integer barangayPopulation) {
        this.barangayPopulation = barangayPopulation;
    }
}
