//GETTERS AND SETTERS FOR BARANGAY DESCRIPTION TABLE 

public class BarangayDescTable {
    private String barangayCaptain;
    private String barangayHazard; 
    private String barangayPopulation; 

    public BarangayDescTable(String barangayCaptain, String barangayHazard, String barangayPopulation) {
        this.barangayCaptain = barangayCaptain;
        this.barangayHazard = barangayHazard;
        this.barangayPopulation = barangayPopulation;
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
    public String getBarangayPopulation() {
        return barangayPopulation;
    }
    public void setBarangayPopulation(String barangayPopulation) {
        this.barangayPopulation = barangayPopulation;
    }
}
