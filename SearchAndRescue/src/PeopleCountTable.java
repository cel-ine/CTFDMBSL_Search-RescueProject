public class PeopleCountTable {
    private int peopleID;
    private int memberCount;
    private String address;
    private String contactNum;
    private int numOfChildren;
    private int numOfAdults;
    private int numOfSeniors;
    private String officerInCharge;

    // constructor
    public PeopleCountTable(String address, String contactNum, int numOfChildren, int numOfAdults, int numOfSeniors, String officerInCharge) {
        this.address = address;
        this.contactNum = contactNum;
        this.numOfAdults = numOfAdults;
        this.numOfSeniors = numOfSeniors;
        this.memberCount = numOfChildren + numOfAdults + numOfSeniors;
        this.officerInCharge = officerInCharge;
    }

    // getters
    public String getAddress () { return address; }
    public String getContactNum () { return contactNum; }
    public int getMemberCount() { return memberCount; }
    public int getNumOfChildren() { return numOfChildren; }
    public int getNumOfAdults() { return numOfAdults; }
    public int getNumOfSeniors() { return numOfSeniors; }
    public String getofficerInCharge() { return officerInCharge; }
}

